package com.anonymous.chat.mapper;

import com.anonymous.chat.entity.ChatContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 通讯录 Mapper 接口
 * Contact Mapper Interface
 */
@Mapper
public interface ChatContactMapper extends BaseMapper<ChatContact> {

    /**
     * 查询账号的通讯录列表
     */
    @Select("SELECT * FROM chat_contact WHERE account_id = #{accountId} ORDER BY create_time DESC")
    List<ChatContact> selectByAccountId(@Param("accountId") Long accountId);

    /**
     * 检查好友关系
     */
    @Select("SELECT COUNT(*) FROM chat_contact WHERE account_id = #{accountId} AND friend_id = #{friendId} AND black_status = 0")
    int checkFriendship(@Param("accountId") Long accountId, @Param("friendId") Long friendId);

    /**
     * 检查拉黑状态
     */
    @Select("SELECT black_status FROM chat_contact WHERE account_id = #{accountId} AND friend_id = #{friendId}")
    Integer checkBlackStatus(@Param("accountId") Long accountId, @Param("friendId") Long friendId);
}
