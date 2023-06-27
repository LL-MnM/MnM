package com.example.MnM.boundedContext.chat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SaveChatDto {
    private final String roomId;
    private final String roomSecretId;
}
