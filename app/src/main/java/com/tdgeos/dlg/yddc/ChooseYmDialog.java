package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Sgcl;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

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
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseYmDialog extends Dialog implements View.OnClickListener {
    private Context context = null;
    private Handler mHandler;
    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;

    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private List<Sgcl> result = null;

    public ChooseYmDialog(Context context, int ydh) {
        super(context);
        this.context = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_sgcl_chooseym);
        this.setTitle("平均样木选择");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
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

        layInflater = LayoutInflater.from(context);
        layList = (LinearLayout) findViewById(R.id.lay_list);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        loadList();
    }

    public List<Sgcl> showDialog() {
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

    private void loadList() {
        List<YmhXj> lst1 = new ArrayList<YmhXj>();
        float pjxj = YangdiMgr.GetPjxj(ydh);
        List<Yangmu> yms = YangdiMgr.GetYangmus(ydh);
        for (int i = 0; i < yms.size(); i++) {
            Yangmu ym = yms.get(i);
            if (YangdiMgr.IsYssz(ydh, ym.ymh)) {
                float xj = pjxj - ym.bqxj;
                if (xj < 0) xj = -xj;
                YmhXj yx = new YmhXj();
                yx.ymh = ym.ymh;
                yx.xj = xj;
                lst1.add(yx);
            }
        }

        List<YmhXj> lst2 = new ArrayList<YmhXj>();
        if (lst1.size() > 0) {
            lst2.add(lst1.get(0));
            for (int i = 1; i < lst1.size(); i++) {
                if (lst1.get(i).xj >= lst2.get(lst2.size() - 1).xj) {
                    lst2.add(lst1.get(i));
                    continue;
                } else {
                    for (int j = 0; j < lst2.size(); j++) {
                        if (lst1.get(i).xj <= lst2.get(j).xj) {
                            lst2.add(j, lst1.get(i));
                            break;
                        }
                    }
                }
            }
        }

        List<Integer> lst = new ArrayList<Integer>();
        for (int i = 0; i < lst2.size(); i++) {
            if (i == 6) break;
            lst.add(lst2.get(i).ymh);
        }

        for (int i = 0; i < lst.size(); i++) {
            Yangmu ym = YangdiMgr.GetYangmu(ydh, lst.get(i));
            if (ym != null) {
                addRow(ym);
            }
        }
    }

    private void addRow(Yangmu item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_dlg_sgcl_chooseym_item, null);
        TextView tvYmh = (TextView) layRow.findViewById(R.id.tv_ymh);
        TextView tvSz = (TextView) layRow.findViewById(R.id.tv_sz);
        TextView tvXj = (TextView) layRow.findViewById(R.id.tv_xj);
        TextView tvLmlx = (TextView) layRow.findViewById(R.id.tv_lmlx);
        TextView tvJclx = (TextView) layRow.findViewById(R.id.tv_jclx);
        tvYmh.setText(String.valueOf(item.ymh));
        tvSz.setText(item.szdm + " " + item.szmc);
        tvXj.setText(String.valueOf(item.bqxj));
        tvLmlx.setText(Resmgr.GetValueByCode("lmlx", item.lmlx));
        tvJclx.setText(Resmgr.GetValueByCode("jclx", item.jclx));
        layList.addView(layRow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                result = new ArrayList<Sgcl>();
                for (int i = 0; i < layList.getChildCount(); i++) {
                    LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
                    CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
                    TextView tvYmh = (TextView) layRow.findViewById(R.id.tv_ymh);
                    if (cb.isChecked()) {
                        int ymh = -1;
                        try {
                            ymh = Integer.parseInt(tvYmh.getText().toString().trim());
                        } catch (Exception e) {
                        }
                        if (ymh > 0) {
                            Yangmu ym = YangdiMgr.GetYangmu(ydh, ymh);
                            Sgcl sgcl = new Sgcl();
                            sgcl.ymh = ym.ymh;
                            sgcl.sz = ym.szdm;
                            sgcl.xj = ym.bqxj;
                            result.add(sgcl);
                        }
                    }
                }
                this.cancel();
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
        }
    }

    class YmhXj {
        int ymh;
        float xj;
    }
}
