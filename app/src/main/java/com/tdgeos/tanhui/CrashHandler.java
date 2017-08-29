package com.tdgeos.tanhui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.tdgeos.lib.MyFile;
import com.tdgeos.yangdi.YangdiMgr;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) return false;

        ex.printStackTrace();

        MyThread thread = new MyThread(ex);
        thread.start();

        return true;
    }

    class MyThread extends Thread {
        private Throwable ex;

        public MyThread(Throwable ex) {
            this.ex = ex;
        }

        @Override
        public void run() {
            Looper.prepare();
            Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
            collectDeviceInfo(mContext);
            String filePath = saveCrashInfo2File(ex);
            uploadFile(filePath);
            Looper.loop();
        }
    }

    public void collectDeviceInfo(Context ctx) {
        infos.put("appname", ctx.getString(R.string.app_name));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long timestamp = System.currentTimeMillis();
        String strtime = sdf.format(new Date(timestamp));
        infos.put("datetime", strtime);

        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                infos.put("version", pi.versionCode + "." + pi.versionName);
            }
        } catch (NameNotFoundException e) {
        }

        infos.put("model", Build.MODEL);
        infos.put("version_release", Build.VERSION.RELEASE);
        infos.put("version_sdk", String.valueOf(Build.VERSION.SDK_INT));
    }

    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String uuid = getUuid();
            String fileName = uuid + "-" + timestamp + ".log";
            String str = Environment.getExternalStorageDirectory().getPath();
            str += "/" + YangdiMgr.WORK_DIR_NAME;
            File dir = new File(str);
            if (!dir.exists()) dir.mkdir();
            str = str + "/log";
            dir = new File(str);
            if (!dir.exists()) dir.mkdir();
            str = str + "/" + fileName;
            FileOutputStream fos = new FileOutputStream(str);
            fos.write(sb.toString().getBytes());
            fos.close();
            return str;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    private String getUuid() {
        final String androidId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        try {
            if (androidId != null && !"9774d56d682e549c".equals(androidId)) {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                if (uuid != null) return uuid.toString();
            } else {
                final String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (deviceId != null) {
                    UUID uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
                    if (uuid != null) return uuid.toString();
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void uploadFile(String srcPath) {
        try {
            if (!isNetworkAvailable()) {
                return;
            }
            String API = "tdgeos.file.upload";
            String SERVER_URL = "http://www.tdgeos.com/webservice/?cmd=log";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("api", API);
            File file = new File(srcPath);
            String s1 = MyFile.GetFileName(srcPath);
            String s2 = s1.replace('-', '_');
            map.put("filename", s2);
            map.put("content", ByteToHex((GetBytesFromFile(file))));
            String params = FormatKeyValue(map);
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.getOutputStream().write(params.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            int code = conn.getResponseCode();
            byte[] bytes;
            if (code == 200) {
                InputStream is = conn.getInputStream();
                bytes = GetBytesFromStream(is);
                is.close();
                String str = new String(bytes);
                JSONObject jsons = new JSONObject(str);
                String jcode = jsons.getJSONObject("packet").getString("code");
                String info = jsons.getJSONObject("packet").getJSONObject("msg").getString("content");
                if (jcode.equals("200")) {
                    //System.out.println("upload succeed.");
                } else {
                    //System.out.println("upload failed : " + info);
                }
            } else {
                //System.out.println("upload failed : unknown code(" + code + ").");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] GetBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    private byte[] GetBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1)
            baos.write(buffer, 0, len);
        is.close();
        baos.close();

        return baos.toByteArray();
    }

    private String ByteToHex(byte[] b) {
        if (b == null)
            return null;
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    private String FormatKeyValue(Map<String, Object> params) {
        if (params == null)
            return null;
        StringBuffer sb = new StringBuffer();
        String key = "", result = "";
        Object value = "";
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append("&");
            }
            result = sb.toString();
            result = result.substring(0, (result.length() - 1));
        }
        return result;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager mgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
