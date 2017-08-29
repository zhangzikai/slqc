package com.tdgeos.dlg.yddc;

import java.io.FileOutputStream;
import java.io.IOException;

import com.tdgeos.dlg.base.MyDirCheckedDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DataExportDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int ydh;

    private CheckBox cbSqlite = null;
    private CheckBox cbExcel = null;
    private CheckBox cbPhoto = null;
    private EditText etDir = null;
    private LinearLayout layExport = null;
    private boolean isExporting = false;

    private Button btnDir = null;
    private Button btnExport = null;
    private Button btnCancel = null;

    public DataExportDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_dataexport);
        setTitle("数据导出");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        cbSqlite = (CheckBox) findViewById(R.id.cb_db);
        cbExcel = (CheckBox) findViewById(R.id.cb_excel);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo);
        cbSqlite.setChecked(true);
        cbExcel.setChecked(true);
        cbPhoto.setChecked(false);

        layExport = (LinearLayout) findViewById(R.id.lay_export);
        etDir = (EditText) findViewById(R.id.et_dir);
        String dir = MyConfig.GetExportdir();
        etDir.setText(dir);

        btnDir = (Button) findViewById(R.id.btn_dir);
        btnExport = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnDir.setOnClickListener(this);
        btnExport.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dir: {
                MyDirCheckedDialog dlg = new MyDirCheckedDialog(context, MyConfig.GetCurdir());
                String dir = dlg.showDialog();
                if (dir != null) {
                    MyConfig.SetCurdir(dir);
                    MyConfig.SetExportdir(dir);
                    etDir.setText(dir);
                }
                break;
            }
            case R.id.btn_ok: {
                if (isExporting) {
                    Toast.makeText(context, "正在执行导出任务，请在当前任务完成后再导出新任务。", 1).show();
                    break;
                }
                boolean b = false;
                if (cbSqlite.isChecked()) b = true;
                if (cbExcel.isChecked()) b = true;
                if (!b) {
                    Toast.makeText(context, "请选择至少一种数据类型。", 1).show();
                    break;
                }
                String dir = etDir.getText().toString();
                if (!MyFile.IsDirectory(dir)) {
                    Toast.makeText(context, "导出位置无效。", 1).show();
                    break;
                }
                ExportTask task = new ExportTask();
                task.execute("");
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    class ExportTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            isExporting = true;
            layExport.setVisibility(1);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String srcFile = YangdiMgr.getDbFile(ydh);
            String dir = etDir.getText().toString();
            String dstFile = dir + "/" + ydh + YangdiMgr.EXPORT_EXNAME;
            MyFile.DeleteFile(dstFile);
            try {
                MyFile.CopyFile(srcFile, dstFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (MyFile.Exists(dstFile)) {
                if (cbPhoto.isChecked()) {
                    //SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dstFile, null);
                    //String sql = "delete from zp where ydh = '" + ydh + "'";
                    //db.execSQL(sql);
                    //db.close();

                    String sql = "select zph,name from zp where ydh = '" + ydh + "'";
                    String[][] sss = YangdiMgr.SelectData(ydh, sql);
                    if (sss != null) {
                        MyFile.MakeDir(dir + "/" + ydh + "_照片");
                        for (int i = 0; i < sss.length; i++) {
                            int zph = -1;
                            try {
                                zph = Integer.parseInt(sss[i][0]);
                            } catch (Exception e) {
                            }
                            Bitmap bmp = YangdiMgr.GetZp(ydh, zph);
                            if (bmp != null) {
                                try {
                                    String pic = dir + "/" + ydh + "_照片/" + sss[i][1] + ".jpg";
                                    pic = MyFile.GetValidFileName(pic, '_', 1);
                                    FileOutputStream outStream = new FileOutputStream(pic);
                                    bmp.compress(CompressFormat.PNG, 100, outStream);
                                    outStream.close();
                                    bmp.recycle();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                if (cbExcel.isChecked()) {
                    String xls = dir + "/" + ydh + ".xls";
                    YangdiMgr.ExportToExcel(dstFile, xls, ydh);
                }

                if (!cbSqlite.isChecked()) {
                    MyFile.DeleteFile(dstFile);
                } else {
                    try {
                        String str = MyFuns.GetDateTimeByString();
                        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dstFile, null);
                        String sql = "update info set export_time = '" + str + "' where ydh = '" + ydh + "'";
                        db.execSQL(sql);
                        db.close();
                        YangdiMgr.encryptFile(dstFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MyFile.DeleteFile(dstFile + "-journal");
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            layExport.setVisibility(4);
            isExporting = false;
            if (result) Toast.makeText(context, "导出完成.", 1).show();
            else Toast.makeText(context, "导出失败.", 1).show();
        }
    }
}
