package com.anonymous.chat.mapper;

import com.anonymous.chat.entity.ChatMatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 匹配记录 Mapper 接口
 * Match Mapper Interface
 */
@Mapper
public interface ChatMatchMapper extends BaseMapper<ChatMatch> {

    /**
     * 查询账号当前匹配状态
     */
    @Select("SELECT * FROM chat_match WHERE account_id = #{accountId} AND status = 0 ORDER BY create_time DESC LIMIT 1")
    ChatMatch selectCurrentMatching(@Param("accountId") Long accountId);

    /**
     * 检查 24 小时内是否已匹配过
     */
    @Select("SELECT COUNT(*) FROM chat_match WHERE (account_id = #{accountId1} AND target_id = #{accountId2}) " +
            "OR (account_id = #{accountId2} AND target_id = #{accountId1}) " +
            "AND status = 1 AND create_time > #{startTime}")
    int checkRecentMatch(@Param("accountId1") Long accountId1, 
                         @Param("accountId2") Long accountId2, 
                         @Param("startTime") LocalDateTime startTime);

    /**
     * 查询在线且可匹配的账号（排除已匹配、同一 clientId、24 小时内匹配过的）
     */
    @Select("<script>" +
            "SELECT a.* FROM anonymous_account a " +
            "WHERE a.id IN (SELECT DISTINCT account_id FROM chat_match WHERE status = 0) " +
            "AND a.is_deleted = 0 " +
            "AND a.client_id != #{clientId} " +
            "AND a.id != #{accountId} " +
            "<if test='genderPreference != null and genderPreference != 0'>" +
            "AND a.gender = #{genderPreference} " +
            "</if>" +
            "<if test='minAge != null'>" +
            "AND a.age >= #{minAge} " +
            "</if>" +
            "<if test='maxAge != null'>" +
            "AND a.age <= #{maxAge} " +
            "</if>" +
            "ORDER BY RAND() LIMIT 1" +
            "</script>")
    com.anonymous.chat.entity.AnonymousAccount findMatchCandidate(
            @Param("accountId") Long accountId,
            @Param("clientId") String clientId,
            @Param("genderPreference") Integer genderPreference,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge);
}
