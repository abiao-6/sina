package com.abiao.sina.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateFormatUtils {

    /**
     * 传入year month为null时的默认方法
     * @return
     */
    public static Date getMonthFirstDay(){
        return getMonthFirstDay(null, null);
    }

    /**
     * 根据传入的年月获取当月起始时间
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthFirstDay(Integer year, Integer month){
        Date now = new Date();
        int queryYear = now.getYear();
        int queryMonth = now.getMonth();
        if (year != null){
            queryYear = year - 1900;
        }

        if (month != null){
            queryMonth = month - 1;
        }

        return new Date(queryYear, queryMonth,1);
    }

    /**
     * 传入year为null时的默认方法
     * @return
     */
    public static Date getYearFirstDay(){
        return getYearFirstDay(null);
    }

    /**
     * 根据传入的年获取当年起始时间
     * @param year
     * @return
     */
    public static Date getYearFirstDay(Integer year){
        Date now = new Date();
        int queryYear = now.getYear();
        if (year != null){
            queryYear = year - 1900;
        }

        return new Date(queryYear, Calendar.JANUARY,1);
    }

    public static String formatYearMonthDayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String formatYearMonthDayHourMinuteSecondDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatHourMinuteSecondDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatHourMinuteDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static Date parseYearMonthDayDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("日期解析错误，原始值：" + date, e);
            }
        }
        return null;
    }

    public static Date parseYearMonthDayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("日期解析错误，原始值：" + date, e);
            }
        }
        return null;
    }

    public static String formatYearDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    public static Date parseYearDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String format = sdf.format(date);
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("日期解析错误，原始值：" + date, e);
            }
        }
        return null;
    }

    public static Date parseYearDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("日期解析错误，原始值：" + date, e);
            }
        }
        return null;
    }
}
