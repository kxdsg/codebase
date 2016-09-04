package com.argus.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.ArrayList;

/**
 * User: xingding
 * Date: Nov 14, 2011
 * Time: 10:20:49 AM
 */
public class ReflectionDemo {

    public static List<Method> getClassMethodFromAnotation(Class reflectClass, Class anotationClass) {
        List<Method> result = new ArrayList();
        Method[] methodList = reflectClass.getMethods();
        for (Method item : methodList) {
            if (item.getAnnotation(anotationClass) != null) {
                result.add(item);
            }
        }
        return result;
    }

    public static List<Annotation> getAnnotationFromMethod(Class reflectClass, Class anotationClass) {
        List<Annotation> infoList = new ArrayList();
        List<Method> methodList = getClassMethodFromAnotation(reflectClass, anotationClass);
        for (Method item : methodList) {
            infoList.add(item.getAnnotation(anotationClass));
        }
        return infoList;

    }


    public static void init(Person p) {

        Fruit fruit = null;

        int pos = 0;
        List<Method> list = getClassMethodFromAnotation(Person.class, AnotationDemo.class);


        for (Method item : list) {

            AnotationDemo annotation = (AnotationDemo)getAnnotationFromMethod(Person.class, AnotationDemo.class).get(pos);
            String name = annotation.name();
            fruit = new Fruit(name);
            try {
                item.invoke(p, new Object[]{fruit});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }


}
