package com.example.MnM.base.objectStorage;

import com.example.MnM.base.exception.InvalidImageFormatException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class TikaServiceTest {

    @DisplayName("이미지 확인 테스트")
    @Test
    void isImage() {
        MockMultipartFile fakeFile =
                new MockMultipartFile("testFile", "fileName", "notImage", "fake image content".getBytes());

        Assertions.assertThatThrownBy(() -> TikaService.checkMimeType(fakeFile))
                .isInstanceOf(InvalidImageFormatException.class)
                .hasMessage("이미지 파일이 아닙니다.");
    }

}