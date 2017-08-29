package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

public class YsszDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler = null;

    private EditText etSz = null;
    private LinearLayout layList = null;
    private LayoutInflater layInflater = null;
    private MyCBListener cbListener = null;

    private Button btnSz = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private String result = null;

    public YsszDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
        layInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_yssz);
        this.setTitle("优势树种");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        layList = (LinearLayout) findViewById(R.id.lay_list);

        etSz = (EditText) findViewById(R.id.et_yssz);
        btnSz = (Button) findViewById(R.id.btn_sz);
        btnSz.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        cbListener = new MyCBListener();

        loadList();
    }

    private void loadList() {
        List<String> lstSz = YangdiMgr.GetShuzhong(ydh);
        if (lstSz.size() == 0) return;

        List<SzItem> lst = new ArrayList<SzItem>();
        float zmj = 0;
        for (int i = 0; i < lstSz.size(); i++) {
            int code = Resmgr.GetSzCode(lstSz.get(i));
            int count = YangdiMgr.GetZhushu(ydh, code);
            float pjxj = YangdiMgr.GetPjxj(ydh, code);
            float dmj = YangdiMgr.GetDmj(ydh, code);
            zmj += dmj;
            SzItem item = new SzItem();
            item.name = lstSz.get(i);
            item.count = count;
            item.xj = pjxj;
            item.dmj = dmj;
            lst.add(item);
        }
        for (int i = 0; i < lst.size(); i++) {
            lst.get(i).bl = lst.get(i).dmj / zmj * 100;
            lst.get(i).bl = MyFuns.MyDecimal(lst.get(i).bl, 2);
        }

        List<SzItem> tmp = new ArrayList<SzItem>();
        tmp.add(lst.get(0));
        for (int i = 1; i < lst.size(); i++) {
            if (lst.get(i).dmj <= tmp.get(tmp.size() - 1).dmj) {
                tmp.add(lst.get(i));
                continue;
            } else {
                for (int j = 0; j < tmp.size(); j++) {
                    if (lst.get(i).dmj >= tmp.get(j).dmj) {
                        tmp.add(j, lst.get(i));
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < tmp.size(); i++) {
            LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_dlg_yssz_item, null);
            CheckBox cbPos = (CheckBox) layRow.findViewById(R.id.cb_pos);
            TextView tvSz = (TextView) layRow.findViewById(R.id.tv_shuzhong);
            TextView tvZs = (TextView) layRow.findViewById(R.id.tv_zhushu);
            TextView tvXj = (TextView) layRow.findViewById(R.id.tv_pingjun);
            TextView tvMj = (TextView) layRow.findViewById(R.id.tv_zong);
            TextView tvBl = (TextView) layRow.findViewById(R.id.tv_bili);
            int szdm = Resmgr.GetSzCode(tmp.get(i).name);
            //layRow.setTag(szdm + " " + tmp.get(i).name);
            //layRow.setOnClickListener(this);
            tvSz.setText(tmp.get(i).name);
            tvZs.setText(String.valueOf(tmp.get(i).count));
            tvXj.setText(String.valueOf(tmp.get(i).xj));
            tvMj.setText(String.valueOf(tmp.get(i).dmj));
            tvBl.setText(tmp.get(i).bl + "%");
            cbPos.setOnCheckedChangeListener(cbListener);
            cbPos.setTag(szdm);
            layList.addView(layRow);
        }
    }

    public String showDialog() {
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
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sz: {
                SzxzDialog dlg = new SzxzDialog(context);
                String str = dlg.showDialog();
                if (str != null) {
                    etSz.setText(str);
                }
                break;
            }
            case R.id.btn_ok: {
                String str = etSz.getText().toString().trim();
                if (str.equals("")) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "没有指定优势树种，是否要清除原有优势树种的设置？", "清除", "取消");
                    if (dlg.showDialog()) {
                        YangdiMgr.SetYssz(ydh, "");
                        result = "";
                        YsszDialog.this.cancel();
                        break;
                    } else {
                        break;
                    }
                }
                String[] ss = str.split(" ");
                int dm = -1;
                String mc = null;
                if (ss.length == 1) {
                    try {
                        dm = Integer.parseInt(str);
                    } catch (Exception e) {
                    }
                    if (dm > 0) {
                        mc = Resmgr.GetSzName(dm);
                        if (mc.equals("")) {
                            Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            break;
                        }
                    } else {
                        mc = str;
                        dm = Resmgr.GetSzCode(mc);
                        if (dm <= 0) {
                            Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            break;
                        }
                    }
                } else if (ss.length == 2) {
                    try {
                        dm = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    if (dm <= 0) {
                        Toast.makeText(context, "树种填写错误！", 1).show();
                        break;
                    }
                    mc = ss[1];
                    if (dm != Resmgr.GetSzCode(mc)) {
                        Toast.makeText(context, "树种填写错误，代码和名称不一致！", 1).show();
                        break;
                    }
                } else {
                    Toast.makeText(context, "树种填写错误，无法识别的格式！", 1).show();
                    break;
                }

                String szs = "";
                for (int i = 0; i < layList.getChildCount(); i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cbPos = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    int szdm = (Integer) cbPos.getTag();
                    if (cbPos.isChecked()) {
                        if (szs.equals("")) szs = String.valueOf(szdm);
                        else szs = szs + "," + szdm;
                    }
                }

                YangdiMgr.SetYssz(ydh, szs);

                result = dm + " " + mc;

                if (result.equals("")) result = null;
                YsszDialog.this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                YsszDialog.this.cancel();
                break;
            }
            default: {
                try {
                    String str = (String) v.getTag();
                    etSz.setText(str);
                } catch (Exception e) {
                }
            }
        }
    }

    class MyCBListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            boolean bZhenye = false;
            boolean bKuoye = false;
            int count = 0;
            int dm = -1;
            for (int i = 0; i < layList.getChildCount(); i++) {
                LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                CheckBox cbPos = (CheckBox) layRow.findViewById(R.id.cb_pos);
                int szdm = (Integer) cbPos.getTag();
                if (cbPos.isChecked()) {
                    count++;
                    dm = szdm;
                    if (Resmgr.IsKuoye(szdm)) bKuoye = true;
                    if (Resmgr.IsZhenye(szdm)) bZhenye = true;
                }
            }
            if (count == 0) {
                etSz.setText("");
            }
            if (count == 1) {
                String mc = Resmgr.GetSzName(dm);
                etSz.setText(dm + " " + mc);
            }
            if (count > 1) {
                if (bKuoye && bZhenye) {
                    String name = "针阔混";
                    int code = Resmgr.GetSzCode(name);
                    etSz.setText(code + " " + name);
                }
                if (bKuoye && !bZhenye) {
                    String name = "阔叶混";
                    int code = Resmgr.GetSzCode(name);
                    etSz.setText(code + " " + name);
                }
                if (!bKuoye && bZhenye) {
                    String name = "针叶混";
                    int code = Resmgr.GetSzCode(name);
                    etSz.setText(code + " " + name);
                }
            }
        }
    }

    class SzItem {
        public String name;
        public int count;
        public float xj;
        public float dmj;
        public float bl;
    }
}
