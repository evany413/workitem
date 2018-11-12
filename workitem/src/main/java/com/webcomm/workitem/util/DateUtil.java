package com.webcomm.workitem.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final String DATE_DELIMITER = "/";

	public static String toTwString(Date date) {
		return toTwString(date, DATE_DELIMITER);
	}

	public static String toTwString(Date date, String delimiter) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return toTwString(calendar, delimiter);
	}

	public static String toTwString(Calendar calendar, String delimiter) {
		int year = calendar.get(1);
		if (year < 1911) {
			throw new RuntimeException("Cannot convert years before 1911");
		}
		StringBuilder sb = new StringBuilder(9);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		if (year < 2011) {
			sb.append('0');
		}
		sb.append(year - 1911).append(delimiter);
		if (month < 10) {
			sb.append('0');
		}
		sb.append(month).append(delimiter);
		if (day < 10) {
			sb.append('0');
		}
		sb.append(day);
		return sb.toString();
	}

	/**
	 * 取得昨日日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYesterday(Date date) {
		Date d = getPureDate(date);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 取得下一日日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Date d = getPureDate(date);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * 取得日期(不含時分秒)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPureDate(Date date) {
		Date d = date;
		if (null == d) {
			d = new Date();
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}
}
