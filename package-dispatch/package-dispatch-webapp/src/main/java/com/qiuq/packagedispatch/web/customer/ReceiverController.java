/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.web.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.customer.ReceiverService;
import com.qiuq.packagedispatch.web.AbstractResourceController;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-3-31
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/receiver")
public class ReceiverController extends AbstractResourceController<Receiver> {

    private ReceiverService receiverService;


    /** @author qiushaohua 2012-3-31 */
    @Autowired
    public void setReceiverService(ReceiverService receiverService) {
        this.receiverService = receiverService;
    }

    @Override
    protected ResourceService<Receiver> getService() {
        return receiverService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<Receiver>> query(WebRequest req, @RequestParam(defaultValue = "+id") String sort,
            @RequestParam(required = false) String query, @RequestHeader(value = "Range", required = false) String range) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new HttpEntity<List<Receiver>>(new ArrayList<Receiver>());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (user.getType() == Type.TYPE_CUSTOMER) {
            params.put("userCompanyId", user.getCompanyId());
        }
        params.put("query", query);

        return doQuery(sort, params, range);
    }
}
