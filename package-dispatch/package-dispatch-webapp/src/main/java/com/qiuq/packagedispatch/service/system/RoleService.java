/**
 * @author qiushaohua 2012-4-8
 */
package com.qiuq.packagedispatch.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiuq.packagedispatch.repository.ResourceRepository;
import com.qiuq.packagedispatch.repository.system.RoleRepository;
import com.qiuq.packagedispatch.service.AbstractResourceService;

/**
 * @author qiushaohua 2012-4-8
 * @version 0.0.1
 */
@Service
public class RoleService extends AbstractResourceService<Map<String, Object>> {
    private RoleRepository roleRepository;

    /** @author qiushaohua 2012-4-8 */
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    protected ResourceRepository<Map<String, Object>> getRepository() {
        return roleRepository;
    }

    /**
     * @param sort
     * @param params
     * @param range
     * @return
     * @author qiushaohua 2012-4-8
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> query(String sort, Map<String, Object> params, long[] range) {
        return roleRepository.query(sort, params, range);
    }

    /**
     * @param params
     * @return
     * @author qiushaohua 2012-4-8
     */
    @Transactional(readOnly = true)
    public long matchedRecordCount(Map<String, Object> params) {
        return roleRepository.matchedRecordCount(params);
    }

}
