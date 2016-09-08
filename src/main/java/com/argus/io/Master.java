package com.argus.io;

import java.io.Serializable;

/**
 * 要点: 实现序列化接口
 * Created by xingding on 2016/9/8.
 */
public class Master implements Serializable {

    private String name;
    private Integer experience;

    public Master(String name, Integer experience) {
        this.name = name;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Master{" +
                "name='" + name + '\'' +
                ", experience=" + experience +
                '}';
    }
}
