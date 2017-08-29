package com.tdgeos.tanhui;

import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SlqcYdbh extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private EditText etQqdl = null;
    private EditText etBqdl = null;
    private EditText etDlbhyy = null;
    private EditText etQqlinzh = null;
    private EditText etBqlinzh = null;
    private EditText etLinzhbhyy = null;
    private EditText etQqqy = null;
    private EditText etBqqy = null;
    private EditText etQybhyy = null;
    private EditText etQqyssz = null;
    private EditText etBqyssz = null;
    private EditText etYsszbhyy = null;
    private EditText etQqlingz = null;
    private EditText etBqlingz = null;
    private EditText etLingzbhyy = null;
    private EditText etQqzblx = null;
    private EditText etBqzblx = null;
    private EditText etZblxbhyy = null;
    private EditText etBz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_ydbh);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[14];

        btnClose = (Button) findViewById(R.id.btn_close);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        etQqdl = (EditText) findViewById(R.id.et_dl_qq);
        etBqdl = (EditText) findViewById(R.id.et_dl_bq);
        etDlbhyy = (EditText) findViewById(R.id.et_dl_bhyy);
        etQqlinzh = (EditText) findViewById(R.id.et_linzh_qq);
        etBqlinzh = (EditText) findViewById(R.id.et_linzh_bq);
        etLinzhbhyy = (EditText) findViewById(R.id.et_linzh_bhyy);
        etQqqy = (EditText) findViewById(R.id.et_qy_qq);
        etBqqy = (EditText) findViewById(R.id.et_qy_bq);
        etQybhyy = (EditText) findViewById(R.id.et_qy_bhyy);
        etQqyssz = (EditText) findViewById(R.id.et_yssz_qq);
        etBqyssz = (EditText) findViewById(R.id.et_yssz_bq);
        etYsszbhyy = (EditText) findViewById(R.id.et_yssz_bhyy);
        etQqlingz = (EditText) findViewById(R.id.et_lingz_qq);
        etBqlingz = (EditText) findViewById(R.id.et_lingz_bq);
        etLingzbhyy = (EditText) findViewById(R.id.et_lingz_bhyy);
        etQqzblx = (EditText) findViewById(R.id.et_zblx_qq);
        etBqzblx = (EditText) findViewById(R.id.et_zblx_bq);
        etZblxbhyy = (EditText) findViewById(R.id.et_zblx_bhyy);
        etBz = (EditText) findViewById(R.id.et_tsdd);

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
                    String sql = "update dczt set ydbh = '" + iStatus + "' where ydh = '" + ydh + "'";
                    YangdiMgr.ExecSQL(ydh, sql);
                    SlqcYdbh.this.finish();
                }
            });
            builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SlqcYdbh.this.finish();
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
                String sql = "update dczt set ydbh = '" + iStatus + "' where ydh = '" + ydh + "'";
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
                String sql = "update dczt set ydbh = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);

                if (b) finish();
                break;
            }
        }
    }

    private void setData() {
        String[] qq = YangdiMgr.GetQqbhyz(ydh);
        String[] bq = YangdiMgr.GetBqbhyz(ydh);
        String[] ydbh = YangdiMgr.GetYdbhyy(ydh);
        if (qq != null) {
            try {
                int code = -1;
                //code = Integer.parseInt(qq[0]);
                code = YangdiMgr.GetQqdl(ydh);
                etQqdl.setText(Resmgr.GetValueByCode("dl", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(qq[1]);
                etQqlinzh.setText(Resmgr.GetValueByCode("linzh", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(qq[2]);
                etQqqy.setText(Resmgr.GetValueByCode("qy", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(qq[3]);
                if (code > 0) etQqyssz.setText(code + " " + Resmgr.GetSzName(code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(qq[4]);
                etQqlingz.setText(Resmgr.GetValueByCode("lingz", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(qq[5]);
                etQqzblx.setText(Resmgr.GetValueByCode("zblx", code));
            } catch (Exception e) {
            }
        }
        if (bq != null) {
            try {
                int code = -1;
                code = Integer.parseInt(bq[0]);
                etBqdl.setText(Resmgr.GetValueByCode("dl", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(bq[1]);
                etBqlinzh.setText(Resmgr.GetValueByCode("linzh", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(bq[2]);
                etBqqy.setText(Resmgr.GetValueByCode("qy", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(bq[3]);
                if (code > 0) etBqyssz.setText(code + " " + Resmgr.GetSzName(code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(bq[4]);
                etBqlingz.setText(Resmgr.GetValueByCode("lingz", code));
            } catch (Exception e) {
            }
            try {
                int code = -1;
                code = Integer.parseInt(bq[5]);
                etBqzblx.setText(Resmgr.GetValueByCode("zblx", code));
            } catch (Exception e) {
            }
        }
        if (ydbh != null) {
            etDlbhyy.setText(ydbh[0]);
            etLinzhbhyy.setText(ydbh[1]);
            etQybhyy.setText(ydbh[2]);
            etYsszbhyy.setText(ydbh[3]);
            etLingzbhyy.setText(ydbh[4]);
            etZblxbhyy.setText(ydbh[5]);
            etBz.setText(ydbh[6]);
        }
    }

    private boolean save() {
        String dl = etDlbhyy.getText().toString();
        String linzh = etLinzhbhyy.getText().toString();
        String qy = etQybhyy.getText().toString();
        String yssz = etYsszbhyy.getText().toString();
        String lingz = etLingzbhyy.getText().toString();
        String zblx = etZblxbhyy.getText().toString();
        String bz = etBz.getText().toString();

        String sql = "select * from ydbh where ydh = '" + ydh + "'";
        if (YangdiMgr.QueryExists(ydh, sql)) {
            sql = "update ydbh set "
                    + "dlbhyy = '" + dl + "', "
                    + "linzhbhyy = '" + linzh + "', "
                    + "qybhyy = '" + qy + "', "
                    + "ysszbhyy = '" + yssz + "', "
                    + "lingzbhyy = '" + lingz + "', "
                    + "zblxbhyy = '" + zblx + "', "
                    + "bz = '" + bz + "' "
                    + " where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
        } else {
            sql = "insert into ydbh(ydh, dlbhyy, linzhbhyy, qybhyy, ysszbhyy, lingzbhyy, zblxbhyy, bz) values("
                    + "'" + ydh + "', "
                    + "'" + dl + "', "
                    + "'" + linzh + "', "
                    + "'" + qy + "', "
                    + "'" + yssz + "', "
                    + "'" + lingz + "', "
                    + "'" + zblx + "', "
                    + "'" + bz + "')";
            YangdiMgr.ExecSQL(ydh, sql);
        }

        String qqdl = etQqdl.getText().toString();
        String bqdl = etBqdl.getText().toString();
        if (!qqdl.equals("") && !qqdl.equals(bqdl) && dl.equals("")) {
            Toast.makeText(this, "地类已经发生变化，地类变化原因不能为空！", 1).show();
            return false;
        }
        String qqlinzh = etQqlinzh.getText().toString();
        String bqlinzh = etBqlinzh.getText().toString();
        if (!qqlinzh.equals("") && !qqlinzh.equals(bqlinzh) && linzh.equals("")) {
            Toast.makeText(this, "林种已经发生变化，林种变化原因不能为空！", 1).show();
            return false;
        }
        String qqqy = etQqqy.getText().toString();
        String bqqy = etBqqy.getText().toString();
        if (!qqqy.equals("") && !qqqy.equals(bqqy) && qy.equals("")) {
            Toast.makeText(this, "起源已经发生变化，起源变化原因不能为空！", 1).show();
            return false;
        }
        String qqyssz = etQqyssz.getText().toString();
        String bqyssz = etBqyssz.getText().toString();
        if (!qqyssz.equals("") && !qqyssz.equals(bqyssz) && yssz.equals("")) {
            Toast.makeText(this, "优势树种已经发生变化，优势树种变化原因不能为空！", 1).show();
            return false;
        }
        String qqlingz = etQqlingz.getText().toString();
        String bqlingz = etBqlingz.getText().toString();
        if (!qqlingz.equals("") && !qqlingz.equals(bqlingz) && lingz.equals("")) {
            Toast.makeText(this, "龄组已经发生变化，龄组变化原因不能为空！", 1).show();
            return false;
        }
        String qqzblx = etQqzblx.getText().toString();
        String bqzblx = etBqzblx.getText().toString();
        if (!qqzblx.equals("") && !qqzblx.equals(bqzblx) && zblx.equals("")) {
            Toast.makeText(this, "植被类型已经发生变化，植被类型变化原因不能为空！", 1).show();
            return false;
        }
        return true;
    }
}
