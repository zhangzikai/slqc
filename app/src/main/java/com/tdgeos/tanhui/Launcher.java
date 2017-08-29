package com.tdgeos.tanhui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONObject;

import com.tdgeos.dlg.base.MyFileOpenDialog;
import com.tdgeos.dlg.base.MyFileSaveDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFolder;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Launcher extends Activity {

    private LinearLayout lay = null;
    private Button btnOk = null;
    private Button btnReset = null;
    private EditText etCdk = null;
    private EditText etId = null;
    private String id = null;
    private Button btnCopy = null;
    private Button btnExport = null;
    private Button btnPaste = null;
    private Button btnImport = null;

    private ProgressBar pbProg = null;
    private TextView tvProg = null;

    private String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();
    private String APP_DIR = SDCARD_DIR + "/" + YangdiMgr.APP_DIR_NAME;
    private String WORK_DIR = SDCARD_DIR + "/" + YangdiMgr.WORK_DIR_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launcher);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int SCREEN_WIDTH = dm.widthPixels;
        int SCREEN_HEIGHT = dm.heightPixels;
        int DENSITY_DPI = dm.densityDpi;
        System.out.println("SCREEN_WIDTH = " + SCREEN_WIDTH);
        System.out.println("SCREEN_HEIGHT = " + SCREEN_HEIGHT);
        System.out.println("DENSITY_DPI = " + DENSITY_DPI);

        MyConfig.SetScreenSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        MyConfig.SetDpi(DENSITY_DPI);

        id = getId();
        MyConfig.SetDeviceId(id);

        pbProg = (ProgressBar) findViewById(R.id.pb_prog);
        tvProg = (TextView) findViewById(R.id.tv_prog);

        lay = (LinearLayout) findViewById(R.id.lay_view);
        lay.setVisibility(4);
        etCdk = (EditText) findViewById(R.id.etCDK);
        etId = (EditText) findViewById(R.id.etId);
        etId.setInputType(InputType.TYPE_NULL);
        etId.setText(id);
        btnOk = (Button) findViewById(R.id.btnActiveOk);
        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String cdk = etCdk.getText().toString().trim();
                if (cdk.equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入激活码!", 1).show();
                    return;
                }
                String str = getMD5(id);
                if (str == null) {
                    Toast.makeText(getApplicationContext(), "加密错误!", 1).show();
                    return;
                } else {
                    if (str.equals(cdk)) {
                        try {
                            FileOutputStream fis = new FileOutputStream(APP_DIR + "/slqc.lic");
                            fis.write(str.getBytes());
                            fis.flush();
                            fis.close();
                        } catch (Exception e) {
                        }

                        Launcher.this.finish();
                        Intent intent = new Intent();
                        intent.setClass(Launcher.this, Main.class);
                        Launcher.this.startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "激活码无效!", 1).show();
                        return;
                    }
                }
            }
        });

        btnReset = (Button) findViewById(R.id.btnActiveReset);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.this.finish();
            }
        });

        btnCopy = (Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etId.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(Launcher.this, "无效的设备号！", 1).show();
                } else {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", text);
                    cmb.setPrimaryClip(clip);
                    Toast.makeText(Launcher.this, "已复制到剪切板！", 1).show();
                }
            }
        });
        btnExport = (Button) findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etId.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(Launcher.this, "无效的设备号！", 1).show();
                } else {
                    MyFileSaveDialog dlg = new MyFileSaveDialog(Launcher.this, SDCARD_DIR, "设备号.txt");
                    String txt = dlg.showDialog();
                    if (txt != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(txt);
                            fos.write(text.getBytes());
                            fos.close();
                            Toast.makeText(Launcher.this, "导出完成！", 1).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btnPaste = (Button) findViewById(R.id.btnPaste);
        btnPaste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (!cmb.hasPrimaryClip()) {
                    Toast.makeText(Launcher.this, "剪切板中无内容！", 1).show();
                    return;
                }
                if (cmb.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    ClipData cdText = cmb.getPrimaryClip();
                    Item item = cdText.getItemAt(0);
                    if (item.getText() == null) {
                        Toast.makeText(getApplicationContext(), "剪贴板中无内容", 1).show();
                        return;
                    } else {
                        etCdk.setText(item.getText());
                    }
                } else {
                    Toast.makeText(Launcher.this, "剪切板中无文本内容！", 1).show();
                    return;
                }
            }
        });
        btnImport = (Button) findViewById(R.id.btnImport);
        btnImport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etId.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(Launcher.this, "无效的设备号！", 1).show();
                } else {
                    MyFileOpenDialog dlg = new MyFileOpenDialog(Launcher.this, SDCARD_DIR, new String[]{"lic"});
                    String txt = dlg.showDialog();
                    if (txt != null) {
                        try {
                            String dst = APP_DIR + "/slqc.lic";
                            MyFile.DeleteFile(dst);
                            MyFile.CopyFile(txt, dst);
                            if (checkCdk(id, APP_DIR + "/slqc.lic")) {
                                Launcher.this.finish();
                                Intent intent = new Intent();
                                intent.setClass(Launcher.this, Main.class);
                                Launcher.this.startActivity(intent);
                            } else {
                                Toast.makeText(Launcher.this, "无效的许可文件！", 1).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(Launcher.this, "导入许可文件出错！", 1).show();
                        }
                    }
                }
            }
        });

        MyThread myThread = new MyThread();
        myThread.start();

        InitTask initTask = new InitTask();
        initTask.execute("");
        
        /*
        final Handler handler = new Handler(){
    		public void handleMessage(Message msg)
    		{
    			if(msg.what == 1) 
    			{
    				lay.setVisibility(1);
    				Launcher.this.finish();
        			Intent intent = new Intent();
        			intent.setClass(Launcher.this, Main.class);
        			Launcher.this.startActivity(intent);
    			}
    			if(msg.what == 0)
    			{
    				Launcher.this.finish();
        			Intent intent = new Intent();
        			intent.setClass(Launcher.this, Main.class);
        			Launcher.this.startActivity(intent);
    			}
    		}
    	};
        
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
        	@Override
        	public void run() {
        		if(checkCdk(id, APP_DIR + "/slqc.lic"))
                {
        			handler.sendEmptyMessage(0);
                }
                else
                {
                	handler.sendEmptyMessage(1);
                }
        	}
        };
        timer.schedule(task, 2000);
        */
    }

    private String getId() {
        String deviceId = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            return deviceId;
        }

        String androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        if (androidId != null && !androidId.equalsIgnoreCase("9774d56d682e549c")) {
            return androidId;
        }
        return null;
    }

    private String getMD5(String s) {
        try {
            char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', '5'};
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte b[] = digest.digest();
            StringBuilder sb = new StringBuilder(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
                sb.append(HEX_DIGITS[b[i] & 0x0f]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean checkCdk(String id, String lic) {
        String md = getMD5(id);
        if (md == null || md.equals("")) return false;

        if (MyFile.Exists(lic)) {
            try {
                FileInputStream fis = new FileInputStream(lic);
                boolean b = false;
                byte[] buf32 = new byte[32];
                int r = fis.read(buf32);
                while (r == 32) {
                    r = -1;
                    String str = new String(buf32);
                    if (str.equals(md)) {
                        b = true;
                        break;
                    }
                    r = fis.read(buf32);
                }
                fis.close();
                if (b) return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void uploadLog() throws IOException {
        String dir = Environment.getExternalStorageDirectory().getPath();
        dir += "/" + YangdiMgr.WORK_DIR_NAME + "/log";

        long time0 = 0;
        String s0 = dir + "/timestamp.dat";
        if (MyFile.Exists(s0)) {
            byte[] buf = new byte[13];
            FileInputStream fis = new FileInputStream(s0);
            fis.read(buf);
            fis.close();
            String s1 = new String(buf);
            time0 = Long.parseLong(s1);
        }
        MyFolder myFolder = MyFile.getCurFile(dir, new String[]{"log"});
        if (myFolder != null && myFolder.files != null && myFolder.files.size() > 0) {
            for (int i = 0; i < myFolder.files.size(); i++) {
                String name = myFolder.files.get(i);
                String str = MyFile.GetFileNameNoEx(name);
                String[] ss = MyFuns.Split(str, '-');
                if (ss.length == 6) {
                    String str1 = ss[5];
                    long time = Long.parseLong(str1);
                    if (time > time0) {
                        String str2 = dir + "/" + name;
                        uploadFile(str2);
                    }
                }
            }
            long timestamp = System.currentTimeMillis();
            byte[] buf = String.valueOf(timestamp).getBytes();
            FileOutputStream fos = new FileOutputStream(s0);
            fos.write(buf);
            fos.close();
        }
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
        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    class MyThread extends Thread {
        @Override
        public void run() {
            if (isNetworkAvailable()) {
                try {
                    uploadLog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class InitTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            pbProg.setVisibility(1);
            tvProg.setVisibility(1);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            File fDir = new File(APP_DIR);
            if (!fDir.exists()) fDir.mkdir();
            MyConfig.SetAppdir(APP_DIR);

            AssetManager mgr = Launcher.this.getAssets();
            try {
                String str = APP_DIR + "/res.dat";
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                InputStream fis = mgr.open("res.dat");
                MyFile.CopyFile(fis, str);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            try{
				String str = APP_DIR + "/qianqi.dat";
				File file = new File(str);
				if(file.exists())
				{
					file.delete();
				}
				InputStream fis = mgr.open("qianqi.dat");
				MyFile.CopyFile(fis, str);
				fis.close();
			}catch(IOException e){
				e.printStackTrace();
			}
	    	*/
            try {
                String str = APP_DIR + "/set.dat";
                File file = new File(str);
                if (!file.exists()) {
                    InputStream fis = mgr.open("set.dat");
                    MyFile.CopyFile(fis, str);
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String str = APP_DIR + "/yangdi" + YangdiMgr.DATA_EXNAME;
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                InputStream fis = mgr.open("yangdi.dat");
                MyFile.CopyFile(fis, str);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String str = APP_DIR + "/trk" + YangdiMgr.DATA_EXNAME;
                File file = new File(str);
                if (!file.exists()) {
                    InputStream fis = mgr.open("trk.dat");
                    MyFile.CopyFile(fis, str);
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String str = APP_DIR + "/yangdi.xls";
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                InputStream fis = mgr.open("yangdi.xls");
                MyFile.CopyFile(fis, str);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String str = APP_DIR + "/yangdi.trk";
                File file = new File(str);
                if (file.exists()) {
                    file.delete();
                }
                InputStream fis = mgr.open("yangdi.trk");
                MyFile.CopyFile(fis, str);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MyFile.MakeDir(SDCARD_DIR + "/BaiduMapSdk/vmp/h");
            try {
                String[] fs = mgr.list("baidumap");
                if (fs != null) {
                    for (int i = 0; i < fs.length; i++) {
                        String str = SDCARD_DIR + "/BaiduMapSdk/vmp/h/" + fs[i];
                        File file = new File(str);
                        if (!file.exists()) {
                            InputStream fis = mgr.open("baidumap/" + fs[i]);
                            MyFile.CopyFile(fis, str);
                            fis.close();
                        }
                        str = SDCARD_DIR + "/BaiduMapSdk/vmp/l/" + fs[i];
                        file = new File(str);
                        if (!file.exists()) {
                            InputStream fis = mgr.open("baidumap/" + fs[i]);
                            MyFile.CopyFile(fis, str);
                            fis.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            fDir = new File(WORK_DIR);
            if (!fDir.exists()) fDir.mkdir();
            MyConfig.SetWorkdir(WORK_DIR);

            fDir = new File(WORK_DIR + "/log");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/yangdi");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/doc");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/track");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/tuku");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/recv");
            if (!fDir.exists()) fDir.mkdir();

            fDir = new File(WORK_DIR + "/temp");
            if (!fDir.exists()) fDir.mkdir();

            try {
                String str = WORK_DIR + "/doc/xize.pdf";
                File file = new File(str);
                if (!file.exists()) {
                    InputStream fis = mgr.open("xize.pdf");
                    MyFile.CopyFile(fis, str);
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String str = WORK_DIR + "/doc/shouce.pdf";
                File file = new File(str);
                if (!file.exists()) {
                    InputStream fis = mgr.open("shouce.pdf");
                    MyFile.CopyFile(fis, str);
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String[] fs = mgr.list("tuku");
                if (fs != null) {
                    for (int i = 0; i < fs.length; i++) {
                        String str = WORK_DIR + "/tuku/" + fs[i];
                        File file = new File(str);
                        if (!file.exists()) {
                            InputStream fis = mgr.open("tuku/" + fs[i]);
                            MyFile.CopyFile(fis, str);
                            fis.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String CUR_DIR = MyConfig.GetCurdir();
            if (CUR_DIR == null) {
                MyConfig.SetCurdir(SDCARD_DIR);
            } else if (CUR_DIR.equals("")) {
                MyConfig.SetCurdir(SDCARD_DIR);
            }
            if (!MyFile.IsDirectory(CUR_DIR)) {
                MyConfig.SetCurdir(SDCARD_DIR);
            }

            String str = WORK_DIR + "_bak";
            fDir = new File(str);
            if (!fDir.exists()) fDir.mkdir();
            String BAK_DIR = MyConfig.GetBackupdir();
            if (BAK_DIR == null) {
                MyConfig.SetBackupdir(str);
            } else if (BAK_DIR.equals("")) {
                MyConfig.SetBackupdir(str);
            }
            if (!MyFile.IsDirectory(BAK_DIR)) {
                MyConfig.SetBackupdir(str);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pbProg.setVisibility(4);
            tvProg.setVisibility(4);
            if (checkCdk(id, APP_DIR + "/slqc.lic")) {
                Launcher.this.finish();
                Intent intent = new Intent();
                intent.setClass(Launcher.this, Main.class);
                Launcher.this.startActivity(intent);
            } else {
                lay.setVisibility(1);
                Launcher.this.finish();
                Intent intent = new Intent();
                intent.setClass(Launcher.this, Main.class);
                Launcher.this.startActivity(intent);
            }
        }
    }
}