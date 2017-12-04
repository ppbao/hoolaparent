package com.unisharing.hoola.hoolacommon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期Util类
 *
 */
public class DateUtil {
    private static String defaultDatePattern = "yyyy-MM-dd";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 返回预设Format的当前日期字符串
     */
    public static String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * 使用预设Format格式化Date成字符串
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用参数Format格式化Date成字符串
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";

        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }

        return (returnValue);
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String strDate) {
    	try {
            return parse(strDate, getDatePattern());
    	}catch (ParseException pe){
    		pe.printStackTrace();
    	}
    	return null;
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(strDate);
    }

    /**
     * 在日期上增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
    /**
     * 在日期上增加天数
     */
    public static Date addday(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }
    
    /**
     * 获取第二天的0点时间
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	public static Date nextDay(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(date.getYear()+1900, date.getMonth(), date.getDate(), 0, 0, 0);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
	}
    
    public static String getTime(String time){
    	Date date=new Date(Long.parseLong(time));
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return df.format(date);
    }
    
    public static long getBefor24HoursTime(){
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -24);
    	return cal.getTimeInMillis();
    }
    
    public static void main(String[] args) {
    	DateUtil.getBefor24HoursTime();
	}
}