package com.shop.util;

import java.lang.reflect.Field;
import java.util.*;

public class CommonUtil {
    public static List<Integer> getIntListFromList(Collection<?> list, String field) {
        if (list == null) {
            throw new RuntimeException("list can not be null");
        }
        List<Integer> intList = new ArrayList<>(list.size());
        for (Object item : list) {
            Class<?> clazz = item.getClass();
            Field declaredField = null;
            try {
                declaredField = clazz.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            declaredField.setAccessible(true);
            try {
                intList.add((Integer) declaredField.get(item));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return intList;
    }

    public static String generateFileName(String originalName) {
        String uuid =  UUID.randomUUID().toString().replaceAll("-", "");
        if (!originalName.contains("."))
            return uuid + ".jpg";
        return uuid + originalName.substring(originalName.lastIndexOf("."));
    }

}
