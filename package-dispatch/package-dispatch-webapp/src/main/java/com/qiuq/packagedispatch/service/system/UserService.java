/**
 * @author qiushaohua 2012-3-20
 */
package com.qiuq.packagedispatch.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.system.UserRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-3-20
 * @version 0.0.1
 */
@Service
public class UserService extends AbstractResourceService<Map<String, Object>> {

    private UserRepository userRepository;

    /** @author qiushaohua 2012-3-20 */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected ResourceRepository<Map<String, Object>> getRepository() {
        return userRepository;
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
     * @author qiushaohua 2012-4-28
     */
    @Transactional(readOnly = true)
    public User getUser(String usercode) {
        return userRepository.getUser(usercode);
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-3-27
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> query(String sort, Map<String, Object> params, long[] range) {
        return userRepository.query(sort, params, range);
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-4
     */
    @Transactional(readOnly = true)
    public long matchedRecordCount(Map<String, Object> params) {
        return userRepository.matchedRecordCount(params);
    }

    /**
     * @param fetcher
     * @return
     * @author qiushaohua 2012-4-6
     */
    @Transactional(readOnly = true)
    public List<User> getUserWithRole(int roleId) {
        return userRepository.getUserWithRole(roleId);
    }

    /**
     * @param user
     * @param newPassword
     * @return
     * @author qiushaohua 2012-4-22
     */
    @Transactional
    public OperateResult modifyPassword(User user, String newPassword) {
        return userRepository.modifyPassword(user, newPassword);
    }

    /**
     * @param loginAccount
     * @param id
     * @return
     * @author qiushaohua 2012-5-3
     */
    @Transactional(readOnly = true)
    public int getUserCount(String loginAccount, int id) {
        return userRepository.getUserCount(loginAccount, id);
    }

    /**
     * @return
     * @author qiushaohua 2012-5-4
     */
    @Transactional(readOnly = true)
    public int getMaxUserCodes() {
        return userRepository.getMaxCode(Type.TYPE_SELF);
    }

    /**
     * @return
     * @author qiushaohua 2012-5-4
     */
    @Transactional(readOnly = true)
    public int getMaxCustomerCodes() {
        return userRepository.getMaxCode(Type.TYPE_CUSTOMER);
    }
}
