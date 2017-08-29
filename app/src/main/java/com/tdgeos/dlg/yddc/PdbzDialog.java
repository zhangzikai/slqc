package com.tdgeos.dlg.yddc;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class PdbzDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int type;

    private ImageView ivPic = null;
    private Button btnOk = null;

    public static final int TYPE_LINGZ = 1001;
    public static final int TYPE_SLQLJG = 1002;

    public PdbzDialog(Context context, int type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_pdbz);
        setTitle("ÅÐ¶¨±ê×¼");
        getWindow().getAttributes().width = MyConfig.GetBigWidth();
        getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ivPic = (ImageView) findViewById(R.id.iv_pic);
        ivPic.setScaleType(ScaleType.FIT_CENTER);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        switch (type) {
            case TYPE_LINGZ: {
                ivPic.setImageResource(R.drawable.pdbz_lingz);
                break;
            }
            case TYPE_SLQLJG: {
                ivPic.setImageResource(R.drawable.pdbz_slqljg);
                break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                this.cancel();
                break;
            }
        }
    }
}
