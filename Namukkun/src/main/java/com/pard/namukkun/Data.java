package com.pard.namukkun;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class Data {
    public static Integer cookieSessionTime = 60 * 60;
    public static String timeFormatString = "yyyy-MM-dd";

    // 현재 시간 리턴하는 메서드 년-월-일
    public static String getNowDateYYYYMMdd() {
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatString);
        return nowDate.format(formatter);
    }

    // 현재 시간 리턴하는 메서드 년-월-일-시-분-초
    public static String getNowDateYYYYMMddHHmmss() {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return nowDate.format(formatter);
    }

    public static Long getDeadLine(String postTime){

        // postTime을 LocalDate로 변환시킴

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatString);
        LocalDate date = LocalDate.parse(postTime, formatter);

        // 7일 더함
        LocalDate plus7Days = date.plusDays(7);

        // 현재 날짜와 7일 후 날짜를 계산
        LocalDate nowDate = LocalDate.now();
        Long daysBetween = ChronoUnit.DAYS.between(nowDate, plus7Days);

        // 만약 차이가 0보다 작으면 그냥 0 반환시킴
        return daysBetween < 0 ? 0 : daysBetween;
    }
}
