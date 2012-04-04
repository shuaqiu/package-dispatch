/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.bean.system;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
public class User implements Type {

    /**
     * 客户类型: 普通客户, 只能下单
     */
    public static final int CUSTOMER_TYPE_NORMAL = 0;
    /**
     * 客户类型: 客户管理员, 可以创建所属公司的普通客户
     */
    public static final int CUSTOMER_TYPE_ADMIN = 1;

    /**
     * 用户状态: 无效的
     */
    public static final int STATE_INVALID = -1;
    /**
     * 用户状态: 未激活(不能登录使用)
     */
    public static final int STATE_NOT_ACTIVATED = 0;
    /**
     * 用户状态: 有效的, 正常使用中
     */
    public static final int STATE_VALID = 1;

    private int id;

    private String code;
    private String name;
    private String alias;
    private String password;
    private String salt;

    private String tel;

    private int companyId;
    private String company;
    private String department;

    private String address;

    /**
     * 用户的类型
     * 
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     */
    private int type;
    /**
     * 客户的类型, 只针对客户
     * 
     * @see #CUSTOMER_TYPE_NORMAL
     * @see #CUSTOMER_TYPE_ADMIN
     */
    private Integer customerType;
    /**
     * 用户状态
     * 
     * @see #STATE_INVALID
     * @see #STATE_NOT_ACTIVATED
     * @see #STATE_VALID
     */
    private int state;

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
    public String getAlias() {
        return alias;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getPassword() {
        return password;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @author qiushaohua 2012-3-24 */
    public String getSalt() {
        return salt;
    }

    /** @author qiushaohua 2012-3-24 */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getTel() {
        return tel;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /** @author qiushaohua 2012-3-19 */
    public int getCompanyId() {
        return companyId;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setCompanyId(int groupId) {
        companyId = groupId;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getCompany() {
        return company;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setCompany(String company) {
        this.company = company;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getDepartment() {
        return department;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setDepartment(String department) {
        this.department = department;
    }

    /** @author qiushaohua 2012-3-19 */
    public String getAddress() {
        return address;
    }

    /** @author qiushaohua 2012-3-19 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 用户的类型
     * 
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     * @author qiushaohua 2012-3-19
     */
    public int getType() {
        return type;
    }

    /**
     * 用户的类型
     * 
     * @see Type#TYPE_SELF
     * @see Type#TYPE_CUSTOMER
     * @author qiushaohua 2012-3-19
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 客户的类型, 只针对客户
     * 
     * @see #CUSTOMER_TYPE_NORMAL
     * @see #CUSTOMER_TYPE_ADMIN
     * @author qiushaohua 2012-3-19
     */
    public Integer getCustomerType() {
        return customerType;
    }

    /**
     * 客户的类型, 只针对客户
     * 
     * @see #CUSTOMER_TYPE_NORMAL
     * @see #CUSTOMER_TYPE_ADMIN
     * @author qiushaohua 2012-3-19
     */
    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    /**
     * 用户状态
     * 
     * @see #STATE_INVALID
     * @see #STATE_NOT_ACTIVATED
     * @see #STATE_VALID
     * @author qiushaohua 2012-3-19
     */
    public int getState() {
        return state;
    }

    /**
     * 用户状态
     * 
     * @see #STATE_INVALID
     * @see #STATE_NOT_ACTIVATED
     * @see #STATE_VALID
     * @author qiushaohua 2012-3-19
     */
    public void setState(int state) {
        this.state = state;
    }

}
