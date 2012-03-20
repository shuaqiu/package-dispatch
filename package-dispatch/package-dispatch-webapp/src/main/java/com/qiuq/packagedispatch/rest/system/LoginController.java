/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.rest.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
public class LoginController {
    private static final Map<String, Object> LOGINED = new HashMap<String, Object>();

    private UserService userService;

    /** @author qiushaohua 2012-3-20 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String usercode, @RequestParam String password) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        if (!StringUtils.hasText(usercode) || !StringUtils.hasText(password)) {
            rmap.put("ok", false);
            rmap.put("errCode", ErrCode.NULL);
            return rmap;
        }

        User user = userService.query(usercode, password);
        if (user == null) {
            rmap.put("ok", false);
            rmap.put("errCode", ErrCode.NOT_FOUND);
            return rmap;
        }

        String credit = generateLoginCredit(usercode);
        LOGINED.put(credit, user);

        rmap.put("ok", true);
        rmap.put("user", user);
        rmap.put("credit", credit);

        return rmap;
    }

    private String generateLoginCredit(String usercode) {
        return usercode + System.currentTimeMillis();
    }
}
