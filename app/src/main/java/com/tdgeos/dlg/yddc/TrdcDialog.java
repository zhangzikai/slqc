package com.tdgeos.dlg.yddc;

import java.util.List;

import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Tr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TrdcDialog extends Dialog implements View.OnClickListener {
    Context context;
    Spinner sp_sd;
    EditText et_rz, et_zd, et_yjt, et_yjtmd, et_tqkxd, et_fyzs, et_yjz, et_sf, et_ph, et_ec,
            et_TN, et_TP, et_TK, et_AN, et_AP, et_AK, et_Cl, et_B, et_Al, et_Ca,
            et_Mg, et_Na, et_Fe, et_Mn, et_Zn, et_Cu, et_S, et_Mo, et_As, et_Cd,
            et_Cr, et_Co, et_Pb, et_Hg, et_Ni, et_Se, et_Ag, et_V, et_C6_9, et_C10_14,
            et_C15_28, et_C29_36, et_Ben, et_JiaBen, et_YiBen, et_DJ_EJB, et_L_EJB;
    Tr tr;
    private Handler mHandler = null;
    Button btn_dlg_ok, btn_dlg_cancel;
    Tr item;

    public TrdcDialog(Context context, Tr value) {
        super(context);
        this.context = context;
        this.item = value;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏输入法软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.slqc_dlg_trdc);
        this.setTitle("土壤调查记录");
        this.setCanceledOnTouchOutside(false);//触摸对话框以外的部分时，对话框不消失
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        /**
         * 使用this.setOnDismissListener(new OnDismissListener(){}时，要有this.dismiss()方法才有效
         */
//		this.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				if(mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
//			}
//		});

        initWidget();
    }

    public Tr showDialog() {
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
        return tr;
    }

    /**
     * 关联控件
     */
    private void initWidget() {
        btn_dlg_ok = (Button) findViewById(R.id.btn_dlg_ok);
        btn_dlg_cancel = (Button) findViewById(R.id.btn_dlg_cancel);
        btn_dlg_ok.setOnClickListener(this);
        btn_dlg_cancel.setOnClickListener(this);

        sp_sd = (Spinner) findViewById(R.id.sp_sd);
        et_rz = (EditText) findViewById(R.id.et_rz);
        et_zd = (EditText) findViewById(R.id.et_zd);
        et_yjt = (EditText) findViewById(R.id.et_yjt);
        et_yjtmd = (EditText) findViewById(R.id.et_yjtmd);
        et_tqkxd = (EditText) findViewById(R.id.et_tqkxd);
        et_fyzs = (EditText) findViewById(R.id.et_fyzs);
        et_yjz = (EditText) findViewById(R.id.et_yjz);
        et_sf = (EditText) findViewById(R.id.et_sf);
        et_ph = (EditText) findViewById(R.id.et_ph);
        et_ec = (EditText) findViewById(R.id.et_ec);
        et_TN = (EditText) findViewById(R.id.et_TN);
        et_TP = (EditText) findViewById(R.id.et_TP);
        et_TK = (EditText) findViewById(R.id.et_TK);
        et_AN = (EditText) findViewById(R.id.et_AN);
        et_AP = (EditText) findViewById(R.id.et_AP);
        et_AK = (EditText) findViewById(R.id.et_AK);
        et_Cl = (EditText) findViewById(R.id.et_Cl);
        et_B = (EditText) findViewById(R.id.et_B);
        et_Al = (EditText) findViewById(R.id.et_Al);
        et_Ca = (EditText) findViewById(R.id.et_Ca);
        et_Mg = (EditText) findViewById(R.id.et_Mg);
        et_Na = (EditText) findViewById(R.id.et_Na);
        et_Fe = (EditText) findViewById(R.id.et_Fe);
        et_Mn = (EditText) findViewById(R.id.et_Mn);
        et_Zn = (EditText) findViewById(R.id.et_Zn);
        et_Cu = (EditText) findViewById(R.id.et_Cu);
        et_S = (EditText) findViewById(R.id.et_S);
        et_Mo = (EditText) findViewById(R.id.et_Mo);
        et_As = (EditText) findViewById(R.id.et_As);
        et_Cd = (EditText) findViewById(R.id.et_Cd);
        et_Cr = (EditText) findViewById(R.id.et_Cr);
        et_Co = (EditText) findViewById(R.id.et_Co);
        et_Pb = (EditText) findViewById(R.id.et_Pb);
        et_Hg = (EditText) findViewById(R.id.et_Hg);
        et_Ni = (EditText) findViewById(R.id.et_Ni);
        et_Se = (EditText) findViewById(R.id.et_Se);
        et_Ag = (EditText) findViewById(R.id.et_Ag);
        et_V = (EditText) findViewById(R.id.et_V);
        et_C6_9 = (EditText) findViewById(R.id.et_C6_9);
        et_C10_14 = (EditText) findViewById(R.id.et_C10_14);
        et_C15_28 = (EditText) findViewById(R.id.et_C15_28);
        et_C29_36 = (EditText) findViewById(R.id.et_C29_36);
        et_Ben = (EditText) findViewById(R.id.et_Ben);
        et_JiaBen = (EditText) findViewById(R.id.et_JiaBen);
        et_YiBen = (EditText) findViewById(R.id.et_YiBen);
        et_DJ_EJB = (EditText) findViewById(R.id.et_DJ_EJB);
        et_L_EJB = (EditText) findViewById(R.id.et_L_EJB);

        //深度
        List<String> list_trsd = Resmgr.GetValueList("trsd");
        ArrayAdapter<String> adapter_trsd = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_trsd);
        adapter_trsd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sd.setAdapter(adapter_trsd);

        if (item != null) {
            sp_sd.setSelection(Resmgr.GetPosByCode("trsd", item.sd));
            if (item.rz > 0) et_rz.setText(String.valueOf(item.rz));
            if (item.zd > 0) et_zd.setText(String.valueOf(item.zd));
            if (item.yjt > 0) et_yjt.setText(String.valueOf(item.yjt));
            if (item.yjtmd > 0) et_yjtmd.setText(String.valueOf(item.yjtmd));
            if (item.tqkxd > 0) et_tqkxd.setText(String.valueOf(item.tqkxd));
            if (item.fyzs > 0) et_fyzs.setText(String.valueOf(item.fyzs));
            if (item.yjz > 0) et_yjz.setText(String.valueOf(item.yjz));
            if (item.sf > 0) et_sf.setText(String.valueOf(item.sf));
            if (item.ph > 0) et_ph.setText(String.valueOf(item.ph));
            if (item.ec > 0) et_ec.setText(String.valueOf(item.ec));
            if (item.TN > 0) et_TN.setText(String.valueOf(item.TN));
            if (item.TP > 0) et_TP.setText(String.valueOf(item.TP));
            if (item.TK > 0) et_TK.setText(String.valueOf(item.TK));
            if (item.AN > 0) et_AN.setText(String.valueOf(item.AN));
            if (item.AP > 0) et_AP.setText(String.valueOf(item.AP));
            if (item.AK > 0) et_AK.setText(String.valueOf(item.AK));
            if (item.Cl > 0) et_Cl.setText(String.valueOf(item.Cl));
            if (item.B > 0) et_B.setText(String.valueOf(item.B));
            if (item.Al > 0) et_Al.setText(String.valueOf(item.Al));
            if (item.Ca > 0) et_Ca.setText(String.valueOf(item.Ca));
            if (item.Mg > 0) et_Mg.setText(String.valueOf(item.Mg));
            if (item.Na > 0) et_Na.setText(String.valueOf(item.Na));
            if (item.Fe > 0) et_Fe.setText(String.valueOf(item.Fe));
            if (item.Mn > 0) et_Mn.setText(String.valueOf(item.Mn));
            if (item.Zn > 0) et_Zn.setText(String.valueOf(item.Zn));
            if (item.Cu > 0) et_Cu.setText(String.valueOf(item.Cu));
            if (item.S > 0) et_S.setText(String.valueOf(item.S));
            if (item.Mo > 0) et_Mo.setText(String.valueOf(item.Mo));
            if (item.As > 0) et_As.setText(String.valueOf(item.As));
            if (item.Cd > 0) et_Cd.setText(String.valueOf(item.Cd));
            if (item.Cr > 0) et_Cr.setText(String.valueOf(item.Cr));
            if (item.Co > 0) et_Co.setText(String.valueOf(item.Co));
            if (item.Pb > 0) et_Pb.setText(String.valueOf(item.Pb));
            if (item.Hg > 0) et_Hg.setText(String.valueOf(item.Hg));
            if (item.Ni > 0) et_Ni.setText(String.valueOf(item.Ni));
            if (item.Se > 0) et_Se.setText(String.valueOf(item.Se));
            if (item.Ag > 0) et_Ag.setText(String.valueOf(item.Ag));
            if (item.V > 0) et_V.setText(String.valueOf(item.V));
            if (item.C6_9 > 0) et_C6_9.setText(String.valueOf(item.C6_9));
            if (item.C10_14 > 0) et_C10_14.setText(String.valueOf(item.C10_14));
            if (item.C15_28 > 0) et_C15_28.setText(String.valueOf(item.C15_28));
            if (item.C29_36 > 0) et_C29_36.setText(String.valueOf(item.C29_36));
            if (item.Ben > 0) et_Ben.setText(String.valueOf(item.Ben));
            if (item.JiaBen > 0) et_JiaBen.setText(String.valueOf(item.JiaBen));
            if (item.YiBen > 0) et_YiBen.setText(String.valueOf(item.YiBen));
            if (item.DJ_EJB > 0) et_DJ_EJB.setText(String.valueOf(item.DJ_EJB));
            if (item.L_EJB > 0) et_L_EJB.setText(String.valueOf(item.L_EJB));
        }
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dlg_ok:
                String sd = sp_sd.getSelectedItem().toString();
                String rz = et_rz.getText().toString();
                String zd = et_zd.getText().toString();
                String yjt = et_yjt.getText().toString();
                String yjtmd = et_yjtmd.getText().toString();
                String tqkxd = et_tqkxd.getText().toString();
                String fyzs = et_fyzs.getText().toString();
                String yjz = et_yjz.getText().toString();
                String sf = et_sf.getText().toString();
                String ph = et_ph.getText().toString();
                String ec = et_ec.getText().toString();
                String TN = et_TN.getText().toString();
                String TP = et_TP.getText().toString();
                String TK = et_TK.getText().toString();
                String AN = et_AN.getText().toString();
                String AP = et_AP.getText().toString();
                String AK = et_AK.getText().toString();
                String Cl = et_Cl.getText().toString();
                String B = et_B.getText().toString();
                String Al = et_Al.getText().toString();
                String Ca = et_Ca.getText().toString();
                String Mg = et_Mg.getText().toString();
                String Na = et_Na.getText().toString();
                String Fe = et_Fe.getText().toString();
                String Mn = et_Mn.getText().toString();
                String Zn = et_Zn.getText().toString();
                String Cu = et_Cu.getText().toString();
                String S = et_S.getText().toString();
                String Mo = et_Mo.getText().toString();
                String As = et_As.getText().toString();
                String Cd = et_Cd.getText().toString();
                String Cr = et_Cr.getText().toString();
                String Co = et_Co.getText().toString();
                String Pb = et_Pb.getText().toString();
                String Hg = et_Hg.getText().toString();
                String Ni = et_Ni.getText().toString();
                String Se = et_Se.getText().toString();
                String Ag = et_Ag.getText().toString();
                String V = et_V.getText().toString();
                String C6_9 = et_C6_9.getText().toString();
                String C10_14 = et_C10_14.getText().toString();
                String C15_28 = et_C15_28.getText().toString();
                String c29_36 = et_C29_36.getText().toString();
                String Ben = et_Ben.getText().toString();
                String JiaBen = et_JiaBen.getText().toString();
                String YiBen = et_JiaBen.getText().toString();
                String DJ_EJB = et_DJ_EJB.getText().toString();
                String L_EJB = et_L_EJB.getText().toString();

                int sd_i = -1;
                float rz_f = -1;
                float zd_f = -1;
                float yjt_f = -1;
                float yjtmd_f = -1;
                float tqkxd_f = -1;
                float fyzs_f = -1;
                float yjz_f = -1;
                float sf_f = -1;
                float ph_f = -1;
                float ec_f = -1;
                float TN_f = -1;
                float TP_f = -1;
                float TK_f = -1;
                float AN_f = -1;
                float AP_f = -1;
                float AK_f = -1;
                float Cl_f = -1;
                float B_f = -1;
                float Al_f = -1;
                float Ca_f = -1;
                float Mg_f = -1;
                float Na_f = -1;
                float Fe_f = -1;
                float Mn_f = -1;
                float Zn_f = -1;
                float Cu_f = -1;
                float S_f = -1;
                float Mo_f = -1;
                float As_f = -1;
                float Cd_f = -1;
                float Cr_f = -1;
                float Co_f = -1;
                float Pb_f = -1;
                float Hg_f = -1;
                float Ni_f = -1;
                float Se_f = -1;
                float Ag_f = -1;
                float V_f = -1;
                float C6_9_f = -1;
                float C10_14_f = -1;
                float C15_28_f = -1;
                float C29_36_f = -1;
                float Ben_f = -1;
                float JiaBen_f = -1;
                float YiBen_f = -1;
                float DJ_EJB_f = -1;
                float L_EJB_f = -1;

                sd_i = Resmgr.GetCodeByValue("trsd", sd);

                try {
                    rz_f = Float.parseFloat(rz);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    zd_f = Float.parseFloat(zd);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    yjt_f = Float.parseFloat(yjt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    yjtmd_f = Float.parseFloat(yjtmd);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tqkxd_f = Float.parseFloat(tqkxd);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    fyzs_f = Float.parseFloat(fyzs);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    yjz_f = Float.parseFloat(yjz);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    sf_f = Float.parseFloat(sf);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    ph_f = Float.parseFloat(ph);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    ec_f = Float.parseFloat(ec);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    TN_f = Float.parseFloat(TN);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    TP_f = Float.parseFloat(TP);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    TK_f = Float.parseFloat(TK);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    AN_f = Float.parseFloat(AN);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    AP_f = Float.parseFloat(AP);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    AK_f = Float.parseFloat(AK);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Cl_f = Float.parseFloat(Cl);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    B_f = Float.parseFloat(B);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Al_f = Float.parseFloat(Al);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Ca_f = Float.parseFloat(Ca);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Mg_f = Float.parseFloat(Mg);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Na_f = Float.parseFloat(Na);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Fe_f = Float.parseFloat(Fe);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Mn_f = Float.parseFloat(Mn);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Zn_f = Float.parseFloat(Zn);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Cu_f = Float.parseFloat(Cu);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    S_f = Float.parseFloat(S);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Mo_f = Float.parseFloat(Mo);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    As_f = Float.parseFloat(As);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Cd_f = Float.parseFloat(Cd);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Cr_f = Float.parseFloat(Cr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Co_f = Float.parseFloat(Co);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Pb_f = Float.parseFloat(Pb);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Hg_f = Float.parseFloat(Hg);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Ni_f = Float.parseFloat(Ni);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Se_f = Float.parseFloat(Se);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Ag_f = Float.parseFloat(Ag);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    V_f = Float.parseFloat(V);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    C6_9_f = Float.parseFloat(C6_9);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    C10_14_f = Float.parseFloat(C10_14);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    C15_28_f = Float.parseFloat(C15_28);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    C29_36_f = Float.parseFloat(c29_36);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Ben_f = Float.parseFloat(Ben);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    JiaBen_f = Float.parseFloat(JiaBen);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    YiBen_f = Float.parseFloat(YiBen);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    DJ_EJB_f = Float.parseFloat(DJ_EJB);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    L_EJB_f = Float.parseFloat(L_EJB);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                tr = new Tr();
                tr.sd = sd_i;
                tr.rz = rz_f;
                tr.zd = zd_f;
                tr.yjt = yjt_f;
                tr.yjtmd = yjtmd_f;
                tr.tqkxd = tqkxd_f;
                tr.fyzs = fyzs_f;
                tr.yjz = yjz_f;
                tr.sf = sf_f;
                tr.ph = ph_f;
                tr.ec = ec_f;
                tr.TN = TN_f;
                tr.TP = TP_f;
                tr.TK = TK_f;
                tr.AN = AN_f;
                tr.AP = AP_f;
                tr.AK = AK_f;
                tr.Cl = Cl_f;
                tr.B = B_f;
                tr.Al = Al_f;
                tr.Ca = Ca_f;
                tr.Mg = Mg_f;
                tr.Na = Na_f;
                tr.Fe = Fe_f;
                tr.Mn = Mn_f;
                tr.Zn = Zn_f;
                tr.Cu = Cu_f;
                tr.S = S_f;
                tr.Mo = Mo_f;
                tr.As = As_f;
                tr.Cd = Cd_f;
                tr.Cr = Cr_f;
                tr.Co = Co_f;
                tr.Pb = Pb_f;
                tr.Hg = Hg_f;
                tr.Ni = Ni_f;
                tr.Se = Se_f;
                tr.Ag = Ag_f;
                tr.V = V_f;
                tr.C6_9 = C6_9_f;
                tr.C10_14 = C10_14_f;
                tr.C15_28 = C15_28_f;
                tr.C29_36 = C29_36_f;
                tr.Ben = Ben_f;
                tr.JiaBen = JiaBen_f;
                tr.YiBen = YiBen_f;
                tr.DJ_EJB = DJ_EJB_f;
                tr.L_EJB = L_EJB_f;

                TrdcDialog.this.cancel();
//			this.dismiss();
                break;
            case R.id.btn_dlg_cancel:
                TrdcDialog.this.cancel();
                break;
            default:
                break;
        }
    }
}
