package com.haoge.shijie.util;


public class StrJudgeUtil {

    public static boolean isCorrectStr(String str){
        return str!=null&&!str.equals("")&&str.length()>0;
    }
    public static boolean isCorrectInt(Integer num){
        return num!=null&&!num.equals("")&&num>=0;
    }



}
