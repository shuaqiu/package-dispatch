/**
 * @author qiushaohua 2012-4-27
 */
package com.qiuq.common.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;

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

    private String charset;

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

    /** @author qiushaohua 2012-5-19 */
    @Value("${web.sms.charset}")
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public OperateResult send(String content, String... numbers) {
        if (numbers.length == 0 || !StringUtils.hasText(content)) {
            return new OperateResult(ErrCode.NULL, "not number or not content to send");
        }

        if (method == null || "GET".equals(method.toUpperCase())) {
            return sendByGet(content, numbers);
        } else if (method.toUpperCase().equals("POST")) {
            return sendByPost(content, numbers);
        }
        return new OperateResult(ErrCode.INVALID, "invalid send method");
    }

    /**
     * @param content
     * @param numbers
     * @return
     * @author qiushaohua 2012-5-16
     */
    protected OperateResult sendByGet(String content, String... numbers) {
        try {
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(address).queryParam(usercodeParameter, usercode)
                    .queryParam(passwordParameter, password)
                    .queryParam(contentParameter, URLEncoder.encode(content, charset))
                    .queryParam(mobileParameter, (Object[]) numbers).build();

            String resp = restTemplate.getForObject(uri.toString(), String.class);
            if (resp.startsWith("1")) {
                return OperateResult.OK;
            }
            return new OperateResult(ErrCode.OPERATE_FAIL, resp);
        } catch (RestClientException e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        }
    }

    /**
     * @param content
     * @param numbers
     * @return
     * @author qiushaohua 2012-5-16
     */
    protected OperateResult sendByPost(String content, String... numbers) {
        MultiValueMap<String, Object> restReq = new LinkedMultiValueMap<String, Object>();
        restReq.add(usercodeParameter, usercode);
        restReq.add(passwordParameter, password);
        restReq.add(contentParameter, content);
        restReq.add(mobileParameter, StringUtils.arrayToCommaDelimitedString(numbers));

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/x-www-form-urlencoded;charset=" + charset);
            httpHeaders.setContentType(mediaType);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                    restReq, httpHeaders);
            ResponseEntity<String> exchange = restTemplate.exchange(address, HttpMethod.POST, requestEntity,
                    String.class);
            String resp = exchange.getBody();
            if (resp.startsWith("1")) {
                return OperateResult.OK;
            }
            return new OperateResult(ErrCode.OPERATE_FAIL, resp);
        } catch (RestClientException e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        }

    }

}
