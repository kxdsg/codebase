package com.argus.util;

import java.util.Map;
import java.util.HashMap;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


public class ObjUtil {

    public static void setProperties(Object message, Map valueMap)
			throws Exception {
		if ((message == null) || (valueMap == null)) {
			return;
		}

		BeanInfo beanInfo = null;

		beanInfo = Introspector.getBeanInfo(message.getClass());

		for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
			String pName = p.getName();

			if (valueMap.containsKey(pName)) {
				Method writeMethod = p.getWriteMethod();
				if (writeMethod != null) {
					Object[] values = new Object[1];
					values[0] = valueMap.get(pName);
					writeMethod.invoke(message, values[0]);
				}
			}

		}

	}

	public static Map getProperties(Object message) {
		Map valueMap = new HashMap();

		try {
			if (message == null) {
				return valueMap;
			}

			BeanInfo beanInfo = Introspector.getBeanInfo(message.getClass());

			for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
				String pName = p.getName();

				Method readMethod = p.getReadMethod();
				if (readMethod == null) {
					return valueMap;
				}

				// Call the property getter and return the value
				Object value = readMethod.invoke(message, new Object[0]);
				if (value != null) {
					valueMap.put(pName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueMap;

	}

}
