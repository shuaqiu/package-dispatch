/**
 * @author qiushaohua 2012-3-4
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * @author qiushaohua 2012-3-4
 * @version 0.0.1
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Map<String, Object> index(WebRequest req) {
        Map<String, Object> rmap = new HashMap<String, Object>();

        Object user = req.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        rmap.put("isLogined", user == null);

        return rmap;
    }
}
