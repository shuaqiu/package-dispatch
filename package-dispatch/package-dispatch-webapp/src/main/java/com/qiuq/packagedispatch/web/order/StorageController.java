/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.packagedispatch.bean.order.State;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.order.OrderService;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-4-24
 * @version 0.0.1
 */
@Controller
@RequestMapping("/storage")
public class StorageController {

    private OrderService orderService;

    /**
     * @author qiushaohua 2012-4-24
     */
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void edit() {
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete() {
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OperateResult insert(WebRequest req, @RequestParam String barcodes) {

        return handleStorage(req, barcodes, State.IN_STORAGE);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(WebRequest req, @RequestParam String barcodes) {

        return handleStorage(req, barcodes, State.OUT_STORAGE);
    }

    protected OperateResult handleStorage(WebRequest req, String barcodes, State state) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return null;
        }

        String[] barcodeArr = barcodes.split("\\n");

        MultiValueMap<ErrCode, String> result = new LinkedMultiValueMap<ErrCode, String>();
        for (String barcode : barcodeArr) {
            OperateResult handleResult = orderService.handleStorage(user, barcode, state);
            result.add(handleResult.getErrCode(), barcode);
        }

        return new OperateResult(true, result);
    }
}
