package com.example.MnM.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public Map convertAsMap(Object o) {

        return objectMapper.convertValue(o, Map.class);

    }

    public <T> Optional<T> convertAsType(Object o, Class<T> classType) {

        return Optional.ofNullable(objectMapper.convertValue(o, classType));

    }
}
