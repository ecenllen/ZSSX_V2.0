package com.gta.utils.helper;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by Administrator on 2015/10/8.
 */
public class Helper_MD5 {
    /**
     * Desc: MD5 encrypting/MD5 加密
     *
     * @param password
     * @return
     */
    public static String encryptToMD5(String password) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d("encryptToMD5", "something wrong in encrypting to MD5");
            return "";
        }

        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hashvalue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hashvalue.append("0");
            }
            hashvalue.append(Integer.toHexString(val));
        }
        return hashvalue.toString();
    }


    /**
     * 大寫密文
     *
     * @param plainText 明文
     * @return 32位大寫密文
     */
    public static String encryptingByMD5For32BitUpper(String plainText) {
        if (TextUtils.isEmpty(plainText))
            return "";
        return encryptingByMD5For32Bit(plainText).toUpperCase(Locale.US);
    }

    /**
     * MD5 加密
     *
     * @param plainText 明文
     * @return 32位密文
     */
    private static String encryptingByMD5For32Bit(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return re_md5;
    }

    //md5加密
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
