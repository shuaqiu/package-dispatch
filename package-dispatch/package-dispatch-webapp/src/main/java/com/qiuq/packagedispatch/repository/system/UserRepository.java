/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.repository.system;

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

}
