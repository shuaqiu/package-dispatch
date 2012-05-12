/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;

import com.qiuq.common.OperateResult;


/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public interface ResourceRepository<T> {

    public T query(int id);

    OperateResult insert(T t);

    boolean delete(int id);

    OperateResult update(T t);
}
