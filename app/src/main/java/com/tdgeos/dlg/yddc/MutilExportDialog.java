package com.tdgeos.dlg.yddc;

import java.io.FileOutputStream;
import java.io.IOException;

import com.tdgeos.dlg.base.MyDirCheckedDialog;
import com.tdgeos.dlg.yddc.MutilUploadDialog.MyTask;
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
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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

public class MutilExportDialog extends Dialog implements View.OnClickListener {
    private Context context = null;
    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;
    private LinearLayout layProg = null;
    private CheckBox cbAll = null;
    private CheckBox cbFinish = null;
    private CheckBox cbDb = null;
    private CheckBox cbExcel = null;
    private CheckBox cbPhoto = null;
    private EditText etDir = null;
    private Button btnDir = null;
    private ProgressBar pbProg = null;
    private TextView tvProg = null;
    private Button btnOk = null;
    private Button btnCancel = null;
    private boolean isRunning = false;
    private boolean isStop = true;

    public MutilExportDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_mutilexport);
        this.setTitle("批量导出数据");
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
        cbPhoto.setChecked(false);

        etDir = (EditText) findViewById(R.id.et_dir);
        String dir = MyConfig.GetExportdir();
        etDir.setText(dir);

        btnDir = (Button) findViewById(R.id.btn_dir);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnDir.setOnClickListener(this);
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
                if (isRunning) {
                    Toast.makeText(context, "正在导出数据！", 1).show();
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
                    Toast.makeText(context, "请至少选择一个需要导出的数据！", 1).show();
                    break;
                }
                if (!cbDb.isChecked() && !cbExcel.isChecked()) {
                    Toast.makeText(context, "请至少选择一种数据类型！", 1).show();
                    break;
                }
                String dir = etDir.getText().toString();
                if (dir.equals("")) {
                    Toast.makeText(context, "尚未设置导出位置！", 1).show();
                    break;
                }
                if (!MyFile.IsDirectory(dir)) {
                    Toast.makeText(context, "无效的导出位置！", 1).show();
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
            tvProg.setText("正在导出...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String dir = etDir.getText().toString();
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
                                for (int k = 0; k < sss.length; k++) {
                                    int zph = -1;
                                    try {
                                        zph = Integer.parseInt(sss[k][0]);
                                    } catch (Exception e) {
                                    }
                                    Bitmap bmp = YangdiMgr.GetZp(ydh, zph);
                                    if (bmp != null) {
                                        try {
                                            String pic = dir + "/" + ydh + "_照片/" + sss[k][1] + ".jpg";
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

                        if (!cbDb.isChecked()) {
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
                }
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pbProg.setVisibility(4);
            tvProg.setText("导出完成。");
            isRunning = false;
            isStop = true;
        }
    }
}
