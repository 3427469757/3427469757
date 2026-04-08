package com.anonymous.chat.mapper;

import com.anonymous.chat.entity.AnonymousAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 虚拟账号 Mapper 接口
 * Account Mapper Interface
 */
@Mapper
public interface AnonymousAccountMapper extends BaseMapper<AnonymousAccount> {

    /**
     * 根据 clientId 查询账号列表
     */
    @Select("SELECT * FROM anonymous_account WHERE client_id = #{clientId} AND is_deleted = 0")
    List<AnonymousAccount> selectByClientId(@Param("clientId") String clientId);

    /**
     * 统计 clientId 下的账号数量
     */
    @Select("SELECT COUNT(*) FROM anonymous_account WHERE client_id = #{clientId} AND is_deleted = 0")
    int countByClientId(@Param("clientId") String clientId);

    /**
     * 检查账号是否属于指定 clientId
     */
    @Select("SELECT COUNT(*) FROM anonymous_account WHERE id = #{accountId} AND client_id = #{clientId} AND is_deleted = 0")
    int checkAccountOwnership(@Param("accountId") Long accountId, @Param("clientId") String clientId);
}
