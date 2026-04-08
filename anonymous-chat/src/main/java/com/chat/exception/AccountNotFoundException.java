package com.chat.exception;

/**
 * Custom exception for when account is not found
 */
public class AccountNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Long accountId, String userId) {
        super("Account with id " + accountId + " not found for user " + userId);
    }
}
