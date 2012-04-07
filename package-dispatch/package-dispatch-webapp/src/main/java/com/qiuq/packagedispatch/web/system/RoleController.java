/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.RoleService;
import com.qiuq.packagedispatch.web.AbstractResourceController;

/**
 * Manage the user
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends AbstractResourceController<Map<String, Object>> {

    private RoleService roleService;

    /** @author qiushaohua 2012-4-8 */
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    protected ResourceService<Map<String, Object>> getService() {
        return roleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Map<String, Object>>> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("query", query);

        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = roleService.matchedRecordCount(params);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<Map<String, Object>> list = roleService.query(sort, params, rangeArr);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<List<Map<String, Object>>>(list, header);

        return entity;
    }
}
