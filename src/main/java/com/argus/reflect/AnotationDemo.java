package com.argus.reflect;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: xingding
 * Date: Nov 14, 2011
 * Time: 10:47:27 AM
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnotationDemo {
    public String name();
}
