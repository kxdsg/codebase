package com.argus.ws;

import javax.jws.WebService;

/**
 * Created by xingding on 18/5/5.
 */
@WebService
public interface Business {
    public String echo(String message);
}
