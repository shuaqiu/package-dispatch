/**
 * @author qiushaohua 2012-3-25
 */
package com.qiuq.common;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.qiuq.packagedispatch.bean.system.User;

/**
 * @author qiushaohua 2012-3-25
 * @version 0.0.1
 */
public class ClassCastTest {

    @Test
    public void testCast() {
        Object nullObj = null;
        User user = (User) nullObj;

        assertTrue(user == null);
    }
}
