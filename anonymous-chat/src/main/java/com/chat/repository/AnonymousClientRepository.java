package com.chat.repository;

import com.chat.entity.AnonymousClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 匿名客户端数据访问层
 */
@Repository
public interface AnonymousClientRepository extends JpaRepository<AnonymousClient, String> {
    
    /**
     * 根据 clientId 查询客户端
     */
    Optional<AnonymousClient> findByClientId(String clientId);
    
    /**
     * 检查 clientId 是否存在
     */
    boolean existsByClientId(String clientId);
}
