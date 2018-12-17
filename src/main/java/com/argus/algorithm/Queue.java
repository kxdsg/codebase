package com.argus.algorithm;

/**
 * Created by xingding on 18/12/17.
 * 队列 － 先进先出
 */
public class Queue {

    private int[] array;
    private int number;
    private int front;
    private int end;
    private int max;


    public Queue(int max){
        this.max = max;
        array =  new int[max];
        number = 0;
        front = 0;
        end = 0;
    }

    public void insert(int value){
        if(isFull()){
            System.out.println("queue is full, can't insert");
            return;
        }
        array[end] = value;
        end++;
        number++;
    }

    public int remove(){
        if(isEmpty()){
            System.out.println("queue is empty, can't remove");
            return -1;
        }
        number--;
        return array[front++];
    }

    public boolean isFull(){
        return number == max;
    }

    public boolean isEmpty(){
        return number == 0;
    }

    public int size(){
        return number;
    }

    public int peekFront(){
        return array[front];
    }


    public static void main(String[] args) {
        Queue q = new Queue(4);
        q.insert(1);
        q.insert(2);
        q.insert(3);
        q.insert(4);
        q.insert(5);
        System.out.println(q.size());
        System.out.println("front is " + q.peekFront());
        while(!q.isEmpty()){
            System.out.println(q.remove());
        }
    }

}