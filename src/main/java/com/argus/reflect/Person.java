package com.argus.reflect;

/**
 * User: xingding
 * Date: Nov 14, 2011
 * Time: 10:18:36 AM
 */
public class Person {

    public String name;

    public Fruit fruit;


    @AnotationDemo(name = "apple")
    public void eat(Fruit fruit) {
        this.fruit = fruit;
    }

    public static void main(String[] args) {
        Person inst = new Person();
        inst.test();
    }

    public void test() {
        ReflectionDemo.init(this);
        fruit.output();
    }

}
