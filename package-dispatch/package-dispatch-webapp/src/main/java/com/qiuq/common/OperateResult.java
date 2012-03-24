/**
 * @author qiushaohua 2012-3-9
 */
package com.qiuq.common;

/**
 * @author qiushaohua 2012-3-9
 * @version 0.0.1
 */
public class OperateResult {

    public static final OperateResult OK = new OperateResult(true);

    private final boolean isOk;

    private final Object obj;

    private final ErrCode errCode;

    private final String message;

    private OperateResult(boolean isOk) {
        this.isOk = true;

        errCode = null;
        message = null;
        obj = null;
    }

    public OperateResult(boolean isOk, Object obj) {
        this.isOk = true;
        this.obj = obj;

        errCode = null;
        message = null;
    }

    public OperateResult(ErrCode errCode, String message) {
        this(errCode, message, null);
    }

    public OperateResult(ErrCode errCode, String message, Object obj) {
        isOk = errCode == ErrCode.OK;
        this.errCode = errCode;
        this.message = message;
        this.obj = obj;
    }

    /**
     * @return the isOk
     */
    public boolean isOk() {
        return isOk;
    }

    /** @author qiushaohua 2012-3-23 */
    public Object getObj() {
        return obj;
    }

    /**
     * @return the errCode
     */
    public ErrCode getErrCode() {
        return errCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
