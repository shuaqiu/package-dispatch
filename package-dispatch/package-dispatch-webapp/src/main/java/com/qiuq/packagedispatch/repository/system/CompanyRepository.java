/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.repository.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * @author qiushaohua 2012-3-18
 * @version 0.0.1
 */
@Repository
public class CompanyRepository {

    /**
     * @return
     * @author qiushaohua 2012-3-18
     */
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> aCom = new HashMap<String, Object>();
        aCom.put("id", "1");
        aCom.put("name", " 惠信");

        list.add(aCom);

        return list;
    }

}
