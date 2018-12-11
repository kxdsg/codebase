package com.argus.util;

import java.text.DecimalFormat;

/**
 *
 * author: xingding
 * Date: Aug 10, 2011
 * Time: 5:55:36 PM
 * 
 */
public class NumberUtil {


    public static boolean isInteger( String value )
    {
        try {
            Integer.parseInt( value );
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    public static boolean isDouble( String value )
    {
        try {
            Double.parseDouble( value );
            return value.contains( "." );
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    public static String getPercent(long a, long b)
    {
        String percent = "";
        double fa = a * 1.0D;
        double fb = b * 1.0D;
        double fp = fa / fb;
        DecimalFormat df = new DecimalFormat("##.00%");
        percent = df.format(fp);
        return percent;
    }

}
