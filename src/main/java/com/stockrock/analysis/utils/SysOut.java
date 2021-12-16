package com.stockrock.analysis.utils;

public class SysOut {

    private static String TAG_ERROR = "ERROR";
    private static String TAG_INFO = "INFO";
    private static String TAG_DEBUG = "DEBUG";

    public static void e(String message){
        System.out.println(TAG_ERROR + " -> " + message);
    }

    public static void i(String message){
        System.out.println(TAG_INFO + " -> " + message);
    }

    public static void d(String message){
        System.out.println(TAG_DEBUG + " -> " + message);
    }
}
