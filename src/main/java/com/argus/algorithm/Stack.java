package com.argus.algorithm;

/**
 * Created by xingding on 18/12/17.
 * 栈 － 后进先出
 */
public class Stack {
    private char[] array;
    private int max;
    private int top;

    public Stack() {

    }

    public Stack(int max) {
        this.max = max;
        array = new char[max];
        top = 0;
    }

    public void push(char value) {
        array[top] = value;
        top++;
    }

    public char pop() {
        --top;
        return array[top];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public boolean isFull() {
        return top == max;
    }

    public void display() {
        while (!isEmpty()) {
            System.out.println(pop());
        }
    }

    public String reverse(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            push(c);
        }
        while (!isEmpty()) {
            sb.append(pop());
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        Stack cs = new Stack(5);
        cs.push('1');
        cs.push('2');
        cs.push('3');
        cs.push('4');
        cs.push('5');
        cs.display();

        String s = "abcd";
        Stack stack = new Stack(s.length());
        System.out.println(stack.reverse(s));
    }
}
