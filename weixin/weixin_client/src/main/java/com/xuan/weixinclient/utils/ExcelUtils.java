package com.xuan.weixinclient.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.entity.Table;
import com.xuan.weixinserver.entity.TableLine;

/**
 * excel导入导出工具
 *
 * @author xuan
 * @version 创建时间：2014-7-29 下午4:14:44
 */
public abstract class ExcelUtils {
	private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

	/**
	 * 解析excel文件
	 *
	 * @param fileName
	 * @return
	 */
	public static ServiceData parse(String fileName){
		ServiceData serviceData = new ServiceData();
		try {
			InputStream is = new FileInputStream(fileName);

			Workbook work = Workbook.getWorkbook(is);
			Sheet sheet = work.getSheet(0);

			Table table = new Table();
			table.setName("root");

			String serviceId = sheet.getCell(0, 0).getContents();//服务器名
			serviceData.setServiceId(serviceId);
			serviceData.addTable(table.getName(), table);

			String key = sheet.getCell(0, 1).getContents();
			String value = sheet.getCell(1, 1).getContents();
			String qualityCode = sheet.getCell(2, 1).getContents();
			String updateTime = sheet.getCell(3, 1).getContents();
			for(int i=2; i<sheet.getRows(); i++){
				TableLine tableLine = new TableLine();
				tableLine.put(key, sheet.getCell(0, i).getContents());
				tableLine.put(value, sheet.getCell(1, i).getContents());
				tableLine.put(qualityCode, sheet.getCell(2, i).getContents());
				tableLine.put(updateTime, sheet.getCell(3, i).getContents());
				table.add(sheet.getCell(0, i).getContents(), tableLine);
			}

			return serviceData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public static void main(String[] args) {
		ServiceData serviceData = parse("D://111.xls");
		String str = JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceData.getServiceId());
		System.out.println(str);

		ServiceData temp = JsonDataUtils.decodeServiceDataFromJsonStr(str);
		String str2 = JsonDataUtils.encodeJsonStrFromServiceData(temp, temp.getServiceId());
		System.out.println(str2);
	}

}
