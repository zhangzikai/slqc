package com.tdgeos.dlg.base;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyAboutDialog extends Dialog implements OnClickListener {
    private TextView tvVersion;
    private Button btnOk;
    private Context context;

    public MyAboutDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_base_about);
        setTitle("¹ØÓÚ");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();

        tvVersion = (TextView) findViewById(R.id.about_tv_version);

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            tvVersion.setText(versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        btnOk = (Button) findViewById(R.id.my_about_dialog_btn_ok);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_about_dialog_btn_ok: {
                this.dismiss();
                break;
            }
        }
    }
}