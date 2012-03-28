/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.packagedispatch.bean.system.User;

/**
 * @author qiushaohua 2012-3-24
 * @version 0.0.1
 */
public class HttpSessionUtil {

    /**
     * attribute name in HTTP session for user
     */
    private static final String USER_ATTR_NAME = "user";

    /**
     * attribute name in HTTP session for user login credit
     */
    private static final String CREDIT_ATTR_NAME = "credit";

    private HttpSessionUtil() {
    }

    public static User getLoginedUser(WebRequest req) {
        return (User) req.getAttribute(USER_ATTR_NAME, RequestAttributes.SCOPE_SESSION);
    }

    public static void setLoginedUser(WebRequest req, User user) {
        req.setAttribute(USER_ATTR_NAME, user, RequestAttributes.SCOPE_SESSION);
    }

    public static void removeLoginedUser(WebRequest req) {
        req.removeAttribute(USER_ATTR_NAME, RequestAttributes.SCOPE_SESSION);
    }

    public static String getLoginedCredit(WebRequest req) {
        return (String) req.getAttribute(CREDIT_ATTR_NAME, RequestAttributes.SCOPE_SESSION);
    }

    public static void setLoginedCredit(WebRequest req, String credit) {
        req.setAttribute(CREDIT_ATTR_NAME, credit, RequestAttributes.SCOPE_SESSION);
    }

    public static void removeLoginedCredit(WebRequest req) {
        req.removeAttribute(CREDIT_ATTR_NAME, RequestAttributes.SCOPE_SESSION);
    }
}