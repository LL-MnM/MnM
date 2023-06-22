package com.example.MnM.standard.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public String writeString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> Optional<T> readObject(String key, Class<T> classType) {

        try {
            return Optional.ofNullable(objectMapper.readValue(key, classType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
