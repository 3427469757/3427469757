package com.chat.repository;

import com.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天消息数据访问层
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 查询两个用户之间的聊天记录（分页）
     */
    Page<ChatMessage> findBySendIdAndReceiveIdOrderByCreateTimeDesc(
        Long sendId, Long receiveId, Pageable pageable);

    /**
     * 查询两个用户之间的所有聊天记录（双向）
     */
    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sendId = :id1 AND m.receiveId = :id2) OR " +
           "(m.sendId = :id2 AND m.receiveId = :id1) " +
           "ORDER BY m.createTime DESC")
    Page<ChatMessage> findBidirectionalMessages(
        @Param("id1") Long id1, 
        @Param("id2") Long id2, 
        Pageable pageable);

    /**
     * 查询未读消息
     */
    List<ChatMessage> findByReceiveIdAndIsRead(Long receiveId, Integer isRead);

    /**
     * 统计未读消息数量
     */
    long countByReceiveIdAndIsRead(Long receiveId, Integer isRead);

    /**
     * 标记消息为已读
     */
    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = 1 WHERE m.receiveId = :receiveId AND m.sendId = :sendId")
    int markAsRead(@Param("receiveId") Long receiveId, @Param("sendId") Long sendId);

    /**
     * 删除指定时间之前的消息（用于定时清理 7 天前的消息）
     */
    @Modifying
    @Query("DELETE FROM ChatMessage m WHERE m.createTime < :threshold")
    int deleteMessagesOlderThan(@Param("threshold") LocalDateTime threshold);

    /**
     * 删除两个用户之间的所有聊天记录
     */
    @Modifying
    @Query("DELETE FROM ChatMessage m WHERE " +
           "(m.sendId = :id1 AND m.receiveId = :id2) OR " +
           "(m.sendId = :id2 AND m.receiveId = :id1)")
    int deleteBidirectionalMessages(@Param("id1") Long id1, @Param("id2") Long id2);
}
