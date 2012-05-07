/**
 * @author qiushaohua 2012-3-23
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.packagedispatch.bean.system.Function;
import com.qiuq.packagedispatch.bean.system.Role;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.RoleService;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-3-23
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/index/body")
public class IndexBodyController {

    private RoleService roleService;

    /** @author qiushaohua 2012-3-23 */
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String body(WebRequest req) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return "";
        }

        Map<String, Role> roleMap = getRoleMap(user);
        HttpSessionUtil.setRoleMap(req, roleMap);

        Map<String, Boolean> funcMap = getFunctionMap(user);
        HttpSessionUtil.setFunctionMap(req, funcMap);

        return "main";
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-5-8
     */
    private Map<String, Role> getRoleMap(User user) {
        List<Role> userRoles = roleService.getUserRoles(user);
        Map<String, Role> roleMap = new HashMap<String, Role>();
        for (Role role : userRoles) {
            roleMap.put(role.getId() + "", role);
        }
        return roleMap;
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-5-7
     */
    private Map<String, Boolean> getFunctionMap(User user) {
        Map<String, Boolean> funcMap = new HashMap<String, Boolean>();
        if (user.getType() == Type.TYPE_SELF) {
            List<Function> funcs = roleService.getAccessableFunctions(user);
            for (Function func : funcs) {
                funcMap.put(func.getCode(), true);
            }
        } else {
            funcMap.put("order", true);
            funcMap.put("receiverCompany", true);
            funcMap.put("receiver", true);
            if (user.getCustomerType() == User.CUSTOMER_TYPE_ADMIN) {
                funcMap.put("account", true);
            }
        }
        return funcMap;
    }
}
