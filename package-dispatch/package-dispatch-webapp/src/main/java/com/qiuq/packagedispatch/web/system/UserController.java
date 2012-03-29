/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

<<<<<<< HEAD
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
=======
import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.UserService;
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d

/**
 * Manage the user
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/user")
<<<<<<< HEAD
public class UserController extends AbstractResourceController<User> {

    private UserService userService;

    private CompanyService companyService;

=======
public class UserController {

    private UserService userService;

>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

<<<<<<< HEAD
    /** @author qiushaohua 2012-3-29 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

=======
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Map<String, Object> create() {
<<<<<<< HEAD
        companyService.query("+id", "");
=======
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query) {
        List<User> coms = userService.query(sort, query);
        return coms;
    }

<<<<<<< HEAD
    @Override
    protected ResourceService<User> getService() {
        return userService;
=======
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
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
    }
}
