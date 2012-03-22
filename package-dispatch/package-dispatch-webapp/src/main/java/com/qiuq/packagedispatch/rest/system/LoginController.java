/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.rest.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private static final Map<String, Object> LOGINED = new HashMap<String, Object>();

    private UserService userService;

    /** @author qiushaohua 2012-3-20 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{usercode}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@PathVariable String usercode, @RequestBody Map<String, String> req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        String password = req.get("password");
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

    @RequestMapping(value = "/{credit}", method = RequestMethod.DELETE)
    @ResponseBody
    public void logout(@PathVariable String credit) {
        LOGINED.remove(credit);
    }
}
