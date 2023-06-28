package com.example.MnM.base.security;


import com.example.MnM.boundedContext.member.entity.emum.UserLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class CustomConverter implements AttributeConverter<Set<UserLevel>, String> {

    @Override
    public String convertToDatabaseColumn(Set<UserLevel> attribute) {
        return attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public Set<UserLevel> convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ?  convertToSet(dbData) : new HashSet<>();
    }

    private Set<UserLevel> convertToSet(String dbData) {
        return Arrays.stream(dbData.split(",")).map(UserLevel::valueOf).collect(Collectors.toSet());
    }
}
