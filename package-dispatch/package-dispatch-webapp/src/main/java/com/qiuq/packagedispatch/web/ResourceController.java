/**
 * @author qiushaohua 2012-3-28
 */
package com.qiuq.packagedispatch.web;

import com.qiuq.common.OperateResult;

/**
 * @author qiushaohua 2012-3-28
 * @version 0.0.1
 */
public interface ResourceController<T> {

    OperateResult delete(int id);

    OperateResult update(int id, T t);

    OperateResult insert(T t);

    T query(int id);

}
