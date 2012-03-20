/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.common.web.http.converter.json;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
public class IgnoreUnknowPropJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {

    public IgnoreUnknowPropJacksonHttpMessageConverter() {
        super();

        ObjectMapper objectMapper = new ObjectMapper();
        setObjectMapper(objectMapper);
    }

    @Override
    public void setObjectMapper(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        super.setObjectMapper(objectMapper);
    }
}
