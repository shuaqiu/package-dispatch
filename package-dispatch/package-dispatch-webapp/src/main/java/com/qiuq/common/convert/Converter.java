/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.common.convert;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.NumberUtils;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Converter {

    /**
     * Create an object with the special type, and initial its value from the map.
     * 
     * @param map
     * @param type
     * @return
     * @author qiushaohua 2012-3-19
     */
    @SuppressWarnings("unchecked")
    public static <T> T mapToBean(Map<String, Object> map, Class<T> type) {
        BeanWrapper wrapper = new BeanWrapperImpl(type);

        MutablePropertyValues value = new MutablePropertyValues(map);
        wrapper.setPropertyValues(value, true);

        return (T) wrapper.getWrappedInstance();
    }

    /**
     * Try to convert the object to a string if possible, otherwise return an empty string ("").
     * 
     * @param object
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static String toString(Object object) {
        return toString(object, "");
    }

    /**
     * Try to convert the object to a primitive int value if possible, otherwise return the default value.
     * 
     * @param object
     * @param defaultValue
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static String toString(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        return object.toString().trim();
    }

    /**
     * Try to convert the object to a primitive int value if possible, otherwise return 0.
     * 
     * @param object
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static int toInt(Object object) {
        return toInt(object, 0);
    }

    /**
     * Try to convert the object to a primitive int value if possible, otherwise return the default value.
     * 
     * @param object
     * @param defaultValue
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static int toInt(Object object, int defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return NumberUtils.parseNumber(object.toString().trim(), Integer.class);
        } catch (Exception e) {
        }

        try {
            return NumberUtils.parseNumber(object.toString().trim(), Long.class).intValue();
        } catch (Exception e) {
        }

        try {
            return (int) Double.parseDouble(object.toString().trim());
        } catch (NumberFormatException e) {
        }

        return defaultValue;
    }

    /**
     * Try to convert the object to a primitive long value if possible, otherwise return 0.
     * 
     * @param object
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static long toLong(Object object) {
        return toLong(object, 0);
    }

    /**
     * Try to convert the object to a primitive long value if possible, otherwise return the default value.
     * 
     * @param object
     * @param defaultValue
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static long toLong(Object object, long defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return NumberUtils.parseNumber(object.toString().trim(), Long.class);
        } catch (Exception e) {
        }

        try {
            return (long) Double.parseDouble(object.toString().trim());
        } catch (NumberFormatException e) {
        }

        return defaultValue;
    }

    /**
     * Try to convert the object to a primitive double value if possible, otherwise return 0.0.
     * 
     * @param object
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static double toDouble(Object object) {
        return toDouble(object, 0.0);
    }

    /**
     * Try to convert the object to a primitive double value if possible, otherwise return the default value.
     * 
     * @param object
     * @param defaultValue
     * @return
     * @author qiushaohua 2012-4-4
     */
    public static double toDouble(Object object, double defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString().trim());
        } catch (NumberFormatException e) {
        }

        return defaultValue;
    }
}
