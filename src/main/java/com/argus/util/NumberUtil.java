package com.argus.util;

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

}
