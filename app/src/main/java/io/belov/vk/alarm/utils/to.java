/*
 * to
 * Copyright (c) 2012 Cybervision. All rights reserved.
 */
package io.belov.vk.alarm.utils;


import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public class to {

    public static Integer integer(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Integer) {
            return (Integer) val;
        } else {
            return integer(val.toString(), null);
        }
    }

    public static Integer integer(String val) {
        return integer(val, null);
    }

    public static Integer integer(Object val, Integer defaultValue) {
        if (val != null) {
            try {
                return doIntConvert(val);
            } catch (Exception e) {
            }
        }

        return defaultValue;
    }

    public static Long l(Object val, Long defaultValue) {
        if (val != null) {
            try {
                return doLongConvert(val);
            } catch (Exception e) {
            }
        }

        return defaultValue;
    }

    public static Long l(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Long) {
            return (Long) val;
        } else {
            return l(val.toString(), null);
        }
    }

    public static Long l(String val) {
        return l(val, null);
    }

    public static URI uri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static Object[] arr(Object... objects) {
        return objects;
    }

    public static String s(String s, Object... params) {
        return s; //todo
    }

    public static Map map(Object key, Object value) {
        Map a = new HashMap();
        a.put(key, value);
        return a;
    }

    public static <T> List<T> list(T value) {
        List<T> answer = new ArrayList<>();
        answer.add(value);
        return answer;
    }

    public static <T> List<T> list(T... value) {
        List<T> answer = new ArrayList<>();
        Collections.addAll(answer, value);
        return answer;
    }

    public static <T> Set<T> set(T value) {
        Set<T> answer = new HashSet<>();
        answer.add(value);
        return answer;
    }

    public static <T> Set<T> set(T... value) {
        Set<T> answer = new HashSet<>();
        Collections.addAll(answer, value);
        return answer;
    }

    public static <T> SortedSet<T> sortedSet(T value) {
        SortedSet<T> answer = new TreeSet<>();
        answer.add(value);
        return answer;
    }

    public static <T> SortedSet<T> sortedSet(T... value) {
        SortedSet<T> answer = new TreeSet<>();
        Collections.addAll(answer, value);
        return answer;
    }

    private static Integer doIntConvert(BigDecimal val) {
        return val.intValue();
    }

    private static Integer doIntConvert(Long val) {
        return val.intValue();
    }

    private static Integer doIntConvert(Integer val) {
        return val;
    }

    private static Integer doIntConvert(Object val) {
        return Integer.parseInt(val.toString());
    }

    private static Long doLongConvert(BigDecimal val) {
        return val.longValue();
    }

    private static Long doLongConvert(Long val) {
        return val;
    }

    private static Long doLongConvert(Integer val) {
        return val.longValue();
    }

    private static Long doLongConvert(Object val) {
        return Long.parseLong(val.toString());
    }

}
