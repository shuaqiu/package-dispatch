/**
 * @author qiushaohua 2012-4-12
 */
package com.qiuq.packagedispatch.bean.system;

/**
 * @author qiushaohua 2012-4-12
 * @version 0.0.1
 */
public class Function {
    private int id;
    private String code;
    private String name;
    private Integer parent_id;
    private int func_index;

    /** @author qiushaohua 2012-4-12 */
    public int getId() {
        return id;
    }

    /** @author qiushaohua 2012-4-12 */
    public void setId(int id) {
        this.id = id;
    }

    /** @author qiushaohua 2012-4-12 */
    public String getCode() {
        return code;
    }

    /** @author qiushaohua 2012-4-12 */
    public void setCode(String code) {
        this.code = code;
    }

    /** @author qiushaohua 2012-4-12 */
    public String getName() {
        return name;
    }

    /** @author qiushaohua 2012-4-12 */
    public void setName(String name) {
        this.name = name;
    }

    /** @author qiushaohua 2012-4-12 */
    public Integer getParent_id() {
        return parent_id;
    }

    /** @author qiushaohua 2012-4-12 */
    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    /** @author qiushaohua 2012-4-12 */
    public int getFunc_index() {
        return func_index;
    }

    /** @author qiushaohua 2012-4-12 */
    public void setFunc_index(int func_index) {
        this.func_index = func_index;
    }

}
