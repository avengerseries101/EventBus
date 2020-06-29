package com.example.eventbusexampleusingretrofit;

public class MessageEventBus {
    public final String message;

    public MessageEventBus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
