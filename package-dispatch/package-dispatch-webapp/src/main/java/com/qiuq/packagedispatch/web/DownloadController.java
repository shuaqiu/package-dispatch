/**
 * @author qiushaohua 2012-5-27
 */
package com.qiuq.packagedispatch.web;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;

/**
 * @author qiushaohua 2012-5-27
 * @version 0.0.1
 */
@Controller
@RequestMapping(value = "/download")
public class DownloadController {

    /**
     * @param filename
     * @return
     * @author qiushaohua 2012-5-27
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<Resource> download(@RequestParam(value = "f") String filename) {
        File file = getFile(filename);

        try {
            HttpHeaders headers = new HttpHeaders();
            String ext = StringUtils.getFilenameExtension(filename);
            headers.set("Content-Disposition", "attachment; filename=\"ImportFail." + ext + "\"");
            MediaType mediaType = MediaType.parseMediaType("application/msexcel");
            headers.setContentType(mediaType);
            return new HttpEntity<Resource>(new FileSystemResource(file), headers);
        } finally {
        }
    }

    /**
     * @param filename
     * @return
     * @author qiushaohua 2012-5-27
     */
    private File getFile(String filename) {
        String dir = System.getProperty("java.io.tmpdir");
        File file = new File(dir, filename);
        return file;
    }

    /**
     * @param filename
     * @return
     * @author qiushaohua 2012-5-27
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public OperateResult delete(@RequestParam(value = "f") String filename) {
        File file = getFile(filename);
        boolean delete = file.delete();
        if (delete) {
            return OperateResult.OK;
        }
        return new OperateResult(ErrCode.OPERATE_FAIL, "delete fail : " + filename);
    }
}
