/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
@SessionAttributes("user")
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
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String usercode, @RequestParam String password) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        if (!StringUtils.hasText(usercode) || !StringUtils.hasText(password)) {
            rmap.put("ok", false);
            rmap.put("errCode", ErrCode.NULL);
            return rmap;
        }

        Map<String, Object> req = new HashMap<String, Object>();
        req.put("usercode", usercode);
        req.put("password", password);
        rmap = restTemplate.postForObject(webRestAddress + "/api/login", req, Map.class);

        return rmap;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map<String, Object> isLogin(WebRequest req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        Object user = req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        rmap.put("ok", user != null);

        return rmap;
    }
}
