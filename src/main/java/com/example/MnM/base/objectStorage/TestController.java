package com.example.MnM.base.objectStorage;

import com.amazonaws.util.IOUtils;
import com.example.MnM.base.objectStorage.service.S3FolderName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Controller
public class TestController {

    private final ObjectStorageService s3Service;
    private final ObjectRepository objectRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @ResponseBody
    @PostMapping("/make")
    public ObjectFile makeBucket(@RequestParam(value = "file") MultipartFile file, String name) {
        String url = s3Service.uploadImage(file, S3FolderName.USER, name);
        ObjectFile objectFile = new ObjectFile();
        objectFile.setUrl(url);
        redisTemplate.opsForValue().set("helloworld",1);
        return objectRepository.save(objectFile);
    }

    @ResponseBody
    @GetMapping(value = "/show", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] makeBucket(String name) {
        ObjectFile url = objectRepository.findByUrl(name);
        InputStream imageStream = s3Service.getImage(S3FolderName.USER, name);
        redisTemplate.opsForValue().get("helloworld");
        try {
            return IOUtils.toByteArray(imageStream);
        } catch (IOException e) {
            // 에러 처리
        }
        return null; // 에러 처리를 위한 기본값
    }

    @ResponseBody
    @PostMapping("/delete")
    public String deleteBucket(String name) {
        s3Service.deleteImage(S3FolderName.USER, name);
        return "delete 성공";
    }
}
