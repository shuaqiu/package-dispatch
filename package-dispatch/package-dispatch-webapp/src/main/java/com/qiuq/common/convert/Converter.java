/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.common.convert;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Converter {

    @SuppressWarnings("unchecked")
    public static <T> T mapToBean(Map<String, Object> map, Class<T> type) {
        BeanWrapper wrapper = new BeanWrapperImpl(type);

        MutablePropertyValues value = new MutablePropertyValues(map);
        wrapper.setPropertyValues(value, true);

        return (T) wrapper.getWrappedInstance();
    }
}
