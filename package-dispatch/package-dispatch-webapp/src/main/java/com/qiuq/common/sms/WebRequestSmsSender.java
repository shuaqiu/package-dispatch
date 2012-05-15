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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author qiushaohua 2012-4-27
 * @version 0.0.1
 */
public class WebRequestSmsSender implements SmsSender {

    private RestTemplate restTemplate;

    private String address;
    private String usercode;
    private String password;

    private String method;
    private String usercodeParameter;
    private String passwordParameter;
    private String mobileParameter;
    private String contentParameter;

    /** @author qiushaohua 2012-4-28 */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.address}")
    public void setAddress(String address) {
        this.address = address;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.usercode}")
    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    /** @author qiushaohua 2012-4-27 */
    @Value("${web.sms.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    /** @author qiushaohua 2012-5-15 */
    @Value("${web.sms.method}")
    public void setMethod(String method) {
        this.method = method;
    }

    /** @author qiushaohua 2012-5-15 */
    @Value("${web.sms.parameter.usercode}")
    public void setUsercodeParameter(String usercodeParameter) {
        this.usercodeParameter = usercodeParameter;
    }

    /** @author qiushaohua 2012-5-15 */
    @Value("${web.sms.parameter.password}")
    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    /** @author qiushaohua 2012-5-15 */
    @Value("${web.sms.parameter.mobile}")
    public void setMobileParameter(String mobileParameter) {
        this.mobileParameter = mobileParameter;
    }

    /** @author qiushaohua 2012-5-15 */
    @Value("${web.sms.parameter.content}")
    public void setContentParameter(String contentParameter) {
        this.contentParameter = contentParameter;
    }

    @Override
    public boolean send(String content, String... numbers) {
        if (numbers.length == 0) {
            return false;
        }

        if (method == null || "GET".equals(method.toUpperCase())) {
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(address).queryParam(usercodeParameter, usercode)
                    .queryParam(passwordParameter, password).queryParam(contentParameter, content)
                    .queryParam(mobileParameter, (Object[]) numbers).build();
            String resp = restTemplate.getForObject(uri.toString(), String.class);
            return resp.startsWith("1");

            // String url = address +
            // "&userCode={userCode}&userPwd={userPwd}&msgContent={msgContent}&numbers={numbers}";
            // Map<String, Object> restReq = new HashMap<String, Object>();
            // restReq.put(usercodeParameter, usercode);
            // restReq.put(passwordParameter, password);
            // restReq.put(contentParameter, content);
            // restReq.put(mobileParameter, StringUtils.arrayToCommaDelimitedString(numbers));

            // String resp = restTemplate.getForObject(url, String.class, restReq);

        } else if (method.toUpperCase().equals("POST")) {
            Map<String, Object> restReq = new HashMap<String, Object>();
            restReq.put(usercodeParameter, usercode);
            restReq.put(passwordParameter, password);
            restReq.put(contentParameter, content);
            restReq.put(mobileParameter, StringUtils.arrayToCommaDelimitedString(numbers));
            String resp = restTemplate.postForObject(address, restReq, String.class);
            return resp.startsWith("1");
        }
        return false;
    }

}
