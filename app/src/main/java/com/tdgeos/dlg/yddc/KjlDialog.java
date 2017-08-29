package com.tdgeos.dlg.yddc;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Kjl;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class KjlDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Handler mHandler;

    private EditText etYdh = null;
    private Spinner spXh = null;
    private EditText etMjbl = null;
    private Spinner spDl = null;
    private Spinner spTdqs = null;
    private Spinner spLmqs = null;
    private Spinner spLinzh = null;
    private Spinner spQy = null;
    private EditText etYssz = null;
    private Spinner spLingz = null;
    private EditText etYbd = null;
    private EditText etPjsg = null;
    private Spinner spSlqljg = null;
    private Spinner spSzjg = null;
    private Spinner spSpljydj = null;

    private Button btnSz = null;
    private Button btnYssz = null;
    private Button btnSgcl = null;
    private Button btnLingz = null;
    private Button btnSlqljg = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private Kjl kjl = null;
    private Kjl result = null;

    private int ri_yssz = 0;
    private String rs_yssz = null;

    public KjlDialog(Context context, Kjl kjl, int ydh) {
        super(context);
        this.context = context;
        this.kjl = kjl;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slqc_dlg_kjl);
        this.setTitle("跨角林调查记录");
        this.getWindow().getAttributes().width = MyConfig.GetBigWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etYdh = (EditText) findViewById(R.id.et_ydh);
        spXh = (Spinner) findViewById(R.id.sp_xh);
        etMjbl = (EditText) findViewById(R.id.et_mjbl);
        etPjsg = (EditText) findViewById(R.id.et_pjsg);
        etYbd = (EditText) findViewById(R.id.et_ybd);
        spDl = (Spinner) findViewById(R.id.sp_dl);
        spTdqs = (Spinner) findViewById(R.id.sp_tdqs);
        spLmqs = (Spinner) findViewById(R.id.sp_lmqs);
        spLinzh = (Spinner) findViewById(R.id.sp_linzh);
        spQy = (Spinner) findViewById(R.id.sp_qy);
        etYssz = (EditText) findViewById(R.id.et_yssz);
        spLingz = (Spinner) findViewById(R.id.sp_lingz);
        spSlqljg = (Spinner) findViewById(R.id.sp_slqljg);
        spSzjg = (Spinner) findViewById(R.id.sp_szjg);
        spSpljydj = (Spinner) findViewById(R.id.sp_spljydj);

        etYdh.setText(String.valueOf(ydh));
        etYdh.setEnabled(false);
        etYssz.setEnabled(false);

        etMjbl.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etYbd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPjsg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        List<String> lstXh = new ArrayList<String>();
        lstXh.add("");
        lstXh.add("1");
        lstXh.add("2");
        lstXh.add("3");
        ArrayAdapter<String> apXh = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstXh);
        apXh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spXh.setAdapter(apXh);
        spXh.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int yssz = YangdiMgr.GetDefaultKjlYssz(ydh, arg2);
                if (yssz > 0 && etYssz.getText().toString().equals(""))
                    etYssz.setText(yssz + " " + Resmgr.GetSzName(yssz));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        List<String> lstDl = Resmgr.GetValueList("dl");
        ArrayAdapter<String> apDl = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstDl);
        apDl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDl.setAdapter(apDl);

        List<String> lstTdqs = Resmgr.GetValueList("tdqs");
        ArrayAdapter<String> apTdqs = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstTdqs);
        apTdqs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTdqs.setAdapter(apTdqs);

        List<String> lstLmqs = Resmgr.GetValueList("lmqs");
        ArrayAdapter<String> apLmqs = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstLmqs);
        apLmqs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLmqs.setAdapter(apLmqs);

        List<String> lstLinzh = Resmgr.GetValueList("linzh");
        ArrayAdapter<String> apLinzh = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstLinzh);
        apLinzh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLinzh.setAdapter(apLinzh);

        List<String> lstQy = Resmgr.GetValueList("qy");
        ArrayAdapter<String> apQy = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstQy);
        apQy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQy.setAdapter(apQy);

        List<String> lstLingz = Resmgr.GetValueList("lingz");
        ArrayAdapter<String> apLingz = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstLingz);
        apLingz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLingz.setAdapter(apLingz);

        List<String> lstSlqljg = Resmgr.GetValueList("slqljg");
        ArrayAdapter<String> apSlqljg = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstSlqljg);
        apSlqljg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlqljg.setAdapter(apSlqljg);

        List<String> lstSzjg = Resmgr.GetValueList("szjg");
        ArrayAdapter<String> apSzjg = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstSzjg);
        apSzjg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSzjg.setAdapter(apSzjg);

        List<String> lstSpljydj = Resmgr.GetValueList("spljydj");
        ArrayAdapter<String> apSpljydj = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lstSpljydj);
        apSpljydj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpljydj.setAdapter(apSpljydj);

        etMjbl.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_KJL_MJBL && f <= YangdiMgr.MAX_KJL_MJBL) {
                    etMjbl.setTextColor(Color.BLACK);
                } else {
                    etMjbl.setTextColor(Color.RED);
                }
            }
        });

        etYbd.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_YBD && f <= YangdiMgr.MAX_YBD) {
                    etYbd.setTextColor(Color.BLACK);
                } else {
                    etYbd.setTextColor(Color.RED);
                }
            }
        });

        etPjsg.addTextChangedListener(new TextWatcher() {
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
                if (f >= YangdiMgr.MIN_SG && f <= YangdiMgr.MAX_SG) {
                    etPjsg.setTextColor(Color.BLACK);
                } else {
                    etPjsg.setTextColor(Color.RED);
                }
            }
        });

        if (kjl != null) {
            etYdh.setText(String.valueOf(ydh));
            if (kjl.mjbl > 0) etMjbl.setText(String.valueOf(kjl.mjbl));
            if (kjl.pjsg > 0) etPjsg.setText(String.valueOf(kjl.pjsg));
            if (kjl.ybd > 0) etYbd.setText(String.valueOf(kjl.ybd));

            if (kjl.yssz > 0)
                etYssz.setText(String.valueOf(kjl.yssz) + " " + Resmgr.GetSzName(kjl.yssz));

            spXh.setSelection(kjl.xh);
            spDl.setSelection(Resmgr.GetPosByCode("dl", kjl.dl));
            spTdqs.setSelection(Resmgr.GetPosByCode("tdqs", kjl.tdqs));
            spLmqs.setSelection(Resmgr.GetPosByCode("lmqs", kjl.lmqs));
            spLinzh.setSelection(Resmgr.GetPosByCode("linzh", kjl.linzh));
            spQy.setSelection(Resmgr.GetPosByCode("qy", kjl.qy));
            spLingz.setSelection(Resmgr.GetPosByCode("lingz", kjl.lingz));
            spSlqljg.setSelection(Resmgr.GetPosByCode("slqljg", kjl.slqljg));
            spSzjg.setSelection(Resmgr.GetPosByCode("szjg", kjl.szjg));
            spSpljydj.setSelection(Resmgr.GetPosByCode("spljydj", kjl.spljydj));
        }

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSz = (Button) findViewById(R.id.btn_sz);
        btnSz.setOnClickListener(this);
        btnSz.setVisibility(8);
        btnLingz = (Button) findViewById(R.id.btn_lingz);
        btnSlqljg = (Button) findViewById(R.id.btn_slqljg);
        btnLingz.setOnClickListener(this);
        btnSlqljg.setOnClickListener(this);
        btnYssz = (Button) findViewById(R.id.btn_yssz);
        btnYssz.setOnClickListener(this);
        btnSgcl = (Button) findViewById(R.id.btn_sgcl);
        btnSgcl.setOnClickListener(this);
    }

    public Kjl showDialog() {
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
                    etYssz.setText(str);
                }
                break;
            }
            case R.id.btn_yssz: {
                int xh = spXh.getSelectedItemPosition();
                if (xh == 0) {
                    Toast.makeText(context, "请先指定一个跨角地类序号！", 1).show();
                    break;
                }
                YsszKjlDialog dlg = new YsszKjlDialog(context, ydh, xh);
                String[] ss = dlg.showDialog();
                if (ss != null) {
                    try {
                        ri_yssz = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    rs_yssz = ss[1];
                    if (ri_yssz > 0) etYssz.setText(ri_yssz + " " + Resmgr.GetSzName(ri_yssz));
                }
                break;
            }
            case R.id.btn_sgcl: {
                int xh = spXh.getSelectedItemPosition();
                if (xh == 0) {
                    Toast.makeText(context, "请先指定一个跨角地类序号！", 1).show();
                    break;
                }
                if (YangdiMgr.GetKjlYssz(ydh, xh) <= 0) {
                    Toast.makeText(context, "尚未设置该跨角林的优势树种！", 1).show();
                    break;
                }
                KjlSgclDialog dlg = new KjlSgclDialog(context, ydh, xh);
                dlg.showDialog();
                float sg = YangdiMgr.GetKjlPjsg(ydh, xh);
                if (sg > 0) etPjsg.setText(String.valueOf(sg));
                else etPjsg.setText("");
                break;
            }
            case R.id.btn_lingz: {
                PdbzDialog dlg = new PdbzDialog(context, PdbzDialog.TYPE_LINGZ);
                dlg.show();
                break;
            }
            case R.id.btn_slqljg: {
                PdbzDialog dlg = new PdbzDialog(context, PdbzDialog.TYPE_SLQLJG);
                dlg.show();
                break;
            }
            case R.id.btn_ok: {
                String s_xh = spXh.getSelectedItem().toString();
                String s_mjbl = etMjbl.getText().toString();
                String s_dl = spDl.getSelectedItem().toString();
                String s_tdqs = spTdqs.getSelectedItem().toString();
                String s_lmqs = spLmqs.getSelectedItem().toString();
                String s_linzh = spLinzh.getSelectedItem().toString();
                String s_qy = spQy.getSelectedItem().toString();
                String s_yssz = etYssz.getText().toString();
                String s_lingz = spLingz.getSelectedItem().toString();
                String s_ybd = etYbd.getText().toString();
                String s_pjsg = etPjsg.getText().toString();
                String s_slqljg = spSlqljg.getSelectedItem().toString();
                String s_szjg = spSzjg.getSelectedItem().toString();
                String s_spljydj = spSpljydj.getSelectedItem().toString();
                int i_xh = -1;
                float f_mjbl = -1;
                int i_dl = -1;
                int i_tdqs = -1;
                int i_lmqs = -1;
                int i_linzh = -1;
                int i_qy = -1;
                int i_yssz = -1;
                int i_lingz = -1;
                float f_ybd = -1;
                float f_pjsg = -1;
                int i_slqljg = -1;
                int i_szjg = -1;
                int i_spljydj = -1;

                if (s_xh.equals("")) {
                    Toast.makeText(context, "跨角地类序号不能为空！", 1).show();
                    break;
                }
            /*
            if(s_mjbl.equals(""))
			{
				Toast.makeText(context, "面积比例不能为空！", 1).show();
				break;
			}
			if(s_dl.equals(""))
			{
				Toast.makeText(context, "地类不能为空！", 1).show();
				break;
			}
			if(s_tdqs.equals(""))
			{
				Toast.makeText(context, "土地权属不能为空！", 1).show();
				break;
			}
			if(s_lmqs.equals(""))
			{
				Toast.makeText(context, "林木权属不能为空！", 1).show();
				break;
			}
			if(s_linzh.equals(""))
			{
				Toast.makeText(context, "林种不能为空！", 1).show();
				break;
			}
			if(s_qy.equals(""))
			{
				Toast.makeText(context, "起源不能为空！", 1).show();
				break;
			}
			if(s_yssz.equals(""))
			{
				Toast.makeText(context, "优势树种不能为空！", 1).show();
				break;
			}
			if(s_lingz.equals(""))
			{
				Toast.makeText(context, "龄组不能为空！", 1).show();
				break;
			}
			if(s_ybd.equals(""))
			{
				Toast.makeText(context, "郁闭度不能为空！", 1).show();
				break;
			}
			if(s_pjsg.equals(""))
			{
				Toast.makeText(context, " 平均树高不能为空！", 1).show();
				break;
			}
			if(s_slqljg.equals(""))
			{
				Toast.makeText(context, "森林群落结构不能为空！", 1).show();
				break;
			}
			if(s_szjg.equals(""))
			{
				Toast.makeText(context, "树种结构不能为空！", 1).show();
				break;
			}
			*/

                try {
                    i_xh = Integer.parseInt(s_xh);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_mjbl = Float.parseFloat(s_mjbl);
                } catch (java.lang.NumberFormatException nfe) {
                }
                i_dl = Resmgr.GetCodeByValue("dl", s_dl);
                i_tdqs = Resmgr.GetCodeByValue("tdqs", s_tdqs);
                i_lmqs = Resmgr.GetCodeByValue("lmqs", s_lmqs);
                i_linzh = Resmgr.GetCodeByValue("linzh", s_linzh);
                i_qy = Resmgr.GetCodeByValue("qy", s_qy);
                i_lingz = Resmgr.GetCodeByValue("lingz", s_lingz);
                try {
                    f_ybd = Float.parseFloat(s_ybd);
                } catch (java.lang.NumberFormatException nfe) {
                }
                try {
                    f_pjsg = Float.parseFloat(s_pjsg);
                } catch (java.lang.NumberFormatException nfe) {
                }
                i_slqljg = Resmgr.GetCodeByValue("slqljg", s_slqljg);
                i_szjg = Resmgr.GetCodeByValue("szjg", s_szjg);
                i_spljydj = Resmgr.GetCodeByValue("spljydj", s_spljydj);

                String[] ss = s_yssz.split(" ");
                int szdm = -1;
                String szmc = null;
                if (ss.length == 1) {
                    try {
                        szdm = Integer.parseInt(s_yssz);
                    } catch (Exception e) {
                    }
                    if (szdm > 0) {
                        szmc = Resmgr.GetSzName(szdm);
                        if (szmc.equals("")) {
                            szdm = -1;
                            //Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            //break;
                        }
                    } else {
                        szmc = s_yssz;
                        szdm = Resmgr.GetSzCode(szmc);
                        if (szdm <= 0) {
                            szmc = "";
                            //Toast.makeText(context, "树种填写错误，无法匹配该树种的代码！", 1).show();
                            //break;
                        }
                    }
                } else if (ss.length == 2) {
                    try {
                        szdm = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    if (szdm <= 0) {
                        szmc = "";
                        //Toast.makeText(context, "树种填写错误！", 1).show();
                        //break;
                    } else {
                        szmc = ss[1];
                        if (szdm != Resmgr.GetSzCode(szmc)) {
                            szdm = -1;
                            szmc = "";
                            //Toast.makeText(context, "树种填写错误，代码和名称不一致！", 1).show();
                            //break;
                        }
                    }
                } else {
                    szdm = -1;
                    szmc = "";
                    //Toast.makeText(context, "优势树种填写错误！", 1).show();
                    //break;
                }
                i_yssz = szdm;

			/*
            if(f_mjbl <= YangdiMgr.MIN_KJL_MJBL || f_mjbl > YangdiMgr.MAX_KJL_MJBL)
			{
				Toast.makeText(context, "面积比例超限！", 1).show();
				break;
			}
			if(i_dl != 111 && i_dl != 120)
			{
				Toast.makeText(context, "地类填写错误，必须是乔木林或疏林地！", 1).show();
				break;
			}
			if(f_ybd <= YangdiMgr.MIN_YBD || f_ybd > YangdiMgr.MAX_YBD)
			{
				Toast.makeText(context, "郁闭度超限！", 1).show();
				break;
			}
			if(i_dl == 111 || i_dl == 113 || i_dl == 1131 || i_dl == 1132 || i_dl == 1133)
			{
				if(f_ybd < 0.2 || f_ybd > 1)
				{
					Toast.makeText(context, "郁闭度填写错误，乔木林和竹林郁闭度应大于0.2！", 1).show();
					break;
				}
			}
			if(i_dl == 120)
			{
				if(f_ybd < 0.1 || f_ybd >= 0.2)
				{
					Toast.makeText(context, "郁闭度填写错误，疏林地郁闭度应小于0.2！", 1).show();
					break;
				}
			}
			if(f_pjsg <= YangdiMgr.MIN_SG || f_pjsg > YangdiMgr.MAX_SG)
			{
				Toast.makeText(context, "平均树高填写错误！", 1).show();
				break;
			}
			if(i_linzh < 200 && i_spljydj > 0)
			{
				Toast.makeText(context, "林种为公益林时商品林经营等级不能填写！", 1).show();
				break;
			}
			if(i_linzh > 200 && i_spljydj <= 0)
			{
				Toast.makeText(context, "林种为商品林时商品林经营等级不能为空！", 1).show();
				break;
			}
			*/

                if (kjl == null || kjl.xh != i_xh) {
                    if (YangdiMgr.IsHasKjlxh(ydh, i_xh)) {
                        Toast.makeText(context, "跨角地类序号已经存在！", 1).show();
                        break;
                    }
                }

                result = new Kjl();
                result.xh = i_xh;
                result.mjbl = f_mjbl;
                result.dl = i_dl;
                result.tdqs = i_tdqs;
                result.lmqs = i_lmqs;
                result.linzh = i_linzh;
                result.qy = i_qy;
                result.yssz = i_yssz;
                result.lingz = i_lingz;
                result.ybd = f_ybd;
                result.pjsg = f_pjsg;
                result.slqljg = i_slqljg;
                result.szjg = i_szjg;
                result.spljydj = i_spljydj;

                //YangdiMgr.SetKjlYssz(ydh, i_xh, ri_yssz, rs_yssz);

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
