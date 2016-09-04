package com.argus.reflect;

/**
 * User: xingding
 * Date: Nov 14, 2011
 * Time: 10:19:14 AM
 */
public class Fruit {

    public String name;

    public Fruit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void output(){
        System.out.println(name);
    }

}
