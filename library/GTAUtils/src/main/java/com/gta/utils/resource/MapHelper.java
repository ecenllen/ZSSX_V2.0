/**
 * @author Woode Wang E-mail:wangwoode@qq.com
 * @version 创建时间：2015-3-8 下午8:37:53
 */
package com.gta.utils.resource;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @description
 * @since 1.0.0
 */
public class MapHelper {

    /**
     * 轮询不同的定位方式，获取location对象
     *
     * @author sunny
     * @param locationManager
     * @param location
     * @return
     */
    private final static Location getLocationAgain(
            LocationManager locationManager, Location location, Context context) {
        if (location == null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location == null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location == null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }
            }
        }
        return location;
    }

    /**
     * 通过LocationManager获取位置
     *
     * @author sunny
     * @param context
     * @return
     */
    public static final Location getLocation(Context context) {

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // 设置获取条件，以获取最好的效果
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);// 设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);// 高度
        criteria.setBearingRequired(false);// 方向
        criteria.setPowerRequirement(Criteria.POWER_LOW);// 使用省电模式

        // getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = locationManager.getBestProvider(criteria, true);

        Location location = null;
        if (providerName == null) {
            location = getLocationAgain(locationManager, location, context);
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                location = locationManager.getLastKnownLocation(providerName);
            }


            if (location == null) {
                location = getLocationAgain(locationManager, location,context);
            }
        }

        if (location == null) {
            return null;
        } else {
            return location;
        }

    }

    /**
     * 根据坐标获取位置
     *
     * @author sunny
     * @param context
     * @return
     */
    public final static String getAddress(Context context) {
        Location location = getLocation(context);
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.d("获取位置", "位置坐标：" + lat + ", " + lng);
            return MapHelper.getAddress(context, lat, lng);
        }
        return null;

    }

    /**
     * 通过坐标获取位置
     *
     * @author sunny
     * @param context
     * @param lat
     * @param lng
     * @return
     */
    public static String getAddress(Context context, double lat, double lng) {

        Geocoder geocoder = new Geocoder(context, Locale.TRADITIONAL_CHINESE);

        try {
            List<Address> addrs = geocoder.getFromLocation(lat, lng, 5);

            Address addr = addrs.get(0);

            StringBuffer sb = new StringBuffer();
            sb.append(addr.getCountryName()).append(addr.getAdminArea())
                    .append(addr.getLocality()).append(addr.getSubLocality())
                    .append(addr.getThoroughfare())
                    .append(addr.getFeatureName());
            String addrDetail = sb.toString();
            Log.d("获取详细地理位置", addrDetail);

            return addrDetail;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
