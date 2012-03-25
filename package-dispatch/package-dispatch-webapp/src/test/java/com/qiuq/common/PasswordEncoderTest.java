/**
 * @author qiushaohua 2012-3-25
 */
package com.qiuq.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * @author qiushaohua 2012-3-25
 * @version 0.0.1
 */
public class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");

    /** @author qiushaohua 2012-3-25 */
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testSimplyPassword() {
        String encodePassword = passwordEncoder.encodePassword("123456", "");
        System.err.println(encodePassword);
        assertEquals("e10adc3949ba59abbe56e057f20f883e", encodePassword);
    }
}
