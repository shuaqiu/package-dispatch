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
            return "error";
        }

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
        HttpSessionUtil.setFunctionMap(req, funcMap);

        return "main";
    }
}
