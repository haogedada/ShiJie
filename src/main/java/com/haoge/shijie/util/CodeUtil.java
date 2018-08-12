package com.haoge.shijie.util;

import java.util.Random;
import java.util.UUID;

public class CodeUtil {
    //生成唯一的激活码
    public static String generateUniqueCode() {
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        long nowtime = (System.currentTimeMillis() - 19970715) * 609218;
        return String.valueOf(nowtime) + "-" + code;
    }

    //生成验证码
    public static String RandomCode() {
        String sjnum = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer misi = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(sjnum.length());
            misi.append(sjnum.charAt(number));
        }
        return misi.toString();
    }


}
