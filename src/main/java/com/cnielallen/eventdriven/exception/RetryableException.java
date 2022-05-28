package com.cnielallen.eventdriven.exception;

public class RetryableException extends RuntimeException {
    public RetryableException(String message, Throwable err) { super(message,err);}
}
