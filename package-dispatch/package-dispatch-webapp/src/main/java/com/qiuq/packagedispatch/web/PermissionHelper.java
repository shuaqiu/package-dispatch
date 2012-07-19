/**
 * @author qiushaohua 2012-7-11
 */
package com.qiuq.packagedispatch.web;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author qiushaohua 2012-7-11
 * @version 0.0.1
 */
@Component
public class PermissionHelper {

    /**
     * @param functionMap
     * @param permission
     * @return
     * @author qiushaohua 2012-5-26
     */
    public boolean isNotPermit(Map<String, Boolean> functionMap, String permission) {
        if (functionMap == null) {
            return true;
        }
        Boolean isHasFunction = functionMap.get(permission);
        if (isHasFunction == null) {
            return true;
        }
        if (isHasFunction) {
            return false;
        }
        return true;
    }
}
