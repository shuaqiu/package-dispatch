/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.bean.system.Role;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.system.UserRepository;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    /** @author qiushaohua 2012-3-20 */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param usercode
     * @param password
     * @return
     * @author qiushaohua 2012-3-20
     */
    public User query(String usercode, String password) {
        return userRepository.query(usercode, password);
    }

    /**
     * @param usercode
     * @return
     * @author qiushaohua 2012-3-22
     */
    public List<User> getReceiverList(String usercode) {
        return userRepository.getReceiverList(usercode);
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-3-23
     */
    public List<Role> getUserRoles(User user) {
        return null;
    }
}
