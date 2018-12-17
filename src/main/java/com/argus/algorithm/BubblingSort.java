package com.argus.algorithm;

/**
 * Created by xingding on 18/12/17.
 * 冒泡排序
 */
public class BubblingSort {
    public static void main(String[] args) {
        int[] data = new int[]{14, 7, 22, 19, 31};
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j + 1];
                    data[j + 1] = data[j];
                    data[j] = temp;

                }
            }
        }

        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]);
            System.out.print(" ");
        }

    }
}
