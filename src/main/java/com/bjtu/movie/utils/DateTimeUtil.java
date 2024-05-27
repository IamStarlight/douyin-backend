package com.bjtu.movie.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static String createNowTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(System.currentTimeMillis());
    }

    public static Long createNowTimeStamp(){
        return timeToTimeStamp(createNowTimeString());
    }

    public static Long timeToTimeStamp(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    public static String timeStampToTime(Long timestamp){
        ZonedDateTime dateTime = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateTime);
    }

    @SneakyThrows
    public static Date getYearStartDate(String startYear){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
       return df.parse(startYear + "-01-01");
    }

    @SneakyThrows
    public static Date getYearEndDate(String endYear){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(endYear + "-12-31");
    }

}
