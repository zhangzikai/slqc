package com.tdgeos.dlg.yddc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.tanhui.MyWifimgr;
import com.tdgeos.yangdi.YangdiMgr;

public class CombineClientDialog extends Dialog implements OnClickListener {
    private Context context;
    private MyHandler myHandler;
    private MyWifimgr myWifimgr;
    private boolean isRuning = false;

    private ProgressBar pbSend = null;
    private TextView tvSend = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private String ip = "192.168.43.1";

    private int ydh = 0;

    public CombineClientDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_datacombine_client);
        setTitle("���ݺϲ�");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setCanceledOnTouchOutside(false);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isRuning = false;
                myWifimgr.CloseWifi();
            }
        });

        myHandler = new MyHandler();

        pbSend = (ProgressBar) findViewById(R.id.pb_send);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvSend.setText("�������ӽ��ն�WIFI�ȵ�...");

        btnOk = (Button) findViewById(R.id.btn_send);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setEnabled(false);

        myWifimgr = new MyWifimgr(context, myHandler);
        if (myWifimgr.IsWifiConnTo("wifi_slqc")) {
            pbSend.setVisibility(4);
            tvSend.setText("����.");
            btnOk.setEnabled(true);
        } else {
            myWifimgr.Connect("wifi_slqc", "12341234", MyWifimgr.WIFICIPHER_WPA);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send: {
                if (isRuning) {
                    Toast.makeText(context, "���ڴ��䣬���ڵ�ǰ��������Ϻ����ԡ�", 1).show();
                    break;
                }
                String filePath = YangdiMgr.getDbFile(ydh);
                UploadTask task = new UploadTask();
                task.execute(filePath);
                btnOk.setEnabled(false);
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == MyWifimgr.MSG_CONN_SUCCESS) {
                pbSend.setVisibility(4);
                tvSend.setText("����.");

                btnOk.setEnabled(true);
            }
            if (msg.what == MyWifimgr.MSG_CONN_FAILED) {
                pbSend.setVisibility(4);
                tvSend.setText("����WIFIʧ��.");
                btnOk.setEnabled(false);
            }
        }
    }

    class UploadTask extends AsyncTask<String, Integer, Boolean> {
        private Socket socket = null;
        private int port = 9901;
        private int timeout = 30000;
        private final String charsetName = "UTF8";

        private int size = 2048;
        private int fileLen = 0;
        private int count = 0;
        private short residue = 0;

        @Override
        protected void onPreExecute() {
            pbSend.setVisibility(1);
            isRuning = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                this.publishProgress(101);
                socket = new Socket(ip, port);
                socket.setSoTimeout(timeout);
            } catch (Exception e) {
                e.printStackTrace();
                this.publishProgress(-1);
            }

            if (socket == null) return false;

            String filePath = params[0];
            File f = new File(filePath);
            if (f.exists()) {
                fileLen = (int) f.length();
                residue = (short) (fileLen % size);
                count = fileLen / size;
            } else {
                this.publishProgress(-2);
                return false;
            }

            OutputStream out = null;
            InputStream in = null;
            FileInputStream fis = null;
            try {
                out = socket.getOutputStream();
                in = socket.getInputStream();
                fis = new FileInputStream(f);

				/* �ϵ���������
                System.out.println("�����ļ���");
				//�����ļ���������ֻ�跢�����غż���
				int index = 0;
				byte[] bts = intToByte(ydh);
				short len = (short)(bts.length + 8);
				byte[] buffer = new byte[len+2];
				writeToBuffer(buffer, shortToByte(len), 0);//���ݿ鳤��
				writeToBuffer(buffer, 0, 2);//�����0��ʾ�ļ�����
				writeToBuffer(buffer, 0, 3);//�����0��ʾ�ļ����ƣ�1�ͻ��˴���ʵ���ļ����ݣ�2֪ͨ������ļ�������3������˸��߿ͻ��˼������䣬4����˸��߿ͻ��˶ϵ�������5����˸��߿ͻ����ļ��������;
				writeToBuffer(buffer, intToByte(count), 4);//�ļ��ֿ����������������ʣ�����ݿ�
				writeToBuffer(buffer, shortToByte(residue), 8);//���ʣ�����ݿ��С
				writeToBuffer(buffer, bts, 10);//ʵ������
				while(!isStop)
				{
					out.write(buffer);
					out.flush();
					this.publishProgress(0);
					
					bts = new byte[10];
					int re = in.read(bts);
					if(re == 10)
					{
						int code = bts[3];
						if(code == 4)
						{
							byte[] bts4 = new byte[4];
							bts4[0] = bts[4];
							bts4[1] = bts[5];
							bts4[2] = bts[6];
							bts4[3] = bts[7];
							index = byteToInt(bts4);
						}
						if(code == 3)
						{
							break;
						}
					}
				}
				
				System.out.println("�����ļ����ݿ�");
				//�����ļ����ݿ�
				for(int i=0;i<count;i++)
				{
					if(isStop)
					{
						break;
					}
					System.out.println("i = " + i);
					bts = new byte[size];
					fis.read(bts);
					len = (short)(bts.length + 8);
					buffer = new byte[len+2];
					writeToBuffer(buffer, shortToByte(len), 0);
					writeToBuffer(buffer, 0, 2);
					writeToBuffer(buffer, 1, 3);
					writeToBuffer(buffer, intToByte(i), 4);
					writeToBuffer(buffer, shortToByte((short)residue), 8);
					writeToBuffer(buffer, bts, 10);
					while(!isStop)
					{
						out.write(buffer);
						out.flush();
						
						bts = new byte[10];
						re = in.read(bts);
						if(re == 10)
						{
							int code = bts[3];
							if(code == 4)
							{
								byte[] bts4 = new byte[4];
								bts4[0] = bts[4];
								bts4[1] = bts[5];
								bts4[2] = bts[6];
								bts4[3] = bts[7];
								index = byteToInt(bts4);
							}
							if(code == 3)
							{
								break;
							}
						}
					}
					int k = i * 100 / count;
					this.publishProgress(k);
				}
				
				if(isStop)
				{
					this.publishProgress(-3);
					if(fis != null) fis.close();
					if(out != null) out.close();
					if(in != null) in.close();
					if(socket != null) socket.close();
					return false;
				}
				
				System.out.println("�������ʣ�����ݿ�");
				//�������ʣ�����ݿ�
				bts = new byte[residue];
				fis.read(bts);
				len = (short)(bts.length + 8);
				buffer = new byte[len+2];
				writeToBuffer(buffer, shortToByte(len), 0);
				writeToBuffer(buffer, 0, 2);
				writeToBuffer(buffer, 1, 3);
				writeToBuffer(buffer, intToByte(count), 4);
				writeToBuffer(buffer, shortToByte((short)residue), 8);
				writeToBuffer(buffer, bts, 10);
				while(!isStop)
				{
					out.write(buffer);
					out.flush();
					
					bts = new byte[10];
					re = in.read(bts);
					if(re == 10)
					{
						int code = bts[3];
						if(code == 4)
						{
							byte[] bts4 = new byte[4];
							bts4[0] = bts[4];
							bts4[1] = bts[5];
							bts4[2] = bts[6];
							bts4[3] = bts[7];
							index = byteToInt(bts4);
						}
						if(code == 3)
						{
							break;
						}
					}
				}
				this.publishProgress(100);
				
				if(isStop)
				{
					this.publishProgress(-3);
					if(fis != null) fis.close();
					if(out != null) out.close();
					if(in != null) in.close();
					if(socket != null) socket.close();
					return false;
				}
				
				System.out.println("��֪���ն��ļ��������");
				//��֪���ն��ļ��������
				len = (short)(8);
				buffer = new byte[len+2];
				writeToBuffer(buffer, shortToByte(len), 0);
				writeToBuffer(buffer, 0, 2);
				writeToBuffer(buffer, 2, 3);
				writeToBuffer(buffer, intToByte(0), 4);
				writeToBuffer(buffer, shortToByte((short)0), 8);
				while(!isStop)
				{
					out.write(buffer);
					out.flush();
					
					bts = new byte[10];
					re = in.read(bts);
					if(re == 10)
					{
						int code = bts[3];
						if(code == 5)
						{
							this.publishProgress(102);
							break;
						}
					}
				}
				*/

                //
                byte[] buf4 = new byte[4];
                byte[] buffer = new byte[size];
                buf4 = MyFuns.intToByte(ydh);
                out.write(buf4);
                buf4 = MyFuns.intToByte(fileLen);
                out.write(buf4);
                out.flush();
                int r = 0;
                int len = 0;
                int k = 0;
                while ((r = fis.read(buffer)) > 0) {
                    if (!isRuning) {
                        this.publishProgress(-3);
                        if (fis != null) fis.close();
                        if (out != null) out.close();
                        if (in != null) in.close();
                        if (socket != null) socket.close();
                        return true;
                    }
                    out.write(buffer, 0, r);
                    out.flush();
                    len += r;
                    k = len * 100 / fileLen;
                    this.publishProgress(k);
                }
                this.publishProgress(102);

                if (fis != null) fis.close();
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                this.publishProgress(-4);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            if (i == 101) {
                pbSend.setVisibility(1);
                tvSend.setText("�������ӽ��ն�...");
            }
            if (i == 102) {
                pbSend.setVisibility(4);
                tvSend.setText("�������.");
            }
            if (i >= 0 && i <= 100) {
                pbSend.setVisibility(1);
                tvSend.setText("���ڷ��ͣ�" + i + "%");
            }
            if (i == -4) {
                pbSend.setVisibility(4);
                tvSend.setText("����ʧ�ܣ������쳣.");
            }
            if (i == -3) {
                pbSend.setVisibility(4);
                tvSend.setText("����ȡ��.");
            }
            if (i == -2) {
                pbSend.setVisibility(4);
                tvSend.setText("û���ҵ������ļ�.");
            }
            if (i == -1) {
                pbSend.setVisibility(4);
                tvSend.setText("�޷����ӽ��ն�.");
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            btnOk.setEnabled(true);
            pbSend.setVisibility(4);
            isRuning = false;
        }
    }
    /*
	private byte[] intToByte(int w)
	{
		byte[] b = new byte[4];
		b[0] = (byte)w;
		b[1] = (byte)(w>>8);
		b[2] = (byte)(w>>16);
		b[3] = (byte)(w>>24);
		return b;
	}
	
	private byte[] shortToByte(short w)
	{
		byte[] b = new byte[2];
		b[0] = (byte)w;
		b[1] = (byte)(w>>8);
		return b;
	}
	
	private int byteToInt( byte[] w )
    {
        return ( w[3] ) << 24 | ( w[2] & 0xff ) << 16 | ( w[1] & 0xff ) << 8 | ( w[0] & 0xff );
    }

	private void writeToBuffer(byte[] buffer, byte[] bts, int offset)
	{
		if(buffer == null) return;
		if(bts == null) return;
		if(offset < 0 || offset >= buffer.length) return;
		if(buffer.length - offset < bts.length) return;
		
		for(int i=0;i<bts.length;i++)
		{
			buffer[i + offset] = bts[i];
		}
	}
	
	private void writeToBuffer(byte[] buffer, int b, int offset)
	{
		if(buffer == null) return;
		if(offset < 0 || offset >= buffer.length) return;
		if(buffer.length - offset < 1) return;
		
		buffer[offset] = (byte)b;
	}
	*/
}
