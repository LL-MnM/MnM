INSERT INTO mbti (name)
VALUES ('INTP'),
       ('INTJ'),
       ('INFJ'),
       ('INFP'),
       ('ISTJ'),
       ('ISTP'),
       ('ISFJ'),
       ('ISFP'),
       ('ENTJ'),
       ('ENTP'),
       ('ENFJ'),
       ('ENFP'),
       ('ESTJ'),
       ('ESTP'),
       ('ESFJ'),
       ('ESFP');
UPDATE Mbti
SET best_mbti =
        CASE
            WHEN id = 1 THEN 'ENTP'
            WHEN id = 2 THEN 'INTP'
            WHEN id = 3 THEN 'ISFJ'
            WHEN id = 4 THEN 'ENFP'
            WHEN id = 5 THEN 'ISFJ'
            WHEN id = 6 THEN 'ESTP'
            WHEN id = 7 THEN 'INFJ'
            WHEN id = 8 THEN 'ISFP'
            WHEN id = 9 THEN 'ENTP'
            WHEN id = 10 THEN 'ENTJ'
            WHEN id = 11 THEN 'ESFP'
            WHEN id = 12 THEN 'ENTP'
            WHEN id = 13 THEN 'ENTJ'
            WHEN id = 14 THEN 'ISTP'
            WHEN id = 15 THEN 'ESFP'
            WHEN id = 16 THEN 'ESFP'
            END
WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

INSERT INTO member (age, create_date, deleted, email, gender, height, hobby, introduce, locate, mbti, modify_date,
                    nickname, password, profile_image, provider_type, username, name)
VALUES (20, '2023-06-24T01:15:54', false, 'user1@email.com', '남자', 170, '운동', '안녕하세요', '서울', 'ISFJ',
        '2023-06-24T01:15:54', '임꺽정', '$2a$10$p2RfN34zGPnclC1gYwGGaenHWcQIMQda2fVoa.BcPk2cXseHMbSa2', NULL, NULL,
        'user1', '임꺽정');
INSERT INTO member (age, create_date, deleted, email, gender, height, hobby, introduce, locate, mbti, modify_date,
                    nickname, password, profile_image, provider_type, username, name)
VALUES (21, '2023-06-24T01:15:54', false, 'user2@email.com', '여자', 178, '운동', '안녕하세요', '서울', 'ENFP',
        '2023-06-24T01:15:54', '홍길동', '$2a$10$p2RfN34zGPnclC1gYwGGaenHWcQIMQda2fVoa.BcPk2cXseHMbSa2', NULL, NULL,
        'user2', '홍길동');
INSERT INTO member (age, create_date, deleted, email, gender, height, hobby, introduce, locate, mbti, modify_date,
                    nickname, password, profile_image, provider_type, username, name)
VALUES (22, '2023-06-24T01:15:54', false, 'user3@email.com', '남자', 175, '운동', '안녕하세요', '서울', 'ENTP',
        '2023-06-24T01:15:54', '김철수', '$2a$10$p2RfN34zGPnclC1gYwGGaenHWcQIMQda2fVoa.BcPk2cXseHMbSa2', NULL, NULL,
        'user3', '김철수');
INSERT INTO member (age, create_date, deleted, email, gender, height, hobby, introduce, locate, mbti, modify_date,
                    nickname, password, profile_image, provider_type, username, name)
VALUES (23, '2023-06-24T01:15:54', false, 'user4@email.com', '여자', 180, '운동', '안녕하세요', '서울', 'ESFJ',
        '2023-06-24T01:15:54', '김미나', '$2a$10$p2RfN34zGPnclC1gYwGGaenHWcQIMQda2fVoa.BcPk2cXseHMbSa2', NULL, NULL,
        'user4', '김미나');


-- -- 조합
-- ENTP,ENTJ
-- ENTJ,ENTP
-- ESTP,ISTP
-- ESFJ,ESFP
-- ESFP,ESFP
-- ESTJ,ENTJ
-- ENFP,ENTP
-- ENFJ,INFJ
-- ISFP,ISFP
-- ISFJ,INFJ
-- INTJ,INTP
-- INFP,ENFP
-- INFJ,ISFJ
-- ISTP,ESTP
-- ISTJ,ISFJ
-- INTP,ENTP
