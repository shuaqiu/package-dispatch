/**
 * @author qiushaohua 2012-3-18
 */
package com.qiuq.packagedispatch.web.order;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
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
import com.qiuq.packagedispatch.service.system.UserService;
import com.qiuq.packagedispatch.web.HttpSessionUtil;

/**
 * @author qiushaohua 2012-4-24
 * @version 0.0.1
 */
@Controller
@RequestMapping("/storage")
public class StorageController {

    private UserService userService;

    private OrderService orderService;

    /** @author qiushaohua 2012-4-28 */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.NOT_LOGINED, "no user is logined");
        }

        Set<String> barcodeArr = splitBarcodes(barcodes);

        MultiValueMap<ErrCode, String> result = new LinkedMultiValueMap<ErrCode, String>();
        for (String barcode : barcodeArr) {
            if (StringUtils.hasText(barcode)) {
                OperateResult handleResult = orderService.handleStorage(user, barcode, State.IN_STORAGE, null);
                result.add(handleResult.getErrCode(), barcode);
            }
        }

        return new OperateResult(true, result);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(WebRequest req, @RequestParam String handlerCode, @RequestParam String barcodes) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return new OperateResult(ErrCode.NOT_LOGINED, "no user is logined");
        }

        User handlerUser = userService.getUser(handlerCode);
        if (handlerUser == null) {
            return new OperateResult(ErrCode.NOT_FOUND, "handler user not found ");
        }

        Set<String> barcodeArr = splitBarcodes(barcodes);

        MultiValueMap<ErrCode, String> result = new LinkedMultiValueMap<ErrCode, String>();
        for (String barcode : barcodeArr) {
            if (StringUtils.hasText(barcode)) {
                OperateResult handleResult = orderService.handleStorage(user, barcode, State.OUT_STORAGE, handlerUser);
                result.add(handleResult.getErrCode(), barcode);
            }
        }

        return new OperateResult(true, result);
    }

    /**
     * @param barcodes
     * @return
     * @author qiushaohua 2012-4-28
     */
    private Set<String> splitBarcodes(String barcodes) {
        String[] arr = StringUtils.trimArrayElements(barcodes.split("[\\n|\\r|\\n\\r]"));

        // put into a set, to remove the duplication @ 2012-5-24
        Set<String> set = new LinkedHashSet<String>();
        for (String str : arr) {
            if (StringUtils.hasText(str)) {
                set.add(str);
            }
        }

        return set;
    }
}
