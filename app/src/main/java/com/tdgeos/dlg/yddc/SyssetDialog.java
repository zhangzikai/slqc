package com.tdgeos.dlg.yddc;

import java.io.File;
import java.io.IOException;

import com.tdgeos.dlg.base.MyDirCheckedDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SyssetDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tvInfo = null;
    private EditText etBackupPath = null;
    private Button btnBackup = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    public SyssetDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_sysset);
        setTitle("ϵͳ����");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tvInfo = (TextView) findViewById(R.id.tv_info);
        String str = "�������ݱ���Ŀ¼��������¼�������ϵͳ���Զ������ݱ��ݵ���Ŀ¼��\n" +
                "4.4.0(����4.4.0)֮ǰ��ϵͳ����ѱ���Ŀ¼���õ����ô洢����4.4.0֮���ϵͳֻ�����õ����ÿ���\n" +
                "��ǰϵͳ�汾��" + android.os.Build.VERSION.RELEASE;
        tvInfo.setText(str);

        etBackupPath = (EditText) findViewById(R.id.et_backup);
        etBackupPath.setText(MyConfig.GetBackupdir());

        btnBackup = (Button) findViewById(R.id.btn_backup);
        btnBackup.setOnClickListener(this);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backup: {
                MyDirCheckedDialog dlg = new MyDirCheckedDialog(context, MyConfig.GetBackupdir());
                String dir = dlg.showDialog();
                if (dir != null) {
                    etBackupPath.setText(dir);
                    MyConfig.SetBackupdir(dir);
                }
                break;
            }
            case R.id.btn_ok: {
                String bak = etBackupPath.getText().toString().trim();
                if (bak.equals("")) {
                    break;
                }
                if (!MyFile.IsDirectory(bak)) {
                    Toast.makeText(context, "����Ŀ¼���ô��󣬲���һ����Ч��Ŀ¼��", 1).show();
                    break;
                } else {
                    String str = bak + "/test.tmp";
                    File file = new File(str);
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file.exists()) {
                        file.delete();
                    } else {
                        Toast.makeText(context, "����Ŀ¼���ô���û��Ȩ�޲�����Ŀ¼��", 1).show();
                        break;
                    }
                }
                MyConfig.SetBackupdir(bak);
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

}