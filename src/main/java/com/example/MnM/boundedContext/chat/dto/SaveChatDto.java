package com.example.MnM.boundedContext.chat.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public class SaveChatDto {
    private final String roomId;
    private final String roomSecretId;
}
