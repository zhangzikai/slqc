package com.tdgeos.dlg.yddc;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

public class UploadSearchDialog extends Dialog implements View.OnClickListener {
    private Context ctt;
    private Handler mHandler;
    private String result = null;

    private boolean isStop = false;

    private LinearLayout lay1 = null;
    private LinearLayout lay2 = null;
    private ScrollView lay3 = null;
    private LinearLayout layList = null;
    private LayoutInflater layInflater = null;

    private Button btnCancel = null;

    public UploadSearchDialog(Context context) {
        super(context);
        ctt = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_pcsearch);
        setTitle("搜索设备");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        lay1 = (LinearLayout) findViewById(R.id.lay_1);
        lay2 = (LinearLayout) findViewById(R.id.lay_2);
        lay3 = (ScrollView) findViewById(R.id.lay_3);
        layList = (LinearLayout) findViewById(R.id.lay_list);
        layInflater = LayoutInflater.from(ctt);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                UploadSearchDialog.this.cancel();
            }
        });

        SearchTask task = new SearchTask();
        task.execute("");
        TimeThread timer = new TimeThread();
        timer.start();
    }

    @Override
    public void onClick(View v) {
        try {
            String info = (String) v.getTag();
            result = info;
            this.cancel();
        } catch (Exception e) {
        }
    }

    public String showDialog() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        super.show();
        try {
            Looper.getMainLooper();
            Looper.loop();
        } catch (RuntimeException e2) {
        }
        return result;
    }

    class TimeThread extends Thread {
        public void run() {
            int i = 0;
            while (i++ < 15) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isStop = true;
        }
    }

    class SearchTask extends AsyncTask<String, Integer, Boolean> {
        private static final String BROADCAST_IP = "230.0.0.1";//广播IP
        private static final int BROADCAST_INT_PORT = 40005;

        MulticastSocket broadSocket;
        InetAddress broadAddress;

        private List<String> lst = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
                broadAddress = InetAddress.getByName(BROADCAST_IP);
                broadSocket.joinGroup(broadAddress);
                broadSocket.setSoTimeout(3000);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            while (!isStop) {
                try {
                    byte[] bts = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(bts, bts.length);
                    broadSocket.receive(packet);
                    String info = new String(bts);
                    info = info.trim();
                    //System.out.println("[" + info + "]");
                    boolean b = false;
                    for (int i = 0; i < lst.size(); i++) {
                        if (lst.get(i).equals(info)) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        lst.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return lst.size() > 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                lay1.setVisibility(8);
                lay3.setVisibility(1);
                for (int i = 0; i < lst.size(); i++) {
                    LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.dlg_yddc_pcsearch_item, null);
                    layRow.setTag(lst.get(i));
                    layRow.setOnClickListener(UploadSearchDialog.this);
                    String[] ss = MyFuns.Split(lst.get(i), '@');
                    if (ss.length != 2) continue;
                    TextView tvName = (TextView) layRow.findViewById(R.id.tv_name);
                    TextView tvIp = (TextView) layRow.findViewById(R.id.tv_ip);
                    tvName.setText(ss[0]);
                    tvIp.setText(ss[1]);
                    layList.addView(layRow);
                }
            } else {
                lay1.setVisibility(8);
                lay2.setVisibility(1);
            }
        }
    }
}

/* 发广播代码
private static final String BROADCAST_IP = "230.0.0.1";//广播IP
private static final int BROADCAST_INT_PORT = 40005;

MulticastSocket broadSocket ;
InetAddress broadAddress ;
DatagramSocket sender ;

public BroadSend() 
{
	try {
		broadSocket = new MulticastSocket(BROADCAST_INT_PORT); 
		broadAddress = InetAddress.getByName(BROADCAST_IP);
		broadSocket.joinGroup(broadAddress);
		sender = new DatagramSocket();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void sendBroad() 
{
	try{
		String info = "192.168.1.12";
		byte[] bts = info.getBytes();  
		DatagramPacket packet = new DatagramPacket(bts, bts.length, broadAddress, BROADCAST_INT_PORT);
		sender.send(packet);     
	}catch (Exception e) {
		e.printStackTrace();
	}
}
*/
