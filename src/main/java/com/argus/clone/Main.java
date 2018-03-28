package com.argus.clone;

import java.util.Vector;

/**
 * Created by xingding on 18/3/25.
 */
public class Main {

    public static void main(String[] args) {
        Student s1 = new Student();
        Vector cs = new Vector();
        cs.add("Java");
        s1.setId(1);
        s1.setName("Kang");
        s1.setCourses(cs);

        Student s2 = s1.newInstance();
        s2.setId(2);
        s2.setName("Xing");
        System.out.println("s1 name: " + s1.getName());
        System.out.println("s2 name: " + s2.getName());
        System.out.println(s1.getCourses() == s2.getCourses());

        System.out.println(s2.getCourses());
    }
}
