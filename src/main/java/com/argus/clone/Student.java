package com.argus.clone;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/3/25.
 */
public class Student implements Cloneable {

    private Integer id;
    private String name;
    private Vector courses;
    public Student(){
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("student constructor called");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector getCourses() {
        return courses;
    }

    public void setCourses(Vector courses) {
        this.courses = courses;
    }

    public Student newInstance(){
        try {
            return (Student)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
