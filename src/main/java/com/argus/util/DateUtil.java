package com.argus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {

	public static final String YYMMDD = "yyMMdd";
	
	public static final String YYYYMMDDHH24MMSS = "yyyyMMdd HH:mm:ss";

	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static Date getFirstDayOfPreMonth(String maxEffDt) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date maxEffdt = format.parse(maxEffDt);
			Calendar cal = Calendar.getInstance();
			cal.setTime(maxEffdt);
			int month = cal.get(Calendar.MONTH);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DATE, 1);
			return cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		return df.format(calendar.getTime());
	}

	public static boolean isWeekend(Date date) {
		boolean isWeekend = false;

		if (date != null) {
			Calendar cal = Calendar.getInstance();

			cal.setTime(date);

			int value = cal.get(Calendar.DAY_OF_WEEK);

			if (value == Calendar.SATURDAY || value == Calendar.SUNDAY) {
				isWeekend = true;
			}
		}

		return isWeekend;

	}

	public static Date getIntervalDate(Date date, int interval) {
		Date preDt = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, interval);
		preDt = cal.getTime();
		return preDt;
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static Date parseDate(String dateStr, String format){
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Date start = parseDate("20140729130000","yyyyMMddHHmmss");
		Date end = parseDate("20140729130060","yyyyMMddHHmmss");
		long interval = end.getTime() - start.getTime();
		System.out.println(interval);
	}

}
