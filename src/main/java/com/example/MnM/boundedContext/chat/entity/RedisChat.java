package com.example.MnM.boundedContext.chat.entity;

public enum RedisChat {
    CHAT;

    public String getKey(String roomSecretId) {
        return "%s:%s".formatted(this.name(), roomSecretId);
    }
}
