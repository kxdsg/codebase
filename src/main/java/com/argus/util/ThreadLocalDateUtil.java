package com.argus.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xingding on 18/10/24.
 *
 */
public class ThreadLocalDateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<>();

    public static DateFormat getDateFormat(){
        DateFormat df = threadLocal.get();
        if(df == null){
            df = new SimpleDateFormat(DATE_FORMAT);
            threadLocal.set(df);
        }
        return df;
    }

    public static String formatDate(Date date) throws Exception{
        return getDateFormat().format(date);
    }

    public static Date parseDate(String strDate) throws Exception{
        return getDateFormat().parse(strDate);
    }

}
