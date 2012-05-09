/**
 * @author qiushaohua 2012-5-3
 */
package com.qiuq.packagedispatch.web;

import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiuq.packagedispatch.service.system.CompanyService;
import com.qiuq.packagedispatch.service.system.UserService;

/**
 * @author qiushaohua 2012-5-3
 * @version 0.0.1
 */
@Component
public class CodeGenerator {

    private final class Code implements Comparable<Code> {
        private int value;
        private String code;

        public Code(int value) {
            this.value = value;
            code = codeGeneratorFormatter.format(value);
        }

        @Override
        public int compareTo(Code o) {
            return value - o.value;
        }
    }

    private NumberFormat codeGeneratorFormatter;

    private CompanyService companyService;

    private UserService userService;

    /** @author qiushaohua 2012-5-8 */
    @Autowired
    public void setCodeGeneratorFormatter(NumberFormat codeGeneratorFormatter) {
        this.codeGeneratorFormatter = codeGeneratorFormatter;
    }

    /** @author qiushaohua 2012-5-4 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /** @author qiushaohua 2012-5-4 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-3
     */
    public String generateCompanyCode() {
        int maxCode = companyService.getMaxCode();
        Code code = generateNewCode(maxCode, 0);

        return code.code;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-3
     */
    public String generateUserCode() {
        int maxCode = userService.getMaxUserCodes();
        Code code = generateNewCode(maxCode, 0);

        return code.code;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-3
     */
    public String generateCustomerCode() {
        int maxCode = userService.getMaxCustomerCodes();
        Code code = generateNewCode(maxCode, 300000);

        return code.code;
    }

    /**
     * @param codes
     * @param initCodeValue
     * @return
     * @author qiushaohua 2012-5-4
     */
    private Code generateNewCode(int maxCode, int initCodeValue) {
        if (maxCode == 0) {
            maxCode = initCodeValue;
        }
        Code code = new Code(maxCode + 1);
        return code;
    }
}
