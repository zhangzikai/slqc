package com.tdgeos.dlg.yddc;


import com.tdgeos.dlg.base.MyDirCheckedDialog;
import com.tdgeos.dlg.base.MyFileOpenDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFolder;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DataImportDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler;

    private TextView tvDir = null;
    private EditText etDir = null;
    private LinearLayout layExport = null;
    private boolean isRunning = false;

    private RadioGroup rgType = null;
    private boolean bType = true;

    private CheckBox cbCover = null;

    private Button btnDir = null;
    private Button btnExport = null;
    private Button btnCancel = null;

    public DataImportDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_dataimport);
        setTitle("数据导入");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setCanceledOnTouchOutside(false);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        layExport = (LinearLayout) findViewById(R.id.lay_export);
        etDir = (EditText) findViewById(R.id.et_dir);
        tvDir = (TextView) findViewById(R.id.tv_dir);

        cbCover = (CheckBox) findViewById(R.id.cb_cover);

        btnDir = (Button) findViewById(R.id.btn_dir);
        btnExport = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnDir.setOnClickListener(this);
        btnExport.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        rgType = (RadioGroup) findViewById(R.id.rg_type);
        rgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup v, int id) {
                if (id == R.id.rb_0) {
                    bType = true;
                    tvDir.setText("数据文件：");
                } else {
                    bType = false;
                    tvDir.setText("数据文件夹：");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dir: {
                if (bType) {
                    MyFileOpenDialog dlg = new MyFileOpenDialog(context, MyConfig.GetCurdir(), new String[]{"slqc"});
                    String dir = dlg.showDialog();
                    if (dir != null) {
                        MyConfig.SetCurdir(MyFile.GetParentPath(dir));
                        etDir.setText(dir);
                    }
                } else {
                    MyDirCheckedDialog dlg = new MyDirCheckedDialog(context, MyConfig.GetCurdir());
                    String dir = dlg.showDialog();
                    if (dir != null) {
                        MyConfig.SetCurdir(dir);
                        etDir.setText(dir);
                    }
                }
                break;
            }
            case R.id.btn_ok: {
                if (isRunning) {
                    Toast.makeText(context, "正在执行导入任务，请在当前任务完成后再导入新任务。", 1).show();
                    break;
                }
                String file = etDir.getText().toString();
                if (!MyFile.Exists(file)) {
                    Toast.makeText(context, "无效的数据文件。", 1).show();
                    break;
                }

                ImportTask task = new ImportTask();
                task.execute("");
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    public void showDialog() {
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
        return;
    }

    class ImportTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            isRunning = true;
            layExport.setVisibility(1);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String file = etDir.getText().toString();
            if (bType) {
                int ydh = 0;
                String name = MyFile.GetFileNameNoEx(file);
                try {
                    ydh = Integer.parseInt(name);
                } catch (Exception e) {
                }
                int xianCode = Qianqimgr.GetXianCodeByYdh(ydh);
                String xianName = Qianqimgr.GetXianName(xianCode);
                if (xianName == null || xianCode <= 0) {
                    //Toast.makeText(context, "样地号无效！", 1).show();
                    publishProgress(-1);
                    return false;
                }

                String srcFile = YangdiMgr.getDbFile(ydh);
                if (MyFile.Exists(srcFile) && !cbCover.isChecked()) {
                    Setmgr.AddTaskByYdh(ydh);
                    return true;
                } else {
                    MyFile.Rename(srcFile, srcFile + ".bak");
                }

                String dbFile = YangdiMgr.getDbFile(ydh);
                MyFile.DeleteFile(dbFile);
                try {
                    MyFile.CopyFile(file, dbFile);
                    YangdiMgr.decodeFile(dbFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Setmgr.RemoveTask(ydh);
                Setmgr.AddTaskByYdh(ydh);

                String sql = "select * from worker where ydh = '" + ydh + "'";
                String[][] sss = YangdiMgr.SelectData(ydh, sql);
                if (sss != null) {
                    for (int k = 0; k < sss.length; k++) {
                        sql = "delete from worker where name = '" + sss[k][2] + "' and phone = '" + sss[k][4] + "'";
                        Setmgr.ExecSQL(sql);
                        sql = "insert into worker(name,type,phone,company,address,notes) values(" +
                                "'" + sss[k][2] + "', '" + sss[k][3] + "', '" + sss[k][4] + "', '" + sss[k][5] + "', '" + sss[k][6] + "', '" + sss[k][7] + "')";
                        Setmgr.ExecSQL(sql);
                    }
                }
            } else {
                MyFolder mf = MyFile.getCurFile(file, new String[]{"slqc"});
                for (int i = 0; i < mf.files.size(); i++) {
                    String strFile = file + "/" + mf.files.get(i);
                    int ydh = 0;
                    String name = MyFile.GetFileNameNoEx(strFile);
                    try {
                        ydh = Integer.parseInt(name);
                    } catch (Exception e) {
                    }
                    int xianCode = Qianqimgr.GetXianCodeByYdh(ydh);
                    String xianName = Qianqimgr.GetXianName(xianCode);
                    if (xianName == null || xianCode <= 0) {
                        continue;
                    }

                    String srcFile = YangdiMgr.getDbFile(ydh);
                    if (MyFile.Exists(srcFile) && !cbCover.isChecked()) {
                        Setmgr.AddTaskByYdh(ydh);
                        continue;
                    } else {
                        MyFile.Rename(srcFile, srcFile + ".bak");
                    }

                    String dbFile = YangdiMgr.getDbFile(ydh);
                    MyFile.DeleteFile(dbFile);
                    try {
                        MyFile.CopyFile(strFile, dbFile);
                        YangdiMgr.decodeFile(dbFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Setmgr.RemoveTask(ydh);
                    Setmgr.AddTaskByYdh(ydh);

                    String sql = "select * from worker where ydh = '" + ydh + "'";
                    String[][] sss = YangdiMgr.SelectData(ydh, sql);
                    if (sss != null) {
                        for (int k = 0; k < sss.length; k++) {
                            sql = "delete from worker where name = '" + sss[k][2] + "' and phone = '" + sss[k][4] + "'";
                            Setmgr.ExecSQL(sql);
                            sql = "insert into worker(name,type,phone,company,address,notes) values(" +
                                    "'" + sss[k][2] + "', '" + sss[k][3] + "', '" + sss[k][4] + "', '" + sss[k][5] + "', '" + sss[k][6] + "', '" + sss[k][7] + "')";
                            Setmgr.ExecSQL(sql);
                        }
                    }
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            if (i == -1) Toast.makeText(context, "无法导入，无效样地号.", 1).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            layExport.setVisibility(4);
            isRunning = false;
            if (result) Toast.makeText(context, "导入完成.", 1).show();
            else Toast.makeText(context, "导入失败.", 1).show();
        }
    }
}
