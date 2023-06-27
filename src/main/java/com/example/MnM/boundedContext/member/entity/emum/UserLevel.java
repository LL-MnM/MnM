package com.example.MnM.boundedContext.member.entity.emum;

import com.example.MnM.base.exception.NotMatchUserLevelException;
import jakarta.persistence.AttributeConverter;
import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum UserLevel {
    NORMAL(3, "NORMAL"),
    ADMIN(7, "ADMIN");

    UserLevel(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;
    private String value;

    public static class Converter implements AttributeConverter<UserLevel, Integer> {
        @Override
        public Integer convertToDatabaseColumn(UserLevel attribute) {
            return attribute.getCode();
        }

        @Override
        public UserLevel convertToEntityAttribute(Integer dbData) {
            return EnumSet.allOf(UserLevel.class).stream()
                    .filter(e -> e.getCode() == dbData)
                    .findAny()
                    .orElseThrow(NotMatchUserLevelException::new);
        }
    }
}