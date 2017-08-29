package com.tdgeos.tanhui;

import java.util.List;

import com.tdgeos.dlg.yddc.TrdcDialog;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Tr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SlqcTrdc extends Activity implements OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnAdd = null;
    private Button btnDel = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private Spinner spYfwz = null;

    private LayoutInflater layInflater = null;
    private LinearLayout layList = null;
    LinearLayout layRow;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏输入法软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.slqc_trdc);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        iStatus = YangdiMgr.GetTrdcStatu(ydh);///////////////////////////////////////////////////////

        initWidget();

        initData();
    }

    private void initWidget() {
        btnClose = (Button) findViewById(R.id.btn_close);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        spYfwz = (Spinner) findViewById(R.id.sp_yfwz);
        List<String> lstYfh = Resmgr.GetValueList("yfwz");
        ArrayAdapter<String> apYfh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstYfh);
        apYfh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYfwz.setAdapter(apYfh);
        spYfwz.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                YangdiMgr.SetYfwz(ydh, arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        layInflater = LayoutInflater.from(this);
        layList = (LinearLayout) findViewById(R.id.lay_list);

    }

    private void initData() {
        layList.removeAllViews();
        List<Tr> lst = YangdiMgr.GetTrList(ydh);//////////////////////////////////////////////////////
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                addRow(lst.get(i));
            }
        }
        int yf = YangdiMgr.GetYfwz(ydh);
        spYfwz.setSelection(Resmgr.GetPosByCode("yfwz", yf));
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_add:
                TrdcDialog dlg = new TrdcDialog(SlqcTrdc.this, null);
                Tr tr = dlg.showDialog();
                if (tr != null) {
                    YangdiMgr.AddTr(ydh, tr);
                    initData();
                }
                break;
            case R.id.btn_del:
                delRow();
                break;
            case R.id.btn_save: {
                int n = YangdiMgr.GetTrCount(ydh);
                if (n != 3) {
                    iStatus = 1;
                }
                if (iStatus != 2) {
                    iStatus = 1;
                }
                String sql = "update tag set f12 = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                int n = YangdiMgr.GetTrCount(ydh);
                if (n <= 0) {
                    Toast.makeText(this, "尚未进行土壤调查！", 1).show();
                    break;
                } else if (n < 3) {
                    Toast.makeText(this, "土壤调查记录过少！", 1).show();
                    break;
                } else if (n > 3) {
                    Toast.makeText(this, "土壤调查记录过多！", 1).show();
                    break;
                }
                String sql = "update tag set f12 = '2' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                finish();
                break;
            }
            default: {
                LinearLayout layRow = null;
                try {
                    layRow = (LinearLayout) v;
                } catch (Exception e) {
                }
                if (layRow != null) {
                    int xh = (Integer) layRow.getTag();
                    Tr tr2 = YangdiMgr.GetTr(ydh, xh);
                    TrdcDialog dlg2 = new TrdcDialog(this, tr2);
                    Tr item = dlg2.showDialog();
                    if (item != null) {
                        YangdiMgr.UpdateTr(ydh, item);
                        TextView tv_sd = (TextView) layRow.findViewById(R.id.tv_sd);
                        TextView tv_rz = (TextView) layRow.findViewById(R.id.tv_rz);
                        TextView tv_zd = (TextView) layRow.findViewById(R.id.tv_zd);
                        TextView tv_yjt = (TextView) layRow.findViewById(R.id.tv_yjt);
                        TextView tv_yjtmd = (TextView) layRow.findViewById(R.id.tv_yjtmd);
                        TextView tv_tqkxd = (TextView) layRow.findViewById(R.id.tv_tqkxd);
                        TextView tv_fyzs = (TextView) layRow.findViewById(R.id.tv_fyzs);
                        TextView tv_yjz = (TextView) layRow.findViewById(R.id.tv_yjz);
                        TextView tv_sf = (TextView) layRow.findViewById(R.id.tv_sf);
                        TextView tv_ph = (TextView) layRow.findViewById(R.id.tv_ph);
                        TextView tv_ec = (TextView) layRow.findViewById(R.id.tv_ec);
                        TextView tv_TN = (TextView) layRow.findViewById(R.id.tv_TN);
                        TextView tv_TP = (TextView) layRow.findViewById(R.id.tv_TP);
                        TextView tv_TK = (TextView) layRow.findViewById(R.id.tv_TK);
                        TextView tv_AN = (TextView) layRow.findViewById(R.id.tv_AN);
                        TextView tv_AP = (TextView) layRow.findViewById(R.id.tv_AP);
                        TextView tv_AK = (TextView) layRow.findViewById(R.id.tv_AK);
                        TextView tv_Cl = (TextView) layRow.findViewById(R.id.tv_Cl);
                        TextView tv_B = (TextView) layRow.findViewById(R.id.tv_B);
                        TextView tv_Al = (TextView) layRow.findViewById(R.id.tv_Al);
                        TextView tv_Ca = (TextView) layRow.findViewById(R.id.tv_Ca);
                        TextView tv_Mg = (TextView) layRow.findViewById(R.id.tv_Mg);
                        TextView tv_Na = (TextView) layRow.findViewById(R.id.tv_Na);
                        TextView tv_Fe = (TextView) layRow.findViewById(R.id.tv_Fe);
                        TextView tv_Mn = (TextView) layRow.findViewById(R.id.tv_Mn);
                        TextView tv_Zn = (TextView) layRow.findViewById(R.id.tv_Zn);
                        TextView tv_Cu = (TextView) layRow.findViewById(R.id.tv_Cu);
                        TextView tv_S = (TextView) layRow.findViewById(R.id.tv_S);
                        TextView tv_Mo = (TextView) layRow.findViewById(R.id.tv_Mo);
                        TextView tv_As = (TextView) layRow.findViewById(R.id.tv_As);
                        TextView tv_Cd = (TextView) layRow.findViewById(R.id.tv_Cd);
                        TextView tv_Cr = (TextView) layRow.findViewById(R.id.tv_Cr);
                        TextView tv_Co = (TextView) layRow.findViewById(R.id.tv_Co);
                        TextView tv_Pb = (TextView) layRow.findViewById(R.id.tv_Pb);
                        TextView tv_Hg = (TextView) layRow.findViewById(R.id.tv_Hg);
                        TextView tv_Ni = (TextView) layRow.findViewById(R.id.tv_Ni);
                        TextView tv_Se = (TextView) layRow.findViewById(R.id.tv_Se);
                        TextView tv_Ag = (TextView) layRow.findViewById(R.id.tv_Ag);
                        TextView tv_V = (TextView) layRow.findViewById(R.id.tv_V);
                        TextView tv_C6_9 = (TextView) layRow.findViewById(R.id.tv_C6_9);
                        TextView tv_C10_14 = (TextView) layRow.findViewById(R.id.tv_C10_14);
                        TextView tv_C15_28 = (TextView) layRow.findViewById(R.id.tv_C15_28);
                        TextView tv_C29_36 = (TextView) layRow.findViewById(R.id.tv_C29_36);
                        TextView tv_Ben = (TextView) layRow.findViewById(R.id.tv_Ben);
                        TextView tv_JiaBen = (TextView) layRow.findViewById(R.id.tv_JiaBen);
                        TextView tv_YiBen = (TextView) layRow.findViewById(R.id.tv_YiBen);
                        TextView tv_DJ_EJB = (TextView) layRow.findViewById(R.id.tv_DJ_EJB);
                        TextView tv_L_EJB = (TextView) layRow.findViewById(R.id.tv_L_EJB);

                        tv_sd.setText(Resmgr.GetValueByCode("trsd", item.sd));
                        if (item.rz > 0) tv_rz.setText(String.valueOf(item.rz));
                        if (item.zd > 0) tv_zd.setText(String.valueOf(item.zd));
                        if (item.yjt > 0) tv_yjt.setText(String.valueOf(item.yjt));
                        if (item.yjtmd > 0) tv_yjtmd.setText(String.valueOf(item.yjtmd));
                        if (item.tqkxd > 0) tv_tqkxd.setText(String.valueOf(item.tqkxd));
                        if (item.fyzs > 0) tv_fyzs.setText(String.valueOf(item.fyzs));
                        if (item.yjz > 0) tv_yjz.setText(String.valueOf(item.yjz));
                        if (item.sf > 0) tv_sf.setText(String.valueOf(item.sf));
                        if (item.ph > 0) tv_ph.setText(String.valueOf(item.ph));
                        if (item.ec > 0) tv_ec.setText(String.valueOf(item.ec));
                        if (item.TN > 0) tv_TN.setText(String.valueOf(item.TN));
                        if (item.TP > 0) tv_TP.setText(String.valueOf(item.TP));
                        if (item.TK > 0) tv_TK.setText(String.valueOf(item.TK));
                        if (item.AN > 0) tv_AN.setText(String.valueOf(item.AN));
                        if (item.AP > 0) tv_AP.setText(String.valueOf(item.AP));
                        if (item.AK > 0) tv_AK.setText(String.valueOf(item.AK));
                        if (item.Cl > 0) tv_Cl.setText(String.valueOf(item.Cl));
                        if (item.B > 0) tv_B.setText(String.valueOf(item.B));
                        if (item.Al > 0) tv_Al.setText(String.valueOf(item.Al));
                        if (item.Ca > 0) tv_Ca.setText(String.valueOf(item.Ca));
                        if (item.Mg > 0) tv_Mg.setText(String.valueOf(item.Mg));
                        if (item.Na > 0) tv_Na.setText(String.valueOf(item.Na));
                        if (item.Fe > 0) tv_Fe.setText(String.valueOf(item.Fe));
                        if (item.Mn > 0) tv_Mn.setText(String.valueOf(item.Mn));
                        if (item.Zn > 0) tv_Zn.setText(String.valueOf(item.Zn));
                        if (item.Cu > 0) tv_Cu.setText(String.valueOf(item.Cu));
                        if (item.S > 0) tv_S.setText(String.valueOf(item.S));
                        if (item.Mo > 0) tv_Mo.setText(String.valueOf(item.Mo));
                        if (item.As > 0) tv_As.setText(String.valueOf(item.As));
                        if (item.Cd > 0) tv_Cd.setText(String.valueOf(item.Cd));
                        if (item.Cr > 0) tv_Cr.setText(String.valueOf(item.Cr));
                        if (item.Co > 0) tv_Co.setText(String.valueOf(item.Co));
                        if (item.Pb > 0) tv_Pb.setText(String.valueOf(item.Pb));
                        if (item.Hg > 0) tv_Hg.setText(String.valueOf(item.Hg));
                        if (item.Ni > 0) tv_Ni.setText(String.valueOf(item.Ni));
                        if (item.Se > 0) tv_Se.setText(String.valueOf(item.Se));
                        if (item.Ag > 0) tv_Ag.setText(String.valueOf(item.Ag));
                        if (item.V > 0) tv_V.setText(String.valueOf(item.V));
                        if (item.C6_9 > 0) tv_C6_9.setText(String.valueOf(item.C6_9));
                        if (item.C10_14 > 0) tv_C10_14.setText(String.valueOf(item.C10_14));
                        if (item.C15_28 > 0) tv_C15_28.setText(String.valueOf(item.C15_28));
                        if (item.C29_36 > 0) tv_C29_36.setText(String.valueOf(item.C29_36));
                        if (item.Ben > 0) tv_Ben.setText(String.valueOf(item.Ben));
                        if (item.JiaBen > 0) tv_JiaBen.setText(String.valueOf(item.JiaBen));
                        if (item.YiBen > 0) tv_YiBen.setText(String.valueOf(item.YiBen));
                        if (item.DJ_EJB > 0) tv_DJ_EJB.setText(String.valueOf(item.DJ_EJB));
                        if (item.L_EJB > 0) tv_L_EJB.setText(String.valueOf(item.L_EJB));
                    }
                }
                break;
            }
        }
    }

    /**
     * 添加一行
     */
    private void addRow(Tr item) {
        if (item == null) return;
        LinearLayout layRow = (LinearLayout) layInflater.inflate(R.layout.slqc_trdc_item, null);
        layRow.setTag(item.xh);
        layRow.setOnClickListener(this);
        CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
        cb.setTag(item.xh);
        TextView tv_sd = (TextView) layRow.findViewById(R.id.tv_sd);
        TextView tv_rz = (TextView) layRow.findViewById(R.id.tv_rz);
        TextView tv_zd = (TextView) layRow.findViewById(R.id.tv_zd);
        TextView tv_yjt = (TextView) layRow.findViewById(R.id.tv_yjt);
        TextView tv_yjtmd = (TextView) layRow.findViewById(R.id.tv_yjtmd);
        TextView tv_tqkxd = (TextView) layRow.findViewById(R.id.tv_tqkxd);
        TextView tv_fyzs = (TextView) layRow.findViewById(R.id.tv_fyzs);
        TextView tv_yjz = (TextView) layRow.findViewById(R.id.tv_yjz);
        TextView tv_sf = (TextView) layRow.findViewById(R.id.tv_sf);
        TextView tv_ph = (TextView) layRow.findViewById(R.id.tv_ph);
        TextView tv_ec = (TextView) layRow.findViewById(R.id.tv_ec);
        TextView tv_TN = (TextView) layRow.findViewById(R.id.tv_TN);
        TextView tv_TP = (TextView) layRow.findViewById(R.id.tv_TP);
        TextView tv_TK = (TextView) layRow.findViewById(R.id.tv_TK);
        TextView tv_AN = (TextView) layRow.findViewById(R.id.tv_AN);
        TextView tv_AP = (TextView) layRow.findViewById(R.id.tv_AP);
        TextView tv_AK = (TextView) layRow.findViewById(R.id.tv_AK);
        TextView tv_Cl = (TextView) layRow.findViewById(R.id.tv_Cl);
        TextView tv_B = (TextView) layRow.findViewById(R.id.tv_B);
        TextView tv_Al = (TextView) layRow.findViewById(R.id.tv_Al);
        TextView tv_Ca = (TextView) layRow.findViewById(R.id.tv_Ca);
        TextView tv_Mg = (TextView) layRow.findViewById(R.id.tv_Mg);
        TextView tv_Na = (TextView) layRow.findViewById(R.id.tv_Na);
        TextView tv_Fe = (TextView) layRow.findViewById(R.id.tv_Fe);
        TextView tv_Mn = (TextView) layRow.findViewById(R.id.tv_Mn);
        TextView tv_Zn = (TextView) layRow.findViewById(R.id.tv_Zn);
        TextView tv_Cu = (TextView) layRow.findViewById(R.id.tv_Cu);
        TextView tv_S = (TextView) layRow.findViewById(R.id.tv_S);
        TextView tv_Mo = (TextView) layRow.findViewById(R.id.tv_Mo);
        TextView tv_As = (TextView) layRow.findViewById(R.id.tv_As);
        TextView tv_Cd = (TextView) layRow.findViewById(R.id.tv_Cd);
        TextView tv_Cr = (TextView) layRow.findViewById(R.id.tv_Cr);
        TextView tv_Co = (TextView) layRow.findViewById(R.id.tv_Co);
        TextView tv_Pb = (TextView) layRow.findViewById(R.id.tv_Pb);
        TextView tv_Hg = (TextView) layRow.findViewById(R.id.tv_Hg);
        TextView tv_Ni = (TextView) layRow.findViewById(R.id.tv_Ni);
        TextView tv_Se = (TextView) layRow.findViewById(R.id.tv_Se);
        TextView tv_Ag = (TextView) layRow.findViewById(R.id.tv_Ag);
        TextView tv_V = (TextView) layRow.findViewById(R.id.tv_V);
        TextView tv_C6_9 = (TextView) layRow.findViewById(R.id.tv_C6_9);
        TextView tv_C10_14 = (TextView) layRow.findViewById(R.id.tv_C10_14);
        TextView tv_C15_28 = (TextView) layRow.findViewById(R.id.tv_C15_28);
        TextView tv_C29_36 = (TextView) layRow.findViewById(R.id.tv_C29_36);
        TextView tv_Ben = (TextView) layRow.findViewById(R.id.tv_Ben);
        TextView tv_JiaBen = (TextView) layRow.findViewById(R.id.tv_JiaBen);
        TextView tv_YiBen = (TextView) layRow.findViewById(R.id.tv_YiBen);
        TextView tv_DJ_EJB = (TextView) layRow.findViewById(R.id.tv_DJ_EJB);
        TextView tv_L_EJB = (TextView) layRow.findViewById(R.id.tv_L_EJB);

        tv_sd.setText(Resmgr.GetValueByCode("trsd", item.sd));
        if (item.rz > 0) tv_rz.setText(String.valueOf(item.rz));
        if (item.zd > 0) tv_zd.setText(String.valueOf(item.zd));
        if (item.yjt > 0) tv_yjt.setText(String.valueOf(item.yjt));
        if (item.yjtmd > 0) tv_yjtmd.setText(String.valueOf(item.yjtmd));
        if (item.tqkxd > 0) tv_tqkxd.setText(String.valueOf(item.tqkxd));
        if (item.fyzs > 0) tv_fyzs.setText(String.valueOf(item.fyzs));
        if (item.yjz > 0) tv_yjz.setText(String.valueOf(item.yjz));
        if (item.sf > 0) tv_sf.setText(String.valueOf(item.sf));
        if (item.ph > 0) tv_ph.setText(String.valueOf(item.ph));
        if (item.ec > 0) tv_ec.setText(String.valueOf(item.ec));
        if (item.TN > 0) tv_TN.setText(String.valueOf(item.TN));
        if (item.TP > 0) tv_TP.setText(String.valueOf(item.TP));
        if (item.TK > 0) tv_TK.setText(String.valueOf(item.TK));
        if (item.AN > 0) tv_AN.setText(String.valueOf(item.AN));
        if (item.AP > 0) tv_AP.setText(String.valueOf(item.AP));
        if (item.AK > 0) tv_AK.setText(String.valueOf(item.AK));
        if (item.Cl > 0) tv_Cl.setText(String.valueOf(item.Cl));
        if (item.B > 0) tv_B.setText(String.valueOf(item.B));
        if (item.Al > 0) tv_Al.setText(String.valueOf(item.Al));
        if (item.Ca > 0) tv_Ca.setText(String.valueOf(item.Ca));
        if (item.Mg > 0) tv_Mg.setText(String.valueOf(item.Mg));
        if (item.Na > 0) tv_Na.setText(String.valueOf(item.Na));
        if (item.Fe > 0) tv_Fe.setText(String.valueOf(item.Fe));
        if (item.Mn > 0) tv_Mn.setText(String.valueOf(item.Mn));
        if (item.Zn > 0) tv_Zn.setText(String.valueOf(item.Zn));
        if (item.Cu > 0) tv_Cu.setText(String.valueOf(item.Cu));
        if (item.S > 0) tv_S.setText(String.valueOf(item.S));
        if (item.Mo > 0) tv_Mo.setText(String.valueOf(item.Mo));
        if (item.As > 0) tv_As.setText(String.valueOf(item.As));
        if (item.Cd > 0) tv_Cd.setText(String.valueOf(item.Cd));
        if (item.Cr > 0) tv_Cr.setText(String.valueOf(item.Cr));
        if (item.Co > 0) tv_Co.setText(String.valueOf(item.Co));
        if (item.Pb > 0) tv_Pb.setText(String.valueOf(item.Pb));
        if (item.Hg > 0) tv_Hg.setText(String.valueOf(item.Hg));
        if (item.Ni > 0) tv_Ni.setText(String.valueOf(item.Ni));
        if (item.Se > 0) tv_Se.setText(String.valueOf(item.Se));
        if (item.Ag > 0) tv_Ag.setText(String.valueOf(item.Ag));
        if (item.V > 0) tv_V.setText(String.valueOf(item.V));
        if (item.C6_9 > 0) tv_C6_9.setText(String.valueOf(item.C6_9));
        if (item.C10_14 > 0) tv_C10_14.setText(String.valueOf(item.C10_14));
        if (item.C15_28 > 0) tv_C15_28.setText(String.valueOf(item.C15_28));
        if (item.C29_36 > 0) tv_C29_36.setText(String.valueOf(item.C29_36));
        if (item.Ben > 0) tv_Ben.setText(String.valueOf(item.Ben));
        if (item.JiaBen > 0) tv_JiaBen.setText(String.valueOf(item.JiaBen));
        if (item.YiBen > 0) tv_YiBen.setText(String.valueOf(item.YiBen));
        if (item.DJ_EJB > 0) tv_DJ_EJB.setText(String.valueOf(item.DJ_EJB));
        if (item.L_EJB > 0) tv_L_EJB.setText(String.valueOf(item.L_EJB));
        layList.addView(layRow);
    }

    /**
     * 删除一行
     */
    private void delRow() {
        int n = layList.getChildCount();
        for (int i = n - 1; i >= 0; i--) {
            LinearLayout layRow = (LinearLayout) layList.getChildAt(i);
            CheckBox cb = (CheckBox) layRow.findViewById(R.id.cb_pos);
            if (cb.isChecked()) {
                index = i;
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("删除后将不可恢复，是否继续删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                layList.removeViewAt(index);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                int xh = (Integer) cb.getTag();
                YangdiMgr.DelTr(ydh, xh);/////////////////////////////////////////////////////////////
            }
        }

    }

    private void toast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

}
