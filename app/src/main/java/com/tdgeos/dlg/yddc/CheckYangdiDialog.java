package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
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

public class CheckYangdiDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int ydh;

    private EditText etLog = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    public CheckYangdiDialog(Context context, int ydh) {
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

        checkYangdi();
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

    private void checkYangdi() {
        etLog.setText("");

        int[] ii = YangdiMgr.GetDczt(ydh);
        int dl = YangdiMgr.GetBqdl(ydh);
        boolean b = Qianqimgr.IsYangfang(ydh);

        List<String> lstError = new ArrayList<String>();
        List<String> lstWarn = new ArrayList<String>();

        lstError.clear();
        lstWarn.clear();
        if (ii[0] != 2) {
            lstError.add("该项尚未完成！");
            setLog("卡片封面", lstError, lstWarn);
        } else {
            setLog("卡片封面", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[1] != 2) {
            lstError.add("样地引点位置图尚未完成！");
        }
        if (ii[2] != 2) {
            lstError.add("样地位置图尚未完成！");
        }
        if (ii[3] != 2) {
            lstError.add("样地引线测量记录尚未完成！");
        }
        if (ii[4] != 2) {
            lstError.add("样地周界测量记录尚未完成！");
        }
        //if(YangdiMgr.GetYdloc(ydh) == null)
        //{
        //	lstError.add("尚未测量样地坐标！");
        //}
        setLog("样地定位与测设", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        if (dl <= 0) {
            lstError.add("没有填写有效的地类！");
            setLog("样地调查", lstError, lstWarn);
            return;
        }

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
        setLog("样地因子调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
        setLog("跨角林调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
        setLog("每木检尺记录", lstError, lstWarn);

        //lst.clear();
        //if(ii[8] != 2)
        //{
        //	lst.add("该项尚未完成！");
        //	setLog(lst, "样木位置示意图", false);
        //}

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckSgcl(ydh, lstError, lstWarn);
        setLog("树(毛竹)高测量记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckSlzh(ydh, lstError, lstWarn);
        setLog("森林灾害情况调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetZbCount(ydh) == 0) {
            lstWarn.add("该项尚未录入！");
            setLog("植被调查记录", lstError, lstWarn);
        } else {
            setLog("植被调查记录", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetXmCount(ydh) == 0) {
            lstWarn.add("该项尚未录入！");
            setLog("下木调查记录", lstError, lstWarn);
        } else {
            setLog("下木调查记录", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckTrgx(ydh, lstError, lstWarn);
        setLog("天然更新情况调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckYdbh(ydh, lstError, lstWarn);
        setLog("复查期内样地变化情况调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckWclzld(ydh, lstError, lstWarn);
        setLog("未成林造林地调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckTrdc(ydh, lstError, lstWarn);
        setLog("土壤调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckKlwdc(ydh, lstError, lstWarn);
        setLog("枯落物调查记录", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckOthers(ydh, lstError, lstWarn);
        setLog("其他", lstError, lstWarn);
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

            int[] ii = YangdiMgr.GetDczt(ydh);
            int dl = YangdiMgr.GetBqdl(ydh);
            boolean b = Qianqimgr.IsYangfang(ydh);

            lstError.clear();
            lstWarn.clear();
            if (ii[0] != 2) {
                lstError.add("该项尚未完成！");
                setLog("卡片封面", lstError, lstWarn);
            } else {
                setLog("卡片封面", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            if (ii[1] != 2) {
                lstError.add("样地引点位置图尚未完成！");
            }
            if (ii[2] != 2) {
                lstError.add("样地位置图尚未完成！");
            }
            if (ii[3] != 2) {
                lstError.add("样地引线测量记录尚未完成！");
            }
            if (ii[4] != 2) {
                lstError.add("样地周界测量记录尚未完成！");
            }
            if (YangdiMgr.GetYdloc(ydh) == null) {
                lstError.add("尚未测量样地坐标！");
            }
            setLog("样地定位与测设", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            if (dl <= 0) {
                lstError.add("没有填写有效的地类！");
                setLog("样地调查", lstError, lstWarn);
            }

            dlg = new ProgressDialog(context);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在初始化数据...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setLog("样地因子调查记录", lstError, lstWarn);

            int dl = YangdiMgr.GetBqdl(ydh);
            boolean b = Qianqimgr.IsYangfang(ydh);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
            setLog("跨角林调查记录", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
            setLog("每木检尺记录", lstError, lstWarn);

            //lst.clear();
            //if(ii[8] != 2)
            //{
            //	lst.add("该项尚未完成！");
            //	setLog(lst, "样木位置示意图", false);
            //}

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckSgcl(ydh, lstError, lstWarn);
            setLog("树(毛竹)高测量记录", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckSlzh(ydh, lstError, lstWarn);
            setLog("森林灾害情况调查记录", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetZbCount(ydh) == 0) {
                lstError.add("该项尚未录入！");
                setLog("植被调查记录", lstError, lstWarn);
            } else {
                setLog("植被调查记录", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetXmCount(ydh) == 0) {
                lstError.add("该项尚未录入！");
                setLog("下木调查记录", lstError, lstWarn);
            } else {
                setLog("下木调查记录", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckTrgx(ydh, lstError, lstWarn);
            setLog("天然更新情况调查记录", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckYdbh(ydh, lstError, lstWarn);
            setLog("复查期内样地变化情况调查记录", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckWclzld(ydh, lstError, lstWarn);
            setLog("未成林造林地调查记录", lstError, lstWarn);

            dlg.cancel();
        }
    }
}
