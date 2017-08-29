package com.tdgeos.dlg.yddc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.tanhui.MyWifimgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;

public class CombineServerDialog extends Dialog implements OnClickListener {
    private Context context;
    private MyHandler myHandler;
    private MyWifimgr myWifimgr;
    private ServerThread serverThread;
    private boolean isRuning = false;

    private EditText etWifiName = null;
    private EditText etWifiPwd = null;

    private ProgressBar pbProg = null;
    private TextView tvInfo = null;

    private CheckBox cbKpfm = null;
    private CheckBox cbYindiantz = null;
    private CheckBox cbYangditz = null;
    private CheckBox cbYxcl = null;
    private CheckBox cbZjcl = null;
    private CheckBox cbYdyz = null;
    private CheckBox cbKjl = null;
    private CheckBox cbMmjc = null;
    private CheckBox cbYmwzt = null;
    private CheckBox cbSgcl = null;
    private CheckBox cbSlzh = null;
    private CheckBox cbZbdc = null;
    private CheckBox cbXmdc = null;
    private CheckBox cbTrgx = null;
    private CheckBox cbYdbh = null;
    private CheckBox cbWclzld = null;
    private CheckBox cbYdzp = null;
    private CheckBox cbTrack = null;

    private CheckBox cbCover = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private String tmpFile = MyConfig.GetWorkdir() + "/temp/combine.dat";
    private int tmpydh = 0;
    private int ydh = 0;

