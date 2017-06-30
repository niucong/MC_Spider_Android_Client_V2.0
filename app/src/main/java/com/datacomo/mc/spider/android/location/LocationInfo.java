package com.datacomo.mc.spider.android.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.datacomo.mc.spider.android.url.L;

/**
 * GSM手机经纬度定位
 */
public class LocationInfo {
    private static final String TAG = "LocationInfo";

    private static LocationInfo instance = null;
    private Context mContext;

    private static LocationManager locationManager;
    private Location location = null;

    private double Latitude;
    private double Longitude;

    /**
     * lon/lat状态
     *
     * 1 以前保留的经纬度 2 GPS当前经纬度 3 无法获得经纬度 4 通过其他方式定位获得经纬度 5
     * 用户自己设置一个位置（用于系统陪聊使用的扩展定义）
     */
    // private String locationResult;

    public synchronized static LocationInfo getInstance(Context ctx) {
        if (instance == null) {
            instance = new LocationInfo(ctx);
        }
        return instance;
    }

    private LocationInfo(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        if (null == locationManager) {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
        // criteria.setAltitudeRequired(true);//要求海拔
        // criteria.setBearingRequired(false);//不要求方位
        // criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
        String provider = locationManager.getBestProvider(criteria, true);
        if (null == provider) {
            L.i(TAG, "init GPS定位");
            provider = LocationManager.GPS_PROVIDER;
        }

        L.i(TAG, "init provider=" + provider);
        // if (LocationManager.GPS_PROVIDER.equals(provider)) {
        // locationResult = "2";
        // } else {
        // locationResult = "4";
        // }

        locationManager.requestLocationUpdates(provider, 1000, 0,
                locationListener);
        location = locationManager.getLastKnownLocation(provider);
        L.i(TAG, "init location=" + location);

        if (location != null) {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            L.i(TAG, "init Latitude=" + Latitude + ",Longitude=" + Longitude);
        } else {
            // locationResult = "3";
        }
    }

    public Location getLocation() {
        return location;
    }

    /**
     * @return 维度
     * @throws Exception
     */
    public Double getLatitude() {
        L.i(TAG, "getLatitude Latitude=" + Latitude);
        return Latitude;
    }

    /**
     * @return 经度
     * @throws Exception
     */
    public Double getLongitude() {
        L.i(TAG, "getLongitude Longitude=" + Longitude);
        return Longitude;
    }

    // public String getLocationResult() {
    // return locationResult;
    // }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            L.i(TAG, "locationListener Provider=" + location.getProvider());
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

    private void updateWithNewLocation(Location location) {
        if (location != null) {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            L.i(TAG, "updateWithNewLocation Latitude=" + Latitude
                    + ",Longitude=" + Longitude);
        } else {
            L.i(TAG, "updateWithNewLocation location is null");
        }
    }
}