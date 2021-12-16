package com.stockrock.analysis.utils;

public class Debug {

    public static void breakpoint(Object object, int breakPoint){
        SysOut.d(object.getClass().getName() + " : Breakpoint - " + breakPoint);
    }
}
