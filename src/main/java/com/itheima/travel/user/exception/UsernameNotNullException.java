package com.itheima.travel.user.exception;

public class UsernameNotNullException extends Throwable {
    public UsernameNotNullException() {
    }

    public UsernameNotNullException(String message) {
        super(message);
    }
}
