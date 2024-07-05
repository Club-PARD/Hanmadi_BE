package com.pard.namukkun;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Data {
    public static Integer cookieSessionTime = 60 * 60;


    // 현재 시간 리턴하는 메서드
    public static String getNowDate() {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return nowDate.format(formatter);
    }
}
