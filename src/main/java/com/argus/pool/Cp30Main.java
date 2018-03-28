package com.argus.pool;

import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/3/24.
 */
public class Cp30Main {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DataSource unpooled = DataSources.unpooledDataSource(
                    "jdbc:mysql://127.0.0.1:3306/test",
                    "root",
                    "admin123");
            DataSource pooled = DataSources.pooledDataSource(unpooled);
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            conn = pooled.getConnection(); //第一次获取数据库连接
            System.out.println("conn class type is " + conn.getClass().getName());
            //com.mchange.v2.c3p0.impl.NewProxyConnection

            Object o1 = getInner(conn);
            System.out.println("inner conn class type is " + o1.getClass().getName());
            //com.mysql.cj.jdbc.ConnectionImpl

            stmt = conn.createStatement();
            stmt.execute("insert into user values(1,'zhangsan','m','1986-06-24','byy','kxdsg@123.com','remark')");
            rs = stmt.executeQuery("select * from user");
            while(rs.next()){
                System.out.println("data from db:" + rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();


            TimeUnit.SECONDS.sleep(5);

            conn = pooled.getConnection(); //第二次取得数据库连接
            Object o2 = getInner(conn);
            if(o1==o2){
                System.out.println("o1 and o2 is same object");
            }
            //相等说明并没有真正地关闭，只是放回了连接池中

            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from user");
            while(rs.next()){
                System.out.println("data from db:" + rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();





        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getInner(Object conn){
        Object re = null;
        Field f;
        try {
            //NewProxyConnection 中定义了 protected Connection inner;
            f = conn.getClass().getDeclaredField("inner");
            f.setAccessible(true);
            re = f.get(conn);//返回对象obj中表示字段的值，也就是Connection
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }


}
