package com.ruoyi.system.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2023/4/1 09:13
 */
public class DateUtils {


    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getCurrentDate(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date());
    }

    public static List<String> getWeekDateList() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();

        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();

        List<String> lDate = new ArrayList<>();
        lDate.add(format.format(mondayDate));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(mondayDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(sundayDate);
        //测试此日期是否在指定日期之后
        while (sundayDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(format.format(calBegin.getTime()));
        }
        return lDate;
    }


    public static List<String> getMonthFullDay(int year, int month) {
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month - 1);
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count; j++) {
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return fullDayList;
    }


    /**
     * 获得12个月时间戳
     */
    public static List<String> getMonthStr() throws Exception {
        List<String> list = new ArrayList<>();
        Long sj = System.currentTimeMillis();//时间戳
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
        String dateStr = dateformat.format(sj);
        for (int i = 1; i <= 12; i++) {
//            List<Object> li = new ArrayList<Object>();
//            li.add(dateStr + "-" + i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String mydate = dateStr + "-" + i;
            Date datetime = sdf.parse(mydate);
            list.add(sdf.format(datetime));
        }
        return list;
    }
}
