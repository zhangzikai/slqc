package com.tdgeos.dlg.yddc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MutilUploadDialog extends Dialog implements View.OnClickListener {
    private Context context = null;
    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;
    private LinearLayout layProg = null;
    private CheckBox cbAll = null;
    private CheckBox cbFinish = null;
    private CheckBox cbDb = null;
    private CheckBox cbExcel = null;
    private CheckBox cbPhoto = null;
    private EditText etServer = null;
    private Button btnSearch = null;
    private ProgressBar pbProg = null;
    private TextView tvProg = null;
    private Button btnOk = null;
    private Button btnCancel = null;
    private boolean isRunning = false;
    private boolean isStop = true;

    public MutilUploadDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_mutilupload);
        this.setTitle("批量上传数据");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isStop = true;
            }
        });

        layInflater = LayoutInflater.from(context);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        layProg = (LinearLayout) findViewById(R.id.lay_prog);
        pbProg = (ProgressBar) findViewById(R.id.pb_prog);
        tvProg = (TextView) findViewById(R.id.tv_prog);

        cbAll = (CheckBox) findViewById(R.id.cb_all);
        cbFinish = (CheckBox) findViewById(R.id.cb_finish);
        cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int n = layList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    cb.setChecked(arg1);
                }
            }
        });
        cbFinish.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                int n = layList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    TextView tvDczt = (TextView) layRow.findViewById(R.id.tv_dczt);
                    String dczt = tvDczt.getText().toString();
                    if (dczt.equalsIgnoreCase("调查完成")) {
                        cb.setChecked(arg1);
                    }
                }
            }
        });

        cbDb = (CheckBox) findViewById(R.id.cb_db);
        cbExcel = (CheckBox) findViewById(R.id.cb_excel);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo);
        cbDb.setChecked(true);
        cbExcel.setChecked(true);
        cbPhoto.setChecked(true);

        etServer = (EditText) findViewById(R.id.et_server);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        loadList();
    }

    private void loadList() {
        String sql = "select ydh,dczt from task where dczt <> '0' order by dczt,ydh";
        String[][] sss = Setmgr.SelectData(sql);
        if (sss == null) return;

        for (int i = 0; i < sss.length; i++) {
            String sydh = sss[i][0];
            String dczt = "未开始";
            int a = 0;
            try {
                a = Integer.parseInt(sss[i][1]);
            } catch (Exception e) {
            }
            if (a == 1) {
                dczt = "正在调查";
            }
            if (a == 2) {
                dczt = "调查完成";
            }
            addRow(sydh, dczt);
        }
    }

    private void addRow(String sydh, String dczt) {
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.dlg_yddc_mutilupload_item, null);
        TextView tvYdh = (TextView) layRow.findViewById(R.id.tv_ydh);
        TextView tvDczt = (TextView) layRow.findViewById(R.id.tv_dczt);
        tvYdh.setText(sydh);
        tvDczt.setText(dczt);
        layList.addView(layRow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search: {
                UploadSearchDialog dlg = new UploadSearchDialog(context);
                String pc = dlg.showDialog();
                if (pc != null) {
                    etServer.setText(pc);
                }
                break;
            }
            case R.id.btn_ok: {
                if (isRunning) {
                    Toast.makeText(context, "正在上传数据！", 1).show();
                    break;
                }

                boolean b = false;
                int n = layList.getChildCount();
                for (int i = 0; i < n; i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    if (cb.isChecked()) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    Toast.makeText(context, "请至少选择一个需要上传的数据！", 1).show();
                    break;
                }
                if (!cbDb.isChecked() && !cbExcel.isChecked()) {
                    Toast.makeText(context, "请至少选择一种数据类型！", 1).show();
                    break;
                }
                String server = etServer.getText().toString();
                if (server.equals("")) {
                    Toast.makeText(context, "尚未设置目标设备！", 1).show();
                    break;
                }
                MyTask task = new MyTask();
                task.execute("");
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    class MyTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            isRunning = true;
            isStop = false;
            pbProg.setVisibility(1);
            tvProg.setText("正在上传...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //String dir = etDir.getText().toString();
            int n = layList.getChildCount();
            for (int i = 0; i < n; i++) {
                if (isStop) {
                    this.publishProgress(-3);
                    break;
                }

                LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                TextView tvYdh = (TextView) layRow.findViewById(R.id.tv_ydh);
                String sydh = tvYdh.getText().toString();
                if (cb.isChecked()) {
                    int ydh = 0;
                    try {
                        ydh = Integer.parseInt(sydh);
                    } catch (Exception e) {
                    }

                    String srcFile = YangdiMgr.getDbFile(ydh);
                    String dir = MyConfig.GetWorkdir() + "/temp";
                    String dbFile = dir + "/" + ydh + YangdiMgr.EXPORT_EXNAME;
                    String xlsFile = dir + "/" + ydh + ".xls";
                    this.publishProgress(1001);
                    try {
                        MyFile.DeleteFile(dbFile);
                        MyFile.CopyFile(srcFile, dbFile);
                        if (!MyFile.Exists(dbFile)) {
                            this.publishProgress(-2);
                            continue;
                        }
                        if (!cbPhoto.isChecked()) {
                            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                            String sql = "delete from zp where ydh = '" + ydh + "'";
                            db.execSQL(sql);
                            db.close();
                        }
                        if (cbExcel.isChecked()) {
                            YangdiMgr.ExportToExcel(dbFile, xlsFile, ydh);
                        }
                        String dt = MyFuns.GetDateTimeByString();
                        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                        String sql = "update info set export_time = '" + dt + "' where ydh = '" + ydh + "'";
                        db.execSQL(sql);
                        db.close();
                        YangdiMgr.encryptFile(dbFile);

                        this.publishProgress(1002);

                        String str = etServer.getText().toString();
                        String[] ss = MyFuns.Split(str, '@');
                        String ip = ss[1];
                        int port = 9905;
                        int timeout = 15000;
                        int size = 2048;
                        int fileLen = 0;
                        if (cbDb.isChecked()) {
                            Socket socket = new Socket(ip, port);
                            socket.setSoTimeout(timeout);

                            File f = new File(dbFile);
                            fileLen = (int) f.length();

                            OutputStream out = socket.getOutputStream();
                            InputStream in = socket.getInputStream();
                            FileInputStream fis = new FileInputStream(f);

                            byte[] buf4 = new byte[4];
                            byte[] buffer = new byte[size];
                            buf4 = intToByte(ydh);
                            out.write(buf4);
                            buf4 = intToByte(fileLen);
                            out.write(buf4);
                            out.write(0);
                            out.flush();
                            int r = 0;
                            int len = 0;
                            int k = 0;
                            while ((r = fis.read(buffer)) > 0) {
                                if (isStop) {
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
                                k = len * 100 / fileLen / 2;
                                this.publishProgress(k);
                            }

                            if (fis != null) fis.close();
                            if (out != null) out.close();
                            if (in != null) in.close();
                            if (socket != null) socket.close();
                        }

                        if (cbExcel.isChecked()) {
                            Socket socket = new Socket(ip, port);
                            socket.setSoTimeout(timeout);

                            File f = new File(xlsFile);
                            fileLen = (int) f.length();

                            OutputStream out = socket.getOutputStream();
                            InputStream in = socket.getInputStream();
                            FileInputStream fis = new FileInputStream(f);

                            byte[] buf4 = new byte[4];
                            byte[] buffer = new byte[size];
                            buf4 = intToByte(ydh);
                            out.write(buf4);
                            buf4 = intToByte(fileLen);
                            out.write(buf4);
                            out.write(1);
                            out.flush();
                            int r = 0;
                            int len = 0;
                            int k = 0;
                            while ((r = fis.read(buffer)) > 0) {
                                if (isStop) {
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
                                k = len * 100 / fileLen / 2 + 50;
                                this.publishProgress(k);
                            }

                            if (fis != null) fis.close();
                            if (out != null) out.close();
                            if (in != null) in.close();
                            if (socket != null) socket.close();
                        }
                        MyFile.DeleteFile(dbFile);
                        MyFile.DeleteFile(xlsFile);
                        this.publishProgress(1003);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.publishProgress(-1);
                    }
                }
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            if (i == -1) {
                pbProg.setVisibility(4);
                tvProg.setText("上传失败，网络异常。");
            }
            if (i == -2) {
                pbProg.setVisibility(4);
                tvProg.setText("数据导出失败。");
            }
            if (i == -3) {
                pbProg.setVisibility(4);
                tvProg.setText("上传被取消。");
            }
            if (i == 1001) {
                pbProg.setVisibility(1);
                tvProg.setText("正在导出数据...");
            }
            if (i == 1002) {
                pbProg.setVisibility(1);
                tvProg.setText("导出完成，准备上传...");
            }
            if (i == 1003) {
                pbProg.setVisibility(4);
                tvProg.setText("上传完成。");
            }
            if (i >= 0 && i <= 100) {
                pbProg.setVisibility(1);
                tvProg.setText("正在上传：" + i + "%");
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pbProg.setVisibility(4);
            isRunning = false;
            isStop = true;
        }

        private byte[] intToByte(int w) {
            byte[] b = new byte[4];
            b[0] = (byte) w;
            b[1] = (byte) (w >> 8);
            b[2] = (byte) (w >> 16);
            b[3] = (byte) (w >> 24);
            return b;
        }
    }
}
