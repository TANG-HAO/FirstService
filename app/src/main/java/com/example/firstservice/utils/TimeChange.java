package com.example.firstservice.utils;

import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeChange {

    public static String timeLongToString(Long l){
        String time=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        time = formatter.format(curDate);
        return  time;
    }
    public static java.sql.Date timeOfDataToSql(){
        // util.date转换成sql.date
        java.util.Date utilDate = new java.util.Date();	//获取当前时间
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return  sqlDate;
    }
    public static java.util.Date timeOfSqlToData(){
        // sql.date转换成util.date
        java.sql.Date sqlDate1 = new java.sql.Date(new java.util.Date().getTime());
        java.util.Date utilDate1 = new java.util.Date(sqlDate1.getTime());
        
        return  utilDate1;
    }


}
