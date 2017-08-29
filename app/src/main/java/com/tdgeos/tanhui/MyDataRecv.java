package com.tdgeos.tanhui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;

import com.tdgeos.dlg.base.MyDirCheckedDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.MyWifimgr;
import com.tdgeos.tanhui.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyDataRecv extends Activity implements View.OnClickListener {
    private MyHandler myHandler;
    private MyWifimgr myWifimgr;

    private EditText etWifiName = null;
    private EditText etWifiPwd = null;
    private EditText etDir = null;
    private Button btnDir = null;

    private ProgressBar pbProg = null;
    private TextView tvInfo = null;
    private boolean isRunning = false;

    private ReceiveTask serverTask = null;
    private BroadTask broadTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        myHandler = new MyHandler();

        etWifiName = (EditText) findViewById(R.id.et_wifiname);
        etWifiPwd = (EditText) findViewById(R.id.et_wifipwd);

        etDir = (EditText) findViewById(R.id.et_dir);
        String dir = MyConfig.GetWorkdir() + "/recv";
        etDir.setText(dir);

        pbProg = (ProgressBar) findViewById(R.id.pb_prog);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        btnDir = (Button) findViewById(R.id.btn_dir);
        btnDir.setOnClickListener(this);

        myWifimgr = new MyWifimgr(this, myHandler);
        myWifimgr.CreateWifiAp("wifi_slqc", "12341234");

        serverTask = new ReceiveTask();
        broadTask = new BroadTask();

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serverTask.Stop();
        broadTask.Stop();
        myWifimgr.CloseWifiAp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dir: {
                MyDirCheckedDialog dlg = new MyDirCheckedDialog(this, MyConfig.GetCurdir());
                String dir = dlg.showDialog();
                if (dir != null) {
                    MyConfig.SetCurdir(dir);
                    etDir.setText(dir);
                }
                break;
            }
        }
    }

    class BroadTask extends AsyncTask<String, Integer, Boolean> {
        private static final String BROADCAST_IP = "230.0.0.1";//广播IP
        private static final int BROADCAST_INT_PORT = 40005;

        MulticastLock multicastLock;
        MulticastSocket broadSocket;
        InetAddress broadAddress;
        DatagramSocket sender;

        public void Stop() {
            isRunning = false;
        }

        @Override
        protected void onPreExecute() {
            System.out.println("1");
            isRunning = true;
            try {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                multicastLock = wifiManager.createMulticastLock("multicast.test");
                multicastLock.acquire();

                broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
                broadAddress = InetAddress.getByName(BROADCAST_IP);
                broadSocket.joinGroup(broadAddress);
                sender = new DatagramSocket();
                System.out.println("2");
            } catch (Exception e) {
                e.printStackTrace();//java.net.SocketException: setsockopt failed: ENODEV (No such device)

            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                while (isRunning) {
                    InetAddress addr = InetAddress.getLocalHost();
                    String ip = addr.getHostAddress().toString();
                    String pc = addr.getHostName().toString();
                    String info = pc + "@" + ip;
                    //System.out.println(info);
                    byte[] bts = info.getBytes();
                    DatagramPacket packet = new DatagramPacket(bts, bts.length, broadAddress, BROADCAST_INT_PORT);
                    sender.send(packet);

                    Thread.sleep(1000);
                }
                broadSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            multicastLock.release();
            System.out.println("3");
        }
    }

    class ReceiveTask extends AsyncTask<String, Integer, Boolean> {
        private ServerSocket server = null;
        private int port = 9901;
        private int timeout = 30000;
        private int size = 2048;

        public void Stop() {
            isRunning = false;
            try {
                if (server != null) server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            isRunning = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                server = new ServerSocket(port, 5);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try {
                while (isRunning) {
                    Socket socket = server.accept();
                    socket.setSoTimeout(timeout);
                    Message msg = new Message();
                    msg.what = 101;
                    Bundle data = new Bundle();
                    data.putInt("prog", 1002);
                    msg.setData(data);
                    myHandler.sendMessage(msg);

                    String dir = etDir.getText().toString();
                    if (!MyFile.IsDirectory(dir)) {
                        this.publishProgress(-1);
                        continue;
                    }

                    OutputStream out = null;
                    InputStream in = null;
                    FileOutputStream fos = null;
                    out = socket.getOutputStream();
                    in = socket.getInputStream();


                    byte[] buf4 = new byte[4];
                    byte[] buffer = new byte[size];
                    in.read(buf4);
                    int ydh = MyFuns.byteToLittleInt(buf4);
                    in.read(buf4);
                    int fileLen = MyFuns.byteToLittleInt(buf4);
                    int fileType = in.read();

                    String ex = fileType == 0 ? ".slqc" : ".xls";
                    String str = dir + "/" + ydh + ex;
                    MyFile.DeleteFile(str);
                    fos = new FileOutputStream(str);

                    int r = 0;
                    int len = 0;
                    int k = 0;
                    while ((r = in.read(buffer)) > 0) {
                        if (!isRunning) {
                            this.publishProgress(-2);
                            if (fos != null) fos.close();
                            if (in != null) in.close();
                            if (out != null) out.close();
                            if (socket != null) socket.close();
                            return false;
                        }
                        fos.write(buffer, 0, r);
                        fos.flush();
                        len += r;
                        k = len * 100 / fileLen;
                        this.publishProgress(k);
                    }
                    this.publishProgress(101);
                    if (fos != null) fos.close();
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.publishProgress(-3);
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int p = values[0];
            switch (p) {
                case -1: {
                    pbProg.setVisibility(4);
                    tvInfo.setText("已停止。");
                    break;
                }
                case -2: {
                    pbProg.setVisibility(4);
                    tvInfo.setText("保存位置无效！");
                    break;
                }
                case -3: {
                    pbProg.setVisibility(4);
                    tvInfo.setText("网络异常！");
                    break;
                }
                case 101: {
                    pbProg.setVisibility(4);
                    tvInfo.setText("就绪。");
                    break;
                }
                default: {
                    if (p > 0 && p < 101) {
                        pbProg.setVisibility(1);
                        tvInfo.setText("正在接收：" + p + "%...");
                    }
                    break;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == MyWifimgr.MSG_AP_SUCCESS) {
                etWifiName.setText("wifi_slqc");
                etWifiPwd.setText("12341234");
                pbProg.setVisibility(4);
                tvInfo.setText("就绪.");

                serverTask.execute("");
                broadTask.execute("");
            }
            if (msg.what == MyWifimgr.MSG_AP_FAILED) {
                pbProg.setVisibility(4);
                tvInfo.setText("创建热点失败.");
            }
        }
    }
}
