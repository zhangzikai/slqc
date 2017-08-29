package com.tdgeos.dlg.yddc;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tdgeos.dlg.base.MyFileSaveDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.base.MyNewNameDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.R;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.TrackInfo;
import com.tdgeos.yangdi.YangdiMgr;

public class TrackDialog extends Dialog implements OnClickListener {
    private Context ctt;
    private Handler mHandler;
    private TrackInfo result = null;
    private Switch swIsOpenTrk = null;
    private Switch swIsShowQqTrk = null;
    private Switch swIsShowTrk = null;
    private Spinner spTrkname = null;
    private Button btnTrkNew = null;
    private EditText etCount = null;
    private EditText etLength = null;
    private EditText etBegin = null;
    private EditText etEnd = null;
    private Button btnExport = null;
    private Button btnDelete = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    private int ydh = 0;
    private List<TrackInfo> lstTrkInfos = null;

    public TrackDialog(Context context, int ydh) {
        super(context);
        ctt = context;
        this.ydh = ydh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_track);
        setTitle("航迹管理");
        this.getWindow().getAttributes().width = MyConfig.GetMiddleWidth();
        //this.getWindow().getAttributes().height = MyConfig.GetDlgHeight();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (mHandler != null) mHandler.sendMessage(mHandler.obtainMessage());
            }
        });

        etCount = (EditText) findViewById(R.id.et_count);
        etLength = (EditText) findViewById(R.id.et_length);
        etBegin = (EditText) findViewById(R.id.et_begin);
        etEnd = (EditText) findViewById(R.id.et_end);

        swIsOpenTrk = (Switch) findViewById(R.id.sw_open);
        swIsShowTrk = (Switch) findViewById(R.id.sw_show);
        swIsShowQqTrk = (Switch) findViewById(R.id.sw_showqq);
        swIsOpenTrk.setChecked(MyConfig.GetOpenTrack());
        swIsShowTrk.setChecked(MyConfig.GetShowTrack());
        swIsShowQqTrk.setChecked(MyConfig.GetShowQqTrack());
        swIsOpenTrk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //MyConfig.SetOpenTrack(isChecked);
            }
        });
        swIsShowTrk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //MyConfig.SetShowTrack(isChecked);
            }
        });
        swIsShowQqTrk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //MyConfig.SetShowQqTrack(isChecked);
            }
        });
        spTrkname = (Spinner) findViewById(R.id.sp_trkname);
        lstTrkInfos = YangdiMgr.GetTrackInfo(ydh);
        List<String> lst = YangdiMgr.GetTrkNames(ydh);
        ArrayAdapter<String> ap = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lst);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrkname.setAdapter(ap);
        spTrkname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                etCount.setText(String.valueOf(lstTrkInfos.get(arg2).count));
                etLength.setText(String.valueOf(lstTrkInfos.get(arg2).length));
                etBegin.setText(lstTrkInfos.get(arg2).begin);
                etEnd.setText(lstTrkInfos.get(arg2).end);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                etCount.setText("");
                etBegin.setText("");
                etEnd.setText("");
            }
        });
        String trk = MyConfig.GetCurTrk();
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).equals(trk)) {
                spTrkname.setSelection(i);
                break;
            }
        }

        btnTrkNew = (Button) findViewById(R.id.btn_trknew);
        btnTrkNew.setOnClickListener(this);

        btnExport = (Button) findViewById(R.id.btn_export);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnExport.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_trknew: {
                List<String> lstNames = YangdiMgr.GetTrkNames(ydh);
                String trkName = Qianqimgr.GetXianJuNameByYdh(ydh) + "-" + ydh + "-" + MyFuns.GetDateByNumberS();

                MyNewNameDialog dlg = new MyNewNameDialog(ctt, trkName, null, null);
                String str = dlg.showDialog();
                if (str != null) {
                    boolean b = false;
                    for (int i = 0; i < lstNames.size(); i++) {
                        if (lstNames.get(i).equals(str)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        Toast.makeText(ctt, "该航迹名称已经存在，请重新设置！", 1).show();
                        break;
                    }
                    YangdiMgr.CreateTrack(ydh, str);
                    MyConfig.SetCurTrk(str);
                    lstTrkInfos = YangdiMgr.GetTrackInfo(ydh);
                    lstNames = YangdiMgr.GetTrkNames(ydh);
                    ArrayAdapter<String> ap = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lstNames);
                    ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTrkname.setAdapter(ap);
                    for (int i = 0; i < lstNames.size(); i++) {
                        if (lstNames.get(i).equals(str)) {
                            spTrkname.setSelection(i);
                            break;
                        }
                    }
                }
                break;
            }
            case R.id.btn_export: {
                if (spTrkname.getChildCount() == 0) break;
                String name = spTrkname.getSelectedItem().toString();
                MyFileSaveDialog dlg = new MyFileSaveDialog(ctt, MyConfig.GetCurdir(), name + ".xls");
                String excel = dlg.showDialog();
                if (excel != null) {
                    YangdiMgr.ExportTrack(ydh, name, excel);
                }
                break;
            }
            case R.id.btn_delete: {
                if (spTrkname.getChildCount() == 0) break;
                MyMakeSureDialog dlg = new MyMakeSureDialog(ctt, "删除", "删除后将无法恢复，是否继续删除？", "删除", "取消");
                if (dlg.showDialog()) {
                    String name = spTrkname.getSelectedItem().toString();
                    ;
                    YangdiMgr.DeleteTrack(ydh, name);
                    String trk = MyConfig.GetCurTrk();
                    if (trk.equals(name)) MyConfig.SetCurTrk("");
                    lstTrkInfos = YangdiMgr.GetTrackInfo(ydh);
                    List<String> lstNames = YangdiMgr.GetTrkNames(ydh);
                    ArrayAdapter<String> ap = new ArrayAdapter<String>(ctt, android.R.layout.simple_spinner_item, lstNames);
                    ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spTrkname.setAdapter(ap);
                    trk = MyConfig.GetCurTrk();
                    for (int i = 0; i < lstNames.size(); i++) {
                        if (lstNames.get(i).equals(trk)) {
                            spTrkname.setSelection(i);
                            break;
                        }
                    }
                }
                break;
            }
            case R.id.btn_ok: {
                if (spTrkname.getCount() > 0) {
                    MyConfig.SetOpenTrack(swIsOpenTrk.isChecked());
                    MyConfig.SetShowTrack(swIsShowTrk.isChecked());
                    MyConfig.SetShowQqTrack(swIsShowQqTrk.isChecked());
                    String trk = spTrkname.getSelectedItem().toString();
                    MyConfig.SetCurTrk(trk);

                    TrackInfo info = lstTrkInfos.get(spTrkname.getSelectedItemPosition());
                    result = info;
                } else {
                    MyConfig.SetCurTrk("");
                    MyConfig.SetOpenTrack(false);
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

    public TrackInfo showDialog() {
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
}
