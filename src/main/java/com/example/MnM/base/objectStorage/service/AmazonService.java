package com.example.MnM.base.objectStorage.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.MnM.base.objectStorage.ObjectStorageService;
import com.example.MnM.base.objectStorage.TikaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonService implements ObjectStorageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Override
    public String uploadImage(MultipartFile multipartFile, S3FolderName folderName, String name) {

        TikaService.checkMimeType(multipartFile);

        String key = folderName.getFolderName(name);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {

            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Override
    public InputStream getImage(S3FolderName folderName, String name) {

        if (amazonS3Client.doesObjectExist(bucket, folderName.getFolderName(name))) {
            S3Object object = amazonS3Client.getObject(bucket, folderName.getFolderName(name));
            return object.getObjectContent();
        }
        return InputStream.nullInputStream();
    }

    @Override
    public void deleteImage(S3FolderName folderName, String name) {
        String key = folderName.getFolderName(name);

        if (amazonS3Client.doesObjectExist(bucket, key)) {
            amazonS3Client.deleteObject(bucket, key);
            log.info("delete object = {}", key);
        }
    }

    @Override
    public boolean existsImage(S3FolderName folderName, String name) {
        return amazonS3Client.doesObjectExist(bucket,folderName.getFolderName(name));
    }
}
