package com.tdgeos.tanhui;

import java.util.List;

import com.tdgeos.dlg.base.MyDatePickerDialog;
import com.tdgeos.dlg.yddc.TrackDialog;
import com.tdgeos.dlg.yddc.WorkerCheckDialog;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.TrackInfo;
import com.tdgeos.yangdi.WorkerInfo;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SlqcKpfm extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private EditText etZtmc = null;
    private EditText etYdh = null;
    private EditText etYdxz = null;
    private EditText etYdmj = null;
    private EditText etDlzzb = null;
    private EditText etDlhzb = null;
    private EditText etYdjj = null;
    private EditText etDxttfh = null;
    private EditText etWph = null;
    private EditText etDfxzbm = null;
    private EditText etLyxzbm = null;

    private EditText etShi = null;
    private EditText etXian = null;
    private EditText etXiang = null;
    private EditText etCun = null;
    private EditText etXdm = null;

    private EditText etLyqyj = null;
    private EditText etZrbhq = null;
    private EditText etSlgy = null;
    private EditText etGylc = null;
    private EditText etJtlc = null;

    private EditText etDcy = null;
    private EditText etDcydw = null;
    private EditText etXd = null;
    private EditText etXddw = null;
    private EditText etJcy = null;
    private EditText etJcydw = null;

    private EditText etDcrq = null;
    private EditText etJcrq = null;

    private EditText etCfsj = null;
    private EditText etDdsj = null;
    private EditText etJssj = null;
    private EditText etFhsj = null;

    private EditText etGpsType = null;
    private EditText etGpsDis = null;
    private EditText etGpsBegin = null;
    private EditText etGpsEnd = null;
    private EditText etTujing = null;
    private EditText etYdPhoto = null;
    private EditText etYmPhoto = null;

    private Button btnUser = null;
    private Button btnXd = null;
    private Button btnJcy = null;
    private Button btnTrack = null;
    private Button btnDcrq = null;
    private Button btnJcrq = null;
    private Button btnCfsj = null;
    private Button btnDdsj = null;
    private Button btnJssj = null;
    private Button btnFhsj = null;
    private Button btnGpsBegin = null;
    private Button btnGpsEnd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_kpfm);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[0];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        btnUser = (Button) findViewById(R.id.btn_worker);
        btnUser.setOnClickListener(this);
        btnXd = (Button) findViewById(R.id.btn_xd);
        btnXd.setOnClickListener(this);
        btnJcy = (Button) findViewById(R.id.btn_jcy);
        btnJcy.setOnClickListener(this);
        btnTrack = (Button) findViewById(R.id.btn_track);
        btnTrack.setOnClickListener(this);
        btnDcrq = (Button) findViewById(R.id.btn_dcrq);
        btnDcrq.setOnClickListener(this);
        btnJcrq = (Button) findViewById(R.id.btn_jcrq);
        btnJcrq.setOnClickListener(this);
        btnCfsj = (Button) findViewById(R.id.btn_cfsj);
        btnCfsj.setOnClickListener(this);
        btnDdsj = (Button) findViewById(R.id.btn_ddsj);
        btnDdsj.setOnClickListener(this);
        btnJssj = (Button) findViewById(R.id.btn_jssj);
        btnJssj.setOnClickListener(this);
        btnFhsj = (Button) findViewById(R.id.btn_fhsj);
        btnFhsj.setOnClickListener(this);
        btnGpsBegin = (Button) findViewById(R.id.btn_gps_begin);
        btnGpsBegin.setOnClickListener(this);
        btnGpsEnd = (Button) findViewById(R.id.btn_gps_end);
        btnGpsEnd.setOnClickListener(this);

        etZtmc = (EditText) findViewById(R.id.et_ztmc);
        etYdh = (EditText) findViewById(R.id.et_ydh);
        etYdxz = (EditText) findViewById(R.id.et_ydxz);
        etYdmj = (EditText) findViewById(R.id.et_ydmj);
        etDlzzb = (EditText) findViewById(R.id.et_dlzzb);
        etDlhzb = (EditText) findViewById(R.id.et_dlhzb);
        etYdjj = (EditText) findViewById(R.id.et_ydjj);
        etDxttfh = (EditText) findViewById(R.id.et_dxttfh);
        etWph = (EditText) findViewById(R.id.et_wph);
        etDfxzbm = (EditText) findViewById(R.id.et_dfxzbm);
        etLyxzbm = (EditText) findViewById(R.id.et_lyxzbm);
        /*
        spLyxzbm = (Spinner)findViewById(R.id.sp_lyxzbm);
        List<String> lst = Resmgr.GetValueList("lyxzbm");
    	ArrayAdapter<String> ap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst);
    	ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spLyxzbm.setAdapter(ap);
    	*/

        etShi = (EditText) findViewById(R.id.et_shi);
        etXian = (EditText) findViewById(R.id.et_xian);
        etXiang = (EditText) findViewById(R.id.et_xiang);
        etCun = (EditText) findViewById(R.id.et_cun);
        etXdm = (EditText) findViewById(R.id.et_xdm);


        etLyqyj = (EditText) findViewById(R.id.et_lyqyj);
        etZrbhq = (EditText) findViewById(R.id.et_zrbhq);
        etSlgy = (EditText) findViewById(R.id.et_slgy);
        etGylc = (EditText) findViewById(R.id.et_gylc);
        etJtlc = (EditText) findViewById(R.id.et_jtlc);

        etDcy = (EditText) findViewById(R.id.et_dcy);
        etDcydw = (EditText) findViewById(R.id.et_dcygzdw);
        etXd = (EditText) findViewById(R.id.et_xd);
        etXddw = (EditText) findViewById(R.id.et_xdgzdw);
        etJcy = (EditText) findViewById(R.id.et_jcy);
        etJcydw = (EditText) findViewById(R.id.et_jcygzdw);

        etDcrq = (EditText) findViewById(R.id.et_dcrq);
        etJcrq = (EditText) findViewById(R.id.et_jcrq);

        etCfsj = (EditText) findViewById(R.id.et_zdcfsj);
        etDdsj = (EditText) findViewById(R.id.et_zdydbzsj);
        etJssj = (EditText) findViewById(R.id.et_dcjssj);
        etFhsj = (EditText) findViewById(R.id.et_fhzdsj);

        etGpsType = (EditText) findViewById(R.id.et_gps_type);
        etGpsDis = (EditText) findViewById(R.id.et_gps_dis);
        etGpsBegin = (EditText) findViewById(R.id.et_gps_begin);
        etGpsEnd = (EditText) findViewById(R.id.et_gps_end);
        etTujing = (EditText) findViewById(R.id.et_tujing);
        etYdPhoto = (EditText) findViewById(R.id.et_ydphoto);
        etYmPhoto = (EditText) findViewById(R.id.et_ymphoto);

        etJcy.setEnabled(false);
        etJcydw.setEnabled(false);
        //etJcrq.setEnabled(false);
        etYdh.setEnabled(false);
        etDlzzb.setEnabled(false);
        etDlhzb.setEnabled(false);
        etZtmc.setEnabled(false);
        etYdxz.setEnabled(false);
        etYdmj.setEnabled(false);
        etYdjj.setEnabled(false);
        etDcy.setEnabled(false);
        etDcydw.setEnabled(false);
        etXd.setEnabled(false);
        etXddw.setEnabled(false);
        etDxttfh.setEnabled(false);
        etDfxzbm.setEnabled(false);
        etLyxzbm.setEnabled(false);
        etShi.setEnabled(false);
        etXian.setEnabled(false);
        etLyqyj.setEnabled(false);

        etDcrq.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidDate(str)) {
                    etDcrq.setTextColor(Color.BLACK);
                } else {
                    etDcrq.setTextColor(Color.RED);
                }
            }
        });

        etCfsj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etCfsj.setTextColor(Color.BLACK);
                } else {
                    etCfsj.setTextColor(Color.RED);
                }
            }
        });

        etDdsj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etDdsj.setTextColor(Color.BLACK);
                } else {
                    etDdsj.setTextColor(Color.RED);
                }
            }
        });

        etGpsBegin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etGpsBegin.setTextColor(Color.BLACK);
                } else {
                    etGpsBegin.setTextColor(Color.RED);
                }
            }
        });

        etGpsEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etGpsEnd.setTextColor(Color.BLACK);
                } else {
                    etGpsEnd.setTextColor(Color.RED);
                }
            }
        });

        etJssj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etJssj.setTextColor(Color.BLACK);
                } else {
                    etJssj.setTextColor(Color.RED);
                }
            }
        });

        etFhsj.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = arg0.toString();
                if (YangdiMgr.IsValidTime(str)) {
                    etFhsj.setTextColor(Color.BLACK);
                } else {
                    etFhsj.setTextColor(Color.RED);
                }
            }
        });

        setData();

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("是否需要保存数据？");
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    boolean b = save();
                    if (iStatus != 2) {
                        iStatus = 1;
                    } else if (!b) {
                        iStatus = 1;
                    }
                    String sql = "update dczt set kpfm = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcKpfm.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcKpfm.this.finish();
                }
            });
            builder.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0: {
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close: {
                this.finish();
                break;
            }
            case R.id.btn_save: {
                boolean b = save();
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (!b) {
                    iStatus = 1;
                }
                String sql = "update dczt set kpfm = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                boolean b = save();
                if (b) {
                    iStatus = 2;
                } else {
                    iStatus = 1;
                }
                String sql = "update dczt set kpfm = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);

                finish();
                break;
            }
            case R.id.btn_worker: {
                String[] ss = new WorkerCheckDialog(this, 0).showDialog();
                if (ss != null) {
                    etDcy.setText(ss[0]);
                    etDcydw.setText(ss[1]);
                }
                break;
            }
            case R.id.btn_xd: {
                String[] ss = new WorkerCheckDialog(this, 2).showDialog();
                if (ss != null) {
                    etXd.setText(ss[0]);
                    etXddw.setText(ss[1]);
                }
                break;
            }
            case R.id.btn_jcy: {
                String[] ss = new WorkerCheckDialog(this, 1).showDialog();
                if (ss != null) {
                    etJcy.setText(ss[0]);
                    etJcydw.setText(ss[1]);
                }
                break;
            }
            case R.id.btn_track: {
                TrackDialog dlg = new TrackDialog(this, ydh);
                TrackInfo trk = dlg.showDialog();
                if (trk != null && trk.count > 0) {
                    etGpsDis.setText(String.valueOf(trk.length));
                    etGpsBegin.setText(MyFuns.DateTimeToTimeS(trk.begin));
                    etGpsEnd.setText(MyFuns.DateTimeToTimeS(trk.end));
                }
                break;
            }
            case R.id.btn_dcrq: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 1);
                String str = dlg.showDialog();
                if (str != null) {
                    etDcrq.setText(str);
                }
                break;
            }
            case R.id.btn_jcrq: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 1);
                String str = dlg.showDialog();
                if (str != null) {
                    etJcrq.setText(str);
                }
                break;
            }
            case R.id.btn_cfsj: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etCfsj.setText(str);
                }
                break;
            }
            case R.id.btn_ddsj: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etDdsj.setText(str);
                }
                break;
            }
            case R.id.btn_jssj: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etJssj.setText(str);
                }
                break;
            }
            case R.id.btn_fhsj: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etFhsj.setText(str);
                }
                break;
            }
            case R.id.btn_gps_begin: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etGpsBegin.setText(str);
                }
                break;
            }
            case R.id.btn_gps_end: {
                MyDatePickerDialog dlg = new MyDatePickerDialog(this, 2);
                String str = dlg.showDialog();
                if (str != null) {
                    etGpsEnd.setText(str);
                }
                break;
            }
        }
    }

    private void setData() {
        etZtmc.setText("上海市");
        etYdxz.setText("正方形");
        etYdmj.setText("0.08");

        etYdjj.setText("2x2公里");
        etDxttfh.setText("");
        etWph.setText("");
        etDfxzbm.setText("");
        etLyxzbm.setText("");
        etShi.setText("");
        etXian.setText("");
        etXiang.setText("");
        etCun.setText("");
        etXdm.setText("");
        etLyqyj.setText("");
        etZrbhq.setText("");
        etSlgy.setText("");
        etGylc.setText("");
        etJtlc.setText("");
        etDcy.setText("");
        etDcydw.setText("");
        etXd.setText("");
        etXddw.setText("");
        etJcy.setText("");
        etJcydw.setText("");
        etDcrq.setText("");
        etJcrq.setText("");
        etCfsj.setText("");
        etDdsj.setText("");
        etJssj.setText("");
        etFhsj.setText("");

        etYdh.setText(String.valueOf(ydh));

        String sql = "select * from kpfm where ydh = '" + ydh + "'";
        String[][] sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            //etZtmc.setText(sss[0][0]);
            //etYdh.setText(sss[0][1]);
            //etYdxz.setText(sss[0][2]);
            //etYdmj.setText(sss[0][3]);
            //etDlzzb.setText(sss[0][4]);
            //etDlhzb.setText(sss[0][5]);
            //etYdjj.setText(sss[0][6]);
            //etDxttfh.setText(sss[0][7]);
            //etWph.setText(sss[0][8]);
            //etDfxzbm.setText(sss[0][9]);
            etLyxzbm.setText(sss[0][10]);
            //etShi.setText(sss[0][11]);
            //etXian.setText(sss[0][12]);
            etXiang.setText(sss[0][13]);
            etCun.setText(sss[0][14]);
            etXdm.setText(sss[0][15]);

            //etLyqyj.setText(sss[0][16]);
            etZrbhq.setText(sss[0][17]);
            etSlgy.setText(sss[0][18]);
            etGylc.setText(sss[0][19]);
            etJtlc.setText(sss[0][20]);

            //etXd.setText(sss[0][21]);
            //etXddw.setText(sss[0][22]);
            etDcrq.setText(sss[0][23]);
        }

        YDInfo yd = Setmgr.GetTask(ydh);
        etDxttfh.setText(yd.dxttfh);
        etDlzzb.setText(String.valueOf(yd.zzb));
        etDlhzb.setText(String.valueOf(yd.hzb));
        String xian = Qianqimgr.GetXianName(yd.xian);
        String shi = Qianqimgr.GetShiName(yd.xian);
        if (etXian.getText().toString().equals("")) etXian.setText(xian);
        if (etShi.getText().toString().equals("")) etShi.setText(shi);
        if (etDfxzbm.getText().toString().equals("")) etDfxzbm.setText(String.valueOf(yd.xian));

        sss = Qianqimgr.GetQqKpfm(ydh);
        if (sss != null) {
            if (etXiang.getText().toString().equals("")) etXiang.setText(sss[0][5]);
            if (etCun.getText().toString().equals("")) etCun.setText(sss[0][6]);
            if (etXdm.getText().toString().equals("")) etXdm.setText(sss[0][7]);
        }

        //etLyqyj.setText(Qianqimgr.GetJuNameByYdh(ydh));

        sql = "select " +
                "ifnull(tujing, '') as tujing," +
                "ifnull(cfsj,'') as cfsj," +
                "ifnull(zdsj,'') as zdsj," +
                "ifnull(jssj,'') as jssj," +
                "ifnull(fhsj,'') as fhsj," +
                "ifnull(gps_type,'') as gps_type," +
                "ifnull(gps_dis,'') as gps_dis," +
                "ifnull(gps_begin,'') as gps_begin," +
                "ifnull(gps_end,'') as gps_end," +
                "ifnull(ydphoto,'') as ydphoto," +
                "ifnull(ymphoto,'') as ymphoto " +
                "from qt where ydh = '" + ydh + "'";
        sss = YangdiMgr.SelectData(ydh, sql);
        if (sss != null) {
            etTujing.setText(sss[0][0]);
            etCfsj.setText(sss[0][1]);
            etDdsj.setText(sss[0][2]);
            etJssj.setText(sss[0][3]);
            etFhsj.setText(sss[0][4]);
            etGpsType.setText(sss[0][5]);
            etGpsDis.setText(sss[0][6]);
            etGpsBegin.setText(sss[0][7]);
            etGpsEnd.setText(sss[0][8]);
            etYdPhoto.setText(sss[0][9]);
            etYmPhoto.setText(sss[0][10]);
        }

        if (etGpsType.getText().toString().equals("")) etGpsType.setText("三星P601");
        if (etGpsDis.getText().toString().equals("")) {
            List<TrackInfo> lst = YangdiMgr.GetTrackInfo(ydh);
            if (lst.size() > 0) {
                int pos = lst.size() - 1;
                String str = MyConfig.GetCurTrk();
                if (str != null && !str.equals("")) {
                    for (int i = 0; i < lst.size(); i++) {
                        if (str.equals(lst.get(i).name)) {
                            pos = i;
                            break;
                        }
                    }
                }
                TrackInfo trk = lst.get(pos);
                if (trk.count > 0) {
                    etGpsDis.setText(String.valueOf(trk.length));
                    etGpsBegin.setText(MyFuns.DateTimeToTimeS(trk.begin));
                    etGpsEnd.setText(MyFuns.DateTimeToTimeS(trk.end));
                }
            }
        }

        List<WorkerInfo> lstWorkers = YangdiMgr.GetWorkerList(ydh);
        if (lstWorkers != null && lstWorkers.size() > 0) {
            String dcy = null;
            String dcydw = null;
            String xd = null;
            String xddw = null;
            String jcy = null;
            String jcydw = null;
            for (int i = 0; i < lstWorkers.size(); i++) {
                if (lstWorkers.get(i).type == 0) {
                    if (dcy == null) {
                        dcy = lstWorkers.get(i).name;
                        dcydw = lstWorkers.get(i).company;
                    } else {
                        dcy += "," + lstWorkers.get(i).name;
                        dcydw += "," + lstWorkers.get(i).company;
                    }
                }
                if (lstWorkers.get(i).type == 1) {
                    if (jcy == null) {
                        jcy = lstWorkers.get(i).name;
                        jcydw = lstWorkers.get(i).company;
                    } else {
                        jcy += "," + lstWorkers.get(i).name;
                        jcydw += "," + lstWorkers.get(i).company;
                    }
                }
                if (lstWorkers.get(i).type == 2) {
                    if (xd == null) {
                        xd = lstWorkers.get(i).name;
                        xddw = lstWorkers.get(i).company;
                    } else {
                        xd += "," + lstWorkers.get(i).name;
                        xddw += "," + lstWorkers.get(i).company;
                    }
                }
            }
            if (dcy != null) etDcy.setText(dcy);
            if (dcydw != null) etDcydw.setText(dcydw);
            if (xd != null) etXd.setText(xd);
            if (xddw != null) etXddw.setText(xddw);
            if (jcy != null) etJcy.setText(jcy);
            if (jcydw != null) etJcydw.setText(jcydw);
        }

        String jcrq = YangdiMgr.GetJcrq(ydh);
        if (jcrq != null) etJcrq.setText(jcrq);

        if (!YangdiMgr.IsValidDate(etDcrq.getText().toString())) etDcrq.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etCfsj.getText().toString())) etCfsj.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etDdsj.getText().toString())) etDdsj.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etJssj.getText().toString())) etJssj.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etFhsj.getText().toString())) etFhsj.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etGpsBegin.getText().toString()))
            etGpsBegin.setTextColor(Color.RED);
        if (!YangdiMgr.IsValidTime(etGpsEnd.getText().toString())) etGpsEnd.setTextColor(Color.RED);
    }

    private boolean save() {
        String ztmc = etZtmc.getText().toString();
        String ydxz = etYdxz.getText().toString();
        String ydmj = etYdmj.getText().toString();
        String zzb = etDlzzb.getText().toString();
        String hzb = etDlhzb.getText().toString();
        String ydjj = etYdjj.getText().toString();
        String dxttfh = etDxttfh.getText().toString();
        String wph = etWph.getText().toString();
        String dfxzbm = etDfxzbm.getText().toString();
        String lyxzbm = etLyxzbm.getText().toString();
        String shi = etShi.getText().toString();
        String xian = etXian.getText().toString();
        String xiang = etXiang.getText().toString();
        String cun = etCun.getText().toString();
        String xdm = etXdm.getText().toString();
        String lyqyj = etLyqyj.getText().toString();
        String zrbhq = etZrbhq.getText().toString();
        String slgy = etSlgy.getText().toString();
        String gylc = etGylc.getText().toString();
        String jtlc = etJtlc.getText().toString();

        //String xd = etXd.getText().toString();
        //String xddw = etXddw.getText().toString();
        String dcrq = etDcrq.getText().toString();

        String cfsj = etCfsj.getText().toString();
        String ddsj = etDdsj.getText().toString();
        String jssj = etJssj.getText().toString();
        String fhsj = etFhsj.getText().toString();

        String tujing = etTujing.getText().toString();
        String gps_type = etGpsType.getText().toString();
        String gps_dis = etGpsDis.getText().toString();
        String gps_begin = etGpsBegin.getText().toString();
        String gps_end = etGpsEnd.getText().toString();
        String ydphoto = etYdPhoto.getText().toString();
        String ymphoto = etYmPhoto.getText().toString();

        String sql = "select * from kpfm where ydh = '" + ydh + "'";
        if (!YangdiMgr.QueryExists(ydh, sql)) {
            sql = "insert into kpfm values("
                    + "'" + ztmc + "', "
                    + "'" + ydh + "', "
                    + "'" + ydxz + "', "
                    + "'" + ydmj + "', "
                    + "'" + zzb + "', "
                    + "'" + hzb + "', "
                    + "'" + ydjj + "', "
                    + "'" + dxttfh + "', "
                    + "'" + wph + "', "
                    + "'" + dfxzbm + "', "
                    + "'" + lyxzbm + "', "
                    + "'" + shi + "', "
                    + "'" + xian + "', "
                    + "'" + xiang + "', "
                    + "'" + cun + "', "
                    + "'" + xdm + "', "
                    + "'" + lyqyj + "', "
                    + "'" + zrbhq + "', "
                    + "'" + slgy + "', "
                    + "'" + gylc + "', "
                    + "'" + jtlc + "', "
                    + "'', "
                    + "'', "
                    + "'" + dcrq + "' "
                    + ")";
            YangdiMgr.ExecSQL(ydh, sql);
        } else {
            sql = "update kpfm set "
                    + "ztmc = '" + ztmc + "', "
                    + "ydh = '" + ydh + "', "
                    + "ydxz = '" + ydxz + "', "
                    + "ydmj = '" + ydmj + "', "
                    + "zzb = '" + zzb + "', "
                    + "hzb = '" + hzb + "', "
                    + "ydjj = '" + ydjj + "', "
                    + "dxttfh = '" + dxttfh + "', "
                    + "wph = '" + wph + "', "
                    + "dfxzbm = '" + dfxzbm + "', "
                    + "lyxzbm = '" + lyxzbm + "', "
                    + "shi = '" + shi + "', "
                    + "xian = '" + xian + "', "
                    + "xiang = '" + xiang + "', "
                    + "cun = '" + cun + "', "
                    + "xdm = '" + xdm + "', "
                    + "lyqyj = '" + lyqyj + "', "
                    + "zrbhq = '" + zrbhq + "', "
                    + "slgy = '" + slgy + "', "
                    + "gylc = '" + gylc + "', "
                    + "jtlc = '" + jtlc + "', "
                    + "xd = '', "
                    + "xddw = '', "
                    + "dcrq = '" + dcrq + "' "
                    + " where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
        }

        sql = "select ydh from qt where ydh = '" + ydh + "'";
        if (!YangdiMgr.QueryExists(ydh, sql)) {
            sql = "insert into qt(ydh, tujing,cfsj,zdsj,jssj,fhsj,gps_type,gps_dis,gps_begin,gps_end,ydphoto,ymphoto) values("
                    + "'" + ydh + "', "
                    + "'" + tujing + "', "
                    + "'" + cfsj + "', "
                    + "'" + ddsj + "', "
                    + "'" + jssj + "', "
                    + "'" + fhsj + "', "
                    + "'" + gps_type + "', "
                    + "'" + gps_dis + "', "
                    + "'" + gps_begin + "', "
                    + "'" + gps_end + "', "
                    + "'" + ydphoto + "', "
                    + "'" + ymphoto + "'"
                    + ")";
            YangdiMgr.ExecSQL(ydh, sql);
        } else {
            sql = "update qt set "
                    + "tujing = '" + tujing + "', "
                    + "cfsj = '" + cfsj + "', "
                    + "zdsj = '" + ddsj + "', "
                    + "jssj = '" + jssj + "', "
                    + "fhsj = '" + fhsj + "', "
                    + "gps_type = '" + gps_type + "', "
                    + "gps_dis = '" + gps_dis + "', "
                    + "gps_begin = '" + gps_begin + "', "
                    + "gps_end = '" + gps_end + "', "
                    + "ydphoto = '" + ydphoto + "', "
                    + "ymphoto = '" + ymphoto + "' "
                    + " where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
        }

        sql = "delete from worker where ydh = '" + ydh + "'";
        YangdiMgr.ExecSQL(ydh, sql);

        String str = etDcy.getText().toString();
        if (str != null && !str.equals("")) {
            String[] ss = MyFuns.Split(str, ',');
            for (int i = 0; i < ss.length; i++) {
                WorkerInfo worker = Setmgr.GetWorker(ss[i], 0);
                YangdiMgr.AddWorker(ydh, worker);
            }
        }

        String str2 = etXd.getText().toString();
        if (str2 != null && !str2.equals("")) {
            String[] ss = MyFuns.Split(str2, ',');
            for (int i = 0; i < ss.length; i++) {
                WorkerInfo worker = Setmgr.GetWorker(ss[i], 2);
                YangdiMgr.AddWorker(ydh, worker);
            }
        }

        String str3 = etJcy.getText().toString();
        if (str3 != null && !str3.equals("")) {
            String[] ss = MyFuns.Split(str3, ',');
            for (int i = 0; i < ss.length; i++) {
                WorkerInfo worker = Setmgr.GetWorker(ss[i], 1);
                YangdiMgr.AddWorker(ydh, worker);
            }
        }

        String jcrq = etJcrq.getText().toString();
        if (YangdiMgr.IsValidDate(jcrq)) {
            YangdiMgr.SetJcrq(ydh, jcrq);
        }

        if (str.equals("")) return false;

        if (!YangdiMgr.IsValidDate(dcrq)) return false;
        if (!YangdiMgr.IsValidTime(cfsj)) return false;
        if (!YangdiMgr.IsValidTime(ddsj)) return false;
        //if(!YangdiMgr.IsValidTime(jssj)) return false;
        //if(!YangdiMgr.IsValidTime(fhsj)) return false;
        //if(!YangdiMgr.IsValidTime(gps_begin)) return false;
        //if(!YangdiMgr.IsValidTime(gps_end)) return false;

        return true;
    }
}
