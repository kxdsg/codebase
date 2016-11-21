package com.argus.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * Created by xingding on 2016/11/21.
 */
public class SfjsonTest {

    public static void main(String[] args) {
        JSONObject contentJsonObject = null;
        String jsonStr = "{\"errCode\":\"000000\",\"msg\":\"success\",\"data\":{\"orgName\":\"abc\",\"healthWorkers\":\"75\"}}";
        contentJsonObject = JSONObject.fromObject(jsonStr);
        JSONArray dataArray = JSONArray.fromObject(contentJsonObject.get("data"));
        for (Object contObj : dataArray) {
            JSONObject dataObject = (JSONObject) contObj;
            //当不存在这个节点时候，会抛出如下异常
            //Exception in thread "main" net.sf.json.JSONException: JSONObject["geriatricCooperation"] not found.
            String geriatricCooperation = dataObject.getString("geriatricCooperation");
            System.out.println(geriatricCooperation);
        }


    }
}
