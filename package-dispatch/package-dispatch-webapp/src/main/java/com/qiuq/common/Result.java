/**
 * @author qiushaohua 2012-3-9
 */
package com.qiuq.common;

/**
 * @author qiushaohua 2012-3-9
 * @version 0.0.1
 */
public class Result {

    public static final Result OK = new Result(true);

    private boolean isOk;

    private int errCode;

    private String message;

    private Exception exception;

    public Result() {
    }

    private Result(boolean isOk) {
        this.isOk = true;
    }

    public Result(int errCode, String message) {
        isOk = errCode == 0;
        this.errCode = errCode;
        this.message = message;
    }

    public Result(ErrCode errCode, String message) {
        isOk = errCode == ErrCode.OK;
        this.errCode = errCode.ordinal();
        this.message = message;
    }

    public Result(int errCode, String message, Exception exception) {
        this(errCode, message);
        this.exception = exception;
    }

    public Result(Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("not exception");
        }

        isOk = false;
        errCode = exception.hashCode();
        message = exception.getMessage();
        this.exception = exception;
    }

    /**
     * @return the isOk
     */
    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
    }

    /**
     * @return the errCode
     */
    public int getErrCode() {
        return errCode;
    }

    /**
     * @param errCode
     *            the errCode to set
     */
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception
     *            the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
