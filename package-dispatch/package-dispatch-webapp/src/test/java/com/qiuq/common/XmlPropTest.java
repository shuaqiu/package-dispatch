/**
 * @author qiushaohua 2012-4-28
 */
package com.qiuq.common;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author qiushaohua 2012-4-28
 * @version 0.0.1
 */
public class XmlPropTest {

    /**
     * @throws java.lang.Exception
     * @author qiushaohua 2012-4-28
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     * @author qiushaohua 2012-4-28
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMessage() {
        MessageFormat f = new MessageFormat("abcd{0}{1}");
        String r = f.format(new Object[] {
                "ef", 1
        });
        assertEquals("abcdef1", r);

        MessageFormat f2 = new MessageFormat("abcd{1}");
        String r2 = f2.format(new Object[] {
                "ef", 1
        });
        assertEquals("abcd1", r2);
    }

}
