package com.argus.io;

import java.io.*;

/**
 * 对象序列化与反序列化
 * Created by xingding on 2016/9/8.
 */
public class SerializeTest {

    public static final String filePath = "d:\\test\\m";

    public static void main(String[] args) throws Exception{
        SerializeTest test = new SerializeTest();
        test.serializeObj();
        test.retrieveObj();
    }

    /**
     * 序列化对象
     * @throws Exception
     */
    public void serializeObj() throws Exception{
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filePath));
        Master m = new Master("allen", 14);
        os.writeObject(m);
        os.close();
    }

    /**
     * 反序列化对象
     * @return
     * @throws Exception
     */
    public Object retrieveObj() throws Exception{
        ObjectInputStream oi = new ObjectInputStream(new FileInputStream(filePath));
        Master m = (Master)oi.readObject();
        oi.close();
        System.out.println(m.toString());
        return m;
    }


}
