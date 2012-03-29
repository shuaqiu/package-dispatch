/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.CompanyService;
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

    private CompanyService companyService;

    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /** @author qiushaohua 2012-3-29 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return null;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Map<String, Object> create() {
        companyService.query("+id", "");
        return null;
    }
    
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public Map<String, Object> company(){
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> query(@RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query) {
        List<User> coms = userService.query(sort, query);
        return coms;
    }

    @Override
    protected ResourceService<User> getService() {
        return userService;
    }
}
