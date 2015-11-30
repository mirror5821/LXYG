package dev.mirror.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {
	
	/**
	 * 普通时间转unix时间
	 * @param time
	 * @return
	 */
	public static  long Time2Unix(String time){
		try {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime()/1000);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * unix时间转普通时间
	 * @param timestampString
	 * @param formats
	 * @return
	 * "1437717600", "yyyy-MM-dd HH:mm:ss"
	 * 
	 */
	public static String TimeStamp2Date(String timestampString, String formats){
		Long timestamp = Long.parseLong(timestampString)*1000;
		String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
		return date;
	}
}
