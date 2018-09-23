package com.haoge.shijie.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

public class DesUtil {
    private static final String KEY_ALGORITHM = "des";

    private static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";

    public static final String ENCODING = "UTF-8";

    public static void main(String[] args)  {
        String data = "abcdefghd";
        String key = "haogedad";
        //输出加密
       // System.out.println(encrypt(data, key));
        //输出解密
        //System.out.println(decrypt("sas", key));
    }
    /**
     * 还原密钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(des);
        return secretKey;
    }

    /**
     * des加密
     * @param data 加密数据
     * @param key 解密密匙
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key k = toKey(key.getBytes(ENCODING));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        String str = Base64.encodeBase64String(cipher.doFinal(data.getBytes(ENCODING)));
        return str;
    }

    /**
     * des解密
     * @param data 加密数据
     * @param key 解密密匙
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key k = toKey(key.getBytes(ENCODING));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.DECRYPT_MODE, k);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(data));
        return new String(bytes, ENCODING);
    }
}
