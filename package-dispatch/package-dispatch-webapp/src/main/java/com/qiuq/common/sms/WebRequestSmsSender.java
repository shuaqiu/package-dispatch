/**
 * @author qiushaohua 2012-4-27
 */
package com.qiuq.common.sms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author qiushaohua 2012-4-27
 * @version 0.0.1
 */
public class WebRequestSmsSender implements SmsSender {

    private RestTemplate restTemplate;

    private String sendAddress;
    private String userCode;
    private String userPwd;
    private String charset;

    /** @author qiushaohua 2012-4-28 */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.sendAddress}")
    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.userCode}")
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.userPwd}")
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.charset}")
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public boolean send(String content, String... numbers) {
        if (numbers.length == 0) {
            return false;
        }

        String url = sendAddress + "&userCode={userCode}&userPwd={userPwd}&msgContent={msgContent}&numbers={numbers}";
        Map<String, Object> restReq = new HashMap<String, Object>();
        restReq.put("userCode", userCode);
        restReq.put("userPwd", userPwd);
        if (!charset.equalsIgnoreCase("utf-8")) {
            url += "&charset={charset}";
            restReq.put("charset", charset.toLowerCase());
        }
        restReq.put("msgContent", content);
        restReq.put("numbers", StringUtils.arrayToCommaDelimitedString(numbers));

        String resp = restTemplate.getForObject(url, String.class, restReq);

        return resp.startsWith("1");
    }

}
