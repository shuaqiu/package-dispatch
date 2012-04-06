/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.system;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Role {

    public static final int ADMINISTRATOR = 0;

    public static final int MANAGER = 1;

    public static final int SCHEDULER = 2;

    public static final int FETCHER = 3;

    public static final int TRANSITER = 4;

    public static final int DELIVERER = 5;

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
