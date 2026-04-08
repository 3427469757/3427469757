package com.chat.service;

import com.chat.dto.CreateAccountRequest;
import com.chat.dto.UpdateAccountRequest;
import com.chat.dto.UserAccountResponse;
import com.chat.entity.UserAccount;
import com.chat.enums.Gender;
import com.chat.exception.AccountLimitExceededException;
import com.chat.exception.AccountNotFoundException;
import com.chat.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing user accounts
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private static final int MAX_ACCOUNTS_PER_USER = 3;

    /**
     * Get all active accounts for a user
     */
    @Transactional(readOnly = true)
    public List<UserAccountResponse> getAccountsByUserId(String userId) {
        List<UserAccount> accounts = userAccountRepository.findByUserIdAndIsActiveTrue(userId);
        return accounts.stream()
                .map(UserAccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific account by ID and user ID
     */
    @Transactional(readOnly = true)
    public UserAccountResponse getAccountById(Long accountId, String userId) {
        UserAccount account = userAccountRepository.findByIdAndUserId(accountId, userId)
                .filter(UserAccount::getIsActive)
                .orElseThrow(() -> new AccountNotFoundException(accountId, userId));
        return UserAccountResponse.fromEntity(account);
    }

    /**
     * Create a new account for a user
     */
    public UserAccountResponse createAccount(String userId, CreateAccountRequest request) {
        // Check if user has reached the account limit
        long activeAccountCount = userAccountRepository.countByUserIdAndIsActiveTrue(userId);
        
        if (activeAccountCount >= MAX_ACCOUNTS_PER_USER) {
            throw new AccountLimitExceededException(
                "You have reached the maximum limit of " + MAX_ACCOUNTS_PER_USER + " accounts. " +
                "Please delete an existing account before creating a new one."
            );
        }

        UserAccount account = UserAccount.builder()
                .userId(userId)
                .nickname(request.getNickname())
                .age(request.getAge())
                .gender(request.getGender())
                .region(request.getRegion())
                .bio(request.getBio())
                .isActive(true)
                .build();

        UserAccount savedAccount = userAccountRepository.save(account);
        return UserAccountResponse.fromEntity(savedAccount);
    }

    /**
     * Update an existing account
     */
    public UserAccountResponse updateAccount(Long accountId, String userId, UpdateAccountRequest request) {
        UserAccount account = userAccountRepository.findByIdAndUserId(accountId, userId)
                .filter(UserAccount::getIsActive)
                .orElseThrow(() -> new AccountNotFoundException(accountId, userId));

        // Update fields if provided
        if (request.getNickname() != null) {
            account.setNickname(request.getNickname());
        }
        if (request.getAge() != null) {
            account.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            account.setGender(request.getGender());
        }
        if (request.getRegion() != null) {
            account.setRegion(request.getRegion());
        }
        if (request.getBio() != null) {
            account.setBio(request.getBio());
        }

        UserAccount updatedAccount = userAccountRepository.save(account);
        return UserAccountResponse.fromEntity(updatedAccount);
    }

    /**
     * Delete (deactivate) an account
     */
    public void deleteAccount(Long accountId, String userId) {
        UserAccount account = userAccountRepository.findByIdAndUserId(accountId, userId)
                .filter(UserAccount::getIsActive)
                .orElseThrow(() -> new AccountNotFoundException(accountId, userId));

        account.setIsActive(false);
        userAccountRepository.save(account);
    }

    /**
     * Get the count of active accounts for a user
     */
    @Transactional(readOnly = true)
    public long getAccountCount(String userId) {
        return userAccountRepository.countByUserIdAndIsActiveTrue(userId);
    }

    /**
     * Get remaining account slots for a user
     */
    @Transactional(readOnly = true)
    public int getRemainingAccountSlots(String userId) {
        long activeCount = getAccountCount(userId);
        return Math.max(0, MAX_ACCOUNTS_PER_USER - (int) activeCount);
    }
}
