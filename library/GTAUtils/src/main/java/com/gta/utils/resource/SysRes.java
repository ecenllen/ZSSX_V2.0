package com.gta.utils.resource;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 公用静态方法类：
 * 包含系统api使用方法及项目资源获取方法
 */
public class SysRes {

    /**
     */
    public static LayoutInflater getLayoutInflater(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater;

    }

    public static View getViewInflater(Context context, int resId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resId, null);
    }

    public static Resources getResource(Context context) {
        return context.getResources();
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static String[] getResourceStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    public static View getView(Context context, int resId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(resId, parent, false);
    }

    // 返回android闹钟
    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    // 返回通知服务
    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static String formatResString(Context context, int resId, Object... args) {
        return String.format(context.getString(resId), args);
    }

    public static String replaceColorInHtmlString(String mChiDesc, String colorString) {
        for (int i = -1; (i = mChiDesc.indexOf("color:", i + 1)) != -1; ) {
            mChiDesc = mChiDesc.substring(0, i + 6) + colorString + mChiDesc.substring(i + 13);
        }
        return mChiDesc;
    }

    public static float getDpDimen(Context context, int value) {
        return TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据不同的分辨率选择不同的dp值
     *
     * @param context
     * @param mid_value
     * @param xhigh_value
     * @return
     */
    public static float chooseDpBy(Context context, int mid_value, int xhigh_value) {
        return chooseDpBy(context, mid_value, mid_value, mid_value, mid_value, xhigh_value, xhigh_value, xhigh_value);
    }

    public static float chooseDpBy(Context context, int mid_value, int high_value, int xhigh_value, int xxhigh_value) {
        return chooseDpBy(context, mid_value, mid_value, mid_value, high_value, xhigh_value, xxhigh_value, xxhigh_value);
    }

    /**
     * 根据不同的分辨率选择不同的dp值
     *
     * @param context
     * @param xlow_value
     * @param low_value
     * @param mid_value
     * @param high_value
     * @param xhigh_value
     * @param xxhigh_value
     * @param xxxhigh_value
     * @return
     */
    public static float chooseDpBy(Context context, int xlow_value, int low_value, int mid_value, int high_value,
                                   int xhigh_value, int xxhigh_value, int xxxhigh_value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.densityDpi < DisplayMetrics.DENSITY_LOW) {
            return getDpDimen(context, xlow_value);
        }
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return getDpDimen(context, low_value);
            case DisplayMetrics.DENSITY_MEDIUM:
                return getDpDimen(context, mid_value);
            case DisplayMetrics.DENSITY_HIGH:
                return getDpDimen(context, high_value);
            default:
            case DisplayMetrics.DENSITY_XHIGH:
                return getDpDimen(context, xhigh_value);
            case DisplayMetrics.DENSITY_XXHIGH:
                return getDpDimen(context, xxhigh_value);
            case DisplayMetrics.DENSITY_XXXHIGH:
                return getDpDimen(context, xxxhigh_value);
        }
    }

    /**
     * @param ENCRYPT_KEY 加密用的密钥原文
     * @return 加密结果
     */
    public static String decryptText(String encryptedText, String ENCRYPT_KEY) {
        // DECODE encryptedPwd String
        String plainText = null;
        try {
            byte[] encrypedPwdBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            byte[] key = ENCRYPT_KEY.getBytes("UTF8");
            DESKeySpec keySpec = new DESKeySpec(key);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES");// cipher is not thread
            // safe
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] plainBytes = cipher.doFinal(encrypedPwdBytes);
            plainText = new String(plainBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plainText;
    }

    /**
     * @param plainText   解密原文
     * @param ENCRYPT_KEY 解密用的密钥原文
     * @return 解密结果
     */
    @SuppressLint("TrulyRandom")
    public static String encryptText(String plainText, String ENCRYPT_KEY) {

        // String to be encoded with Base64

        String encryptedText = null;
        try {
            // DES算法
            byte[] key = ENCRYPT_KEY.getBytes("UTF8");
            DESKeySpec keySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            // ENCODE plainTextPassword String
            // F16C03A7849D102D,2,2014-10-28-14-10-11
            byte[] plainBytes = plainText.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread
            // safe
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypedBytes = cipher.doFinal(plainBytes);
            // ZN0BsY5mlJLhIoUfPugbI9Q8IbjbxNFh33P/hSgHqHop5+7rUNY8PQ==
            // String encryptedPwd = base64encoder.encode(doFinal);
            encryptedText = Base64.encodeToString(encrypedBytes, Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptedText;
    }

    public static void setVisibleOrGone(View view, boolean ifVisibale) {
        int visibility = ifVisibale ? View.VISIBLE : View.GONE;
        view.setVisibility(visibility);
    }

    /**
     * @param mContext
     * @param mFragmentManager
     * @param fragment           即将要跳转到的fragment
     * @param pageTag            即将要跳转到的fragment的名字
     * @param mainContentframeId
     * @return
     */
    public static int gotoFragment(Context mContext, FragmentManager mFragmentManager, Fragment fragment,
                                   String pageTag, int mainContentframeId) {
        return SysRes.gotoFragment(mContext, mFragmentManager, fragment, pageTag, null, mainContentframeId);
    }

    public static int gotoFragment(Context mContext, FragmentManager mFragmentManager, Fragment fragment,
                                   String pageTag, String CURRENT_PAGE, int mainContentframeId) {
        // CURRENT_PAGE = CURRENT_PAGE==null?"上":CURRENT_PAGE;
        L.e(pageTag, "从%1$s页面跳到\n%2$s页面", CURRENT_PAGE, pageTag);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 设置动画
        // 执行跳转
        transaction.replace(mainContentframeId, fragment, pageTag);
        transaction.addToBackStack(null);
        int fragmentID = transaction.commit();

        return fragmentID;
    }

    public static void showSoftInputOrNot(EditText mobilEditText, boolean show) {
        mobilEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mobilEditText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(mobilEditText, 0);
        } else {
            imm.hideSoftInputFromWindow(mobilEditText.getWindowToken(), 0);
        }
    }

    public static <T> T chooseContentAccordingToLanguageMode(Context context, Locale LanguageMode,
                                                             T simChiObject, T chiObject, T engObject) {
        if (Locale.ENGLISH.equals(LanguageMode)) {
            return engObject;
        } else if (Locale.SIMPLIFIED_CHINESE.equals(LanguageMode)) {
            return simChiObject;
        } else if (Locale.TRADITIONAL_CHINESE.equals(LanguageMode)) {
            return chiObject;
        } else {
            return chiObject;
        }
    }



    /**
     * 发送邮件
     *
     * @param address  邮箱地址;
     * @param subject  邮件主题；
     * @param bodyText 正文 ；
     * @param chosser  存在多个邮箱客户端时，弹出的选择列表标题
     */
    public static void sendEmail(Context context, String address, String subject, String bodyText, String chosser) {
        Intent emailIntent = createEmailIntent(address, subject, bodyText, chosser);
        context.startActivity(emailIntent);
    }

    /**
     * 发送邮件
     *
     * @param address  邮箱地址;
     * @param subject  邮件主题；
     * @param bodyText 正文 ；
     * @param chosser  存在多个邮箱客户端时，弹出的选择列表标题
     */
    public static Intent createEmailIntent(String address, String subject, String bodyText, String chosser) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        String[] reciver = new String[]{address};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, reciver);

        if (null != subject) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        if (null != bodyText) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
        }
        Intent.createChooser(emailIntent, chosser);
        return emailIntent;
    }

	/* 唯一標誌 */

    /**
     * //检查网络连接状况
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private static final boolean IS_GINGERBREAD = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

    /**
     * 获取当前设备唯一标示码 <br>
     * 具体工作原理参见:
     * http://stackoverflow.com/questions/2785485/is-there-a-unique-android
     * -device-id
     *
     * @param mContext
     * @return
     */
    public static String getDeviceUniqueID(Context mContext) {

        String identification;
        if (IS_GINGERBREAD) {
            identification = Build.SERIAL;
        } else {
            identification = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        }

        if (identification != null && !"".equals(identification)) {
            return identification;
        } else {
            return getUniquePsuedoID();
        }
        /*
         * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
		 * available.
		 */
        // TelephonyManager tm = (TelephonyManager) mContext
        // .getSystemService(Context.TELEPHONY_SERVICE);
        // String identification = tm.getDeviceId();
        // if (!"".equals(identification)) {
        // return identification;
        // }
        //
        // identification = getMACAddress("wlan0");
        // if (!"".equals(identification)) {
        // return identification;
        // }
        //
        // identification = getMACAddress("eth0");
        // if (!"".equals(identification)) {
        // return identification;
        // }
        // L.e(mContext, "获取不到唯一标示identification");
    }

    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    private static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a
        // duplicate entry
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to
        // create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static final String MAC_INTERFACE_WLAN = "wlan0";
    public static final String MAC_INTERFACE_ETH = "eth0";

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    @SuppressLint("NewApi")
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*
         * try { // this is so Linux hack return
		 * loadFileAsString("/sys/class/net/" +interfaceName +
		 * "/address").toUpperCase().trim(); } catch (IOException ex) { return
		 * null; }
		 */
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static <T> T getObjByLocale(Locale language, T chiObj, T simChiObj, T engObj) {
        if (language.equals(Locale.TAIWAN) || language.equals(Locale.TRADITIONAL_CHINESE)) {
            return chiObj;
        } else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
            return simChiObj;
        } else if (language.equals(Locale.ENGLISH)) {
            return engObj;
        } else
            return chiObj;
    }

    /**
     * 按照固定的间隔时长、从左向右的顺序依次滚动显示viewpager的item，
     *
     * @param lViewPager           需轮播的viewpager
     * @param lIntervalDelayMillis 轮播的间隔时长
     */
    public static void startSwitchViewPager(final ViewPager lViewPager, final long lIntervalDelayMillis) {
        if (lViewPager.getAdapter().getCount() < 2) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = lViewPager.getCurrentItem();
                currentItem = currentItem ==
                        lViewPager.getAdapter().getCount() - 1 ? 0 : currentItem + 1;
                lViewPager.setCurrentItem(currentItem);
                startSwitchViewPager(lViewPager, lIntervalDelayMillis);
            }
        }, lIntervalDelayMillis);
    }
}
