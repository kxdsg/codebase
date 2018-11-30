package com.argus.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
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
        map.put("entryDate",new Date());
        System.out.println(JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd"));

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


        /*
        String jsonStr = "[{\"subColumnId\":684,\"masterColumnId\":665,\"subColumnName\":\"remark\",\"masterColumnName\":\"icon_url\"},{\"subColumnId\":686,\"masterColumnId\":671,\"subColumnName\":\"table_id\",\"masterColumnName\":\"res_desc\"}]";
        JSONArray jsonArray = JSON.parseArray(jsonStr);
        StringBuilder sb = new StringBuilder();
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj!=null){
                    String masterColumnName = obj.getString("masterColumnName");
                    String subColumnName = obj.getString("subColumnName");
                    if(!StringUtils.isEmpty(masterColumnName) && !StringUtils.isEmpty(subColumnName)){
                        sb.append(masterColumnName).append("=").append(subColumnName).append(" and ");
                    }
                }
            }
        }
        String s = sb.toString();
        if(s.endsWith(" and ")){
            s = s.substring(0,s.lastIndexOf(" and "));
        }
        System.out.println(s);
        */


        /*
        Map map = new HashMap();
        map.put("account","kxdsg");
        map.put("password","123456");
        String json = JSON.toJSONString(map);
        System.out.println(json);
        */


        String jsonVal = "[\"a\",\"b\",\"c\"]";
        JSONArray array = JSON.parseArray(jsonVal);
        System.out.println(array);
        for(int i=0;i<array.size();i++){
            if("a".equalsIgnoreCase(array.getString(i))){
                System.out.println("match");
            }
        }


        String resultJson = "{\n" +
                "    \"code\": \"000000\",\n" +
                "    \"message\": null,\n" +
                "    \"result\": \"data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAACA0lEQVQ4T62U0Y3TcAzGPzsnEAia\\nbEBGSJUOUB4hReoIYQKywYUJriMcG/QEgcfrvTdqRwgbJKXAgYiNnF6kHG2jgvg/VXL9s2P7+wj/\\n+dHH5WYqkDct13Eevn4xfFT8ax26Xqm3xTYwAItMAZRR6KZdYJZv4igcXJ5ShLp/er/6MuZakmjk\\nGrh5H/IqATQmwFPl88lo8K4PfA+4A5TrSeg1HVv336RaP2Y3uMWtV9c/UyL1hZ301fDp4hC4F5jl\\n1UyBYhK6szY5W20DyK+ZKvTQvPeA2bKaA/pMgZKIxqp6sBMQAgJ53TgR3ewD8ypVha8O7y2Baomb\\nTkkDgC4JuBDm53cLHTehP+dgi7HEycjdJXdelld329exzZFFrqPQbRi7GBV7QFvEV6kW7WJOBi6r\\nuTg82wM21ZZlGY087+86LIso9PzDwLxcgM+SaPhkfXKHeS+wSklp/XI0mJ8CbAQhdRqF3vhIh5sY\\nkHNV3NM0EXwroIBHFiMK7GxMRSC6MskeBFqSVe2cgwrzTbdbFvXtXgH6LExFq5yjwEaGy8pubbcc\\nQhGFbmI/P62++yI/FnaLpnPw2bSddz+wo+vu5u3mTEkmSbM/JQ1ah+oFZnlZgLnxShV5295mAzEH\\nYr5QqRMQX7X21gu0TzOHMaDjPEi7xmseCUisoHnXPHqBfb53LPYbPe4rk1TBXRgAAAAASUVORK5C\\nYII=\",\n" +
                "    \"status\": \"OK\"\n" +
                "}";
        JSONObject object = JSON.parseObject(resultJson);
        System.out.println(object.getString("result"));

    }


}
