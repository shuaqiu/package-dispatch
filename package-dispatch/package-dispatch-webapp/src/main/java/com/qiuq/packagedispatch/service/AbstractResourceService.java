/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.qiuq.common.OperateResult;
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
        OperateResult beforeResult = beforeQuery(id);
        if (beforeResult.isOk() == false) {
            return null;
        }
        T t = getRepository().query(id);
        afterQuery(id, t);
        return t;
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult beforeQuery(int id) {
        return OperateResult.OK;
    }

    /**
     * @param id
     * @param t
     * @author qiushaohua 2012-5-14
     */
    protected void afterQuery(int id, T t) {
    }

    @Override
    @Transactional
    public OperateResult insert(T t) {
        OperateResult beforeResult = beforeInsert(t);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult insertResult = getRepository().insert(t);

        return afterInsert(t, insertResult);
    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult beforeInsert(T t) {
        return OperateResult.OK;
    }

    /**
     * @param t
     * @param insertResult
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult afterInsert(T t, OperateResult insertResult) {
        return insertResult;
    }

    @Override
    @Transactional
    public OperateResult delete(int id) {
        OperateResult beforeResult = beforeDelete(id);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult deleteResult = getRepository().delete(id);
        return afterDelete(id, deleteResult);
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult beforeDelete(int id) {
        return OperateResult.OK;
    }

    /**
     * @param id
     * @param deleteResult
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult afterDelete(int id, OperateResult deleteResult) {
        return deleteResult;
    }

    @Override
    @Transactional
    public OperateResult update(T t) {
        OperateResult beforeResult = beforeUpdate(t);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult updateResult = getRepository().update(t);
        return afterUpdate(t, updateResult);
    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult beforeUpdate(T t) {
        return OperateResult.OK;
    }

    /**
     * @param t
     * @param updateResult
     * @return
     * @author qiushaohua 2012-5-14
     */
    protected OperateResult afterUpdate(T t, OperateResult updateResult) {
        return updateResult;
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> query(String sort, Map<String, Object> params, long[] range) {
        return getRepository().query(sort, params, range);
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Override
    @Transactional(readOnly = true)
    public long matchedRecordCount(Map<String, Object> params) {
        return getRepository().matchedRecordCount(params);
    }

}
