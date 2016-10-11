package com.argus.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 使用jxl进行excel文件处理
 */
public class ExcelUtil {

	public static void main(String[] args) throws Exception {
		readExcel();
	}
	
	
	public static void readExcel() throws Exception {
		List list = new ArrayList();
		Workbook rwb = null;
		Cell cell = null;
		
		InputStream is = new FileInputStream("D:\\test\\全国公司名录—样本数据.xls");
		rwb = Workbook.getWorkbook(is);
		
		//获取第一个sheet
		Sheet sheet = rwb.getSheet(0);
		//从第二行开始取数据,
		for(int i=1;i<sheet.getRows();i++){
			//
//			String[] cols = new String[sheet.getColumns()];
//			for(int j=0;j<sheet.getColumns();j++){
				cell = sheet.getCell(0,i); //只取第一列的数据
				String content = cell.getContents();
//				cols[j] = cell.getContents();
//			}
			list.add( content);
		}
		
		System.out.println(list.size());
		
		/*
		for(int i=0;i<list.size();i++){
//			String[] str = (String[])list.get(i);
//			for(int j=0;j<str.length;j++){
//				System.out.println(str[j]);
//			}
			System.out.println(list.get(i));
		}
		*/
		

		
		
	}
	
}
