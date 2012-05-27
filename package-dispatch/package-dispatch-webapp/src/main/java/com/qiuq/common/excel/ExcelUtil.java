/**
 * @author qiushaohua 2012-5-21
 */
package com.qiuq.common.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.util.StringUtils;

/**
 * @author qiushaohua 2012-5-21
 * @version 0.0.1
 */
public class ExcelUtil {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /** @author qiushaohua 2012-5-21 */
    public void setDateFormat(DateFormat dateFormat) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }
    }

    /** @author qiushaohua 2012-5-21 */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDatePattern(String pattern) {
        if (StringUtils.hasText(pattern)) {
            dateFormat = new SimpleDateFormat(pattern);
        }
    }

    /**
     * @param cell
     * @return
     * @author qiushaohua 2012-5-21
     */
    public Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {

        case Cell.CELL_TYPE_STRING:
        case Cell.CELL_TYPE_BLANK:
            return cell.getStringCellValue();

        case Cell.CELL_TYPE_NUMERIC:
            // cell.get
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            return cell.getNumericCellValue();

        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();

        case Cell.CELL_TYPE_ERROR:
        case Cell.CELL_TYPE_FORMULA:
            return "";
        }

        return "";
    }

    /**
     * @param cell
     * @return
     * @author qiushaohua 2012-5-21
     */
    public String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {

        case Cell.CELL_TYPE_STRING:
        case Cell.CELL_TYPE_BLANK:
            return cell.getStringCellValue();

        case Cell.CELL_TYPE_NUMERIC:
            // cell.get
            if (DateUtil.isCellDateFormatted(cell)) {
                return dateFormat.format(cell.getDateCellValue());
            }
            return "" + cell.getNumericCellValue();

        case Cell.CELL_TYPE_BOOLEAN:
            return "" + cell.getBooleanCellValue();

        case Cell.CELL_TYPE_ERROR:
        case Cell.CELL_TYPE_FORMULA:
            return "";
        }

        return "";
    }
}
