/**
 * @author qiushaohua 2012-4-27
 */
package com.qiuq.common.sms;

/**
 * @author qiushaohua 2012-4-27
 * @version 0.0.1
 */
public interface SmsSender {
    /**
     * @param content
     * @param numbers
     * @return
     * @author qiushaohua 2012-4-27
     */
    boolean send(String content, String... numbers);
}
