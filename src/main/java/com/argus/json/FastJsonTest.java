package com.argus.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *  fastjson解析测试类
 * Created by xingding on 2016/9/5.
 */
public class FastJsonTest {

    /*
    Fastjson API入口类是com.alibaba.fastjson.JSON，常用的序列化操作都可以在JSON类上的静态方法直接完成。

    public static final Object parse(String text); // 把JSON文本parse为JSONObject或者JSONArray
    public static final JSONObject parseObject(String text)； // 把JSON文本parse成JSONObject
    public static final <T> T parseObject(String text, Class<T> clazz); // 把JSON文本parse为JavaBean
    public static final JSONArray parseArray(String text); // 把JSON文本parse成JSONArray
    public static final <T> List<T> parseArray(String text, Class<T> clazz); //把JSON文本parse成JavaBean集合
    public static final String toJSONString(Object object); // 将JavaBean序列化为JSON文本
    public static final String toJSONString(Object object, boolean prettyFormat); // 将JavaBean序列化为带格式的JSON文本
    public static final Object toJSON(Object javaObject); 将JavaBean转换为JSONObject或者JSONArray（和上面方法的区别是返回值是不一样的）
     */


    /*  测试报文
        {
        "errCode": "000000",
        "msg": "success",
        "data": {
            "certificateNo": "1234213",
            "name": "李逵",
            "organization": "会计师事务所有限公司",
            "approvalNo": "协字（1998）84号",
            "approvalDate": "1998-11-30",
            "approach": "考试",
            "lastAnnualDate": "2007-04-30",
            "list": [
                {
                    "orgName": "会计师事务所有限公司",
                    "certificateNo": "41210069",
                    "punishType": "11",
                    "punishDate": ""
                }
            ]
        }
    }
     */
    public static void main(String[] args) {

        //map to josn
        Map<String,Object> map = new HashMap<>();
        map.put("name","kang");
        map.put("age",100);
        System.out.println(JSON.toJSONString(map));

        //parse json string
        String jsonStr = "{\"errCode\":\"000000\",\"msg\":\"success\",\"data\":{\"certificateNo\":\"1234213\",\"name\":\"李逵\",\"organization\":\"会计师事务所有限公司\",\"approvalNo\":\"协字（1998）84号\",\"approvalDate\":\"1998-11-30\",\"approach\":\"考试\",\"lastAnnualDate\":\"2007-04-30\",\"list\":[{\"orgName\":\"会计师事务所有限公司\",\"certificateNo\":\"41210069\",\"punishType\":\"11\",\"punishDate\":\"\"}]}}";
        //获取errCode
        JSONObject rootObj = JSON.parseObject(jsonStr);
        //将json字符串转换成person对象
//        Person person = JSON.parseObject(jsonStr, Person.class);
        //将json字符串转换成person对象
//        List<Person> personList = JSON.parseArray(jsonStr,Person.class);
        System.out.println(rootObj.get("errCode"));
        //获取certificateNo
        JSONObject dataObj = rootObj.getJSONObject("data");
        System.out.println(dataObj.get("certificateNo"));
        //获取list
        JSONArray list = dataObj.getJSONArray("list");
        System.out.println(list.size());
        //获取orgName
        for(int i=0;i<list.size();i++){
            JSONObject itemObj = (JSONObject) list.get(i);
            System.out.println(itemObj.get("orgName"));
        }
    }

}
