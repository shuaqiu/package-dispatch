/**
 * @author qiushaohua 2012-3-27
 */
package com.qiuq.packagedispatch.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.service.ResourceService;

/**
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
public abstract class AbstractResourceController<T> implements ResourceController<T> {

    protected Log logger = LogFactory.getLog(getClass());

    protected abstract ResourceService<T> getService();

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public T query(@PathVariable int id) {
        OperateResult beforeResult = beforeQuery(id);
        if (beforeResult.isOk() == false) {
            return null;
        }
        T t = getService().query(id);
        afterQuery(id, t);
        return t;
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-5-8
     */
    protected OperateResult beforeQuery(int id) {
        return OperateResult.OK;
    }

    /**
     * @param id
     * @param t
     * @author qiushaohua 2012-5-8
     */
    protected void afterQuery(int id, T t) {
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(@RequestBody T t) {
        OperateResult beforeResult = beforeInsert(t);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult insertResult = getService().insert(t);

        return afterInsert(t, insertResult);

    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-5-8
     */
    protected OperateResult beforeInsert(T t) {
        return OperateResult.OK;
    }

    /**
     * @param t
     * @param insertResult
     * @return
     * @author qiushaohua 2012-5-10
     */
    protected OperateResult afterInsert(T t, OperateResult insertResult) {
        return insertResult;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult update(@PathVariable int id, @RequestBody T t) {
        OperateResult beforeResult = beforeUpdate(t);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult updateResult = getService().update(t);
        return afterUpdate(t, updateResult);
    }

    /**
     * @param t
     * @return
     * @author qiushaohua 2012-5-8
     */
    protected OperateResult beforeUpdate(T t) {
        return OperateResult.OK;
    }

    /**
     * @param t
     * @param updateResult
     * @return
     * @author qiushaohua 2012-5-10
     */
    protected OperateResult afterUpdate(T t, OperateResult updateResult) {
        return updateResult;
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(@PathVariable int id) {
        OperateResult beforeResult = beforeDelete(id);
        if (beforeResult.isOk() == false) {
            return beforeResult;
        }

        OperateResult isDeleted = getService().delete(id);
        return afterDelete(id, isDeleted);
    }

    /**
     * @param id
     * @return
     * @author qiushaohua 2012-5-8
     */
    protected OperateResult beforeDelete(int id) {
        return OperateResult.OK;
    }

    /**
     * @param id
     * @param deleteResult
     * @return
     * @author qiushaohua 2012-5-10
     */
    protected OperateResult afterDelete(int id, OperateResult deleteResult) {
        return deleteResult;
    }

    /**
     * @param range
     * @return
     * @author qiushaohua 2012-4-2
     */
    protected long[] range(String range) {
        if (StringUtils.hasText(range) && range.length() > 1) {
            // items=0-24
            String[] arr = range.split("=");
            if (arr.length != 2) {
                return null;
            }

            arr = arr[1].split("-");
            if (arr.length != 2) {
                return null;
            }
            Long b = NumberUtils.parseNumber(arr[0], Long.class);
            Long e = NumberUtils.parseNumber(arr[1], Long.class);
            return new long[] {
                    b + 1, e + 1
            };
        }

        return null;
    }

    protected Map<String, Object> parseParameter(MultiValueMap<String, String> params) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        for (String key : params.keySet()) {
            List<String> list = params.get(key);
            if (list == null || list.size() == 0) {
            } else if (list.size() == 1) {
                map.put(key, list.get(0));
            } else {
                map.put(key, list);
            }
        }

        return map;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-3
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-3
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public Map<String, Object> edit() {
        return null;
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-5-10
     */
    protected HttpEntity<List<T>> doQuery(String sort, Map<String, Object> params, String range) {
        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = getService().matchedRecordCount(params);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<T> list = getService().query(sort, params, range(range));
        HttpEntity<List<T>> entity = new HttpEntity<List<T>>(list, header);

        return entity;
    }

    /**
     * @param functionMap
     * @param permission
     * @return
     * @author qiushaohua 2012-5-26
     */
    protected boolean isNotPermission(Map<String, Boolean> functionMap, String permission) {
        if (functionMap == null) {
            return true;
        }
        Boolean isHasFunction = functionMap.get(permission);
        if (isHasFunction == null) {
            return true;
        }
        if (isHasFunction) {
            return false;
        }
        return true;
    }
}
