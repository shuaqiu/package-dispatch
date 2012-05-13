/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.qiuq.common.OperateResult;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Transactional
public interface ResourceService<T> {

    T query(int id);

    OperateResult insert(T t);

    OperateResult delete(int id);

    OperateResult update(T t);

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-5-13
     */
    long matchedRecordCount(Map<String, Object> params);

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-5-13
     */
    List<T> query(String sort, Map<String, Object> params, long[] range);
}
