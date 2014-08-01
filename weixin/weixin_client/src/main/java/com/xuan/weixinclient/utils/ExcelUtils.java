package com.xuan.weixinclient.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.entity.Constants;
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
	 * serviceData数据写到excel文件中
	 *
	 * @param serviceData
	 */
	public static void writeToFile(String fileName, ServiceData serviceData){
		try {
			Workbook wb = Workbook.getWorkbook(new File(fileName));
			WritableWorkbook wwb = Workbook.createWorkbook(new File(fileName), wb);
			WritableSheet sheet = wwb.getSheet(0);

			WritableCell oldCell = sheet.getWritableCell(0,0);
			sheet.addCell(new Label(oldCell.getColumn(), oldCell.getRow(), serviceData.getServiceId(), oldCell.getCellFormat()));

			Table table = serviceData.getTable(Constants.TABLE);
			if(null == table){
				log.error("table表为空");
				return;
			}

			Map<String, TableLine> map = table.getMap();
			int row = 2;
			for(Entry<String, TableLine> entry : map.entrySet()){
				TableLine tableLine = entry.getValue();

				WritableCell keyCell = sheet.getWritableCell(0,row);
				WritableCell valueCell = sheet.getWritableCell(1,row);
				WritableCell qualityCode = sheet.getWritableCell(2,row);
				WritableCell updateTime = sheet.getWritableCell(3,row);

				sheet.addCell(new Label(keyCell.getColumn(), keyCell.getRow(), tableLine.get(Constants.TABLE_COLUMN_1), keyCell.getCellFormat()));
				sheet.addCell(new Label(valueCell.getColumn(), valueCell.getRow(), tableLine.get(Constants.TABLE_COLUMN_2), valueCell.getCellFormat()));
				sheet.addCell(new Label(qualityCode.getColumn(), qualityCode.getRow(), tableLine.get(Constants.TABLE_COLUMN_3), qualityCode.getCellFormat()));
				sheet.addCell(new Label(updateTime.getColumn(), updateTime.getRow(), tableLine.get(Constants.TABLE_COLUMN_4), updateTime.getCellFormat()));
				row++;
			}

			wwb.write();
			wwb.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 解析excel文件
	 *
	 * @param fileName
	 * @return
	 */
	public static ServiceData loadFromfile(String fileName){
		ServiceData serviceData = new ServiceData();
		try {
			InputStream is = new FileInputStream(fileName);

			Workbook work = Workbook.getWorkbook(is);
			Sheet sheet = work.getSheet(0);

			Table table = new Table();
			table.setName(Constants.TABLE);

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
		ServiceData serviceData = loadFromfile("D://111.xls");
		String str = JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceData.getServiceId());
		System.out.println(str);

		serviceData.getTable("root").get("as").put("value", "111111444111111111111111");
		String str2 = JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceData.getServiceId());
		System.out.println(str2);

		writeToFile("D://111.xls", serviceData);

//		ServiceData temp = JsonDataUtils.decodeServiceDataFromJsonStr(str);
//		String str2 = JsonDataUtils.encodeJsonStrFromServiceData(temp, temp.getServiceId());
//		System.out.println(str2);
	}

}
