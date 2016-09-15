package com.argus.json;

import java.util.List;

/**
 *
 * Created by xingding on 2016/9/16.
 */
public class Department {
    private String depName;
    private List<Staff> persons;

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public List<Staff> getPersons() {
        return persons;
    }

    public void setPersons(List<Staff> persons) {
        this.persons = persons;
    }
}
