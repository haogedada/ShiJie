package com.haoge.shijie.util;


public class StrJudgeUtil {

    //判断是否是合法字符串
    public static boolean isCorrectStr(String str) {
        return str != null && !str.equals("") && str.length() > 0;
    }

    //判断是否是合法数字
    public static boolean isCorrectInt(Integer num) {
        return num != null && !num.equals("") && num >= 0;
    }

    //判断是否是合法邮箱
    public static boolean isEmail(String email) {
        return email.matches("^\\w+@(\\w+\\.)+\\w+$");
    }

}
