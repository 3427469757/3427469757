package com.chat.repository;

import com.chat.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserAccount entity
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    /**
     * Find all active accounts for a given user ID
     */
    List<UserAccount> findByUserIdAndIsActiveTrue(String userId);

    /**
     * Count active accounts for a given user ID
     */
    long countByUserIdAndIsActiveTrue(String userId);

    /**
     * Find a specific account by ID and user ID
     */
    Optional<UserAccount> findByIdAndUserId(Long id, String userId);

    /**
     * Check if an account exists with the given ID and user ID
     */
    boolean existsByIdAndUserId(Long id, String userId);
}
