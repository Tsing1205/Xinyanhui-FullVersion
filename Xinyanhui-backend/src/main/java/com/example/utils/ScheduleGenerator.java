package com.example.utils;

import com.example.constants.TypeConstant;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleGenerator {
    private static String KEY="schedule";
    public static LocalDate getFirstDayOfNextMonth(){
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 1);
    }

    public static LocalDate getLastDayOfNextMonth(LocalDate firstDayOfMonth){
        return firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
    }


    public static List<LocalDate> generateDate(LocalDate firstDayOfMonth,LocalDate lastDayOfMonth,String dayStr){
        List<LocalDate> scheduleDates = new ArrayList<>();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayStr.toUpperCase());

        // 获取本月第一天是周几
        DayOfWeek startDayOfWeek = firstDayOfMonth.getDayOfWeek();

        // 计算第一个匹配的日期
        LocalDate currentDate = firstDayOfMonth;
        if (!startDayOfWeek.equals(dayOfWeek)) {
            int daysToAdd = (dayOfWeek.getValue() - startDayOfWeek.getValue() + 7) % 7;
            currentDate = firstDayOfMonth.plusDays(daysToAdd);
        }

        // 遍历本月所有符合条件的日期
        while (!currentDate.isAfter(lastDayOfMonth)) {
            scheduleDates.add(currentDate);
            currentDate = currentDate.plusWeeks(1); // 移动到下一周相同的日子
        }

        return scheduleDates;
    }

    public static List<LocalDate> generateDate(String dayStr){
        LocalDate firstDayOfMonth = getFirstDayOfNextMonth();
        LocalDate lastDayOfMonth = getLastDayOfNextMonth(firstDayOfMonth);
        return generateDate(firstDayOfMonth,lastDayOfMonth,dayStr);
    }

    public static int[] getTime(String time){
        int[] Time = new int[2];    //Time[0] is start time,Time[1] is end time
        if(time.equals("AM")){
            Time[0] = 8;
            Time[1] = 12;
        }
        else if(time.equals("PM")){
            Time[0] = 13;
            Time[1] = 17;
        }
        return Time;
    }

    public static String getConsultantKey(String time,String day){
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return KEY+":"+nextMonth.getMonthValue()+":"+ TypeConstant.CONSULTANT_STR+":"+day+":"+time;
    }

    public static String getSupervisorKey(String time,String day){
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return KEY+":"+nextMonth.getMonthValue()+":"+ TypeConstant.SUPERVISOR_STR+":"+day+":"+time;
    }
}
