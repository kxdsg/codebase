package com.argus.json;

/**
 *
 * Created by xingding on 2016/9/16.
 */
public class Staff {

    private String name;
    private String age;
    private boolean isadmin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "name=" + name +
                ", age=" + age +
                ", isadmin=" + isadmin +
                '}';
    }
}
