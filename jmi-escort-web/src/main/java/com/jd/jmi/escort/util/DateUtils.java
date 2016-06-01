package com.jd.jmi.escort.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtils {
    private static final Log log = LogFactory.getLog(DateUtils.class);

    public static final long TIME_CARRY = 60; /*时间进位制*/
    public static final long SECOND_MILLIS = 1000; /*一秒的毫秒数*/
    public static final long MINUTE_MILLIS = SECOND_MILLIS * 60; /*一分钟的毫秒数*/
    public static final long HOUR_MILLIS = MINUTE_MILLIS * 60;  /*一个小时的毫秒数*/
    public static final long DAY_MILLIS = HOUR_MILLIS * 24; /*一天的毫秒数*/

    public static final String SIMPLE_FOMAT = "yyyy-MM-dd";

    public static final String FULL_FOMAT = "yyyy-MM-dd hh:mm:ss";

    /**
     * 字符串转时间，转失败返回默认
     *
     * @param dateString
     * @param pattern
     * @param defaultValue
     * @return
     */
    public static Date parse(String dateString, String pattern, Date defaultValue) {
        if (StringUtils.isEmpty(dateString) || StringUtils.isEmpty(pattern)) {
            return defaultValue;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            log.error("parse date error", e);
        }
        return defaultValue;
    }


    /**
     * getWeekTime
     *
     * @param flag: 0 本周 1下周 n下n周
     * @return
     */
    public static Date getWeekTime(int flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, flag);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new Date(calendar.getTimeInMillis());
    }

    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return 7;
        } else return dayOfWeek - 1;
    }

    public static boolean withinPeriod(long bTime, long eTime) {
        return withinPeriod(System.currentTimeMillis(), bTime, eTime);
    }

    public static boolean withinPeriod(long nowTime, long bTime, long eTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTimeInMillis() + bTime;
        long endTime = calendar.getTimeInMillis() + eTime;
        if (nowTime >= beginTime && nowTime <= endTime) {
            return true;
        } else {
            return false;
        }
    }

    public static String format(long time, String format) {
        if (format == null) {
            format = "MM-dd HH:mm:ss";
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(new Date(time)).toString();
    }

    public static String format(long time) {
        return format(time, null);
    }

    public static String format2(long time) {
        return format(time, "MM月dd日 HH:mm:ss");
    }

    /**
     * 获得当天零时零分零秒 的毫秒数
     *
     * @return long
     */
    public static long getDailyTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获得当时零分零秒 的毫秒数
     *
     * @return long
     */
    public static long getHourlyTime(long time) {
        return getHourlyTime(time, 0);
    }

    /**
     * 获得当前时间的 小时的数值
     *
     * @return int
     */
    public static int getHour(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得当前时间的 分钟数
     *
     * @return int
     */
    public static int getMinute(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获得当前时间是这个月的几号
     *
     * @return int
     */
    public static int getDayOfMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前时间是周几？ SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, and SATURDAY
     *
     * @return int
     */
    public static int getDayOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得此刻前N个或后N个小时零分零秒 的毫秒数
     *
     * @return long
     */
    public static long getHourlyTime(long time, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int hour = calendar.get(Calendar.HOUR_OF_DAY) + n;
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        return calendar.getTimeInMillis();
    }

    public static long getDailyTime(long time, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH) + n;
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }

    public static long getDailyTime(long time) {
        return getDailyTime(time, 0);
    }


    /**
     * 判断日期是否在今天之前
     *
     * @param endTime
     * @return
     */
    public static boolean isBeforeToday(long endTime) {
        return endTime < getDailyTime(System.currentTimeMillis(), 0);
    }

    /**
     * 判断是否是0时0分0秒
     *
     * @param time
     * @return
     */
    public static boolean isDailyTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0;
    }

    /**
     * 判断是否在同一天
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean isInOneDay(long beginTime, long endTime) {
        return endTime == beginTime;
    }

    /**
     * 判断是否是今天
     *
     * @return
     */
    public static boolean isToday(long beginTime, long endTime) {
        return isTodayBegin(beginTime) && isTodayEnd(endTime);
    }

    /**
     * 判断是否为一天开始时间
     *
     * @return
     */
    public static boolean isTodayBegin(long time) {
        return time == getDailyTime();
    }

    /**
     * 判断是否为一天结束时间
     *
     * @return
     */
    public static boolean isTodayEnd(long time) {
        return time == getDailyTime();//getDailyTime(System.currentTimeMillis(),1);
    }

    /**
     * 获得当日0时0分0秒
     *
     * @param time
     * @return Calendar
     */
    public static Calendar getAdjustedCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 计算总共经历了多少天.如1.1-1.2结果为2天.
     *
     * @param fDate
     * @param eDate
     * @return
     * @author shiyue[jianpingcao@sohu-inc.com][2012-2-7]
     */
    public static long getDisDays(Date fDate, Date eDate) {
        Calendar fc = Calendar.getInstance();
        fc.setTime(fDate);
        fc.set(Calendar.HOUR_OF_DAY, 0);
        fc.set(Calendar.MINUTE, 0);
        fc.set(Calendar.SECOND, 0);
        fc.set(Calendar.MILLISECOND, 0);
        //System.out.println(fc.getTime());

        Calendar ec = Calendar.getInstance();
        ec.setTime(eDate);
        ec.set(Calendar.HOUR_OF_DAY, 0);
        ec.set(Calendar.MINUTE, 0);
        ec.set(Calendar.SECOND, 0);
        ec.set(Calendar.MILLISECOND, 0);
        //System.out.println(ec.getTime());

        long days = (ec.getTimeInMillis() - fc.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        return days < 0 ? -1 : days + 1;
    }

    /**
     * 获取当前日期 格式: yyyy-M-d
     *
     * @return
     */
    public static String getDate() {
        Date date = new Date();
        String formater = "yyyy-M-d"; // yyyy-M-d
        SimpleDateFormat format = new SimpleDateFormat(formater);
        return format.format(date);
    }

    /**
     * 获取当前日期 格式:参数 例如 yyyy-MM-dd
     *
     * @param formatStr
     * @return
     */
    public static String getDate(String formatStr) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(formatStr);
        // TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");
        // df.setTimeZone(zone);
        return df.format(c.getTime());
    }

    /**
     * 格式化指定日期 格式:参数 例如 yyyy-MM-dd
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String getDate(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr == null ? "yyyy-MM-dd HH:mm:ss" : formatStr);// yyyy-MM-dd
        // HH:mm:ss
        return date == null ? "" : format.format(date);
    }

    /**
     * 获取当前日期时间 格式: yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static Date getDateTime() {
        return getDateTime(getDate("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取当前日期时间 格式: yyyy-MM-dd HH:mm:ss
     *
     * @param dtime
     * @return Date
     */
    public static Date getDateTime(String dtime) {
        try {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format1.parse(dtime);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 获取当前日期时间 格式:参数 例如 yyyy-MM-dd HH:mm:ss
     *
     * @param dtime
     * @param formatStr
     * @return
     */
    public static Date getDateTime(String dtime, String formatStr) {
        try {
            DateFormat format1 = new SimpleDateFormat(formatStr);
            return format1.parse(dtime);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 格式化时间 格式: yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return String
     */
    public static String formatDate(String date) {
        String formater = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(formater);
        return date == null ? null : format.format(getDateTime(date));
    }

    /**
     * 格式化时间 格式:第2参数 例如 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param formatStr
     * @return String
     */
    public static String formatDate(String date, String formatStr) {
        String formater = formatStr;
        SimpleDateFormat format = new SimpleDateFormat(formater);
        return date == null ? null : format.format(getDateTime(date));
    }

    /**
     * 时间间隔 是否在当前时间范围
     *
     * @param btime
     * @param etime
     * @return
     */
    public static boolean dateBound(String btime, String etime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date sd = formatter.parse(btime, new ParsePosition(0));
        Date ed = formatter.parse(etime, new ParsePosition(0));
        return dateBound(sd, ed);
    }

    public static boolean dateBound(Date sd, Date ed) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date td = formatter.parse(getDate(), new ParsePosition(0)); // 当前时间
        if (sd == null || ed == null || td == null) return false;
        if (sd.getTime() > td.getTime() || ed.getTime() < td.getTime()) return false;
        return true;
    }

    /**
     * 显示当前时间与目标时间的距离
     * <p/>
     * **秒前  ** 分钟前  ** 小时前 超过7天显示时间
     *
     * @param ctime
     * @param format
     * @return
     */
    public static String distance(Date ctime, String format) {
        if (ctime == null) return "";
        format = format == null ? "yyyy-MM-dd HH:mm" : format;
        long result = Math.abs(System.currentTimeMillis() - ctime.getTime());
        if (result < 60000) {
            return (result / 1000) + "秒钟前";
        } else if (result >= 60000 && result < 3600000) {
            return (result / 60000) + "分钟前";
        } else if (result >= 3600000 && result < 86400000) {
            return (result / 3600000) + "小时前";
        } else if (result >= 86400000 && result < 86400000 * 7) {
            return (result / 86400000) + "天前";
        } else {
            return getDate(ctime, format);
        }
    }

    public static void main(String[] args) {
        System.out.println(getDisDays(new Date(), new Date()));
    }
}

