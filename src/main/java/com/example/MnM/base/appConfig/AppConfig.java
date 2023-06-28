package com.example.MnM.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class AppConfig {
    @Getter
    private static String key;

    @Value("${custom.cookie.key}")
    public void setLikeablePersonFromMax(String key) {
        AppConfig.key = key;
    }

    @Getter
    private static int tokenValiditySeconds;

    @Value("${custom.cookie.tokenValiditySeconds}")
    public void setLikeablePersonModifyCoolTime(int tokenValiditySeconds) {
        AppConfig.tokenValiditySeconds = tokenValiditySeconds;
    }

}