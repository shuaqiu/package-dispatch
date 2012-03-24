/**
 * @author qiushaohua 2012-3-24
 */
package com.qiuq.packagedispatch.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author qiushaohua 2012-3-24
 * @version 0.0.1
 */
@Component
public class RestAddressBuilder {

    private String restAddress;

    /** @author qiushaohua 2012-3-19 */
    @Value("${web.rest.address}")
    public void setRestAddress(String restAddress) {
        this.restAddress = restAddress;
    }

    public String buildRestUrl(String url) {
        if (!StringUtils.hasText(restAddress)) {
            throw new IllegalStateException("base rest address is not initialized");
        }
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("rest url is needed");
        }

        if (restAddress.endsWith("/")) {
            if (url.startsWith("/")) {
                return restAddress + url.substring(1);
            }
            return restAddress + url;
        }
        if (url.startsWith("/")) {
            return restAddress + url;
        }
        return restAddress + "/" + url;
    }
}
