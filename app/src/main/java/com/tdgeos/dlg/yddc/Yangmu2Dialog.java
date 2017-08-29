package com.tdgeos.dlg.yddc;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

public class Yangmu2Dialog extends Dialog implements View.OnClickListener {
    private Handler myHandler = null;
    private Context context;

    private int ydh = 0;
    private Yangmu yangmu = null;

    private int iCkd = 1;
    private int iTake = 0;

    private TextView tvBqxj = null;

    private Spinner spCkd = null;
    private Spinner spYmlx = null;
    private EditText etYmh = null;
    private Spinner spLmlx = null;
    private Spinner spCjlx = null;
    private EditText etSzmc = null;
    private EditText etQqxj = null;
    private EditText etBqxj = null;
    private Spinner spCfgllx = null;
    private Spinner spLc = null;
    private Spinner spKjdlxh = null;
    private EditText etFwj = null;
    private EditText etSpj = null;
    private EditText etBeizhu = null;

    private Button btnSzmc = null;
    private Button btnTake = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private Button btnPrev = null;
    private Button btnNext = null;

    private ListView listView;
    private PopupWindow pop;

    public Yangmu2Dialog(Context context, Handler handler, int ydh) {
        super(context);
        this.context = context;
        myHandler = handler;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_ym);
        this.setTitle("样木");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                myHandler.sendEmptyMessage(100);
            }
        });

        listView = new ListView(context);

        spCkd = (Spinner) findViewById(R.id.sp_ckd);
        List<String> lstCkd = Resmgr.GetValueList("ckd");
        ArrayAdapter<String> apCkd = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstCkd);
        apCkd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCkd.setAdapter(apCkd);
        spCkd.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                iCkd = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spCkd.setSelection(iCkd);

        spYmlx = (Spinner) findViewById(R.id.sp_ymlx);
        List<String> lstYmlx = Resmgr.GetValueList("szlx");
        ArrayAdapter<String> apYmlx = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstYmlx);
        apYmlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYmlx.setAdapter(apYmlx);

        tvBqxj = (TextView) findViewById(R.id.tv_bqxj);

        etYmh = (EditText) findViewById(R.id.et_ymh);
        spLmlx = (Spinner) findViewById(R.id.sp_lmlx);
        spCjlx = (Spinner) findViewById(R.id.sp_cjlx);
        etSzmc = (EditText) findViewById(R.id.et_szmc);
        etQqxj = (EditText) findViewById(R.id.et_qqxj);
        etBqxj = (EditText) findViewById(R.id.et_bqxj);
        spCfgllx = (Spinner) findViewById(R.id.sp_cfgllx);
        spLc = (Spinner) findViewById(R.id.sp_lc);
        spKjdlxh = (Spinner) findViewById(R.id.sp_kjdlxh);
        etFwj = (EditText) findViewById(R.id.et_fwj);
        etSpj = (EditText) findViewById(R.id.et_spj);
        etBeizhu = (EditText) findViewById(R.id.et_bz);

        etQqxj.setEnabled(false);

        List<String> lstXh = Resmgr.GetValueList("kjdlxh");
        ArrayAdapter<String> apXh = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstXh);
        apXh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKjdlxh.setAdapter(apXh);

        List<String> lstLmlx = Resmgr.GetValueList("lmlx");
        ArrayAdapter<String> apLmlx = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstLmlx);
        apLmlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLmlx.setAdapter(apLmlx);

        List<String> lstCjlx = Resmgr.GetValueList("jclx");
        ArrayAdapter<String> apCjlx = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstCjlx);
        apCjlx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCjlx.setAdapter(apCjlx);

        List<String> lstCfgllx = Resmgr.GetValueList("cfgllx");
        ArrayAdapter<String> apCfgllx = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstCfgllx);
        apCfgllx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCfgllx.setAdapter(apCfgllx);

        List<String> lstLc = Resmgr.GetValueList("ymlcjg");
        ArrayAdapter<String> apLc = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstLc);
        apLc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLc.setAdapter(apLc);

        spCjlx.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String s_ymh = etYmh.getText().toString();
                if (s_ymh.equals("")) return;
                int i_ymh = -1;
                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (Exception e) {
                }
                if (i_ymh < 0) return;

                String s_jclx = spCjlx.getSelectedItem().toString();
                int jclx = Resmgr.GetCodeByValue("jclx", s_jclx);
                if (jclx == 13 || jclx == 14 || jclx == 15 || jclx == 17 || jclx == 24) {
                    Yangmu ym = YangdiMgr.GetQqYangmu(ydh, i_ymh);
                    if (ym != null) {
                        etBqxj.setText(String.valueOf(ym.bqxj));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        etSzmc.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (pop == null) {
                        pop = new PopupWindow(listView, etSzmc.getWidth(), LayoutParams.WRAP_CONTENT);
                        pop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                        pop.setOutsideTouchable(true);
                    }
                    pop.showAsDropDown(etSzmc);
                } else {
                    if (pop != null) pop.dismiss();
                }
            }
        });

        etSzmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop == null) {
                    pop = new PopupWindow(listView, etSzmc.getWidth(), LayoutParams.WRAP_CONTENT);
                    pop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                    pop.setOutsideTouchable(true);
                }
                pop.showAsDropDown(etSzmc);
            }
        });

        btnSzmc = (Button) findViewById(R.id.btn_szmc);
        btnSzmc.setOnClickListener(this);
        btnTake = (Button) findViewById(R.id.btn_take);
        btnTake.setOnClickListener(this);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        etBqxj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_YM_XJ && f <= YangdiMgr.MAX_YM_XJ) {
                    etBqxj.setTextColor(Color.BLACK);
                } else {
                    etBqxj.setTextColor(Color.RED);
                }
            }
        });

        etFwj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_FWJ && f <= YangdiMgr.MAX_FWJ) {
                    etFwj.setTextColor(Color.BLACK);
                } else {
                    etFwj.setTextColor(Color.RED);
                }
            }
        });

        etSpj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                float f = -1;
                try {
                    f = Float.parseFloat(str);
                } catch (Exception e) {
                }
                if (f >= YangdiMgr.MIN_YM_SPJ && f <= YangdiMgr.MAX_YM_SPJ) {
                    etSpj.setTextColor(Color.BLACK);
                } else {
                    etSpj.setTextColor(Color.RED);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (iTake == 0) {
            setData();
        } else {
            iTake = 0;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void SetData(Yangmu ym) {
        yangmu = ym;
    }

    private void setData() {
        iCkd = 1;
        spCkd.setSelection(0);
        spYmlx.setSelection(0);
        etYmh.setText("");
        spLmlx.setSelection(0);
        spCjlx.setSelection(0);
        etSzmc.setText("");
        etQqxj.setText("");
        etBqxj.setText("");
        spCfgllx.setSelection(0);
        spLc.setSelection(1);
        spKjdlxh.setSelection(0);
        etFwj.setText("");
        etSpj.setText("");
        etBeizhu.setText("");

        etBqxj.setTextColor(Color.RED);
        tvBqxj.setTextColor(0xffff0000);

        setSZList();

        if (yangmu != null) {
            iCkd = yangmu.ckd;
            spCkd.setSelection(iCkd);
            spYmlx.setSelection(yangmu.szlx);
            if (yangmu.ymh > 0) etYmh.setText(String.valueOf(yangmu.ymh));
            spLmlx.setSelection(Resmgr.GetPosByCode("lmlx", yangmu.lmlx));
            spCjlx.setSelection(Resmgr.GetPosByCode("jclx", yangmu.jclx));
            //String szmc = yangmu.szmc == null ? "" : yangmu.szmc;
            //if(yangmu.szdm > 0) etSzmc.setText(yangmu.szdm + " " + szmc);
            if (yangmu.qqxj > 0) etQqxj.setText(String.valueOf(yangmu.qqxj));
            if (yangmu.bqxj > 0) etBqxj.setText(String.valueOf(yangmu.bqxj));
            spCfgllx.setSelection(Resmgr.GetPosByCode("cfgllx", yangmu.cfgllx));
            spLc.setSelection(Resmgr.GetPosByCode("ymlcjg", yangmu.lc));
            spKjdlxh.setSelection(yangmu.kjdlxh);
            if (yangmu.fwj >= YangdiMgr.MIN_FWJ) etFwj.setText(String.valueOf(yangmu.fwj));
            if (yangmu.spj >= YangdiMgr.MIN_YM_SPJ) etSpj.setText(String.valueOf(yangmu.spj));
            etBeizhu.setText(yangmu.bz);

            if (yangmu.szdm <= 0) {
                String qqym = Qianqimgr.GetQqYmsz(ydh, yangmu.ymh);
                if (qqym != null) {
                    etSzmc.setText(qqym);
                    etSzmc.setTextColor(Color.RED);
                } else {
                    etSzmc.setTextColor(Color.BLACK);
                }
            } else {
                //String szmc = yangmu.szmc == null ? "" : yangmu.szmc;
                //etSzmc.setText(yangmu.szdm + " " + szmc);
                etSzmc.setText(yangmu.szdm + " " + yangmu.szmc);
                etSzmc.setTextColor(Color.BLACK);
            }

            if (yangmu.bqxj < 5) {
                tvBqxj.setTextColor(Color.RED);
            } else {
                tvBqxj.setTextColor(Color.BLACK);
            }
        }

        if (yangmu == null || yangmu.ymh <= 0) {
            etYmh.setText(String.valueOf(getNextYmh()));
            spKjdlxh.setSelection(MyConfig.GetLastKjxh());
            int sz = MyConfig.GetLastSzdm();
            if (sz > 0) {
                etSzmc.setText(sz + " " + Resmgr.GetSzName(sz));
                etSzmc.setTextColor(Color.BLACK);
            }
        }

        if (spLmlx.getSelectedItemPosition() == 0) spLmlx.setSelection(1);
        if (spCjlx.getSelectedItemPosition() == 0) spCjlx.setSelection(2);
        if (spLc.getSelectedItemPosition() == 0) spLc.setSelection(1);
        if (spCfgllx.getSelectedItemPosition() == 0) spCfgllx.setSelection(1);
    }

    private void setSZList() {
        List<String> lstSz = Setmgr.GetCyszList();
        DropdownAdapter adapter = new DropdownAdapter(context, lstSz);
        listView.setAdapter(adapter);
    }

    private int getNextYmh() {
        int bqYmh = YangdiMgr.GetMaxYmh(ydh);
        int qqYmh = YangdiMgr.GetMaxYmhQq(ydh);
        bqYmh++;
        qqYmh++;
        return bqYmh > qqYmh ? bqYmh : qqYmh;
    }

    private boolean saveYangmu() {
        int zt = 1;
        String szmc = null;
        int szdm = -1;
        int ymh = -1;
        String symh = etYmh.getText().toString();
        if (symh.equals("")) {
            Toast.makeText(context, "样木号不能为空！", 1).show();
            return false;
        }
        try {
            ymh = Integer.parseInt(symh);
        } catch (java.lang.NumberFormatException nfe) {
        }
        if (ymh == 0) {
            Toast.makeText(context, "样木号填写错误！", 1).show();
            return false;
        }
        if (yangmu == null && YangdiMgr.IsHasYangmu(ydh, ymh)) {
            Toast.makeText(context, "样木号已经存在，请重新输入！", 1).show();
            return false;
        }
        if (yangmu != null && yangmu.ymh != ymh && YangdiMgr.IsHasYangmu(ydh, ymh)) {
            Toast.makeText(context, "样木号已经存在，请重新输入！", 1).show();
            return false;
        }
        Yangmu qqym = YangdiMgr.GetQqYangmu(ydh, ymh);

        int lmlx = Resmgr.GetCodeByValue("lmlx", spLmlx.getSelectedItem().toString());
        if (lmlx == -1) {
            zt = 0;
            Toast.makeText(context, "立木类型不能为空！", 1).show();
            //return false;
        }
        int jclx = Resmgr.GetCodeByValue("jclx", spCjlx.getSelectedItem().toString());
        if (jclx == -1) {
            zt = 0;
            Toast.makeText(context, "检尺类型不能为空！", 1).show();
            //return false;
        }
        if (qqym != null) {
            if (jclx == 12 || jclx == 16) {
                zt = 0;
                Toast.makeText(context, "检尺类型与前期数据冲突！", 1).show();
                //return false;
            }
        }
        if (qqym == null) {
            if (jclx != 12 && jclx != 16 && jclx != 1 && jclx != 10) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "前期数据中无此样木，是否继续按此捡尺类型保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
        }
        String str = etSzmc.getText().toString().trim();
        String[] ss = str.split(" ");
        szdm = -1;
        szmc = null;
        if (ss.length == 1) {
            try {
                szdm = Integer.parseInt(str);
            } catch (Exception e) {
            }
            if (szdm > 0) {
                szmc = Resmgr.GetSzName(szdm);
                if (szmc.equals("")) {
                    zt = 0;
                    Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                    //return false;
                }
            } else {
                szmc = str;
                szdm = Resmgr.GetSzCode(szmc);
                if (szdm <= 0) {
                    zt = 0;
                    Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                    //return false;
                }
            }
        } else if (ss.length == 2) {
            try {
                szdm = Integer.parseInt(ss[0]);
            } catch (Exception e) {
            }
            if (szdm <= 0) {
                zt = 0;
                Toast.makeText(context, "树种填写错误！", 1).show();
                //return false;
            }
            szmc = ss[1];
            if (szdm != Resmgr.GetSzCode(szmc)) {
                zt = 0;
                Toast.makeText(context, "树种填写错误，代码和名称不一致！", 1).show();
                //return false;
            }
        } else {
            zt = 0;
            Toast.makeText(context, "树种名称填写错误！", 1).show();
            //return false;
        }
        etSzmc.setTextColor(Color.BLACK);

        if (Resmgr.IsHunjiaoSz(szdm)) {
            zt = 0;
            Toast.makeText(context, "检尺样木树种名称不能填写填写混交树种！", 1).show();
            //return false;
        }

        if (qqym != null) {
            if (qqym.szdm != szdm) {
                if (jclx != 19) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该样木的本期树种与前期树种不一致，也不是'树种错测木'，是否继续保存?", "保存", "修改");
                    if (!dlg.showDialog()) {
                        return false;
                    }
                }
            }
        }
        String sqqxj = etQqxj.getText().toString();
        float qqxj = -1;
        if (!sqqxj.equals("")) {
            try {
                qqxj = Float.parseFloat(sqqxj);
            } catch (java.lang.NumberFormatException nfe) {
            }
        }

        float bqxj = -1;
        try {
            bqxj = Float.parseFloat(etBqxj.getText().toString());
            bqxj = MyFuns.MyDecimal(bqxj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }
        if (bqxj < YangdiMgr.MIN_YM_XJ || bqxj > YangdiMgr.MAX_YM_XJ) {
            zt = 0;
            Toast.makeText(context, "本期胸径超限！", 1).show();
            //return false;
        }
        if (qqym != null && bqxj > 0) {
            if (qqxj > 0 && bqxj < qqxj && jclx != 18) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该样木的本期胸径小于前期胸径，也不是'胸径错测木'，是否继续保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
        }
        if (bqxj - qqxj > 40) {
            MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该样木的本期胸径增长过大，是否继续保存?", "保存", "修改");
            if (!dlg.showDialog()) {
                return false;
            }
        }
        if (qqym != null) {
            if (jclx == 13 || jclx == 14 || jclx == 15 || jclx == 17 || jclx == 24) {
                if (qqxj != bqxj && bqxj > 0) {
                    zt = 0;
                    Toast.makeText(context, "本期胸径填写错误，应与前期胸径保持一致！", 1).show();
                    //return false;
                }
            }
        }
        if (jclx == 12) {
            float f = YangdiMgr.GetJinjiemuSize(ydh);
            if (f > 0 && bqxj > f) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该进界木的本期胸径大于前期最大的进界木胸径，是否继续保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            } else if (bqxj > 15) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该进界木的胸径过大，是否继续保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
        }
        if (qqym != null) {
            if (bqxj > qqym.bqxj + 40) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(context, "警告", "该样木的胸径增长量过大，是否继续保存?", "保存", "修改");
                if (!dlg.showDialog()) {
                    return false;
                }
            }
        }

        int cfgllx = Resmgr.GetCodeByValue("cfgllx", spCfgllx.getSelectedItem().toString());
        if (cfgllx == -1) {
            zt = 0;
            Toast.makeText(context, "采伐管理类型不能为空！", 1).show();
            //return false;
        }
        int lc = Resmgr.GetCodeByValue("ymlcjg", spLc.getSelectedItem().toString());
        if (lmlx == 11 && lc == -1) {
            zt = 0;
            Toast.makeText(context, "林层不能为空！", 1).show();
            //return false;
        }
        int kjdlxh = spKjdlxh.getSelectedItemPosition();

        String sfwj = etFwj.getText().toString();
        String sspj = etSpj.getText().toString();
        if (sfwj.equals("") || sspj.equals("")) {
            zt = 0;
            Toast.makeText(context, "样木坐标不能为空！", 1).show();
            //return false;
        }
        float fwj = 0;
        try {
            fwj = Float.parseFloat(etFwj.getText().toString());
            fwj = MyFuns.MyDecimal(fwj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }
        float spj = 0;
        try {
            spj = Float.parseFloat(etSpj.getText().toString());
            spj = MyFuns.MyDecimal(spj, 1);
        } catch (java.lang.NumberFormatException nfe) {
        }

        String bz = etBeizhu.getText().toString();
        int tag = spYmlx.getSelectedItemPosition();

        Yangmu ym = new Yangmu();
        ym.ymh = ymh;
        ym.lmlx = lmlx;
        ym.jclx = jclx;
        ym.szmc = szmc;
        ym.szdm = szdm;
        ym.qqxj = qqxj;
        ym.bqxj = bqxj;
        ym.cfgllx = cfgllx;
        ym.lc = lc;
        ym.kjdlxh = kjdlxh;
        ym.fwj = fwj;
        ym.spj = spj;
        ym.bz = bz;
        ym.ckd = iCkd;
        ym.szlx = tag;
        ym.jczt = zt;

        float dis = ym.GetErrorDis();
        if (dis > 3) {
            Toast.makeText(context, "样木坐标误差过大！", 1).show();
            return false;
        }
        if (dis > 0.1f && dis < 3) {
            MyMakeSureDialog dlg = new MyMakeSureDialog(context, "提示", "此样木位于标准样地范围外" + dis + "米处，是否仍然保存？", "保存", "修改");
            if (!dlg.showDialog()) {
                ym = null;
                return false;
            }
        }

        Setmgr.AddCysz(szdm, szmc);

        if (yangmu == null) {
            YangdiMgr.AddYangmu(ydh, ym);
            MyConfig.SetLastSzdm(szdm);
            MyConfig.SetLastKjxh(kjdlxh);
        } else {
            YangdiMgr.UpdateYangmu(ydh, ym);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_szmc: {
                SzxzDialog dlg = new SzxzDialog(context);
                String str = dlg.showDialog();
                if (str != null) {
                    etSzmc.setText(str);
                }
                break;
            }
            case R.id.btn_ok: {
                if (saveYangmu()) {
                    this.cancel();
                }
                break;
            }
            case R.id.btn_cancel: {
                this.cancel();
                break;
            }
            case R.id.btn_take: {
                String s_ymh = etYmh.getText().toString();
                int i_ymh = 0;
                try {
                    i_ymh = Integer.parseInt(s_ymh);
                } catch (Exception e) {
                }

                ZpmcDialog dlg = new ZpmcDialog(context, ydh, i_ymh);
                String[] ss = dlg.showDialog();
                if (ss != null) {
                    int picType = 1;
                    try {
                        picType = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    int picYmh = 0;
                    try {
                        picYmh = Integer.parseInt(ss[1]);
                    } catch (Exception e) {
                    }
                    String picInfo = ss[2];
                    String picPath = MyConfig.GetWorkdir() + "/temp/" + ss[3];
                    MyFile.DeleteFile(picPath);
                    iTake = 1;
                    Bundle data = new Bundle();
                    data.putInt("picType", picType);
                    data.putInt("picYmh", picYmh);
                    data.putString("picInfo", picInfo);
                    data.putString("picPath", picPath);
                    Message msg = new Message();
                    msg.what = 101;
                    msg.setData(data);
                    myHandler.sendMessage(msg);
                    this.dismiss();
                }
                break;
            }
            case R.id.btn_prev: {
                if (saveYangmu()) {
                    int i_ymh = 0;
                    try {
                        i_ymh = Integer.parseInt(etYmh.getText().toString());
                    } catch (Exception e) {
                    }
                    Yangmu ym = YangdiMgr.GetPrevYangmu(ydh, i_ymh);
                    if (ym == null) {
                        Toast.makeText(context, "已经是第一株样木！", 1).show();
                        break;
                    } else {
                        yangmu = ym;
                        setData();
                    }
                }
                break;
            }
            case R.id.btn_next: {
                if (saveYangmu()) {
                    int i_ymh = 0;
                    try {
                        i_ymh = Integer.parseInt(etYmh.getText().toString());
                    } catch (Exception e) {
                    }
                    Yangmu ym = YangdiMgr.GetNextYangmu(ydh, i_ymh);
                    if (ym == null) {
                        Toast.makeText(context, "已经是最后一株样木！", 1).show();
                        break;
                    } else {
                        yangmu = ym;
                        setData();
                    }
                }
                break;
            }
        }
    }

    class DropdownAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<String> list;
        private TextView content;
        private ImageButton close;

        public DropdownAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.slqc_cysz_item, null);
            close = (ImageButton) convertView.findViewById(R.id.close_row);
            content = (TextView) convertView.findViewById(R.id.text_row);
            final String editContent = list.get(position);
            content.setText(list.get(position).toString());
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSzmc.setText(editContent);
                    pop.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String name = list.get(position);
                    int code = Resmgr.GetSzCode(name);
                    Setmgr.DelCysz(code);
                    list.remove(position);
                    DropdownAdapter.this.notifyDataSetChanged();
                    pop.setHeight(LayoutParams.WRAP_CONTENT);
                }
            });
            return convertView;
        }
    }
}
