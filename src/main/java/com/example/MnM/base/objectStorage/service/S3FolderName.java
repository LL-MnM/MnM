package com.example.MnM.base.objectStorage.service;

public enum S3FolderName {

    USER;

    String getFolderName(String name) {
        return "%s/%s".formatted(this.name(), name);
    }

}
