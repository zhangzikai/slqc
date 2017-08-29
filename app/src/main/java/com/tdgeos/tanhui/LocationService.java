package com.tdgeos.tanhui;

import com.tdgeos.lib.MyPoint;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class LocationService extends Service {
    private LocationManager locationManager = null;
    private MyLocationListener locListener = null;
    private MyPoint ptLocation = null;
    private MyHandler myHandler = null;
    private MyLocCheckThread myLocCheckTask = null;
    private boolean isOpen = false;

    private static final int CHECK_INTERVAL = 5000;

    @Override
    public void onCreate() {
        super.onCreate();
        locListener = new MyLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates("gps", CHECK_INTERVAL, 0, locListener);
            isOpen = true;
        }
        myHandler = new MyHandler();
        myLocCheckTask = new MyLocCheckThread();
        myLocCheckTask.start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        myLocCheckTask.Stop();
        locationManager.removeUpdates(locListener);
        super.onDestroy();
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude();
            double lat = location.getLatitude();
            double alt = location.getAltitude();
            if (ptLocation == null) {
                ptLocation = new MyPoint(lon, lat);
            } else {
                ptLocation.x = (float) lon;
                ptLocation.y = (float) lat;
            }

            int ydh = MyConfig.GetCurYd();
            String trkname = MyConfig.GetCurTrk();
            if (ydh > 0 && !trkname.equals("") && MyConfig.GetOpenTrack()) {
                YangdiMgr.AddTrkPoint(ydh, trkname, lon, lat);
            }

            Bundle data = new Bundle();
            data.putBoolean("type", true);
            data.putDouble("lon", lon);
            data.putDouble("lat", lat);
            data.putDouble("alt", alt);
            Intent intent = new Intent();
            intent.putExtras(data);
            intent.setAction("location_changed");
            sendBroadcast(intent);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                locationManager.requestLocationUpdates("gps", CHECK_INTERVAL, 0, locListener);
                isOpen = true;
            } else {
                locationManager.removeUpdates(locListener);
                isOpen = false;
                ptLocation = null;
            }
        }
    }

    class MyLocCheckThread extends Thread {
        private boolean isRun = true;
        private int iTime = 0;
        private MyPoint ptTemp = null;

        public void run() {
            while (isRun) {
                boolean b = MyConfig.GetOpenLoc();
                if (b && !isOpen && locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    myHandler.sendEmptyMessage(1);
                } else if (!b && isOpen) {
                    myHandler.sendEmptyMessage(0);
                }
                if (iTime > 10) {
                    iTime = 0;
                    ptTemp = null;
                    Bundle data = new Bundle();
                    data.putBoolean("type", false);
                    Intent intent = new Intent();
                    intent.putExtras(data);
                    intent.setAction("location_changed");
                    sendBroadcast(intent);
                }
                if (ptLocation != null) {
                    if (ptTemp == null) {
                        ptTemp = new MyPoint(ptLocation);
                        iTime = 0;
                        continue;
                    } else {
                        if (ptTemp.IsEqual(ptLocation)) {
                            iTime++;
                        } else {
                            ptTemp.x = ptLocation.x;
                            ptTemp.y = ptLocation.y;
                            iTime = 0;
                        }

                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void Stop() {
            isRun = false;
        }
    }
}