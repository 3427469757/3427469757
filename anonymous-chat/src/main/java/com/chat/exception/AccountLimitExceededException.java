package com.chat.exception;

/**
 * Custom exception for when account limit is reached
 */
public class AccountLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccountLimitExceededException(String message) {
        super(message);
    }

    public AccountLimitExceededException() {
        super("Account limit exceeded. Maximum 3 accounts allowed per user.");
    }
}
