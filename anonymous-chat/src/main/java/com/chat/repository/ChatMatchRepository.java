package com.chat.repository;

import com.chat.entity.ChatMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 匹配记录数据访问层
 */
@Repository
public interface ChatMatchRepository extends JpaRepository<ChatMatch, Long> {

    /**
     * 查询账号当前的匹配状态
     */
    Optional<ChatMatch> findByAccountIdAndStatus(Long accountId, Integer status);

    /**
     * 检查两个账号在指定时间内是否匹配过
     */
    @Query("SELECT COUNT(m) > 0 FROM ChatMatch m WHERE " +
           "((m.accountId = :id1 AND m.targetId = :id2) OR " +
           "(m.accountId = :id2 AND m.targetId = :id1)) AND " +
           "m.status = 1 AND m.createTime > :since")
    boolean hasMatchedRecently(
        @Param("id1") Long id1, 
        @Param("id2") Long id2, 
        @Param("since") LocalDateTime since);

    /**
     * 查询指定时间之前的匹配记录（用于清理）
     */
    List<ChatMatch> findByCreateTimeBefore(LocalDateTime threshold);

    /**
     * 更新匹配状态
     */
    @Modifying
    @Query("UPDATE ChatMatch m SET m.status = :status WHERE m.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 取消匹配（将状态设为已取消）
     */
    @Modifying
    @Query("UPDATE ChatMatch m SET m.status = 3 WHERE m.accountId = :accountId AND m.status = 0")
    int cancelMatching(@Param("accountId") Long accountId);
}
