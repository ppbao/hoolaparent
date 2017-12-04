package com.unisharing.hoola.hoolacommon.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HoolaBeanUtils {

	public static Map convertToMap(Object obj) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();
		Class clazz = obj.getClass();
		BeanInfo bean = Introspector.getBeanInfo(clazz);

		PropertyDescriptor[] propertys = bean.getPropertyDescriptors();
		for (PropertyDescriptor property : propertys) {
			
			String propertyName = property.getName();
	            if (!propertyName.equals("class")) { 
	                Method readMethod = property.getReadMethod(); 
	                Object result = readMethod.invoke(bean, new Object[0]); 
	                if (result != null) { 
	                	map.put(propertyName, result); 
	                } else { 
	                	map.put(propertyName, ""); 
	                } 
	            } 
		}

		return null;
	}

}
