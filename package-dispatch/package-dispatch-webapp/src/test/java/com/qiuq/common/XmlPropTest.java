/**
 * @author qiushaohua 2012-4-28
 */
package com.qiuq.common;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

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
    public void testWrite() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.put("testKey", "中文测试");
        prop.put("testKeyForEn", "abcdafada");

        prop.storeToXML(new FileOutputStream("test.xml"), "test.xml");
    }

    @Test
    public void testRead() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.loadFromXML(new FileInputStream("test.xml"));
        assertEquals("abcdafada", prop.getProperty("testKeyForEn"));
        assertEquals("中文测试", prop.getProperty("testKey"));
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
