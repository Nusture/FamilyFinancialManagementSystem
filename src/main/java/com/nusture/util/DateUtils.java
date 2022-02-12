package com.nusture.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    public Calendar toCalendar(Date date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca;
    }

    public int getDay(Date date) throws ParseException {
        Calendar ca = toCalendar(date);
        return ca.get(Calendar.DAY_OF_WEEK);
    }
    public int getWeek(Date date) throws ParseException {
        Calendar ca = toCalendar(date);
        return ca.get(Calendar.WEEK_OF_YEAR)-1;
    }
    public int getMonth(Date date) throws ParseException {
        Calendar ca = toCalendar(date);
        return ca.get(Calendar.MONTH);
    }
    public int getYear(Date date) throws ParseException {
        Calendar ca = toCalendar(date);
        return ca.get(Calendar.YEAR);
    }
    public List<Map<String, Object>> getAllYearDate(String year) throws ParseException {
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        List<String> dateList = new ArrayList<>();
        String dateStart=year+"-01-01";
        String dateEnd=year+"-12-31";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long startTime = sdf.parse(dateStart).getTime();//start
        long endTime = sdf.parse(dateEnd).getTime();//end
        long day=1000*60*60*24;
        for(long i=startTime;i<=endTime;i+=day) {
            dateList.add(sdf.format(new Date(i)));
        }
        int x = 1;
        int week =0;
        for(String s:dateList){
            map = new HashMap<>();
            map.put("date",s);
            map.put("week",week+"");
            map.put("month",getMonth(sdf.parse(s)));
            map.put("commits",0);
            map.put("day",x);
            x+=1;
            if(x==8){
                x=0;
                week+=1;
                map.put("day",x);
                map.put("week",week+"");
                x+=1;
            }
            list.add(map);
        }
        return list;
    }
}
