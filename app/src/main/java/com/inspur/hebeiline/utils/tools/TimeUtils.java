package com.inspur.hebeiline.utils.tools;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lixu on 2018/4/3.
 */

public class TimeUtils {
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static String getNowTimeToMounth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    //把日期转为字符串
    public static String converToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }
    /**
     * 时间转换为时间戳
     * @param time:需要转换的时间
     * @return
     */
    public static String dateToStamp(String time)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /**
     * 时间戳转换为字符串
     * @param time:时间戳
     * @return
     */
    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }
    /**
     *获取距现在某一小时的时刻
     * @param hour hour=-1为上一个小时，hour=1为下一个小时
     * @return
     */
    public static String getLongTime(int hour){
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int h; // 需要更改的小时
        h = c.get(Calendar.HOUR_OF_DAY) - hour;
        c.set(Calendar.HOUR_OF_DAY, h);
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Log.v("time",df.format(c.getTime()));
        return df.format(c.getTime());
    }
    /**
     * 比较时间大小
     * @param str1：要比较的时间
     * @param str2：要比较的时间
     * @return
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 当地时间 ---> UTC时间
     * @return
     */
    public static String Local2UTC(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }

    /**
     * UTC时间 ---> 当地时间
     * @param utcTime   UTC时间
     * @return
     */
    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }




    /**
     * 从时间(毫秒)中提取出日期
     *
     * @param millisecond
     * @return
     */
    public static String getDateFromMillisecond(Long millisecond) {

        Date date = null;
        try {
            date = new Date(millisecond);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Calendar current = Calendar.getInstance();


        ////今天
        Calendar today = Calendar.getInstance();

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));

        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        //昨天
        Calendar yesterday = Calendar.getInstance();

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);


        // 今年
        Calendar thisYear = Calendar.getInstance();

        thisYear.set(Calendar.YEAR, current.get(Calendar.YEAR));
        thisYear.set(Calendar.MONTH, 0);
        thisYear.set(Calendar.DAY_OF_MONTH, 0);
        thisYear.set(Calendar.HOUR_OF_DAY, 0);
        thisYear.set(Calendar.MINUTE, 0);
        thisYear.set(Calendar.SECOND, 0);


        current.setTime(date);

        //今年以前
        if (current.before(thisYear)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = format.format(date);
            return dateStr;
        } else if (current.after(today)) {
            return "今天";
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String dateStr = format.format(date);
            return dateStr;
        }
    }

    /**
     * 从时间(毫秒)中提取出时间(时:分)
     * 时间格式:  时:分
     *
     * @param millisecond
     * @return
     */
    public static String getTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(millisecond);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    /**
     * 将时间转化成毫秒
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static Long timeStrToSecond(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long second = format.parse(time).getTime();
            return second;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1l;
    }

    /**
     * 获取时间间隔
     *
     * @param millisecond
     * @return
     */
    public static String getSpaceTime(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();

        //间隔秒
        Long spaceSecond = (currentMillisecond - millisecond) / 1000;

        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return "片刻之前";
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟之前";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时之前";
        }
        //3天之内
        else if (spaceSecond/(60*60*24)>0&&spaceSecond/(60*60*24)<3){
            return spaceSecond/(60*60*24)+"天之前";
        }else {
            return getDateTimeFromMillisecond(millisecond);
        }
    }
}
