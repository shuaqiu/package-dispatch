/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

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

import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.AbstractResourceController;

/**
 * Manage the user
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractResourceController<User> {

    private UserService userService;


    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected ResourceService<User> getService() {
        return userService;
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public Map<String, Object> company(){
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<User>> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {

        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = userService.matchedRecordCount(query);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<User> list = userService.query(sort, query, rangeArr);
        HttpEntity<List<User>> entity = new HttpEntity<List<User>>(list, header);

        return entity;
    }
}
