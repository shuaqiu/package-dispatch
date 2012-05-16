/**
 * @author qiushaohua 2012-5-16
 */
package com.qiuq.common.sms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author qiushaohua 2012-5-16
 * @version 0.0.1
 */
public class SmsSenderTest {


    private ApplicationContext context;

    /**
     * @throws java.lang.Exception
     * @author qiushaohua 2012-5-16
     */
    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("SmsSenderTest.xml");
    }

    /**
     * @throws java.lang.Exception
     * @author qiushaohua 2012-5-16
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWebRequestSender() {
        final SmsSender sender = context.getBean("webRequestSmsSender", SmsSender.class);

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sender.send("短信发送测试", "13824432124");
                }
            }).start();
        }
    }

    @Test
    public void testQueuedSender() {
        final SmsSender sender = context.getBean("queuedSmsSender", SmsSender.class);

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sender.send("短信发送测试", "13824432124");
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        SmsSenderTest test = new SmsSenderTest();
        test.setUp();

        final SmsSender sender = test.context.getBean("webRequestSmsSender", SmsSender.class);
        sender.send("短信发送测试", "13824432124");

        // test.testWebRequestSender();
    }
}
