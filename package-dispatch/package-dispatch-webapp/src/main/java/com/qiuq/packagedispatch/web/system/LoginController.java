/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.web.HttpSessionUtil;
import com.qiuq.packagedispatch.web.RestAddressBuilder;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private RestTemplate restTemplate;

    private RestAddressBuilder restAddressBuilder;

    /** @author qiushaohua 2012-3-19 */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** @author qiushaohua 2012-3-25 */
    @Autowired
    public void setRestAddressBuilder(RestAddressBuilder restAddressBuilder) {
        this.restAddressBuilder = restAddressBuilder;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{usercode}", method = RequestMethod.POST)
    @ResponseBody
    public OperateResult login(@PathVariable String usercode, @RequestParam String password, WebRequest req) {
        if (!StringUtils.hasText(usercode) || !StringUtils.hasText(password)) {
            return new OperateResult(ErrCode.NULL, "username or password is empty");
        }

        String restUrl = restAddressBuilder.buildRestUrl("api/login/{usercode}");
        Map<String, Object> restReq = new HashMap<String, Object>();
        restReq.put("password", password);
        Map<String, Object> rmap = restTemplate.postForObject(restUrl, restReq, Map.class, usercode);

        OperateResult result;
        if ((Boolean) rmap.get("ok")) {
            User user = Converter.mapToBean((Map<String, Object>) rmap.get("user"), User.class);
            HttpSessionUtil.setLoginedUser(req, user);
            HttpSessionUtil.setLoginedCredit(req, (String) rmap.get("credit"));

            result = new OperateResult(true, user);
        } else {
            ErrCode errCode = ErrCode.valueOf((String) rmap.get("errCode"));
            result = new OperateResult(errCode, (String) rmap.get("message"));
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public OperateResult isLogin(WebRequest req) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user != null) {
            return OperateResult.OK;
        }

        return new OperateResult(ErrCode.NOT_FOUND, "can't find a logined user");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult logout(WebRequest req) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.NULL, "there are already not user in HTTP session");
        }

        String loginedCredit = HttpSessionUtil.getLoginedCredit(req);
        restTemplate.delete(restAddressBuilder.buildRestUrl("api/login/{credit}"), loginedCredit);

        HttpSessionUtil.removeLoginedUser(req);
        HttpSessionUtil.removeLoginedCredit(req);

        HttpSessionUtil.removeRoleMap(req);
        HttpSessionUtil.removeFunctionMap(req);

        return OperateResult.OK;
    }
}
