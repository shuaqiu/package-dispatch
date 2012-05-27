/**
 * @author qiushaohua 2012-3-31
 */
package com.qiuq.packagedispatch.web.customer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.qiuq.common.ErrCode;
import com.qiuq.common.OperateResult;
import com.qiuq.common.excel.ExcelUtil;
import com.qiuq.packagedispatch.bean.customer.Receiver;
import com.qiuq.packagedispatch.bean.system.Company;
import com.qiuq.packagedispatch.bean.system.Type;
import com.qiuq.packagedispatch.bean.system.User;
import com.qiuq.packagedispatch.service.ResourceService;
import com.qiuq.packagedispatch.service.customer.ReceiverService;
import com.qiuq.packagedispatch.service.system.CompanyService;
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

    private CompanyService companyService;

    private ExcelUtil excelUtil;

    /** @author qiushaohua 2012-3-31 */
    @Autowired
    public void setReceiverService(ReceiverService receiverService) {
        this.receiverService = receiverService;
    }

    /** @author qiushaohua 2012-5-21 */
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /** @author qiushaohua 2012-5-21 */
    @Autowired
    public void setExcelUtil(ExcelUtil excelUtil) {
        this.excelUtil = excelUtil;
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

    /**
     * @param req
     * @param file
     * @author qiushaohua 2012-5-20
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Object doImport(WebRequest req, @RequestPart("uploadedfiles[]") MultipartFile file,
            @RequestParam(required = false) String plugin) {
        User user = HttpSessionUtil.getLoginedUser(req);
        if (user == null) {
            return buildReturnObject(plugin, new OperateResult(ErrCode.NOT_LOGINED, "no user is logined"));
        }

        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(file.getInputStream());
            OperateResult importResult = doImport(user, inputStream);

            return buildReturnObject(plugin, importResult);
        } catch (InvalidFormatException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return buildReturnObject(plugin, new OperateResult(ErrCode.INVALID, e.getMessage()));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return buildReturnObject(plugin, new OperateResult(ErrCode.EXCEPTION, e.getMessage()));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * @param plugin
     * @param result
     * @return
     * @author qiushaohua 2012-5-27
     */
    private Object buildReturnObject(String plugin, OperateResult result) {
        if (plugin != null && plugin.equals("iframe")) {
            try {
                String json = new ObjectMapper().writeValueAsString(result);
                return "<textarea>" + json + "</textarea>";
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * @param user
     * @param inputStream
     * @throws IOException
     * @throws InvalidFormatException
     * @author qiushaohua 2012-5-21
     */
    private OperateResult doImport(User user, InputStream inputStream) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheetAt(0);

        if (sheet.getPhysicalNumberOfRows() < 2) {
            return new OperateResult(ErrCode.NULL, "not content");
        }

        Map<String, String> expected = initBeanColumnDefinition(user);

        Row headerRow = sheet.getRow(sheet.getFirstRowNum());
        Map<String, Integer> headerMapping = parseHeader(headerRow, expected);
        if (headerMapping == null) {
            return new OperateResult(ErrCode.NOT_CORRECT, "导入的文件格式不正确, 请参考页面标题填写要导入的数据");
        }

        MultiValueMap<Integer, Receiver> map = extractData(user, sheet, headerMapping);

        MultiValueMap<Err, Receiver> fails = removeUncorrectData(map);

        for (Integer userCompanyId : map.keySet()) {
            List<Receiver> receivers = map.get(userCompanyId);
            importByUserCompany(receivers, userCompanyId, fails);
        }

        if (fails.size() > 0) {
            Workbook errWb = null;
            String ext = null;
            if (wb instanceof HSSFWorkbook) {
                errWb = new HSSFWorkbook();
                ext = ".xls";
            } else if (wb instanceof XSSFWorkbook) {
                errWb = new XSSFWorkbook();
                ext = ".xlsx";
            }

            if (errWb != null) {
                writeErrorFile(fails, errWb, expected);

                String dir = System.getProperty("java.io.tmpdir");
                String filename = System.currentTimeMillis() + (int) (Math.random() * 1000) + ext;

                OutputStream outputStream = null;
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(new File(dir, filename)));
                    errWb.write(outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                return new OperateResult(true, filename);
            }
        }

        return OperateResult.OK;
    }

    /**
     * @param fails
     * @param errWb
     * @param expected
     * @author qiushaohua 2012-5-27
     */
    private void writeErrorFile(MultiValueMap<Err, Receiver> fails, Workbook errWb, Map<String, String> expected) {
        int rownum = 0;

        Sheet sheet = errWb.createSheet();

        Row headerRow = sheet.createRow(rownum++);
        createHeaderRow(headerRow, expected);

        for (Err err : fails.keySet()) {
            List<Receiver> list = fails.get(err);
            for (Receiver t : list) {
                Row row = sheet.createRow(rownum++);
                createBodyRow(row, expected, t, err);
            }
        }
    }

    /**
     * @param headerRow
     * @param expected
     * @author qiushaohua 2012-5-27
     */
    private void createHeaderRow(Row headerRow, Map<String, String> expected) {
        int column = 0;
        for (String desc : expected.keySet()) {
            Cell cell = headerRow.createCell(column++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(desc);
        }
        Cell errCell = headerRow.createCell(column++, Cell.CELL_TYPE_STRING);
        errCell.setCellValue("失败原因");
    }

    /**
     * @param row
     * @param expected
     * @param t
     * @param err
     * @author qiushaohua 2012-5-27
     */
    private void createBodyRow(Row row, Map<String, String> expected, Receiver t, Err err) {
        int column = 0;
        for (String prop : expected.values()) {
            Cell cell = row.createCell(column++, Cell.CELL_TYPE_STRING);

            String value = getPropValue(t, prop);
            cell.setCellValue(value);
        }

        String desc = err.getDesc();
        if (err == Err.NULL_NEEDED_FIELD) {
            desc += ": " + getNullColumn(expected, t);
        }

        Cell errCell = row.createCell(column++, Cell.CELL_TYPE_STRING);
        errCell.setCellValue(desc);
    }

    /**
     * @param t
     * @param prop
     * @return
     * @author qiushaohua 2012-5-27
     */
    private String getPropValue(Receiver t, String prop) {
        String value = "";
        if (prop.equals("userCompany")) {
            value = t.getUserCompany();
        } else if (prop.equals("name")) {
            value = t.getName();
        } else if (prop.equals("tel")) {
            value = t.getTel();
        } else if (prop.equals("company")) {
            value = t.getCompany();
        } else if (prop.equals("address")) {
            value = t.getAddress();
        }
        return value;
    }

    /**
     * @param map
     * @return
     * @author qiushaohua 2012-5-27
     */
    private MultiValueMap<Err, Receiver> removeUncorrectData(MultiValueMap<Integer, Receiver> map) {
        MultiValueMap<Err, Receiver> fails = new LinkedMultiValueMap<Err, Receiver>();

        List<Receiver> uncorrectUserCompany = map.remove(-Err.UNCORRECT_USER_COMPANY.ordinal());
        if (uncorrectUserCompany != null) {
            fails.put(Err.UNCORRECT_USER_COMPANY, uncorrectUserCompany);
        }
        List<Receiver> nullNeededField = map.remove(-Err.NULL_NEEDED_FIELD.ordinal());
        if (nullNeededField != null) {
            fails.put(Err.NULL_NEEDED_FIELD, nullNeededField);
        }
        return fails;
    }

    /**
     * @param map
     * @param userCompanyId
     * @author qiushaohua 2012-5-27
     */
    private void importByUserCompany(List<Receiver> receivers, Integer userCompanyId,
            MultiValueMap<Err, Receiver> fails) {
        MultiValueMap<String, Receiver> nameMapping = getNameMapping(userCompanyId);

        List<Receiver> newT = new ArrayList<Receiver>();
        List<Receiver> oldT = new ArrayList<Receiver>();

        for (Receiver t : receivers) {
            Receiver dbT = getDbObject(nameMapping, t);
            if (dbT == null) {
                newT.add(t);
            } else {
                t.setId(dbT.getId());
                oldT.add(t);
            }
        }

        if (newT.size() > 0) {
            OperateResult batchInsert = receiverService.batchInsert(newT);
            if (batchInsert.isOk() == false) {
                @SuppressWarnings("unchecked")
                Collection<? extends Receiver> collection = (Collection<? extends Receiver>) batchInsert.getObj();
                for (Receiver t : collection) {
                    fails.add(Err.INSERT_FAIL, t);
                }
            }
        }
        if (oldT.size() > 0) {
            OperateResult batchUpdate = receiverService.batchUpdate(oldT);
            if (batchUpdate.isOk() == false) {
                @SuppressWarnings("unchecked")
                Collection<? extends Receiver> collection = (Collection<? extends Receiver>) batchUpdate.getObj();
                for (Receiver t : collection) {
                    fails.add(Err.UPDATE_FAIL, t);
                }
            }
        }
    }

    private String getNullColumn(Map<String, String> expected, Receiver t) {
        List<String> nullColumns = new ArrayList<String>();

        for (String desc : expected.keySet()) {
            String prop = expected.get(desc);
            String value = getPropValue(t, prop);
            if (!StringUtils.hasText(value)) {
                nullColumns.add(desc);
            }
        }
        return StringUtils.collectionToCommaDelimitedString(nullColumns);
    }

    /**
     * @param nameMapping
     * @param t
     * @return
     * @author qiushaohua 2012-5-21
     */
    private Receiver getDbObject(MultiValueMap<String, Receiver> nameMapping, Receiver t) {
        List<Receiver> list = nameMapping.get(t.getName());
        if (list == null || list.size() == 0) {
            return null;
        }

        for (Receiver dbT : list) {
            if (dbT.getCompany() != null && dbT.getCompany().equals(t.getCompany())) {
                return dbT;
            }
        }

        return null;
    }

    /**
     * @param userCompanyId
     * @return
     * @author qiushaohua 2012-5-21
     */
    private MultiValueMap<String, Receiver> getNameMapping(Integer userCompanyId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userCompanyId", userCompanyId);
        List<Receiver> query = receiverService.query("+id", params, null);

        MultiValueMap<String, Receiver> nameMapping = new LinkedMultiValueMap<String, Receiver>();
        for (Receiver t : query) {
            nameMapping.add(t.getName(), t);
        }
        return nameMapping;
    }

    /**
     * @param user
     * @param sheet
     * @param headerMapping
     * @return
     * @author qiushaohua 2012-5-21
     */
    private MultiValueMap<Integer, Receiver> extractData(User user, Sheet sheet, Map<String, Integer> headerMapping) {
        Map<String, Integer> companyMapping = null;
        if (user.getType() == Type.TYPE_SELF) {
            companyMapping = getCompanyMapping();
        }

        MultiValueMap<Integer, Receiver> map = new LinkedMultiValueMap<Integer, Receiver>();

        Iterator<Row> iterator = sheet.iterator();
        iterator.next(); // skip the header row
        while (iterator.hasNext()) {
            Row row = iterator.next();
            Receiver t = parseData(row, headerMapping);

            if (isNullOnNeededField(user, t)) {
                map.add(-Err.NULL_NEEDED_FIELD.ordinal(), t);
                continue;
            }

            if (user.getType() == Type.TYPE_SELF) {
                Integer userCompanyId = companyMapping.get(t.getUserCompany());
                if (userCompanyId == null) {
                    map.add(-Err.UNCORRECT_USER_COMPANY.ordinal(), t);
                    continue;
                }
                t.setUserCompanyId(userCompanyId);
            } else {
                t.setUserCompany(user.getCompany());
                t.setUserCompanyId(user.getCompanyId());
            }
            map.add(t.getUserCompanyId(), t);
        }
        return map;
    }

    private boolean isNullOnNeededField(User user, Receiver t) {
        if (user.getType() == Type.TYPE_SELF && t.getUserCompany() == null) {
            return true;
        }
        if (t.getName() == null || t.getTel() == null || t.getCompany() == null || t.getAddress() == null) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @author qiushaohua 2012-5-21
     */
    private Map<String, Integer> getCompanyMapping() {
        List<Company> companys = companyService.query("+id", null, null);

        Map<String, Integer> mapping = new HashMap<String, Integer>();
        for (Company aCom : companys) {
            mapping.put(aCom.getName(), aCom.getId());
        }
        return mapping;
    }

    /**
     * @param user
     * @return
     * @author qiushaohua 2012-5-21
     */
    private Map<String, String> initBeanColumnDefinition(User user) {
        Map<String, String> expected = new LinkedHashMap<String, String>();
        if (user.getType() == Type.TYPE_SELF) {
            expected.put("客户公司", "userCompany");
        }
        expected.put("姓名", "name");
        expected.put("电话", "tel");
        expected.put("收件人公司", "company");
        expected.put("地址", "address");
        return expected;
    }

    /**
     * @param row
     * @param expected
     * @return
     * @author qiushaohua 2012-5-21
     */
    private Map<String, Integer> parseHeader(Row row, Map<String, String> expected) {
        Map<String, Integer> headerMapping = new LinkedHashMap<String, Integer>();

        Iterator<Cell> iterator = row.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            String str = excelUtil.getCellStringValue(cell);
            if (expected.containsKey(str)) {
                headerMapping.put(expected.get(str), cell.getColumnIndex());
            }
        }

        if (headerMapping.size() == expected.size()) {
            // if the size is equals, it is ok
            return headerMapping;
        }
        return null;
    }

    /**
     * @param row
     * @param headerMapping
     * @return
     * @author qiushaohua 2012-5-21
     */
    private Receiver parseData(Row row, Map<String, Integer> headerMapping) {
        // BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(Receiver.class);
        //
        // Iterator<Cell> iterator = row.iterator();
        // while (iterator.hasNext()) {
        // Cell cell = iterator.next();
        //
        // String value = excelUtil.getCellStringValue(cell);
        // beanWrapperImpl.setPropertyValue(headerMapping.get(cell.getColumnIndex()), value);
        // }
        //
        // return (Receiver) beanWrapperImpl.getWrappedInstance();

        Receiver t = new Receiver();

        for (String prop : headerMapping.keySet()) {
            Integer column = headerMapping.get(prop);

            Cell cell = row.getCell(column);
            String value = excelUtil.getCellStringValue(cell);
            if (!StringUtils.hasText(value)) {
                value = null;
            }

            if (prop.equals("userCompany")) {
                t.setUserCompany(value);
            } else if (prop.equals("name")) {
                t.setName(value);
            } else if (prop.equals("tel")) {
                String tel = getTel(excelUtil.getCellValue(cell));
                t.setTel(tel);
            } else if (prop.equals("company")) {
                t.setCompany(value);
            } else if (prop.equals("address")) {
                t.setAddress(value);
            }
        }

        return t;
    }

    /**
     * @param value
     * @return
     * @author qiushaohua 2012-5-21
     */
    private String getTel(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.length() > 20) {
                return str.substring(0, 20);
            }
            return str;
        }

        if (value instanceof Number) {
            return String.valueOf(((Number) value).longValue());
        }

        return value.toString();
    }

    /**
     * @author qiushaohua 2012-5-27
     * @version 0.0.1
     */
    private enum Err {
        NULL_NEEDED_FIELD("必填的字段为空"),

        UNCORRECT_USER_COMPANY("找不到对应的客户公司"),

        INSERT_FAIL("插入数据库失败, 请稍后再试"),

        UPDATE_FAIL("更新数据失败, 请稍后再试");

        private String desc;

        private Err(String desc) {
            this.desc = desc;
        }

        /** @author qiushaohua 2012-5-27 */
        public String getDesc() {
            return desc;
        }
    }
}
