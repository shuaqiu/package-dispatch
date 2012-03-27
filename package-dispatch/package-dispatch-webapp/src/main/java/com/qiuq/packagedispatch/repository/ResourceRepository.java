/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.repository;


/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public interface ResourceRepository<T> {

    public T query(int id);

    boolean insert(T t);

    boolean delete(int id);

    boolean update(int id, T t);
}
