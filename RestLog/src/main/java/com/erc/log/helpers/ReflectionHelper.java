package com.erc.log.helpers;

import com.erc.dal.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectionHelper {

    public static Object getFieldValue(Object instance, String fieldName) {
        Field field = null;
        try {
            field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<java.lang.reflect.Field> getFields(Object entity) {
        ArrayList<java.lang.reflect.Field> fields = new ArrayList<java.lang.reflect.Field>();
        try {
            java.lang.reflect.Field[] allFields = entity.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : allFields) {
                if (field.isAnnotationPresent(com.erc.dal.Field.class)) {
                    fields.add(field);
                }
            }
        } catch (Exception e) {
            Log.e("Error: getFields", e);
        }
        return fields;
    }

    public static Object getInstance(Class baseClass, Object[] paramsConstructor, Class[] paramsConstructorTypes) {
        Object object = null;
        try {
            Class<?> clazz = Class.forName(baseClass.getName());
            Constructor<?> ctor = clazz.getConstructor(paramsConstructorTypes);
            object = ctor.newInstance(paramsConstructor);
        } catch (Exception e) {
            Log.e("Please make sure that exist a Default constructor without parameters...", e);
        }
        return object;
    }


}
