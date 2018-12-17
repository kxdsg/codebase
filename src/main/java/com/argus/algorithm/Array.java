package com.argus.algorithm;

/**
 * Created by xingding on 18/12/17.
 */
public class Array {
    private int[] array;
    private int number;
    private int max;

    public Array(int max) {
        this.max = max;
        array = new int[max];
    }

    public void insert(int value) {
        array[number] = value;
        number++;
    }

    public int find(int value) {
        int index = 0;
        for (int i = 0; i < number; i++) {
            if (array[i] == value) {
                index = i;
                return index;
            }
        }
        return -1;
    }

    public boolean delete(int value) {
        int index = find(value);
        //can find the value
        if (index != -1) {
            for (int i = index; i < number - 1; i++) {
                //left move
                array[i] = array[i + 1];
            }
            number--;
            return true;
        }
        return false;
    }

    public void display() {
        for (int i = 0; i < number; i++) {
            System.out.println(array[i]);
        }
    }

    public static void main(String[] args) {
        Array a = new Array(5);
        a.insert(1);
        a.insert(2);
        a.insert(3);
        a.insert(4);
        a.insert(5);
        a.display();
        if (a.find(6) != -1) {
            System.out.println("find the value");
        } else {
            System.out.println("can't find");
        }
        if (!a.delete(6)) {
            System.out.println("can't delete");
        }

        a.display();
    }

}