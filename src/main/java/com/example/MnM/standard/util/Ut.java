package com.example.MnM.standard.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Ut {

    public static class json {

        public static String toStr(Map map) {
            try {
                return new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class url {
        public static String encode(String str) {
            return URLEncoder.encode(str, StandardCharsets.UTF_8);
        }

        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);

            return url;
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (!url.contains("?")) {
                url += "?";
            }

            if (!url.endsWith("?") && !url.endsWith("&")) {
                url += "&";
            }

            url += paramName + "=" + paramValue;

            return url;
        }

        private static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);

            return url.substring(0, startPoint) + urlAfter;
        }
    }

    public static String getTempPassword(int length) {
        int index = 0;
        char[] charArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }

        return sb.toString();
    }

    public static class time {
        public static String diffFormat1Human(LocalDateTime time1, LocalDateTime time2) {
            String suffix = time1.isAfter(time2) ? "전" : "후";

            // 두개의 시간의 차이를 초로 환산
            long diff = Math.abs(ChronoUnit.SECONDS.between(time1, time2));

            long diffSeconds = diff % 60; // 초 부분만
            long diffMinutes = diff / (60) % 60; // 분 부분만
            long diffHours = diff / (60 * 60) % 24; // 시간 부분만
            long diffDays = diff / (60 * 60 * 24); // 나머지는 일 부분으로

            StringBuilder sb = new StringBuilder();

            if (diffDays > 0) sb.append(diffDays).append("일 ");
            if (diffHours > 0) sb.append(diffHours).append("시간 ");
            if (diffMinutes > 0) sb.append(diffMinutes).append("분 ");
            if (diffSeconds > 0) sb.append(diffSeconds).append("초 ");

            if (sb.isEmpty()) sb.append("1초 ");

            return sb.append(suffix).toString();
        }
    }
}
