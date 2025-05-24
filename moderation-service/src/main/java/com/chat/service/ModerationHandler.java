package com.chat.service;

public abstract class ModerationHandler {

    protected ModerationHandler next;

    public ModerationHandler setNext(ModerationHandler next) {
        this.next = next;
        return next;
    }

    public abstract boolean handle(String message);
}