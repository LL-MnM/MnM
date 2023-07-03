package com.example.MnM.base.objectStorage;

import com.example.MnM.base.objectStorage.s3.S3FolderName;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ObjectStorageService {

    String uploadImage(MultipartFile file, S3FolderName folderName, String name);

    InputStream getImage(S3FolderName folderName, String name);

    void deleteImage(S3FolderName folderName, String name);

    boolean existsImage(S3FolderName folderName, String name);
}
