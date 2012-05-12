/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.service;

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

    boolean delete(int id);

    OperateResult update(T t);
}
