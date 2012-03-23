/**
 * @author qiushaohua 2012-3-19
 */
package com.qiuq.packagedispatch.bean.system;

/**
 * @author qiushaohua 2012-3-19
 * @version 0.0.1
 */
public class Company implements Type {

    private int id;
    private String code;
    private String name;
    private String address;

    private int parentId;
    /**
     * 上级ID 列表
     */
    private String fullId;

    /**
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     */
    private int type;

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

    /** @author qiushaohua 2012-3-19 */
    public String getAddress() {
        return address;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setAddress(String address) {
        this.address = address;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getParentId() {
        return parentId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getFullId() {
        return fullId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    /**
     * @author qiushaohua 2012-3-19
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     */
    public int getType() {
        return type;
    }

    /**
     * @author qiushaohua 2012-3-19
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     */
    public void setType(int type) {
        this.type = type;
    }
}
