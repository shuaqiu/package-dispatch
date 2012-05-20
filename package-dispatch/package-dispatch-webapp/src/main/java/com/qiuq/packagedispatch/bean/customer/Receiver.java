/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.bean.customer;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
public class Receiver {

    private int id;

    private int userCompanyId;
    private String userCompany;

    private String name;
    private String tel;
    private String company;
    private String address;

    /** @author qiushaohua 2012-3-31 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setId(int id) {
        this.id = id;
    }


    /** @author qiushaohua 2012-3-31 */
    public int getUserCompanyId() {
        return userCompanyId;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setUserCompanyId(int userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    /** @author qiushaohua 2012-5-20 */
    public String getUserCompany() {
        return userCompany;
    }

    /** @author qiushaohua 2012-5-20 */
    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    /** @author qiushaohua 2012-3-31 */
    public String getName() {
        return name;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setName(String name) {
        this.name = name;
    }

    /** @author qiushaohua 2012-3-31 */
    public String getTel() {
        return tel;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /** @author qiushaohua 2012-3-31 */
    public String getCompany() {
        return company;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setCompany(String company) {
        this.company = company;
    }

    /** @author qiushaohua 2012-3-31 */
    public String getAddress() {
        return address;
    }

    /** @author qiushaohua 2012-3-31 */
    public void setAddress(String address) {
        this.address = address;
    }

}
