/**
 * @author qiushaohua 2012-5-16
 */
package com.qiuq.common.sms;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;

/**
 * @author qiushaohua 2012-5-16
 * @version 0.0.1
 */
public class QueuedSmsSender implements SmsSender {

    private SmsSender sender;

    private int threadNumber = 1;

    private ExecutorService threadPool;

    /**
     * 
     */
    public QueuedSmsSender() {
        threadPool = Executors.newFixedThreadPool(threadNumber);
    }

    /** @author qiushaohua 2012-5-16 */
    @Autowired
    public void setSender(SmsSender sender) {
        this.sender = sender;
    }

    /** @author qiushaohua 2012-5-16 */
    @Autowired
    public void setThreadNumber(int nThreads) {
        threadNumber = nThreads;
    }

    @Override
    public OperateResult send(final String content, final String... numbers) {
        Future<OperateResult> future = threadPool.submit(new Callable<OperateResult>() {
            @Override
            public OperateResult call() throws Exception {
                return sender.send(content, numbers);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new OperateResult(ErrCode.EXCEPTION, e.getMessage());
        }
    }


}
