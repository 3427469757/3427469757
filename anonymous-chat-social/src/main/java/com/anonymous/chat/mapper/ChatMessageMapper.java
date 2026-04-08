package com.anonymous.chat.mapper;

import com.anonymous.chat.entity.ChatMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 聊天消息 Mapper 接口
 * Message Mapper Interface
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 分页查询聊天记录（按双方账号）
     */
    @Select("SELECT * FROM chat_message WHERE (send_id = #{sendId} AND receive_id = #{receiveId}) " +
            "OR (send_id = #{receiveId} AND receive_id = #{sendId}) " +
            "ORDER BY create_time DESC")
    IPage<ChatMessage> selectMessages(Page<ChatMessage> page, 
                                       @Param("sendId") Long sendId, 
                                       @Param("receiveId") Long receiveId);

    /**
     * 统计未读消息数量
     */
    @Select("SELECT COUNT(*) FROM chat_message WHERE receive_id = #{accountId} AND is_read = 0")
    int countUnreadMessages(@Param("accountId") Long accountId);

    /**
     * 批量标记消息为已读
     */
    @Select("UPDATE chat_message SET is_read = 1 WHERE receive_id = #{accountId} AND send_id = #{sendId} AND is_read = 0")
    int markMessagesAsRead(@Param("accountId") Long accountId, @Param("sendId") Long sendId);

    /**
     * 删除双方聊天记录
     */
    @Select("DELETE FROM chat_message WHERE (send_id = #{accountId} AND receive_id = #{friendId}) " +
            "OR (send_id = #{friendId} AND receive_id = #{accountId})")
    int deleteMessagesBetween(@Param("accountId") Long accountId, @Param("friendId") Long friendId);
}
