package com.tdgeos.tanhui;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class MyWifimgr {
    private WifiManager wifiManager;
    private Context context;
    private Handler handler;

    public static final int WIFI_CONNECTED = 0x01;
    public static final int WIFI_NOCONNECT = 0x02;
    public static final int WIFI_CONNECTING = 0x03;

    public static final int WIFICIPHER_WEP = 0x04;
    public static final int WIFICIPHER_WPA = 0x05;
    public static final int WIFICIPHER_NOPASS = 0x06;
    public static final int WIFICIPHER_INVALID = 0x07;

    public static final int MSG_AP_SUCCESS = 1001;
    public static final int MSG_AP_FAILED = 1002;

    public static final int MSG_CONN_SUCCESS = 2001;
    public static final int MSG_CONN_FAILED = 2002;

    public MyWifimgr(Context context, Handler handler) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.context = context;
        this.handler = handler;
    }

    public boolean OpenWifi() {
        boolean b = true;
        if (!wifiManager.isWifiEnabled()) {
            b = wifiManager.setWifiEnabled(true);
        }
        return b;
    }

    public void CloseWifi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    private int GetWifiContectStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetworkInfo.getDetailedState() == DetailedState.OBTAINING_IPADDR || wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTING) {
            return WIFI_CONNECTING;
        } else if (wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {
            return WIFI_CONNECTED;
        } else {
            return WIFI_NOCONNECT;
        }
    }

    private String GetSsid() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return (wifiInfo == null) ? null : wifiInfo.getSSID();
    }

    public boolean Connect(String SSID, String Password, int type) {
        if (!this.OpenWifi()) {
            return false;
        }

        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = CreateWifiInfo(SSID, Password, type);
        if (wifiConfig == null) {
            return false;
        }

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

//		自定义网络设置，要使其生效需调用wifiManager.updateNetwork(wifiConfig);
//       try {
//       	//高级选项
//       	String ip  ="192.168.1.201";
//       	int networkPrefixLength =24;
//       	InetAddress intetAddress  = InetAddress.getByName(ip);
//       	int intIp = inetAddressToInt(intetAddress);  
//       	String dns = (intIp & 0xFF ) + "." + ((intIp >> 8 ) & 0xFF) + "." + ((intIp >> 16 ) & 0xFF) + ".1";
//       	setIpAssignment("STATIC", wifiConfig); //"STATIC" or "DHCP" for dynamic setting
//       	setIpAddress(intetAddress, networkPrefixLength, wifiConfig);
//       	setGateway(InetAddress.getByName(dns), wifiConfig);
//       	setDNS(InetAddress.getByName(dns), wifiConfig);
//       } catch (Exception e) {
//       	e.printStackTrace();
//       }

        int netID = wifiManager.addNetwork(wifiConfig);
        boolean bRet = wifiManager.enableNetwork(netID, true);
//   	wifiManager.updateNetwork(wifiConfig);

        MyWifiConnListener thread = new MyWifiConnListener();
        thread.start();

        return bRet;
    }

    //查看以前是否也配置过这个网络
    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs == null) return null;
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals(SSID)) {
                return existingConfig;
            }
        }
        return null;
    }

    public boolean IsWifiConnTo(String ssid) {
        int r = GetWifiContectStatus();
        if (r == WIFI_CONNECTED) {
            String str = GetSsid();
            if (str != null && (str.equals("\"" + ssid + "\"") || str.equals(ssid))) {
                return true;
            }
        }
        return false;
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = SSID;
        if (type == WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == WIFICIPHER_WEP) {
            config.preSharedKey = Password;
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == WIFICIPHER_WPA) {
            config.preSharedKey = Password;
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            return null;
        }
        return config;
    }

    public void CreateWifiAp(String ssid, String pwd) {
        CloseWifi();

        Method method1 = null;
        try {
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = ssid;
            netConfig.preSharedKey = pwd;

            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            method1.invoke(wifiManager, netConfig, true);

            MyWifiApListener thread = new MyWifiApListener();
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CloseWifiAp() {
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class MyWifiApListener extends Thread {
        private int timeout = 15;

        public void run() {
            int t = 0;
            while (t < timeout) {
                if (isWifiApEnabled()) {
                    handler.sendEmptyMessage(MSG_AP_SUCCESS);
                    return;
                }
                t++;
                try {
                    Thread.currentThread();
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
            handler.sendEmptyMessage(MSG_AP_FAILED);
        }
    }

    private class MyWifiConnListener extends Thread {
        private int timeout = 30;

        public void run() {
            int t = 0;
            while (t < timeout) {
                if (IsWifiConnTo("wifi_slqc")) {
                    handler.sendEmptyMessage(MSG_CONN_SUCCESS);
                    return;
                }
                t++;
                try {
                    Thread.currentThread();
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
            handler.sendEmptyMessage(MSG_CONN_FAILED);
        }
    }
}