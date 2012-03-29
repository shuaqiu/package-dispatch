/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.service.ResourceService;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public abstract class AbstractResourceController<T> implements ResourceController<T> {

    protected abstract ResourceService<T> getService();

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public T query(@PathVariable int id) {
        T t = getService().query(id);
        return t;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(@RequestBody T t) {
        boolean isInserted = getService().insert(t);
        if (isInserted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new resource fail");
        }
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult update(@PathVariable int id, @RequestBody T t) {
        boolean isUpdated = getService().update(id, t);
        if (isUpdated) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.UPDATE_FAIL, "update resource fail");
        }
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(@PathVariable int id) {
        boolean isDeleted = getService().delete(id);
        if (isDeleted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.DELETE_FAIL, "delete resource fail");
        }
    }
}
