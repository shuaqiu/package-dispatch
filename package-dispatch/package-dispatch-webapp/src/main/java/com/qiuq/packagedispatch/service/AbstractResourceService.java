/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.service;

import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.repository.ResourceRepository;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public abstract class AbstractResourceService<T> implements ResourceService<T> {

    protected abstract ResourceRepository<T> getRepository();

    @Override
    @Transactional(readOnly = true)
    public T query(int id) {
        return getRepository().query(id);
    }

    @Override
    @Transactional
    public boolean insert(T t) {
        return getRepository().insert(t);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return getRepository().delete(id);
    }

    @Override
    @Transactional
    public boolean update(int id, T t) {
        return getRepository().update(id, t);
    }

}
