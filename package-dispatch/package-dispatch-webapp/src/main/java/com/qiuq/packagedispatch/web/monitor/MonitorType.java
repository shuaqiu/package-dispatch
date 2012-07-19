/**
 * @author qiushaohua 2012-7-11
 */
package com.qiuq.packagedispatch.web.monitor;


/**
 * @author qiushaohua 2012-7-11
 * @version 0.0.1
 */
public enum MonitorType {
    /**
     * add a new order, and should be scheduled
     */
    NEW_ORDER("schedule", false),

    /**
     * order is handled by somebody
     */
    HANDLE_ORDER(null, false),

    /**
     * order has been scheduled, and recode the schedule history.
     */
    SCHEDULE_ORDER(null, false);

    /**
     * special permission.
     */
    private String permission;

    /**
     * is send NOT_PERMISSION error if the monitor user has the special permission.
     */
    private boolean isSendErrorIfNotPermit;

    /**
     *
     */
    private MonitorType(String permission, boolean isSendErrorIfNotPermit) {
        this.permission = permission;
        this.isSendErrorIfNotPermit = isSendErrorIfNotPermit;
    }

    /**
     * special permission.
     *
     * @author qiushaohua 2012-6-26
     */
    public String getPermission() {
        return permission;
    }

    /**
     * is send NOT_PERMISSION error if the monitor user has the special permission.
     *
     * @author qiushaohua 2012-7-18
     */
    public boolean isSendErrorIfNotPermit() {
        return isSendErrorIfNotPermit;
    }
}