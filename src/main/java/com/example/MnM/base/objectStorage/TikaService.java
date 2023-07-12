package com.example.MnM.base.objectStorage;

import com.example.MnM.base.exception.imageStorage.InvalidImageFormatException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class TikaService {

    private static boolean isImage(String mimeType) {
        MediaType mediaType = MediaType.parse(mimeType);
        return mediaType != null && mediaType.getType().equals("image");
    }

    public static void checkMimeType(MultipartFile file) {
        Tika tika = new Tika();
        String mimeType;

        try {
            mimeType = tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!TikaService.isImage(mimeType)) {
            throw new InvalidImageFormatException("이미지 파일이 아닙니다.");
        }
    }
}
