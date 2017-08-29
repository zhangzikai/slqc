package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CheckYdyzDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int ydh;

    private EditText etLog = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    public CheckYdyzDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_datacheck);
        setTitle("数据检查");
        getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

            }
        });

        etLog = (EditText) findViewById(R.id.et_log);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        checkYdyz();
        //new CheckTask().execute("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    private void checkYdyz() {
        etLog.setText("");

        int dl = YangdiMgr.GetBqdl(ydh);

        List<String> lstError = new ArrayList<String>();
        List<String> lstWarn = new ArrayList<String>();
        lstError.clear();
        lstWarn.clear();
        if (dl <= 0) {
            lstError.add("没有填写有效的地类！");
            setLog("样地因子调查记录", lstError, lstWarn);
            return;
        }
        YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
        setLog("样地因子调查记录", lstError, lstWarn);
    }

    private void setLog(String table, List<String> lstError, List<String> lstWarn) {
        if (lstError == null || lstWarn == null) return;
        SpannableString ss = new SpannableString(table + "\n");
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etLog.append(ss);
        for (int i = 0; i < lstError.size(); i++) {
            SpannableString text = new SpannableString(lstError.get(i) + "\n");
            text.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            etLog.append(text);
        }
        for (int i = 0; i < lstWarn.size(); i++) {
            SpannableString text = new SpannableString(lstWarn.get(i) + "\n");
            text.setSpan(new ForegroundColorSpan(0xffe0c040), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            etLog.append(text);
        }
        if (lstError.size() == 0 && lstWarn.size() == 0) {
            SpannableString text = new SpannableString("检查通过！\n");
            text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            etLog.append(text);
        }
    }

    class CheckTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog dlg;
        private List<String> lstError = new ArrayList<String>();
        private List<String> lstWarn = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            etLog.setText("");
            dlg = new ProgressDialog(context);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在初始化数据...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            int dl = YangdiMgr.GetBqdl(ydh);
            lstError.clear();
            lstWarn.clear();
            if (dl <= 0) {
                lstError.add("没有填写有效的地类！");
            } else {
                YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setLog("样地因子调查记录", lstError, lstWarn);
            dlg.cancel();
        }
    }
}
