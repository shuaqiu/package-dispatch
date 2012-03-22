/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.repository.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.qiuq.packagedispatch.bean.system.User;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Repository
public class UserRepository {

    /**
     * @param usercode
     * @param password
     * @return
     * @author qiushaohua 2012-3-20
     */
    public User query(String usercode, String password) {
        User user = new User();
        user.setCode(usercode);
        user.setName(usercode);
        return user;
    }

    /**
     * @param usercode
     * @return
     * @author qiushaohua 2012-3-21
     */
    public List<User> getReceiverList(String usercode) {
        List<User> list = new ArrayList<User>();

        User user = new User();
        user.setName("u1");
        user.setTel("1111");
        user.setAddress("add1");
        user.setCompany("com1");
        user.setDepartment("dep1");

        list.add(user);
        return list;
    }

}
