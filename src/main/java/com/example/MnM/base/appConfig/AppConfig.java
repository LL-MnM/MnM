package com.example.MnM.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
    @Getter
    private static String siteName;
    @Getter
    private static String siteBaseUrl;

    private static String activeProfile;
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


    @Getter
    private static String chatUrl;

    @Value("${custom.site.baseUrl}")
    public void setChatUrl(String chatUrl) {
        AppConfig.chatUrl = chatUrl;
    }

    public static boolean isNotProd() {
        return !isProd();
    }

    public static boolean isProd() {
        return activeProfile.equals("prod");

    }

    @Value("${spring.profiles.active}")
    public void setActiveProfile(String value) {
        activeProfile = value;
    }

    @Value("${custom.siteName}")
    public void setSiteName(String siteName) {
        AppConfig.siteName = siteName;
    }

    @Value("${custom.site.baseUrlWithPort}")
    public void setSiteBaseUrl(String baseUrlWithPort) {
        AppConfig.siteBaseUrl = baseUrlWithPort;
    }

}