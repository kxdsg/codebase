package com.argus.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.lang.System.in;

/**
 * @author xingding
 * @date 17/7/28.
 */
public class TryRes {

    public static void main(String[] args) {
        try {
            try(Scanner in = new Scanner(new FileInputStream("in.txt"));
                    PrintWriter pw = new PrintWriter("out.txt")
            ) {
                while(in.hasNext()){
                    pw.println(in.next().toUpperCase());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
