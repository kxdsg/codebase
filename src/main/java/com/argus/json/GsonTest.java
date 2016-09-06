package com.argus.json;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * google.code.gson测试类
 * Created by xingding on 2016/9/5.
 */
public class GsonTest {

    private static Gson gson ;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();//如果值为null，也会序列化
        gson = builder.create();
    }

    public static void main(String[] args) {
//        mapToJson();
//        jsonToMap();
        wrapResult();
    }

    /**
     * map转成json string
     * @param
     * @return
     */
    public static String mapToJson(){
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name","argus");
        m.put("age",30);
        m.put("salary",null);
        String jsonStr = gson.toJson(m);
        System.out.println(jsonStr);
        /*
        {"age":30,"name":"argus","salary":null}
         */
        return jsonStr;
    }

    /**
     * json string 转成map,这个方法尽量不使用，通常结合java bean使用
     * @return
     */
    public static Map<String,Object> jsonToMap(){
        String jsonStr = "{\"age\":30,\"name\":\"argus\",\"salary\":null}";
        Map<String,Object> map = gson.fromJson(jsonStr,Map.class);
        System.out.println(map);
        /*
        {age=30.0, name=argus, salary=null}
        这里是个坑,age变成了30.0，在做接口转换时，注意避免使用转换方法对原始接口的返回做操作，见wrapResult方法
         */
        return map;
    }

    /**
     * 报文转换
     * 例如：
     * {"status":0,"reason":"","content":{"name":"argus","age":30}}
     * ==>
     * {"errCode":"000000","msg":"success","data":{"name":"argus","age":30}}
     * @return
     */
    public static String wrapResult(){
        //第三方原始接口报文
        String origJson = "{\"status\":0,\"reason\":\"\",\"content\":{\"name\":\"argus\",\"age\":30}}";
        String result = "";
        //获取原始接口的值

        /* 如果返回字段有int类型，避免采用这种方式
         Map<String, Object> orig = new HashMap<String, Object>();
         orig = gson.fromJson(origJson, Map.class);
         System.out.println(orig); //{status=0.0, reason=, content={name=argus, age=30.0}} status,age的值都变了，错误
        */

        //正确的方式
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(origJson);
        JsonObject rootObj = element.getAsJsonObject();
        int status = rootObj.get("status").getAsInt();
        String reason = rootObj.get("reason").getAsString();
        JsonObject content = rootObj.get("content").getAsJsonObject();
        String name = content.get("name").getAsString();
        int age = content.get("age").getAsInt();
        //组装目标返回报文
        Map<String, Object> map = new HashMap<String, Object>();
        if(status==0){
            map.put("errCode","000000");
        } else {
            map.put("errCode", StringUtils.leftPad(String.valueOf(status),6,"0"));
        }
        if(StringUtils.isEmpty(reason)){
            map.put("msg","success");
        } else {
            map.put("msg","error");
        }
        Map<String,Object> contentMap = new HashMap<String, Object>();
        contentMap.put("name",name);
        contentMap.put("age",age);
        map.put("data", contentMap);
        System.out.println(gson.toJson(map));
        return result;
    }

}
