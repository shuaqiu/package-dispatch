/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.system.User;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    @Value("${web.rest.address}")
    private String webRestAddress;

    private RestTemplate restTemplate;

    /** @author qiushaohua 2012-3-19 */
    public void setWebRestAddress(String webRestAddress) {
        this.webRestAddress = webRestAddress;
    }

    /** @author qiushaohua 2012-3-19 */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{usercode}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@PathVariable String usercode, @RequestParam String password, WebRequest req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        if (!StringUtils.hasText(usercode) || !StringUtils.hasText(password)) {
            rmap.put("ok", false);
            rmap.put("errCode", ErrCode.NULL);
            return rmap;
        }

        Map<String, Object> restReq = new HashMap<String, Object>();
        restReq.put("password", password);
        rmap = restTemplate.postForObject(webRestAddress + "/api/login/{usercode}", restReq, Map.class, usercode);

        if ((Boolean) rmap.get("ok")) {
            User user = Converter.mapToBean((Map<String, Object>) rmap.get("user"), User.class);
            req.setAttribute("user", user, RequestAttributes.SCOPE_SESSION);
            req.setAttribute("credit", rmap.get("credit"), RequestAttributes.SCOPE_SESSION);
        }

        return rmap;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> isLogin(WebRequest req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        Object user = req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        rmap.put("ok", user != null);

        return rmap;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> logout(WebRequest req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        Object user = req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        if (user == null) {
            rmap.put("ok", false);
            rmap.put("errCode", ErrCode.NULL);
            return rmap;
        }

        Object credit = req.getAttribute("credit", RequestAttributes.SCOPE_SESSION);
        restTemplate.delete(webRestAddress + "/api/login/{credit}", credit);

        req.removeAttribute("user", RequestAttributes.SCOPE_SESSION);
        req.removeAttribute("credit", RequestAttributes.SCOPE_SESSION);
        rmap.put("ok", true);

        return rmap;
    }
}
