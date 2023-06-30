package com.example.MnM.boundedContext.room.entity;

public enum RedisRoom {
    COUNT;

    public String getKey(String roomSecretId) {
        return "%s:%s".formatted(this.name(), roomSecretId);
    }
}
