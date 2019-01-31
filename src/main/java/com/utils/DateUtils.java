package com.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    private final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    private final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static String formatDateTime(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            return df.format(date);
        }
        return null;
    }

    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
            return df.format(date);
        }
        return null;
    }

}
