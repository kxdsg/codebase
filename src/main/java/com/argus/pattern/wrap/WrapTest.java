package com.argus.pattern.wrap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author xingding
 * @date 2017/8/6.
 */
public class WrapTest {


    public static void main(String[] args) {
        int c;
        try {
            InputStream in = new LowerCaseInputStream(new BufferedInputStream(new FileInputStream("out.txt")));
            while((c=in.read())>=0){
                System.out.print((char)c);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
