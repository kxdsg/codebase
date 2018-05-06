package com.argus.ws;

/**
 * Created by xingding on 18/5/5.
 */
public class BusinessImpl implements Business {
    @Override
    public String echo(String message) {
        if("quit".equals(message)){
            System.out.println("Server will be shutdown!");
            System.exit(0);
        }
        System.out.println("message from client: " + message);
        return "Server Response: " + message;
    }
}
