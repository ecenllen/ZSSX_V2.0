package com.gta.zssx.pub.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by  on 2016-11-04 10:05
 * @since 1.0.0
 */
public class DES3Utils {
    // 定义加密算法
    private static final String Algorithm = "DESede";
    // 加密密钥
//    private static final String PASSWORD_CRYPT_KEY = "9x6slPpxxu6HEaMyS7yY8d4uH7KlI5MjbZnUJztEDW1Vxa1";

    public static String encrypt3DESMode(String data,String key){
        byte[] respone = Base64.encode(encryptMode(data,key), Base64.DEFAULT);
        return new String(respone);
    }

    public static String decrypt3DESMode(String data,String key){
        byte[] result = decryptMode(data,key);
        return new String(result);
    }

    // 加密 src为源数据的字节数组
    public static byte[] encryptMode(String data,String key) {
        try {// 生成密钥
            byte[] dataByte = data.getBytes("UTF-8");
            SecretKey deskey = new SecretKeySpec(build3Deskey(key), Algorithm);
            // 实例化cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(dataByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密函数
    public static byte[] decryptMode(String src,String key) {
        SecretKey deskey;
        try {
            byte[] dataByte = Base64.decode(src.getBytes("UTF-8"), Base64.DEFAULT);
            deskey = new SecretKeySpec(build3Deskey(key), Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            return cipher.doFinal(dataByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据字符串生成密钥24位的字节数组
    public static byte[] build3Deskey(String keyStr) throws Exception {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
