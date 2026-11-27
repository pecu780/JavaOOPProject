package com.taskflow.domain;

public class InvalidTaskException extends Exception {
    public InvalidTaskException(String message) {
        super(message);
    }
}
