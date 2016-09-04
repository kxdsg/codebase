package com.argus.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;


public class StringUtil {

    public static boolean isNumber(String txtNum) {
        try {
            Double.parseDouble(txtNum);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String extract(int decPlaces, String txt) {
        if (txt.length() >= decPlaces) {
            return txt.substring(0, decPlaces);
        }
        return "";
    }

    public static String crop(int decPlaces, String txt) {
        if (txt.length() >= decPlaces) {
            return txt.substring(decPlaces);
        }
        return "";
    }

    public static String padString(int decPlaces, String txt, String padTxt) {
        if (txt == null) {
            txt = padTxt;
        }
        int numberLength = txt.length();
        if (numberLength < decPlaces) {
            for (int i = 0; i < (decPlaces - numberLength); i++) {
                txt = padTxt + txt;
            }
        } else {
            txt = txt.substring(0, decPlaces);
        }

        return txt;
    }

    public static String bufNumber(int decPlaces, String number) {
        String numberStr = "0";
        try {
            if (number != null) {
                int numberInt = Integer.parseInt(number);
                numberStr = Integer.toString(numberInt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            numberStr = "0";
        }
        return padString(decPlaces, numberStr, "0");
    }

    public static String bufNumber(int decPlaces, int number) {
        String numberStr = Integer.toString(number);
        return padString(decPlaces, numberStr, "0");
    }

    public static String concat(Object... objects) {
        StringBuffer buf = new StringBuffer();
        for (Object obj : objects) {
            buf.append(obj);
        }

        return buf.toString();
    }

    public static int countIdcStr(String s, String substr) {
        int index = -1;
        int num = 0;
        do {
            index = s.indexOf(substr, index + 1);
            if (index >= 0) {
                num++;
            }
        } while (index >= 0);
        return num;
    }
    
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    public static String leftPad(String original, int len, String padStr) {
        return StringUtils.leftPad(original, len, padStr);
    }


    public static String generateRandom(int maxLen) {
        String result = "";
        int i = 0;
        do {
            int intVal = (int) (Math.random() * 26 + 97);
            result = result + (char) intVal;
            i++;

        } while (i < maxLen);

        return result;

    }

    private static Stack<Character> stack = new Stack<Character>();

    public static boolean symmetrical(String value) {
        if (value == null || "".equals(value.trim())) {
            return true;
        }
        int i = 0;
        while (i < value.length()) {
            char ch = value.charAt(i);
            if (ch == '{') {
                stack.push(ch);
            } else if (ch == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
            i++;
        }

        return stack.isEmpty();
    }

    public static boolean palindrome(long value) {
        long newValue = 0;
        long temp = value;
        while (temp != 0) {
            newValue = newValue * 10 + temp % 10;
            temp = temp / 10;
        }
        return (newValue == value);
    }

    public static boolean palindrome(String str) {
        int len = str.length();
        if (len == 0 || len == 1) {
            return true;
        }
        char first = str.charAt(0);
        char last = str.charAt(len - 1);
        if (first != last) {
            return false;
        }
        return palindrome(str.substring(1, len - 1));
    }

    /**
     * stream copy
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while(true){
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) break;
            out.write(buffer, 0, bytesRead);
        }

    }

    /**
     * the method is to illustrate the escape with slash in java
     */
    public static void escapeWithSlash(){
        //original literal string "sdf\a\aa"
        String s = "sdf\\a\\aa"; //declare the variable, append escape \ for per \ automatically
        //replace \ with \\
//        s = s.replace("\\", "\\\\");
//        s = s.replaceAll("\\\\","\\\\\\\\"); // use regular expression, append one more escape \
        System.out.println(s);
    }




    public static void main(String[] args) throws Exception {
//        String origStr = "test ##ruleFilePath## example";
//
//        origStr = origStr.replaceAll("##ruleFilePath##", "d:\\ec\\temp\\input.txt");

//        origStr = origStr.replace("##ruleFilePath##", "d:\\ec\\temp\\input.txt");
//        System.out.println(origStr);

//        escapeWithSlash();
        
//        System.out.println(StringUtils.leftPad(String.valueOf(1), 8, '0'));
    	String origUrl = "http://www.mas.cn/tongcheng/tongcheng/service/InfoBill.queryIndex.do?cid=95&servicecode=APP00011";
    	int index = origUrl.indexOf("servicecode=");
		String serviceCode = "001";
		String url = origUrl.substring(0, index) + "servicecode=" + serviceCode;
		System.out.println(url);
        



    }
    
    
    


}
