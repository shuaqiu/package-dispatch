/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * Manage the user
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Map<String, Object> create() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query) {
        List<User> coms = userService.query(sort, query);
        return coms;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(@RequestBody User user) {
        boolean isInserted = userService.insert(user);
        if (isInserted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.INSERT_FAIL, "add new user fail");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User query(@PathVariable int id) {
        User user = userService.query(id);
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult update(@PathVariable int id, @RequestBody User user) {
        boolean isUpdated = userService.update(id, user);
        if (isUpdated) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.UPDATE_FAIL, "update fail");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(@PathVariable int id) {
        boolean isDeleted = userService.delete(id);
        if (isDeleted) {
            return OperateResult.OK;
        } else {
            return new OperateResult(ErrCode.DELETE_FAIL, "delete fail");
        }
    }
}
