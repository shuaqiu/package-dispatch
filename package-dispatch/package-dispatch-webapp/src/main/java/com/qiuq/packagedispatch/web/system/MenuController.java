/**
 * @author qiushaohua 2012-3-23
 */
package com.qiuq.packagedispatch.web.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.packagedispatch.bean.system.Role;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * @author qiushaohua 2012-3-23
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController {

    private UserService userService;

    /** @author qiushaohua 2012-3-23 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getMenu(WebRequest req) {

        Object role = req.getAttribute("role", RequestAttributes.SCOPE_SESSION);
        if (role == null) {
            User user = (User) req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
            if (user == null) {
                return "error";
            }
            List<Role> roles = userService.getUserRoles(user);
        }

        return "menu";
    }
}
