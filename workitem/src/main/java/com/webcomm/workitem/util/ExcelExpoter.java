package com.webcomm.workitem.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webcomm.workitem.model.Item;
import com.webcomm.workitem.model.Schedule;
import com.webcomm.workitem.service.ItemService;
import com.webcomm.workitem.service.ScheduleService;

/**
 * @author Evan
 *
 */
@Component
public class ExcelExpoter {

	private static final Map<Integer, String> map;
	static {
		map = new HashMap<Integer, String>();
		map.put(1, "一月");
		map.put(2, "二月");
		map.put(3, "三月");
		map.put(4, "四月");
		map.put(5, "五月");
		map.put(6, "六月");
		map.put(7, "七月");
		map.put(8, "八月");
		map.put(9, "九月");
		map.put(10, "十月");
		map.put(11, "十一月");
		map.put(12, "十二月");
	}

	@Autowired
	ItemService itemService;
	@Autowired
	ScheduleService scheduleService;

	HSSFWorkbook wb = new HSSFWorkbook();
	// CreationHelper 可以理解為一個工具類，由這個工具類可以獲得 日期格式化的一個例項
	CreationHelper creationHelper = wb.getCreationHelper();

	/* 假日樣式 */
	CellStyle dayOffStyle = wb.createCellStyle();
	/* 工作樣式 */
	CellStyle style1 = wb.createCellStyle();
	/* 休假樣式 */
	CellStyle style2 = wb.createCellStyle();
	/* 時間格式 */
	CellStyle timestyle = wb.createCellStyle();
	// 設定時間樣式

	public HSSFWorkbook exportXLS() {
		timestyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));
		HSSFSheet sheet;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		List<Item> list = itemService.findAll();

		createSKDSheet();

		String tempDate = "";
		Integer count = 0;
		for (Item i : list) {
			if (!tempDate.equals(sdf.format(i.getCreateDate()))) {
				tempDate = sdf.format(i.getCreateDate());
			}
			if (wb.getSheet(tempDate) != null) {
				sheet = wb.getSheet(tempDate);
				count += 1;
			} else {
				sheet = wb.createSheet(tempDate);
				count = 0;
			}

			// 建立行（從0開始）
			Row row = sheet.createRow(count);
			// 建立單元格(第一列)
			Cell indexCell = row.createCell(0); // 項次
			Cell userCell = row.createCell(1); // 使用者
			Cell contentCell = row.createCell(2); // 分類
			Cell categoryCell = row.createCell(3); // 分類細項
			Cell categoryDetailCell = row.createCell(3); // 工作內容
			Cell workTimeCell = row.createCell(4); // 時數
			Cell dateCell = row.createCell(5); // 建立日期

			// 給單元格賦值
			if (count == 0) {
				indexCell.setCellValue("項次");
				userCell.setCellValue("使用者");
				categoryCell.setCellValue("分類");
				categoryDetailCell.setCellValue("分類細項");
				contentCell.setCellValue("工作內容");
				workTimeCell.setCellValue("時數");
				dateCell.setCellValue("建立日期");
			} else {
				indexCell.setCellValue(count);
				userCell.setCellValue(i.getPccDeveloper().getName());
				categoryCell.setCellValue(i.getCategoryDetail().getDescription());
				categoryDetailCell.setCellValue(i.getCategoryDetail().getCategory().getDescription());
				contentCell.setCellValue(i.getContent());
				workTimeCell.setCellValue(i.getWorkTime());
				dateCell.setCellValue(i.getCreateDate());
				dateCell.setCellStyle(timestyle);
			}

		}
		return wb;
	}

	public void createSKDSheet() {
		Calendar calendar = Calendar.getInstance();
		Date startDate = itemService.getStartDate();
		Date endDate = itemService.getLastDate();
		calendar.setTime(startDate);
		Integer startYear = calendar.get(Calendar.YEAR);
		calendar.setTime(endDate);
		Integer endYear = calendar.get(Calendar.YEAR);
		List<Schedule> skdList = new ArrayList<Schedule>();
		for (int i = startYear; i <= endYear; i++) {
			for (Schedule skd : scheduleService.getByYear(i)) {
				skdList.add(skd);
			}
		}
		HSSFSheet sheet = null;
		if (null == wb.getSheet(startYear + "行事曆")) {
			sheet = wb.createSheet(startYear + "行事曆");
		} else {
			sheet = wb.getSheet(startYear + "行事曆");
		}
		Schedule skd = skdList.get(0); // 第一天
		calendar.setTime(skd.getSkdDate());
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);

		int count = 0;
		count = count + weekday - 1; // Calendar 0 為星期六，從第二行開始填日期
		int currentRow = 0;

		Calendar currentC = Calendar.getInstance();
		Calendar lastC = Calendar.getInstance();

		for (int i = 0; i < skdList.size(); i++) {

			Schedule s = skdList.get(i); // 目前schedule
			Schedule lastS; // 上次schedule
			if (i >= 1) {
				lastS = skdList.get(i - 1);
			} else {
				lastS = skdList.get(i);
			}

			currentC.setTime(s.getSkdDate()); // 目前日期
			lastC.setTime(lastS.getSkdDate()); // 上次日期

			// 設定 月、星期
			if (currentC.get(Calendar.DATE) <= lastC.get(Calendar.DATE)) {
				currentRow = setHeadRow(sheet, currentRow, currentC.get(Calendar.MONTH) + 1);
			}

			Row r;// 設定行
			if (null == sheet.getRow(currentRow)) {// 若有此行改get，若無create
				r = sheet.createRow(currentRow);
			} else {
				r = sheet.getRow(currentRow);
			}
			Cell c = r.createCell(count % 7); // 設定列
			c.setCellValue(currentC.get(Calendar.DATE) + (null == s.getNote() ? "" : s.getNote()));
			fillColor(s, c);
			count++;
			if (count % 7 == 0)
				currentRow++;

		}
