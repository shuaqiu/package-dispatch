/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.system;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Role {

    public static final int ADMINISTRATORS = 0;

    public static final int DUTY_MANAGERS = 1;

    public static final int SCHEDULERS = 2;

    public static final int FETCHERS_AND_DELIVERERS = 3;

    public static final int TRANSITERS = 4;

    private int id;
    private String code;
    private String name;

    /** @author qiushaohua 2012-3-19 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setId(int id) {
        this.id = id;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getCode() {
        return code;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setCode(String code) {
        this.code = code;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getName() {
        return name;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setName(String name) {
        this.name = name;
    }

}
