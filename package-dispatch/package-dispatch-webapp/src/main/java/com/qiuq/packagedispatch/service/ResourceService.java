/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Transactional
public interface ResourceService<T> {

    public T query(int id);

    public boolean insert(T t);

    boolean delete(int id);

    boolean update(int id, T t);
}
