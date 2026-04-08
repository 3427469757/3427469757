package com.chat.repository;

import com.chat.entity.ChatContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通讯录数据访问层
 */
@Repository
public interface ChatContactRepository extends JpaRepository<ChatContact, Long> {

    /**
     * 查询指定账号的所有好友
     */
    List<ChatContact> findByAccountIdOrderByCreateTimeDesc(Long accountId);

    /**
     * 查询指定的好友关系（单向）
     */
    Optional<ChatContact> findByAccountIdAndFriendId(Long accountId, Long friendId);

    /**
     * 检查是否是好友关系（双向检查）
     */
    @Query("SELECT COUNT(c) > 0 FROM ChatContact c WHERE " +
           "(c.accountId = :id1 AND c.friendId = :id2) OR " +
           "(c.accountId = :id2 AND c.friendId = :id1)")
    boolean areFriends(@Param("id1") Long id1, @Param("id2") Long id2);

    /**
     * 检查是否被拉黑
     */
    @Query("SELECT COUNT(c) > 0 FROM ChatContact c WHERE " +
           "((c.accountId = :accountId AND c.friendId = :friendId) OR " +
           "(c.accountId = :friendId AND c.friendId = :accountId)) AND " +
           "c.blackStatus = 1")
    boolean isBlacklisted(@Param("accountId") Long accountId, @Param("friendId") Long friendId);

    /**
     * 删除好友关系（双向）
     */
    @Modifying
    @Query("DELETE FROM ChatContact c WHERE " +
           "(c.accountId = :id1 AND c.friendId = :id2) OR " +
           "(c.accountId = :id2 AND c.friendId = :id1)")
    int deleteFriendship(@Param("id1") Long id1, @Param("id2") Long id2);

    /**
     * 更新拉黑状态
     */
    @Modifying
    @Query("UPDATE ChatContact c SET c.blackStatus = :status WHERE " +
           "(c.accountId = :id1 AND c.friendId = :id2) OR " +
           "(c.accountId = :id2 AND c.friendId = :id1)")
    int updateBlackStatus(
        @Param("id1") Long id1, 
        @Param("id2") Long id2, 
        @Param("status") Integer status);

    /**
     * 统计未读消息数（关联查询）
     */
    @Query("SELECT SUM(CASE WHEN m.isRead = 0 THEN 1 ELSE 0 END) " +
           "FROM ChatMessage m " +
           "WHERE m.receiveId = :accountId AND m.sendId = :friendId")
    Long countUnreadMessages(@Param("accountId") Long accountId, @Param("friendId") Long friendId);
}