    public CombineServerDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_datacombine_server);
        setTitle("数据合并");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setCanceledOnTouchOutside(false);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                serverThread.Stop();
                myWifimgr.CloseWifiAp();
            }
        });

        myHandler = new MyHandler();

        etWifiName = (EditText) findViewById(R.id.et_wifiname);
        etWifiPwd = (EditText) findViewById(R.id.et_wifipwd);

        pbProg = (ProgressBar) findViewById(R.id.pb_prog);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        cbKpfm = (CheckBox) findViewById(R.id.cb_kpfm);
        cbYindiantz = (CheckBox) findViewById(R.id.cb_yindiantz);
        cbYangditz = (CheckBox) findViewById(R.id.cb_yangditz);
        cbYxcl = (CheckBox) findViewById(R.id.cb_yxcl);
        cbZjcl = (CheckBox) findViewById(R.id.cb_zjcl);
        cbYdyz = (CheckBox) findViewById(R.id.cb_ydyz);
        cbKjl = (CheckBox) findViewById(R.id.cb_kjl);
        cbMmjc = (CheckBox) findViewById(R.id.cb_mmjc);
        cbYmwzt = (CheckBox) findViewById(R.id.cb_ymwzt);
        cbSgcl = (CheckBox) findViewById(R.id.cb_sgcl);
        cbSlzh = (CheckBox) findViewById(R.id.cb_slzh);
        cbZbdc = (CheckBox) findViewById(R.id.cb_zbdc);
        cbXmdc = (CheckBox) findViewById(R.id.cb_xmdc);
        cbTrgx = (CheckBox) findViewById(R.id.cb_trgx);
        cbYdbh = (CheckBox) findViewById(R.id.cb_ydbh);
        cbWclzld = (CheckBox) findViewById(R.id.cb_wclzld);
        cbYdzp = (CheckBox) findViewById(R.id.cb_zp);
        cbTrack = (CheckBox) findViewById(R.id.cb_track);

        cbCover = (CheckBox) findViewById(R.id.cb_cover);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setEnabled(false);

        pbProg.setVisibility(1);
        tvInfo.setText("正在创建WIFI热点...");
        myWifimgr = new MyWifimgr(context, myHandler);
        myWifimgr.CreateWifiAp("wifi_slqc", "12341234");

        serverThread = new ServerThread();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                if (isRuning) {
                    Toast.makeText(context, "正在合并。", 1).show();
                    break;
                }
                if (!MyFile.Exists(tmpFile)) {
                    Toast.makeText(context, "没有找到接收的数据文件。", 1).show();
                    break;
                }
                if (Setmgr.GetTask(tmpydh) == null) {
                    Toast.makeText(context, "数据对应的样地号不在任务列表中。", 1).show();
                    break;
                }
                if (ydh != tmpydh) {
                    Toast.makeText(context, "数据对应的样地号与当前样地号不一致。", 1).show();
                    break;
                }
                boolean b = false;
                if (cbKpfm.isChecked()) b = true;
                if (cbYindiantz.isChecked()) b = true;
                if (cbYangditz.isChecked()) b = true;
                if (cbYxcl.isChecked()) b = true;
                if (cbZjcl.isChecked()) b = true;
                if (cbYdyz.isChecked()) b = true;
                if (cbKjl.isChecked()) b = true;
                if (cbMmjc.isChecked()) b = true;
                if (cbYmwzt.isChecked()) b = true;
                if (cbSgcl.isChecked()) b = true;
                if (cbSlzh.isChecked()) b = true;
                if (cbZbdc.isChecked()) b = true;
                if (cbXmdc.isChecked()) b = true;
                if (cbTrgx.isChecked()) b = true;
                if (cbYdbh.isChecked()) b = true;
                if (cbWclzld.isChecked()) b = true;
                if (cbYdzp.isChecked()) b = true;
                if (cbTrack.isChecked()) b = true;
                if (!b) {
                    Toast.makeText(context, "必须至少选择一个合并项。", 1).show();
                    break;
                }

                CombineTask task = new CombineTask();
                task.execute("");
                btnOk.setEnabled(false);
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == MyWifimgr.MSG_AP_SUCCESS) {
                String ip = getLocalIpAddress();

                etWifiName.setText("wifi_slqc");
                etWifiPwd.setText("12341234");
                pbProg.setVisibility(4);
                tvInfo.setText("就绪.");

                serverThread.start();
            }
            if (msg.what == MyWifimgr.MSG_AP_FAILED) {
                pbProg.setVisibility(4);
                tvInfo.setText("创建热点失败.");
            }
            if (msg.what == 101) {
                int i = msg.getData().getInt("prog");
                if (i == 1002) {
                    pbProg.setVisibility(4);
                    tvInfo.setText("发送端已经连接，等待接收数据..。");
                }
                if (i == 1003) {
                    pbProg.setVisibility(4);
                    tvInfo.setText("数据接收完成，准备合并数据.");
                    btnOk.setEnabled(true);
                }
                if (i >= 0 && i <= 100) {
                    pbProg.setVisibility(1);
                    tvInfo.setText("正在接收数据：" + i + "%");
                }
                if (i == -1) {
                    pbProg.setVisibility(4);
                    tvInfo.setText("接收数据失败，网络异常。");
                }
                if (i == -2) {
                    pbProg.setVisibility(4);
                    tvInfo.setText("任务被取消。");
                }
            }
        }
    }

    class CombineTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            pbProg.setVisibility(1);
            tvInfo.setText("开始合并数据...");
            isRuning = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean isCover = cbCover.isChecked();
            if (cbKpfm.isChecked()) {
                this.publishProgress(1);
                YangdiMgr.CombineKpfm(tmpFile, isCover, ydh);
            }
            if (cbYindiantz.isChecked()) {
                this.publishProgress(2);
                YangdiMgr.CombineYindiantz(tmpFile, isCover, ydh);
            }
            if (cbYangditz.isChecked()) {
                this.publishProgress(3);
                YangdiMgr.CombineYangditz(tmpFile, isCover, ydh);
            }
            if (cbYxcl.isChecked()) {
                this.publishProgress(4);
                YangdiMgr.CombineYxcl(tmpFile, isCover, ydh);
            }
            if (cbZjcl.isChecked()) {
                this.publishProgress(5);
                YangdiMgr.CombineZjcl(tmpFile, isCover, ydh);
            }
            if (cbYdyz.isChecked()) {
                this.publishProgress(6);
                YangdiMgr.CombineYdyz(tmpFile, isCover, ydh);
            }
            if (cbKjl.isChecked()) {
                this.publishProgress(7);
                YangdiMgr.CombineKjl(tmpFile, isCover, ydh);
            }
            if (cbMmjc.isChecked()) {
                this.publishProgress(8);
                YangdiMgr.CombineMmjc(tmpFile, isCover, ydh);
            }
            if (cbYmwzt.isChecked()) {
                this.publishProgress(9);
                YangdiMgr.CombineYmwzt(tmpFile, isCover, ydh);
            }
            if (cbSgcl.isChecked()) {
                this.publishProgress(10);
                YangdiMgr.CombineSgcl(tmpFile, isCover, ydh);
            }
            if (cbSlzh.isChecked()) {
                this.publishProgress(11);
                YangdiMgr.CombineSlzh(tmpFile, isCover, ydh);
            }
            if (cbZbdc.isChecked()) {
                this.publishProgress(12);
                YangdiMgr.CombineZbdc(tmpFile, isCover, ydh);
            }
            if (cbXmdc.isChecked()) {
                this.publishProgress(13);
                YangdiMgr.CombineXmdc(tmpFile, isCover, ydh);
            }
            if (cbTrgx.isChecked()) {
                this.publishProgress(14);
                YangdiMgr.CombineTrgx(tmpFile, isCover, ydh);
            }
            if (cbYdbh.isChecked()) {
                this.publishProgress(15);
                YangdiMgr.CombineYdbh(tmpFile, isCover, ydh);
            }
            if (cbWclzld.isChecked()) {
                this.publishProgress(16);
                YangdiMgr.CombineWclzld(tmpFile, isCover, ydh);
            }
            if (cbYdzp.isChecked()) {
                this.publishProgress(17);
                YangdiMgr.CombineYdzp(tmpFile, isCover, ydh);
            }
            if (cbTrack.isChecked()) {
                this.publishProgress(19);
                YangdiMgr.CombineTrack(tmpFile, isCover, ydh);
            }
            this.publishProgress(20);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            switch (i) {
                case 1: {
                    tvInfo.setText("正在合并卡片封面数据...");
                    break;
                }
                case 2: {
                    tvInfo.setText("正在合并引点特征数据...");
                    break;
                }
                case 3: {
                    tvInfo.setText("正在合并样地特征数据...");
                    break;
                }
                case 4: {
                    tvInfo.setText("正在合并引线测量数据...");
                    break;
                }
                case 5: {
                    tvInfo.setText("正在合并周界测量数据...");
                    break;
                }
                case 6: {
                    tvInfo.setText("正在合并样地因子数据...");
                    break;
                }
                case 7: {
                    tvInfo.setText("正在合并跨角林数据...");
                    break;
                }
                case 8: {
                    tvInfo.setText("正在合并每木检尺数据...");
                    break;
                }
                case 9: {
                    tvInfo.setText("正在合并样木位置图数据...");
                    break;
                }
                case 10: {
                    tvInfo.setText("正在合并树高测量数据...");
                    break;
                }
                case 11: {
                    tvInfo.setText("正在合并森林灾害数据...");
                    break;
                }
                case 12: {
                    tvInfo.setText("正在合并植被调查数据...");
                    break;
                }
                case 13: {
                    tvInfo.setText("正在合并下木调查数据...");
                    break;
                }
                case 14: {
                    tvInfo.setText("正在合并天然更新数据...");
                    break;
                }
                case 15: {
                    tvInfo.setText("正在合并样地变化数据...");
                    break;
                }
                case 16: {
                    tvInfo.setText("正在合并未成林造林地数据...");
                    break;
                }
                case 20: {
                    pbProg.setVisibility(4);
                    tvInfo.setText("合并完成.");
                    break;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isRuning = false;
            btnOk.setEnabled(true);
            pbProg.setVisibility(4);
        }
    }

    class ServerThread extends Thread {
        private boolean isStop = false;
        private ServerSocket server = null;
        private int port = 9901;
        private int timeout = 30000;
        private int size = 2048;

        public void Stop() {
            isStop = true;
            try {
                if (server != null) server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                server = new ServerSocket(port, 5);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try {
                while (!isStop) {
                    if (isRuning) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    Socket socket = server.accept();
                    socket.setSoTimeout(timeout);
                    Message msg = new Message();
                    msg.what = 101;
                    Bundle data = new Bundle();
                    data.putInt("prog", 1002);
                    msg.setData(data);
                    myHandler.sendMessage(msg);

                    MyFile.DeleteFile(tmpFile);

                    OutputStream out = null;
                    InputStream in = null;
                    FileOutputStream fos = null;
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
                    fos = new FileOutputStream(tmpFile);

                    int fileLen = 0;
                    byte[] buf4 = new byte[4];
                    byte[] buffer = new byte[size];
                    in.read(buf4);
                    tmpydh = MyFuns.byteToLittleInt(buf4);
                    in.read(buf4);
                    fileLen = MyFuns.byteToLittleInt(buf4);
                    int r = 0;
                    int len = 0;
                    int k = 0;
                    while ((r = in.read(buffer)) > 0) {
                        if (isStop) {
                            msg = new Message();
                            msg.what = 101;
                            data = new Bundle();
                            data.putInt("prog", -2);
                            msg.setData(data);
                            myHandler.sendMessage(msg);
                            if (fos != null) fos.close();
                            if (in != null) in.close();
                            if (out != null) out.close();
                            if (socket != null) socket.close();
                            return;
                        }
                        fos.write(buffer, 0, r);
                        fos.flush();
                        len += r;
                        k = len * 100 / fileLen;
                        msg = new Message();
                        msg.what = 101;
                        data = new Bundle();
                        data.putInt("prog", k);
                        msg.setData(data);
                        myHandler.sendMessage(msg);
                    }
                    msg = new Message();
                    msg.what = 101;
                    data = new Bundle();
                    data.putInt("prog", 1003);
                    msg.setData(data);
                    myHandler.sendMessage(msg);
                    if (fos != null) fos.close();
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = 101;
                Bundle data = new Bundle();
                data.putInt("prog", -1);
                msg.setData(data);
                myHandler.sendMessage(msg);
            }
        }
    }
}
