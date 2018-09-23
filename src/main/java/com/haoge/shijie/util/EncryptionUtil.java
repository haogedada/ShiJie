package com.haoge.shijie.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class EncryptionUtil {

    /**
     * IvParameterSpec参数
     */
    public static final byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
    private final static String DES = "DES";
    private static final String CIPHER_ALGORITHM = "DESEDE/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "desde";
    private static final String ENCODING = "UTF-8";


    public static void main(String[] args) {

    }
    /**
     * 3des加密
     */
    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        try {
            // 添加一个安全提供者
            Security.addProvider(new BouncyCastleProvider());
            // 获得密钥
            Key desKey = keyGenerator(key);
            // 获取密码实例
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            // 初始化密码
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);
            // 执行加密
            byte[] bytes = cipher.doFinal(data.getBytes(ENCODING));
            return Base64.encodeBase64String(bytes);
        } catch (InvalidKeyException e) {
            return "无效KEY"; // 无效KEY
        } catch (NoSuchAlgorithmException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (InvalidKeySpecException e) {
            return "无效KeySpec"; // 无效KeySpec
        } catch (NoSuchPaddingException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (IllegalBlockSizeException e) {
            return "无效字节"; // 无效字节
        } catch (BadPaddingException e) {
            return "解析异常"; // 解析异常
        } catch (UnsupportedEncodingException e) {
            return "编码异常"; // 编码异常
        } catch (InvalidAlgorithmParameterException e) {
            return "摘要参数异常"; // 摘要参数异常
        }

    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) {
        try {
            Key desKey = keyGenerator(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.DECRYPT_MODE, desKey, ips);
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(data));
            return new String(bytes, ENCODING);
        } catch (InvalidKeyException e) {
            return "无效KEY"; // 无效KEY
        } catch (NoSuchAlgorithmException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (InvalidKeySpecException e) {
            return "无效KeySpec"; // 无效KeySpec
        } catch (NoSuchPaddingException e) {
            return "无效算法名称"; // 无效算法名称
        } catch (IllegalBlockSizeException e) {
            return "无效字节"; // 无效字节
        } catch (BadPaddingException e) {
            return "解析异常"; // 解析异常
        } catch (UnsupportedEncodingException e) {
            return "编码异常"; // 编码异常
        } catch (InvalidAlgorithmParameterException e) {
            return "摘要参数异常"; // 摘要参数异常
        }

    }

    /**
     * 生成密钥key对象
     *
     * @param key 密钥字符串
     * @return 密钥对象
     * @throws InvalidKeyException      无效的key
     * @throws NoSuchAlgorithmException 算法名称未发现
     * @throws InvalidKeySpecException  无效的KeySpec
     */
    private static Key keyGenerator(String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] input = hexString2Bytes(key);
        DESedeKeySpec desKey = new DESedeKeySpec(input);
        // 创建一个密钥工厂，然后用它把DESKeySpec转化
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        // 获得一个密钥
        SecretKey secretKey = keyFactory.generateSecret(desKey);
        return secretKey;
    }

    /**
     * 从十六进制字符串到字节数组转化
     *
     * @param key 密钥
     */
    private static byte[] hexString2Bytes(String key) {
        byte[] b = new byte[key.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = key.charAt(j++);
            char c1 = key.charAt(j++);
            // c0做b[i]的高字节，c1做低字节
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /**
     * 将字符转换为int值
     *
     * @param c 要转化的字符
     * @return ASCII码值
     */
    private static int parse(char c) {
        if (c >= 'a') {
            return (c - 'a' + 10) & 0x0f;
        }
        if (c >= 'A') {
            return (c - 'A' + 10) & 0x0f;
        }
        return (c - '0') & 0x0f;
    }


    /**
     * des加密
     */
    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt1(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
        String str = new BASE64Encoder().encode(bt);
        return str;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt1(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes("UTF-8"));
        return new String(bt, "UTF-8");
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    public static String getIsoToUtf_8(String str) {
        if (!StrJudgeUtil.isCorrectStr(str)) {
            return "";
        }
        String newStr = "";
        try {
            newStr = new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }
}
