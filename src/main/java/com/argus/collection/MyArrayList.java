package com.argus.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author xingding
 * @date 2017/8/5.
 */
public class MyArrayList<T> implements Iterable<T> {
    public static final int DEFAULT_CAPACITY = 10;

    private int theSize;
    private T[] theItems;

    public MyArrayList(){
        clear();
    }

    public void clear(){
        theSize = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    public boolean isEmpty(){
        return theSize == 0;
    }

    public void ensureCapacity(int newCapacity){
        if(newCapacity <theSize){
            return;
        }
        T[] old = theItems;
        theItems = (T[])new Object[newCapacity];
        for(int i=0;i<theSize;i++){
            theItems[i] = old[i];
        }
    }

    public T get(int index){
        if(index<0||index>=theSize){
            throw new ArrayIndexOutOfBoundsException();
        }
        return theItems[index];
    }

    public T set(int index, T newVal){
        if(index<0||index>=theSize){
            throw new ArrayIndexOutOfBoundsException();
        }
        T old = theItems[index];
        theItems[index] = newVal;
        return old;
    }

    public boolean add(T x){
        add(theSize,x);
        return true;
    }

    public void add(int index, T x){
        if(theItems.length == theSize){
            ensureCapacity(theSize*2+1);
        }
        for(int i = theSize; i>index;i--){
            theItems[i] = theItems[i-1];
        }
        theItems[index] = x;
        theSize++;
    }

    public T remove(int index){
        T removeItem = theItems[index];
        for(int i=index;i<theSize-1;i++){
            theItems[i] = theItems[i+1];
        }
        theSize--;
        return removeItem;
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();//自己实现Iterator
    }

    private class ArrayListIterator implements Iterator<T>{
        private int current = 0;

        public boolean hasNext(){
            return current<theSize;
        }

        public T next(){
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return theItems[current++];
        }
        public void remove(){
            MyArrayList.this.remove(--current);
        }
    }


}
