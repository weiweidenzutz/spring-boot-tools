package spring.boot.redis.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String formatDate(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	
	/**
     * 日期相减(返回秒值)
     * @param date Date
     * @param date1 Date
     * @return int
     * @author 
     */
    public static Long diffDateTime(Date date, Date date1) {
        return (Long) ((getMillis(date) - getMillis(date1))/1000);
    }
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }
 	/**
	 * 获取 指定日期 后 指定毫秒后的 Date
	 * 
	 * @param date
	 * @param millSecond
	 * @return
	 */
	public static Date getDateAddMillSecond(Date date, int millSecond) {
		Calendar cal = Calendar.getInstance();
		if (null != date) {// 没有 就取当前时间
			cal.setTime(date);
		}
		cal.add(Calendar.MILLISECOND, millSecond);
		return cal.getTime();
	}

	/**
	 * @Description: 取将来时间
	 * @param date
	 * @param expire
	 * @param idate
	 * @return Date
	 * @throws
	 */
	public static Date getDateAdd(Date date, int expire, int idate) {
		Calendar calendar = Calendar.getInstance();
		if (null != date) {// 默认当前时间
			calendar.setTime(date);
		}
		calendar.add(idate, expire);
		return calendar.getTime();
	}
}
