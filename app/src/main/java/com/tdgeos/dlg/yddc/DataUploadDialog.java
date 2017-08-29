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
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DataUploadDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int ydh;

    private CheckBox cbSqlite = null;
    private CheckBox cbExcel = null;
    private CheckBox cbPhoto = null;
    private EditText etServer = null;
    private LinearLayout layUpload = null;
    private ProgressBar pbUpload = null;
    private TextView tvUpload = null;
    private boolean isUploading = false;
    private boolean isStop = false;

    private Button btnSearch = null;
    private Button btnUpload = null;
    private Button btnCancel = null;

    public DataUploadDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_dataupload);
        setTitle("数据传输");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

            }
        });

        cbSqlite = (CheckBox) findViewById(R.id.cb_db);
        cbExcel = (CheckBox) findViewById(R.id.cb_excel);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo);
        cbSqlite.setChecked(true);
        cbPhoto.setChecked(true);

        layUpload = (LinearLayout) findViewById(R.id.lay_upload);
        pbUpload = (ProgressBar) findViewById(R.id.pb_upload);
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        etServer = (EditText) findViewById(R.id.et_server);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnUpload = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSearch.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        pbUpload.setVisibility(4);
        tvUpload.setText("");

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
                if (isUploading) {
                    Toast.makeText(context, "正在执行上传任务，请在当前任务完成后再上传新任务。", 1).show();
                    break;
                }
                boolean b = false;
                if (cbSqlite.isChecked()) b = true;
                if (cbExcel.isChecked()) b = true;
                if (!b) {
                    Toast.makeText(context, "请选择至少一种数据类型。", 1).show();
                    break;
                }
                String str = etServer.getText().toString();
                if (str.equals("")) {
                    Toast.makeText(context, "无效的目标设备。", 1).show();
                    break;
                }
                UploadTask task = new UploadTask();
                task.execute("");
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    class UploadTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            isUploading = true;
            pbUpload.setVisibility(1);
        }

        @Override
        protected Boolean doInBackground(String... params) {
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
                    return false;
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
                if (cbSqlite.isChecked()) {
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
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            if (i == -1) {
                pbUpload.setVisibility(4);
                tvUpload.setText("上传失败，网络异常。");
            }
            if (i == -2) {
                pbUpload.setVisibility(4);
                tvUpload.setText("数据导出失败。");
            }
            if (i == -3) {
                pbUpload.setVisibility(4);
                tvUpload.setText("上传被取消。");
            }
            if (i == 1001) {
                pbUpload.setVisibility(1);
                tvUpload.setText("正在导出数据...");
            }
            if (i == 1002) {
                pbUpload.setVisibility(1);
                tvUpload.setText("导出完成，准备上传...");
            }
            if (i == 1003) {
                pbUpload.setVisibility(4);
                tvUpload.setText("上传完成。");
            }
            if (i >= 0 && i <= 100) {
                pbUpload.setVisibility(1);
                tvUpload.setText("正在上传：" + i + "%");
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isUploading = false;
            pbUpload.setVisibility(4);
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
