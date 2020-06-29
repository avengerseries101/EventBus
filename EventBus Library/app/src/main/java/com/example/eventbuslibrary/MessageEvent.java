package com.example.eventbuslibrary;

public class MessageEvent {

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
