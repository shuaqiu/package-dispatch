/**
 * @author qiushaohua 2012-5-25
 */
package com.qiuq.packagedispatch.web;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * @author qiushaohua 2012-5-25
 * @version 0.0.1
 */
@Component
public class SimpleMessageQueue<T> {

    /**
     * 10 minute
     */
    private static final int KEEP_ALIVE_TIME = 10 * 60 * 1000;

    protected final Log logger = LogFactory.getLog(getClass());

    private Map<String, BlockingQueue<T>> queues = new ConcurrentHashMap<String, BlockingQueue<T>>();
    private Map<String, Long> lastAccessTime = new ConcurrentHashMap<String, Long>();

    /**
     * @param t
     * @author qiushaohua 2012-5-25
     */
    public void push(T t) {
        // remove unused first
        removeUnused();

        for (String session : queues.keySet()) {
            BlockingQueue<T> queue = queues.get(session);
            if (queue != null) {
                queue.offer(t);
                if (logger.isInfoEnabled()) {
                    logger.info("offer a new value to queue : " + session);
                }
            }
        }
    }

    /**
     * @param session
     * @return
     * @author qiushaohua 2012-5-25
     */
    public T poll(String session) {
        BlockingQueue<T> queue = queues.get(session);
        if (queue == null) {
            // if not queue relative with the session, create one
            queue = new LinkedBlockingQueue<T>();
            queues.put(session, queue);
            if (logger.isInfoEnabled()) {
                logger.info("initilize a new queue with session id : " + session);
            }
        }
        // update the last access time
        lastAccessTime.put(session, System.currentTimeMillis());

        // remove unused first
        removeUnused();

        try {
            T t = queue.poll(1, TimeUnit.MINUTES);
            if (logger.isInfoEnabled()) {
                logger.info("poll return : " + t);
            }
            return t;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author qiushaohua 2012-5-25
     */
    private void removeUnused() {
        long current = System.currentTimeMillis();

        Iterator<String> iterator = lastAccessTime.keySet().iterator();
        while (iterator.hasNext()) {
            String session = iterator.next();
            Long last = lastAccessTime.get(session);

            if (current - last > KEEP_ALIVE_TIME) {
                queues.remove(session);
                iterator.remove();

                if (logger.isInfoEnabled()) {
                    logger.info("remove a unused queue with session id : " + session);
                }
            }
        }
    }

    /**
     * @param session
     * @author qiushaohua 2012-5-26
     */
    public void remove(String session) {
        queues.remove(session);
        lastAccessTime.remove(session);

        if (logger.isInfoEnabled()) {
            logger.info("manually remove a queue with session id : " + session);
        }
    }
}
