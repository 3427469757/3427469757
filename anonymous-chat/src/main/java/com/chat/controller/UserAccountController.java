package com.chat.controller;

import com.chat.dto.ApiResponse;
import com.chat.dto.CreateAccountRequest;
import com.chat.dto.UpdateAccountRequest;
import com.chat.dto.UserAccountResponse;
import com.chat.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing user accounts
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * Get all accounts for a user
     * @param userId The unique user identifier
     * @return List of user accounts
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserAccountResponse>>> getAccounts(
            @RequestParam String userId) {
        List<UserAccountResponse> accounts = userAccountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    /**
     * Get a specific account by ID
     * @param userId The unique user identifier
     * @param accountId The account ID
     * @return User account details
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<UserAccountResponse>> getAccount(
            @RequestParam String userId,
            @PathVariable Long accountId) {
        UserAccountResponse account = userAccountService.getAccountById(accountId, userId);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    /**
     * Create a new account
     * @param userId The unique user identifier
     * @param request Account creation request
     * @return Created account details
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserAccountResponse>> createAccount(
            @RequestParam String userId,
            @Valid @RequestBody CreateAccountRequest request) {
        UserAccountResponse account = userAccountService.createAccount(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Account created successfully", account));
    }

    /**
     * Update an existing account
     * @param userId The unique user identifier
     * @param accountId The account ID
     * @param request Account update request
     * @return Updated account details
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<UserAccountResponse>> updateAccount(
            @RequestParam String userId,
            @PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountRequest request) {
        UserAccountResponse account = userAccountService.updateAccount(accountId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", account));
    }

    /**
     * Delete (deactivate) an account
     * @param userId The unique user identifier
     * @param accountId The account ID
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(
            @RequestParam String userId,
            @PathVariable Long accountId) {
        userAccountService.deleteAccount(accountId, userId);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", null));
    }

    /**
     * Get account count and remaining slots for a user
     * @param userId The unique user identifier
     * @return Account count information
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountCount(
            @RequestParam String userId) {
        long count = userAccountService.getAccountCount(userId);
        int remaining = userAccountService.getRemainingAccountSlots(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("activeAccounts", count);
        result.put("maxAccounts", 3);
        result.put("remainingSlots", remaining);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
