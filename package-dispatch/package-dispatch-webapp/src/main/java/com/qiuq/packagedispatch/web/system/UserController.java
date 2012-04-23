/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.convert.Converter;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

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

    private PasswordEncoder passwordEncoder;

    /** @author qiushaohua 2012-3-27 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /** @author qiushaohua 2012-4-23 */
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected ResourceService<User> getService() {
        return userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<User>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {

        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", getControllerUserType());
        params.put("query", query);

        if (user.getType() == Type.TYPE_CUSTOMER && user.getCustomerType() == User.CUSTOMER_TYPE_ADMIN) {
            params.put("companyId", user.getCompanyId());
        }

        long[] rangeArr = range(range);

        HttpHeaders header = new HttpHeaders();
        if (rangeArr != null) {
            long count = userService.matchedRecordCount(params);
            header.set("Content-Range", " items " + (rangeArr[0] - 1) + "-" + (rangeArr[1] - 1) + "/" + count);
        }

        List<User> list = userService.query(sort, params, rangeArr);
        HttpEntity<List<User>> entity = new HttpEntity<List<User>>(list, header);

        return entity;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-8
     */
    protected int getControllerUserType() {
        return Type.TYPE_SELF;
    }

    /**
     * @param req
     * @param params
     * @return
     * @author qiushaohua 2012-4-22
     */
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    @ResponseBody
    public OperateResult modifyPassword(WebRequest req, @RequestBody Map<String, Object> params) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return null;
        }

        String currentPassword = Converter.toString(params.get("currentPassword"));
        String newPassword = Converter.toString(params.get("newPassword"));
        if (!StringUtils.hasText(currentPassword) || !StringUtils.hasText(newPassword)) {
            return new OperateResult(ErrCode.NULL, "password can't be empty");
        }

        boolean isPasswordValid = passwordEncoder.isPasswordValid(user.getPassword(), currentPassword, user.getSalt());

        if (!isPasswordValid) {
            return new OperateResult(ErrCode.NOT_CORRECT, "Current password is incorrect");
        }

        return userService.modifyPassword(user, newPassword);
    }
}
