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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.CodeGenerator;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * Manage the user
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractResourceController<Map<String, Object>> {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    protected CodeGenerator codeGenerator;

    private CompanyService companyService;

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

    /** @author qiushaohua 2012-5-8 */
    @Autowired
    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    /** @author qiushaohua 2012-5-13 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    protected ResourceService<Map<String, Object>> getService() {
        return userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Map<String, Object>>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
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

        List<Map<String, Object>> list = userService.query(sort, params, rangeArr);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<List<Map<String, Object>>>(list, header);

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
            return new OperateResult(ErrCode.NOT_LOGINED, "no user is logined");
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

    @RequestMapping(value = "/check/{loginAccount}")
    @ResponseBody
    public Map<String, Integer> getUserCount(@PathVariable String loginAccount, @RequestParam(defaultValue = "-1") int id) {
        int count = userService.getUserCount(loginAccount, id);
        Map<String, Integer> rmap = new HashMap<String, Integer>();
        rmap.put("count", count);
        return rmap;
    }

    @Override
    protected OperateResult beforeInsert(Map<String, Object> t) {
        OperateResult checkResult = checkLoginAccount(t);
        if (!checkResult.isOk()) {
            return checkResult;
        }

        int companyId = Converter.toInt(t.get("companyId"), -1);
        if (companyId == -1) {
            companyId = getCompanyId(t);
            if (companyId == -1) {
                return new OperateResult(ErrCode.OPERATE_FAIL, "could not insert the company");
            }
            t.put("companyId", companyId);
        }

        t.put("code", generateCode());
        return super.beforeInsert(t);
    }

    /**
     * @return
     * @author qiushaohua 2012-5-8
     */
    protected String generateCode() {
        return codeGenerator.generateUserCode();
    }

    @Override
    protected OperateResult beforeUpdate(Map<String, Object> t) {
        OperateResult checkResult = checkLoginAccount(t);
        if (!checkResult.isOk()) {
            return checkResult;
        }

        int companyId = Converter.toInt(t.get("companyId"), -1);
        if (companyId == -1) {
            companyId = getCompanyId(t);
            if (companyId == -1) {
                return new OperateResult(ErrCode.OPERATE_FAIL, "could not insert the company");
            }
            t.put("companyId", companyId);
        }

        return super.beforeUpdate(t);
    }

    /**
     * @param t
     * @author qiushaohua 2012-5-12
     * @return
     */
    private OperateResult checkLoginAccount(Map<String, Object> t) {
        String loginAccount = Converter.toString(t.get("loginAccount"));
        int id = Converter.toInt(t.get("id"));
        int count = userService.getUserCount(loginAccount, id);
        if (count > 0) {
            return new OperateResult(ErrCode.DUPLICATE, "such account is already exist");
        }
        return OperateResult.OK;
    }

    private int getCompanyId(Map<String, Object> map) {
        String name = Converter.toString(map.get("company"));

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("nameOp", "=");
        List<Company> companys = companyService.query("+id", params, null);
        if (companys.size() > 0) {
            return companys.get(0).getId();
        }

        Company company = new Company();
        company.setCode(codeGenerator.generateCompanyCode());
        company.setName(name);
        company.setAddress(Converter.toString(map.get("address")));

        OperateResult insertResult = companyService.insert(company);
        if (insertResult.isOk()) {
            GeneratedKeyHolder keyHolder = (GeneratedKeyHolder) insertResult.getObj();
            return keyHolder.getKey().intValue();
        }
        return -1;
    }
}
