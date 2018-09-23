package com.haoge.shijie.util;

public class CryptoHelper {
    private static String key = "haogedad";

    public static boolean isStringInArray(String str, String[] array) {
        for (String val : array) {
            if (str.equals(val)) {
                return true;
            }
        }
        return false;
    }

    public static String encode(String str) {
        String enStr = "";
        try {
            enStr = DesUtil.encrypt(str, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return enStr;
    }

    public static String decode(String str) {
        String deStr = "";
        try {
            deStr = DesUtil.decrypt(str, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deStr;
    }
}

