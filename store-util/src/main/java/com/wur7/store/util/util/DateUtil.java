package com.wur7.store.util.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";//默认
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DD = "dd";
    public static final String YYYY__MM__DD = "YYYY年MM月DD日";

    private DateUtil() {
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        if (pattern == null || "".equals(pattern.trim())){
            pattern = YYYY_MM_DD;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 解析日期
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date, String pattern) throws ParseException {
        if (date == null || "".equals(date.trim())){
            throw new IllegalArgumentException("date not allowed to be null or empty");
        }
        if (pattern == null || "".equals(pattern.trim())){
            pattern = YYYY_MM_DD;
        }
        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * 以指定时间为基准，加减年
     *
     * @param date
     * @param addYears
     * @return
     */
    public static Date addYears(Date date, int addYears) {
        return add(date,addYears,0,0,0,0,0);
    }

    /**
     * 以指定时间为基准，加减月份
     *
     * @param date
     * @param addMonths
     * @return
     */
    public static Date addMonths(Date date, int addMonths) {
        return add(date,0,addMonths,0,0,0,0);
    }

    /**
     * 以指定时间为基准，加减天数
     *
     * @param date
     * @param addDays
     * @return
     */
    public static Date addDays(Date date, int addDays) {
        return add(date,0,0,addDays,0,0,0);
    }

    /**
     * 以指定时间为基准，加减小时
     *
     * @param date
     * @param addHours
     * @return
     */
    public static Date addHours(Date date, int addHours) {
        return add(date,0,0,0,addHours,0,0);
    }

    /**
     * 以指定时间为基准，加减分钟
     *
     * @param date
     * @param addMinutes
     * @return
     */
    public static Date addMinutes(Date date, int addMinutes) {
        return add(date,0,0,0,0,addMinutes,0);
    }

    /**
     * 以指定时间为基准，加减秒
     *
     * @param date
     * @param addSeconds
     * @return
     */
    public static Date addSeconds(Date date, int addSeconds) {
        return add(date,0,0,0,0,0,addSeconds);
    }

    public static Date add(Date date,int addYears,int addMonths,int addDays,int addHours,int addMinutes,int addSeconds) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, addYears);
        c.add(Calendar.MONTH, addMonths);
        c.add(Calendar.DATE, addDays);
        c.add(Calendar.HOUR, addHours);
        c.add(Calendar.MINUTE, addMinutes);
        c.add(Calendar.SECOND, addSeconds);
        return c.getTime();
    }

    /**
     * 两个时间相差多少天
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static int daysBetween(Date minDate, Date maxDate) {
        if (null == minDate || null == maxDate){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(minDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(maxDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 两个日期之间相差几个月
     * tips:
     *      2021-06-01,2021-06-30 return 0
     *      2021-06-30,2021-07-01 return 1
     *      2021-12-30,2022-01-01 return 1
     *      2021-12-30,2023-01-01 return 13
     * @param minDate
     * @param maxDate
     * @return
     */
    public static int monthsBetween(Date minDate, Date maxDate) {
        if (null == minDate || null == maxDate){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(minDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(maxDate);
        int years = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        if(years == 0){
            return Math.abs(cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
        }else if(years > 0){
            return (years - 1) * 12 + cal2.get(Calendar.MONTH) + (12 - cal1.get(Calendar.MONTH));
        }else {
            return Math.abs((years + 1) * 12) + cal1.get(Calendar.MONTH) + (12 - cal2.get(Calendar.MONTH));
        }
    }

    public static void main(String[] args) throws ParseException {
		System.out.println(today());
        Date date1 = DateUtil.parseDate("2021-01-01", DateUtil.YYYY_MM_DD);
        Date date2 = DateUtil.getLastTimeInMonth(date1, 9);
        System.out.println(DateUtil.formatDate(date2,DateUtil.YYYY_MM_DD));
        System.out.println(monthsBetween(date1, date2));
        System.out.println(monthsBetween(date2, date1));
    }

    /**
     * 两个时间相差多少秒
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static long secondsBetween(Date minDate, Date maxDate) {
        if (null == minDate || null == maxDate){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(minDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(maxDate);
        long time2 = cal.getTimeInMillis();
        long between_seconds = (time2 - time1) / 1000L;
        return between_seconds;
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (null == date1 || null == date2){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取两个时间内的每一天的集合
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static Date[] iteratorDate(Date minDate, Date maxDate) {
        if (null == minDate || null == maxDate){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        int daysBetween = daysBetween(minDate, maxDate);
        Date[] dates = new Date[daysBetween + 1];
        dates[0] = minDate;

        for (int i = 1; i <= daysBetween; ++i) {
            dates[i] = addDays(minDate, i);
        }
        return dates;
    }

    /**
     * 获取指定时间在月份中的第几天
     *
     * @param date
     * @return
     */
    public static int sumInMonth(Date date) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        return gcLast.get(Calendar.DATE);
    }

    /**
     * 以某个时间为准，获取前后某个月中的第几天
     *
     * @param date
     * @param addMonth
     * @param daySum
     * @return
     */
    public static Date getDateInMonth(Date date, int addMonth, int daySum) {
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.add(Calendar.MONTH, addMonth);
        gcLast.set(Calendar.DATE, daySum);
        return gcLast.getTime();
    }

    /**
     * 是否为月份中的第一天
     *
     * @param date
     * @return
     */
    public static boolean isFirstInMonth(Date date) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        int i = sumInMonth(date);
        return i == 1 ? true : false;
    }

    /**
     * 是否为月份中的最后一天
     *
     * @param date
     * @return
     */
    public static boolean isLastInMonth(Date date) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当天的开始时间，精确到毫秒
     *
     * @param date
     * @return
     */
    public static Date getFirstTime(Date date) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当天的结束时间，精确到毫秒
     *
     * @param date
     * @return
     */
    public static Date getLastTime(Date date) {
        if (null == date) {
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 以当前时间为基准，获取某月的开始时间，精确到毫秒
     * @param addMonth
     * @return
     */
    public static Date getFirstTimeInMonth(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, addMonth);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 以指定时间为基准，获取前后某个月的开始时间，精确到毫秒
     *
     * @param addMonth
     * @return
     */
    public static Date getFirstTimeInMonth(Date date, int addMonth) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonth);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 以当前时间为基准，获取某月的结束时间，精确到毫秒
     * @param addMonth
     * @return
     */
    public static Date getLastTimeInMonth(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, addMonth);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 以指定时间为基准，获取前后某个月的结束时间，精确到毫秒
     *
     * @param addMonth
     * @return
     */
    public static Date getLastTimeInMonth(Date date, int addMonth) {
        if (null == date){
            throw new IllegalArgumentException("date not allowed to be null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonth);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 以指定时间为基准，加减天数
     * @param date
     * @param days
     * @return
     */
    public static Date getFixedDays(Date date, int days) {
        if(null == date){
            throw new IllegalArgumentException("date is null");
        }
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.add(Calendar.DATE, days);
        return day.getTime();
    }

    /**
     * 获取上个月年月
     */
    public static String getLastMonth() {
        return getLastMonth(0);
    }

    /**
     * 获取前几个月
     * @return
     */
    public static String getLastMonth(int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, month);
        Date m = c.getTime();
        return sdf.format(m);
    }


    /**
     * 获取某个月第一天的开始时刻
     * @param month
     * @return
     */
    public static Date getFirstDayTimeOfMonth(int month) throws ParseException {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期，获取开始时刻
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return parseDate(sdf.format(cal.getTime()) + " 00:00:00",YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获得某月的最后一天的最后时刻
     * @param month  要获取的月份
     * @return
     */
    public static Date getLastDayTimeOfMonth(int month) throws ParseException {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取月份的最大天数
        int lastDay = 0;
        //2月份每年的天数不固定
        if (month == 2) {
            lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期，获取最后时刻
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return parseDate(sdf.format(cal.getTime()) + " 23:59:59",YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取某个月第一天
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期，获取开始时刻
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    /**
     * 获得某月的最后一天
     * @param month  要获取的月份
     * @return
     */
    public static String getLastDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取月份的最大天数
        int lastDay = 0;
        //2月份每年的天数不固定
        if (month == 2) {
            lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期，获取最后时刻
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonthYear(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonthYear(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }


    //获取指定时间的上一个周一
    public static String getLastWeekMonday(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (1 == cal.get(Calendar.DAY_OF_WEEK)) {//判断当前日期是否为周末，因为周末是本周第一天，如果不向后推迟一天的到的将是下周一的零点，而不是本周周一零点
            cal.add(Calendar.DATE, -1);
        }
        cal.add(Calendar.DAY_OF_MONTH, -7);//时间减去7天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//Calendar.MONDAY 周一  想获取周几就更换这个

        return DateUtil.formatDate(cal.getTime(),DateUtil.YYYY_MM_DD);
    }

    //获取指定时间上一个周日
    public static String getLastWeekSunday(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (1 == cal.get(Calendar.DAY_OF_WEEK)) {//判断当前日期是否为周末，因为周末是本周第一天，如果不向后推迟一天的到的将是下周一的零点，而不是本周周一零点
            cal.add(Calendar.DATE, -1);
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//因为周末为一周的第一天，因此时间不需要在当前时间的基础上面减少7天；直接设置就可以
        return DateUtil.formatDate(cal.getTime(),DateUtil.YYYY_MM_DD);
    }

    //本周周一
    public static String getWeekStart(){
        Calendar cal=Calendar.getInstance();
        if (1 == cal.get(Calendar.DAY_OF_WEEK)) {//判断当前日期是否为周末，因为周末是本周第一天，如果不向后推迟一天的到的将是下周一的零点，而不是本周周一零点
            cal.add(Calendar.DATE, -1);
        }
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtil.formatDate(cal.getTime(),DateUtil.YYYY_MM_DD);
    }

    //本周周天
    public static String getWeekEnd(){
        Calendar cal=Calendar.getInstance();
        if (1 == cal.get(Calendar.DAY_OF_WEEK)) {//判断当前日期是否为周末，因为周末是本周第一天，如果不向后推迟一天的到的将是下周一的零点，而不是本周周一零点
            cal.add(Calendar.DATE, -1);
        }
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return DateUtil.formatDate(cal.getTime(),DateUtil.YYYY_MM_DD);
    }

    //指定时间的上一个月
    public static String getLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        /**
         * 除1月31日外；其余31日获取前一月存在问题
         */
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        calendar.add(Calendar.MONTH, -1);
        return DateUtil.formatDate(calendar.getTime(),DateUtil.YYYY_MM);
    }
	
	public static Date today() {
		return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
