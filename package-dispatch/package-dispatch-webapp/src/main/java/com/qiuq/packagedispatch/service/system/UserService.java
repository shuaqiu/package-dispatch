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
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.system.UserRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Service
public class UserService extends AbstractResourceService<User> {

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
    @Transactional(readOnly = true)
    public User getLoginUser(String usercode, String password) {
        return userRepository.getLoginUser(usercode, password);
    }

    /**
     * @param usercode
     * @return
     * @author qiushaohua 2012-3-22
     */
    @Transactional(readOnly = true)
    public List<User> getReceiver(int userId) {
        return userRepository.getReceiver(userId);
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-3-23
     */
    @Transactional(readOnly = true)
    public List<Role> getUserRoles(User user) {
        return null;
    }

    /**
     * @param sort
     * @param query
     * @return
     * @author qiushaohua 2012-3-27
     */
    @Transactional(readOnly = true)
    public List<User> query(String sort, String query) {
        return userRepository.query(sort, query);
    }

    @Override
    protected ResourceRepository<User> getRepository() {
        return userRepository;
    }
}
