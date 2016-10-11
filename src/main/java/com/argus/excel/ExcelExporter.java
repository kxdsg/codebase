package com.argus.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 使用poi进行excel文件处理
 * 导出excel文件类
 * Created by xingding on 2016/9/30.
 */
public class ExcelExporter {

    public static void main(String[] args) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("示例");
        HSSFRow row = sheet.createRow(0);
        HSSFCell c0 = row.createCell(0);
        c0.setCellValue(new HSSFRichTextString("合并单元格"));
        Region region = new Region(0, (short)0, 1, (short)8);

        sheet.addMergedRegion(region);

        File outputFile = new File("d:\\test\\excelexport.xls");
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileOutputStream output=new FileOutputStream(outputFile);
        workbook.write(output);
        output.flush();

    }
}
