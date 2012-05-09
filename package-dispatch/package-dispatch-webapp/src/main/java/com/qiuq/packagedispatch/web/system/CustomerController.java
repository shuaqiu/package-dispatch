/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web.system;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qiuq.packagedispatch.bean.system.Type;

/**
 * Manage the customer
 * 
 * @author qiushaohua 2012-3-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController extends UserController {

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public Map<String, Object> company(){
        return null;
    }

    /**
     * @return
     * @author qiushaohua 2012-4-8
     */
    @Override
    protected int getControllerUserType() {
        return Type.TYPE_CUSTOMER;
    }

    @Override
    protected String generateCode() {
        return codeGenerator.generateCustomerCode();
    }
}
