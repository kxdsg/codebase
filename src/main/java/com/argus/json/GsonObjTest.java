package com.argus.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 *
 * Created by xingding on 2016/9/16.
 */
public class GsonObjTest {

    private static  Gson gson = new Gson();

    public static void main(String[] args) {
//        String json = "{\"name\":\"kang\",\t\"age\":20,\"isadmin\": true}";
//        jsonToObj(json);

        /*
        formatted json
        {
            "depName": "interface",
            "persons": [{
                "name": "kang",
                "age": 20,
                "isadmin": true
            }, {
                "name": "bob",
                "age": 21,
                "isadmin": false
            }, {
                "name": "lucy",
                "age": 18,
                "isadmin": false
            }]
        }
         */

//        String complexJson = "{\"depName\":\"interface\",\"persons\":[{\"name\":\"kang\",\"age\":20,\"isadmin\":true},{\"name\":\"bob\",\"age\":21,\"isadmin\":false},{\"name\":\"lucy\",\"age\":18,\"isadmin\":false}]}";
//
//        complexJsonToObj(complexJson);

        String listJson = "[{\"name\":\"kang\",\"age\":20,\"isadmin\":true},{\"name\":\"bob\",\"age\":21,\"isadmin\":false},{\"name\":\"lucy\",\"age\":18,\"isadmin\":false}]";
        jsonToList(listJson);

    }

    /**
     * 把简单json 转成javabean对象
     * @param json
     */
    public static void jsonToObj(String json){
        Staff p = gson.fromJson(json, Staff.class);
        System.out.println(p.toString());
    }

    /**
     * 把复杂嵌套json 转成 javabean对象(包含普通属性、集合属性)
     * @param json
     */
    public static void complexJsonToObj(String json){
        Department department = gson.fromJson(json, Department.class);
        System.out.println(department.getDepName());
        System.out.println(department.getPersons());
    }

    /**
     * 把数组json字符串转成集合
     * @param json
     */
    public static void jsonToList(String json){
        List<Staff> personList = gson.fromJson(json, new TypeToken<List<Staff>>(){}.getType());
        System.out.println(personList.size());
        System.out.println(personList.get(0));
    }

}
