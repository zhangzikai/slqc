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
        setTitle("���ݼ��");
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
            lstError.add("������δ��ɣ�");
            setLog("��Ƭ����", lstError, lstWarn);
        } else {
            setLog("��Ƭ����", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        if (ii[1] != 2) {
            lstError.add("��������λ��ͼ��δ��ɣ�");
        }
        if (ii[2] != 2) {
            lstError.add("����λ��ͼ��δ��ɣ�");
        }
        if (ii[3] != 2) {
            lstError.add("�������߲�����¼��δ��ɣ�");
        }
        if (ii[4] != 2) {
            lstError.add("�����ܽ������¼��δ��ɣ�");
        }
        //if(YangdiMgr.GetYdloc(ydh) == null)
        //{
        //	lstError.add("��δ�����������꣡");
        //}
        setLog("���ض�λ�����", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        if (dl <= 0) {
            lstError.add("û����д��Ч�ĵ��࣡");
            setLog("���ص���", lstError, lstWarn);
            return;
        }

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckYdyz(ydh, lstError, lstWarn);
        setLog("�������ӵ����¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
        setLog("����ֵ����¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
        setLog("ÿľ��߼�¼", lstError, lstWarn);

        //lst.clear();
        //if(ii[8] != 2)
        //{
        //	lst.add("������δ��ɣ�");
        //	setLog(lst, "��ľλ��ʾ��ͼ", false);
        //}

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckSgcl(ydh, lstError, lstWarn);
        setLog("��(ë��)�߲�����¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckSlzh(ydh, lstError, lstWarn);
        setLog("ɭ���ֺ���������¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetZbCount(ydh) == 0) {
            lstWarn.add("������δ¼�룡");
            setLog("ֲ�������¼", lstError, lstWarn);
        } else {
            setLog("ֲ�������¼", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetXmCount(ydh) == 0) {
            lstWarn.add("������δ¼�룡");
            setLog("��ľ�����¼", lstError, lstWarn);
        } else {
            setLog("��ľ�����¼", lstError, lstWarn);
        }

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckTrgx(ydh, lstError, lstWarn);
        setLog("��Ȼ������������¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckYdbh(ydh, lstError, lstWarn);
        setLog("�����������ر仯��������¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckWclzld(ydh, lstError, lstWarn);
        setLog("δ�������ֵص����¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckTrdc(ydh, lstError, lstWarn);
        setLog("���������¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckKlwdc(ydh, lstError, lstWarn);
        setLog("����������¼", lstError, lstWarn);

        lstError.clear();
        lstWarn.clear();
        YangdiMgr.CheckOthers(ydh, lstError, lstWarn);
        setLog("����", lstError, lstWarn);
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
            SpannableString text = new SpannableString("���ͨ����\n");
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
                lstError.add("������δ��ɣ�");
                setLog("��Ƭ����", lstError, lstWarn);
            } else {
                setLog("��Ƭ����", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            if (ii[1] != 2) {
                lstError.add("��������λ��ͼ��δ��ɣ�");
            }
            if (ii[2] != 2) {
                lstError.add("����λ��ͼ��δ��ɣ�");
            }
            if (ii[3] != 2) {
                lstError.add("�������߲�����¼��δ��ɣ�");
            }
            if (ii[4] != 2) {
                lstError.add("�����ܽ������¼��δ��ɣ�");
            }
            if (YangdiMgr.GetYdloc(ydh) == null) {
                lstError.add("��δ�����������꣡");
            }
            setLog("���ض�λ�����", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            if (dl <= 0) {
                lstError.add("û����д��Ч�ĵ��࣡");
                setLog("���ص���", lstError, lstWarn);
            }

            dlg = new ProgressDialog(context);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("���ڳ�ʼ������...");
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
            setLog("�������ӵ����¼", lstError, lstWarn);

            int dl = YangdiMgr.GetBqdl(ydh);
            boolean b = Qianqimgr.IsYangfang(ydh);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckKjl(ydh, lstError, lstWarn);
            setLog("����ֵ����¼", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
            setLog("ÿľ��߼�¼", lstError, lstWarn);

            //lst.clear();
            //if(ii[8] != 2)
            //{
            //	lst.add("������δ��ɣ�");
            //	setLog(lst, "��ľλ��ʾ��ͼ", false);
            //}

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckSgcl(ydh, lstError, lstWarn);
            setLog("��(ë��)�߲�����¼", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckSlzh(ydh, lstError, lstWarn);
            setLog("ɭ���ֺ���������¼", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetZbCount(ydh) == 0) {
                lstError.add("������δ¼�룡");
                setLog("ֲ�������¼", lstError, lstWarn);
            } else {
                setLog("ֲ�������¼", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            if (b && YangdiMgr.IsNeedYangfang(dl) && YangdiMgr.GetXmCount(ydh) == 0) {
                lstError.add("������δ¼�룡");
                setLog("��ľ�����¼", lstError, lstWarn);
            } else {
                setLog("��ľ�����¼", lstError, lstWarn);
            }

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckTrgx(ydh, lstError, lstWarn);
            setLog("��Ȼ������������¼", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckYdbh(ydh, lstError, lstWarn);
            setLog("�����������ر仯��������¼", lstError, lstWarn);

            lstError.clear();
            lstWarn.clear();
            YangdiMgr.CheckWclzld(ydh, lstError, lstWarn);
            setLog("δ�������ֵص����¼", lstError, lstWarn);

            dlg.cancel();
        }
    }
}
