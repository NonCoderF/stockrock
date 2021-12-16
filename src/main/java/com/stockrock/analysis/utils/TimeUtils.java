package com.stockrock.analysis.utils;

import com.stockrock.analysis.constants.GlobalConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {


    public static String parseCurrentTime(){
        Date date = new Date();
        return date.toString();
    }

    public static String parseSpecificTime(String string){
        try {
            Date date = DateUtil.parse(string);
            return date.toString();
        } catch (ParseException e) {
            SysOut.e(e.getMessage());
        }

        return null;
    }

    public static Date parseStringTime(String time){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(GlobalConstants.DATE_FROMAT);
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String lastNthDay(int n){
        Calendar calendar = Calendar.getInstance();
        CalendarUtil.addDays(calendar, -n);
        return calendar.getTime().toString();
    }

    public static String lastNthMin(int n){
        Calendar calendar = Calendar.getInstance();
        CalendarUtil.addMinutes(calendar, -n);
        return calendar.getTime().toString();
    }

    public static String lastNthHour(int n){
        Calendar calendar = Calendar.getInstance();
        CalendarUtil.addHours(calendar, -n);
        return calendar.getTime().toString();
    }


    public static long getTimeInMilliseconds(String time){
        try {
            Date date = DateUtil.parse(time);
            return date.getTime();
        }catch (Exception e){
            SysOut.e(e.getMessage());
        }
        return -1;
    }

    public static long getTimeInMilliseconds(Date time){
        return time.getTime();
    }

    public static long getTimeInSeconds(String time){
        long ms = getTimeInMilliseconds(time);
        if (ms == -1) return ms;
        return ms/1000;
    }

    public static long getTimeInSeconds(Date time){
        return time.getTime()/1000;
    }

    public static long getTimeInMinutes(String time){
        long ms = getTimeInMilliseconds(time);
        if (ms == -1) return ms;
        return ms/(1000 * 60);
    }

    public static long getTimeInMinutes(Date time){
        return time.getTime()/(1000 * 60);
    }



}