//		fillColor(sheet);

	}

	/**
	 * 填上非工作日顏色
	 * 
	 * @param skd
	 * @param cell
	 */
	public void fillColor(Schedule skd, Cell cell) {

		dayOffStyle.setFillForegroundColor(IndexedColors.PINK.getIndex()); // 前景色
		dayOffStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (skd.getIsDayOff() == 2) {// 代表放假
			System.out.println(skd.getSkdDate());
			cell.setCellStyle(dayOffStyle);
		}
	}

	/**
	 * 填上workitem顏色
	 * 
	 * @param item
	 * @param cell
	 */
	public void fillColor(HSSFSheet sheet) {

		/* 工作樣式 */
		style1.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // 前景色
		style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		/* 休假樣式 */
		style2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // 前景色
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Calendar calendar = Calendar.getInstance();
		List<Item> itemList = itemService.findAllAsc();
		int totalTime = 0;
		Date currentDate = null;
		Set<String> workSet = new HashSet<String>();
		Item firstItem = null; // WI的起頭
		for (int i = 0; i < itemList.size(); i++) {
			if (null == firstItem) {
				firstItem = itemList.get(i);
			}
			Item currentItem = itemList.get(i);

			if (null == currentDate) {
				currentDate = currentItem.getCreateDate(); // 設定workitem開始的時間
			}

			if (totalTime % 8 == 0 && totalTime / 8 > 0) {
				int currRow = 0;
				int currCell = 0;
				// 取得第一個item的格子
				calendar.setTime(currentDate);
				int month = calendar.get(Calendar.MONTH) + 1;// 0起算
				int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);// 1起算
				int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);// 1起算
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);// 1為星期日
				currRow = currRow + weekOfYear + month * 2;
				currCell = dayOfWeek;
				// 填入格子 totaltime-8
				while (totalTime > 0) {
					System.out.println(currRow);
					System.out.println(currCell);
//					if ("休假事項".equals(currentItem.getCategory().getDescription())) {
//						sheet.getRow(currRow - 1).getCell(currCell).setCellValue("cool");
//						sheet.getRow(currRow - 1).getCell(currCell).setCellStyle(style2);
//						fillcell = cell.getRow().createCell(cell.getColumnIndex() + 1); // 下一格
//					} else {
//						sheet.getRow(currRow - 1).getCell(currCell).setCellValue("cool");
//						sheet.getRow(currRow - 1).getCell(currCell).setCellStyle(style1);
//						fillcell = cell.getRow().createCell(cell.getColumnIndex() + 1);
//					}
					currCell = (currCell + 1) % 7;
					currRow = currCell / 7;
					totalTime -= 8;
				}
				getStringConcation(workSet);
				currentDate = null; // 重置workitem日期
				totalTime = 0;// 重置workitem時間
			} else {
//				workSet.add(currentItem.getCategory().getDescription());
				totalTime += currentItem.getWorkTime();
			}

		}

	}

	/**
	 * 新增兩行header(月份、星期
	 * 
	 * @param sheet 分頁
	 * @param rnum  要新增的row index
	 * @param month 月份
	 * @return
	 */
	public Integer setHeadRow(HSSFSheet sheet, int rnum, int month) {

		/* 工作樣式 */
		style1.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // 前景色
		style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style1.setAlignment(HorizontalAlignment.CENTER); // 水平方向

		/* 休假樣式 */
		style2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // 前景色
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int currentRow = rnum;
		Row firstRow = sheet.createRow(currentRow);
		firstRow.createCell(0).setCellValue(map.get(month));
		firstRow.getCell(0).setCellStyle(style1);
		sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 6));
		currentRow++; // 插入第一行
		Row secondRow = sheet.createRow(currentRow);
		secondRow.createCell(0).setCellValue("日");
		secondRow.createCell(1).setCellValue("一");
		secondRow.createCell(2).setCellValue("二");
		secondRow.createCell(3).setCellValue("三");
		secondRow.createCell(4).setCellValue("四");
		secondRow.createCell(5).setCellValue("五");
		secondRow.createCell(6).setCellValue("六");
		secondRow.getCell(0).setCellStyle(style2);
		secondRow.getCell(1).setCellStyle(style2);
		secondRow.getCell(2).setCellStyle(style2);
		secondRow.getCell(3).setCellStyle(style2);
		secondRow.getCell(4).setCellStyle(style2);
		secondRow.getCell(5).setCellStyle(style2);
		secondRow.getCell(6).setCellStyle(style2);
		currentRow++; // 插入第二行
		return currentRow; // 接下來要從這行開始
	}

	public String getStringConcation(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
		}
		return sb.toString();
	}

}
