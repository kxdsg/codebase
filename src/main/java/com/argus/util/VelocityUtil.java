package com.argus.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xingding on 16/6/8.
 */
public class VelocityUtil {


    public static String merge(String templateName, Map<String, Object> paramMap){
        //specify how to load vm template
        Properties p = new Properties();
        p.put("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //initial velocity
        Velocity.init(p);
        //set parameters
        VelocityContext ctx = new VelocityContext();
        Iterator it = paramMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Object> entry = (Map.Entry<String, Object>)it.next();
            ctx.put(entry.getKey(), entry.getValue());
        }
        Template template = null;
        try {
            template = Velocity.getTemplate(templateName);
            StringWriter sw = new StringWriter();
            template.merge(ctx, sw);
            return sw.toString();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String,Object>();
        //usually the value will get from web page form
        params.put("field1", "name");
        params.put("field2", "age");
        String result = merge("vm/first.vm", params);
        System.out.println(result);

    }


}
