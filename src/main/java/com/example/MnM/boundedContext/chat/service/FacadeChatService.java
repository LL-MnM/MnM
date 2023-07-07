package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.base.exception.NotFoundParticipantException;
import com.example.MnM.boundedContext.chat.infra.InspectSentimentService;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;

import static com.example.MnM.boundedContext.room.entity.RedisRoom.MEMBERS;


/*
 * Single 전용 객체입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FacadeChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final InspectSentimentService inspectSentimentService;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;
    private final RoomService roomService;
    private final EmotionService emotionService;

    public void inspectChat(String roomSecretId, String chat) {

        List<String> values = redisTemplate.opsForSet()
                .members(MEMBERS.getKey(roomSecretId))
                .stream()
                .limit(2)
                .map(Object::toString)
                .toList();

        String firstPerson = values.get(0);
        String secondPerson = values.get(1);

        try {
            inspectSentimentService.chatInspectSentiment(chat)
                    .thenAccept((emotionDegree) -> {
                        transactionTemplate.executeWithoutResult((status) -> {

                            Member first = memberRepository.findByUsername(firstPerson)
                                    .orElseThrow(() -> new NotFoundParticipantException("존재 하지 않는 유저입니다."));
                            Member second = memberRepository.findByUsername(secondPerson)
                                    .orElseThrow(() -> new NotFoundParticipantException("존재 하지 않는 유저입니다."));
                            emotionService.saveIfLargerScore(first,second.getMbti(),emotionDegree);
                            emotionService.saveIfLargerScore(second,first.getMbti(),emotionDegree);
                        });
                    });
        } catch (IOException e) {
            log.error("inspectSentimentService error", e);
            throw new RuntimeException(e);
        } finally {
            roomService.deleteCacheRoom(roomSecretId);
        }

    }
}
