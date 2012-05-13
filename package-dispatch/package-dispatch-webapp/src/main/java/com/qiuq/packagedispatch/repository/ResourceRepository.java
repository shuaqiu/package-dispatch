/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

import java.util.List;
import java.util.Map;

import com.qiuq.common.OperateResult;


/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public interface ResourceRepository<T> {

    public T query(int id);

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
