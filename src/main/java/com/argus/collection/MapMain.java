package com.argus.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xingding on 18/3/24.
 */
public class MapMain {

    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put("1","a");
        map.put("2","b");
        map.put("3","c");
        map.put("4","d");
        Iterator it = map.keySet().iterator();
        while(it.hasNext()){
            String name = (String)it.next();
            System.out.println(name+":" + map.get(name));

        }
    }
}
