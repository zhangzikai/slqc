package com.tdgeos.tanhui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyAboutDialog;
import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.CombineClientDialog;
import com.tdgeos.dlg.yddc.CombineServerDialog;
import com.tdgeos.dlg.yddc.CheckYangdiDialog;
import com.tdgeos.dlg.yddc.DataExportDialog;
import com.tdgeos.dlg.yddc.DataUploadDialog;
import com.tdgeos.dlg.yddc.SyssetDialog;
import com.tdgeos.dlg.yddc.YdhDialog;
import com.tdgeos.dlg.yddc.ZjclTypeDialog;
import com.tdgeos.dlg.yddc.ZpmcDialog;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFile;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MyDataInput extends Activity implements View.OnClickListener {
    private Button btnMenu = null;
    private PopupWindow popMenu = null;
    private ListView lvMenu = null;

    private TextView tvYdh = null;
    private TextView tvStatus = null;
    private Button btnYdh = null;
    private Button btnCheck = null;
    private Button btnTake = null;
    private Button btnPhoto = null;
    private Button btnTrk = null;

    private int ydh = 0;
    private int picType = 1;
    private int picYmh = 0;
    private String picInfo = null;
    private String picPath = null;
    private MyAboutDialog dlgAbout = null;

    private Button btnYddcjl = null;
    private Button btnYdydwzt = null;
    private Button btnYdwzt = null;
    private Button btnYdyxcljl = null;
    private Button btnYdzjcljl = null;
    private Button btnYdyzdcjl = null;
    private Button btnKjldcjl = null;
    private Button btnMmjcjl = null;
    private Button btnYmwzt = null;
    private Button btnPjymdcjl = null;
    private Button btnSlzhdcjl = null;
    private Button btnZbdcjl = null;
    private Button btnTrgxdcjl = null;
    private Button btnYdbhdcjl = null;
    private Button btnXmdcjl = null;
    private Button btnWclzlddcjl = null;
    private Button btnTrdc = null;
    private Button btnKlwdc = null;

    private ImageView ivYddcjl = null;
    private ImageView ivYdydwzt = null;
    private ImageView ivYdwzt = null;
    private ImageView ivYdyxcljl = null;
    private ImageView ivYdzjcljl = null;
    private ImageView ivYdyzdcjl = null;
    private ImageView ivKjldcjl = null;
    private ImageView ivMmjcjl = null;
    private ImageView ivYmwzt = null;
    private ImageView ivPjymdcjl = null;
    private ImageView ivSlzhdcjl = null;
    private ImageView ivZbdcjl = null;
    private ImageView ivTrgxdcjl = null;
    private ImageView ivYdbhdcjl = null;
    private ImageView ivXmdcjl = null;
    private ImageView ivWclzlddcjl = null;
    private ImageView ivTrdc = null;
    private ImageView ivKlwdc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        float dpiScale = getResources().getDisplayMetrics().densityDpi * 1.0f / 160;

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        MyConfig.SetCurYd(ydh);

        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        java.util.Map<String, String> item = new java.util.HashMap<String, String>();
        item.put("text", "数据导出");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "数据上传");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "数据合并");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "地图");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "技术标准");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "设置");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "关于");
        list.add(item);
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.lay_menu_item, new String[]{"text"}, new int[]{R.id.tv_item});
        lvMenu = new ListView(this);
        lvMenu.setAdapter(adapter);
        popMenu = new PopupWindow(lvMenu, (int) (120 * dpiScale), LayoutParams.WRAP_CONTENT);
        popMenu.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        popMenu.setOutsideTouchable(true);
        popMenu.setFocusable(true);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                popMenu.dismiss();
                switch (arg2) {
                    case 0: {
                        DataExportDialog dlg = new DataExportDialog(MyDataInput.this, ydh);
                        dlg.show();
                        break;
                    }
                    case 1: {
                        DataUploadDialog dlg = new DataUploadDialog(MyDataInput.this, ydh);
                        dlg.show();
                        break;
                    }
                    case 2: {
                        List<String> lst = new ArrayList<String>();
                        lst.add("接收数据");
                        lst.add("发送数据");
                        MyListDialog dlg = new MyListDialog(MyDataInput.this, "数据合并", lst);
                        int r = dlg.showDialog();
                        if (r == 0) {
                            CombineServerDialog dlgServer = new CombineServerDialog(MyDataInput.this, ydh);
                            dlgServer.show();
                        } else if (r == 1) {
                            CombineClientDialog dlgClient = new CombineClientDialog(MyDataInput.this, ydh);
                            dlgClient.show();
                        }
                        break;
                    }
                    case 3: {
                        if (YangdiMgr.MY_MAP_TYPE == 54) {
                            Intent intent = new Intent();
                            intent.putExtra("ydh", ydh);
                            intent.setClass(MyDataInput.this, MyMap.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("ydh", ydh);
                            intent.setClass(MyDataInput.this, MyMap84.class);
                            startActivity(intent);
                        }
                        MyDataInput.this.finish();
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent();
                        intent.setClass(MyDataInput.this, Web.class);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        SyssetDialog dlgSysset = new SyssetDialog(MyDataInput.this);
                        dlgSysset.show();
                        break;
                    }
                    case 6: {
                        dlgAbout.show();
                        break;
                    }
                }
            }
        });

        dlgAbout = new MyAboutDialog(this);

        btnMenu = (Button) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(this);

        tvYdh = (TextView) findViewById(R.id.tv_ydh);
        btnYdh = (Button) findViewById(R.id.btn_ydh);
        btnYdh.setOnClickListener(this);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);

        btnTake = (Button) findViewById(R.id.btn_take);
        btnPhoto = (Button) findViewById(R.id.btn_photo);
        btnTake.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnTrk = (Button) findViewById(R.id.btn_trk);
        btnTrk.setOnClickListener(this);

        btnYddcjl = (Button) findViewById(R.id.main_btn_yddcjl);
        btnYddcjl.setOnClickListener(this);
        btnYdydwzt = (Button) findViewById(R.id.main_btn_ydydwzt);
        btnYdydwzt.setOnClickListener(this);
        btnYdwzt = (Button) findViewById(R.id.main_btn_ydwzt);
        btnYdwzt.setOnClickListener(this);
        btnYdyxcljl = (Button) findViewById(R.id.main_btn_ydyxcljl);
        btnYdyxcljl.setOnClickListener(this);
        btnYdzjcljl = (Button) findViewById(R.id.main_btn_ydzjcljl);
        btnYdzjcljl.setOnClickListener(this);
        btnYdyzdcjl = (Button) findViewById(R.id.main_btn_ydyzdcjl);
        btnYdyzdcjl.setOnClickListener(this);
        btnKjldcjl = (Button) findViewById(R.id.main_btn_kjldcjl);
        btnKjldcjl.setOnClickListener(this);
        btnMmjcjl = (Button) findViewById(R.id.main_btn_mmjcjl);
        btnMmjcjl.setOnClickListener(this);
        btnYmwzt = (Button) findViewById(R.id.main_btn_ymwzt);
        btnYmwzt.setOnClickListener(this);
        btnPjymdcjl = (Button) findViewById(R.id.main_btn_pjymdcjl);
        btnPjymdcjl.setOnClickListener(this);
        btnSlzhdcjl = (Button) findViewById(R.id.main_btn_slzhdcjl);
        btnSlzhdcjl.setOnClickListener(this);
        btnZbdcjl = (Button) findViewById(R.id.main_btn_zbdcjl);
        btnZbdcjl.setOnClickListener(this);
        btnTrgxdcjl = (Button) findViewById(R.id.main_btn_trgxdcjl);
        btnTrgxdcjl.setOnClickListener(this);
        btnYdbhdcjl = (Button) findViewById(R.id.main_btn_ydbhdcjl);
        btnYdbhdcjl.setOnClickListener(this);
        btnXmdcjl = (Button) findViewById(R.id.main_btn_xmdcjl);
        btnXmdcjl.setOnClickListener(this);
        btnWclzlddcjl = (Button) findViewById(R.id.main_btn_wclzlddcjl);
        btnWclzlddcjl.setOnClickListener(this);
        btnTrdc = (Button) findViewById(R.id.main_btn_trdc);
        btnTrdc.setOnClickListener(this);
        btnKlwdc = (Button) findViewById(R.id.main_btn_klwdc);
        btnKlwdc.setOnClickListener(this);

        ivYddcjl = (ImageView) findViewById(R.id.main_iv_yddcjl);
        ivYdydwzt = (ImageView) findViewById(R.id.main_iv_ydydwzt);
        ivYdwzt = (ImageView) findViewById(R.id.main_iv_ydwzt);
        ivYdyxcljl = (ImageView) findViewById(R.id.main_iv_ydyxcljl);
        ivYdzjcljl = (ImageView) findViewById(R.id.main_iv_ydzjcljl);
        ivYdyzdcjl = (ImageView) findViewById(R.id.main_iv_ydyzdcjl);
        ivKjldcjl = (ImageView) findViewById(R.id.main_iv_kjldcjl);
        ivMmjcjl = (ImageView) findViewById(R.id.main_iv_mmjcjl);
        ivYmwzt = (ImageView) findViewById(R.id.main_iv_ymwzt);
        ivPjymdcjl = (ImageView) findViewById(R.id.main_iv_pjymdcjl);
        ivSlzhdcjl = (ImageView) findViewById(R.id.main_iv_slzhdcjl);
        ivZbdcjl = (ImageView) findViewById(R.id.main_iv_zbdcjl);
        ivTrgxdcjl = (ImageView) findViewById(R.id.main_iv_trgxdcjl);
        ivYdbhdcjl = (ImageView) findViewById(R.id.main_iv_ydbhdcjl);
        ivXmdcjl = (ImageView) findViewById(R.id.main_iv_xmdcjl);
        ivWclzlddcjl = (ImageView) findViewById(R.id.main_iv_wclzlddcjl);
        ivTrdc = (ImageView) findViewById(R.id.main_iv_trdc);
        ivKlwdc = (ImageView) findViewById(R.id.main_iv_klwdc);

        resetStatus();

        new InitTask().execute("");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        resetStatus();
        YangdiMgr.BackupData(this, ydh);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyConfig.SetOpenTrack(false);
        MyConfig.SetOpenLoc(false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0: {
                    if (picType == 1 || picType == 3)
                        YangdiMgr.InsertZp(ydh, picType, 0, picInfo, picPath);
                    else YangdiMgr.InsertZp(ydh, picType, picYmh, picInfo, picPath);
                    YangdiMgr.ResetZpCount(ydh);
                    MyFile.DeleteFile(picPath);
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
            case R.id.btn_menu: {
                popMenu.showAsDropDown(btnMenu);
                break;
            }
            case R.id.btn_ydh: {
                YdhDialog dlg = new YdhDialog(this, ydh);
                int r = dlg.showDialog();
                if (r > 0 && r != ydh) {
                    ydh = r;
                    MyConfig.SetCurYd(ydh);
                    YangdiMgr.InitYangdi(ydh);
                    int qqdl = YangdiMgr.GetQqdl(ydh);
                    int bqdl = YangdiMgr.GetBqdl(ydh);
                    String qq = Resmgr.GetValueByCode("dl", qqdl);
                    String bq = Resmgr.GetValueByCode("dl", bqdl);
                    if (qq.equals("")) qq = "无";
                    if (bq.equals("")) bq = "无";
                    tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");
                    new InitTask().execute("");
                }
                break;
            }
            case R.id.btn_check: {
                CheckYangdiDialog dlg = new CheckYangdiDialog(this, ydh);
                dlg.show();
                break;
            }
            case R.id.btn_take: {
                ZpmcDialog dlg = new ZpmcDialog(this, ydh, 0);
                String[] ss = dlg.showDialog();
                if (ss != null) {
                    picType = 1;
                    try {
                        picType = Integer.parseInt(ss[0]);
                    } catch (Exception e) {
                    }
                    picYmh = 0;
                    try {
                        picYmh = Integer.parseInt(ss[1]);
                    } catch (Exception e) {
                    }
                    picInfo = ss[2];
                    picPath = MyConfig.GetWorkdir() + "/temp/" + ss[3];
                    MyFile.DeleteFile(picPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picPath)));
                    startActivityForResult(intent, 0);
                }
                break;
            }
            case R.id.btn_photo: {
                if (YangdiMgr.GetZpCount(ydh) == 0) {
                    Toast.makeText(this, "该样地没有照片！", 1).show();
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.putExtra("type", 0);
                intent.setClass(this, SlqcPhoto.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_trk: {
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "回采航迹需要返回地图界面，是否继续回采？", "回采", "取消");
                if (!dlg.showDialog()) {
                    break;
                }

                if (YangdiMgr.MY_MAP_TYPE == 54) {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", ydh);
                    intent.putExtra("trk", 1);
                    intent.setClass(MyDataInput.this, MyMap.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", ydh);
                    intent.putExtra("trk", 1);
                    intent.setClass(MyDataInput.this, MyMap84.class);
                    startActivity(intent);
                }
                MyDataInput.this.finish();
                break;
            }
            case R.id.main_btn_yddcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcKpfm.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydydwzt: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYindiantu.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydwzt: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYangditu.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydyxcljl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYxcl.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydzjcljl: {
            /*
            int type = YangdiMgr.GetZjclType(ydh);
			if(type == -1)
			{
				ZjclTypeDialog dlg = new ZjclTypeDialog(this, 0);
				type = dlg.showDialog();
				if(type == -1) break;
				YangdiMgr.SetZjclType(ydh, type);
			}
			*/
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                //intent.putExtra("type", type);
                intent.setClass(this, SlqcZjcl.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydyzdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYdyz.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_kjldcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcKjl.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_mmjcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcMmjc.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ymwzt: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYmwzt.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_pjymdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcSgcl.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_slzhdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcSlzh.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_zbdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcZbdc.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_trgxdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcTrgx.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_ydbhdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcYdbh.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_xmdcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcXmdc.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_wclzlddcjl: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcWclzld.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_trdc: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcTrdc.class);
                startActivity(intent);
                break;
            }
            case R.id.main_btn_klwdc: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, SlqcKlwdc.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void resetStatus() {
        int qqdl = YangdiMgr.GetQqdl(ydh);
        int bqdl = YangdiMgr.GetBqdl(ydh);
        String qq = Resmgr.GetValueByCode("dl", qqdl);
        String bq = Resmgr.GetValueByCode("dl", bqdl);
        if (qq.equals("")) qq = "无";
        if (bq.equals("")) bq = "无";
        tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");

        boolean b = YangdiMgr.Check(ydh);
        if (b) {
            String sql = "update dczt set yangdi = '2' where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
            Setmgr.ResetTaskStatus(ydh, 2);
        } else {
            String sql = "update dczt set yangdi = '1' where ydh = '" + ydh + "'";
            YangdiMgr.ExecSQL(ydh, sql);
            Setmgr.ResetTaskStatus(ydh, 1);
        }

        MyCoord c = YangdiMgr.GetYdloc(ydh);
        String str_loc = c == null ? "，未定位" : "，已定位";

        int[] ii = YangdiMgr.GetDczt(ydh);
        if (ii == null) return;

        if (ii[16] == 0) tvStatus.setText("调查状态：未开始" + str_loc);
        if (ii[16] == 1) tvStatus.setText("调查状态：正在调查" + str_loc);
        if (ii[16] == 2) tvStatus.setText("调查状态：调查完成" + str_loc);

        if (ii[0] == 0) ivYddcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[0] == 1) ivYddcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[0] == 2) ivYddcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[1] == 0) ivYdydwzt.setImageResource(R.drawable.ic_status_0);
        if (ii[1] == 1) ivYdydwzt.setImageResource(R.drawable.ic_status_1);
        if (ii[1] == 2) ivYdydwzt.setImageResource(R.drawable.ic_status_2);

        if (ii[2] == 0) ivYdwzt.setImageResource(R.drawable.ic_status_0);
        if (ii[2] == 1) ivYdwzt.setImageResource(R.drawable.ic_status_1);
        if (ii[2] == 2) ivYdwzt.setImageResource(R.drawable.ic_status_2);

        if (ii[3] == 0) ivYdyxcljl.setImageResource(R.drawable.ic_status_0);
        if (ii[3] == 1) ivYdyxcljl.setImageResource(R.drawable.ic_status_1);
        if (ii[3] == 2) ivYdyxcljl.setImageResource(R.drawable.ic_status_2);

        if (ii[4] == 0) ivYdzjcljl.setImageResource(R.drawable.ic_status_0);
        if (ii[4] == 1) ivYdzjcljl.setImageResource(R.drawable.ic_status_1);
        if (ii[4] == 2) ivYdzjcljl.setImageResource(R.drawable.ic_status_2);

        if (ii[5] == 0) ivYdyzdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[5] == 1) ivYdyzdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[5] == 2) ivYdyzdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[6] == 0) ivKjldcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[6] == 1) ivKjldcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[6] == 2) ivKjldcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[7] == 0) ivMmjcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[7] == 1) ivMmjcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[7] == 2) ivMmjcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[8] == 0) ivYmwzt.setImageResource(R.drawable.ic_status_0);
        if (ii[8] == 1) ivYmwzt.setImageResource(R.drawable.ic_status_1);
        if (ii[8] == 2) ivYmwzt.setImageResource(R.drawable.ic_status_2);

        if (ii[9] == 0) ivPjymdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[9] == 1) ivPjymdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[9] == 2) ivPjymdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[10] == 0) ivSlzhdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[10] == 1) ivSlzhdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[10] == 2) ivSlzhdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[11] == 0) ivZbdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[11] == 1) ivZbdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[11] == 2) ivZbdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[12] == 0) ivXmdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[12] == 1) ivXmdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[12] == 2) ivXmdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[13] == 0) ivTrgxdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[13] == 1) ivTrgxdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[13] == 2) ivTrgxdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[14] == 0) ivYdbhdcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[14] == 1) ivYdbhdcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[14] == 2) ivYdbhdcjl.setImageResource(R.drawable.ic_status_2);

        if (ii[15] == 0) ivWclzlddcjl.setImageResource(R.drawable.ic_status_0);
        if (ii[15] == 1) ivWclzlddcjl.setImageResource(R.drawable.ic_status_1);
        if (ii[15] == 2) ivWclzlddcjl.setImageResource(R.drawable.ic_status_2);

        int iTrdc = YangdiMgr.GetTrdcStatu(ydh);
        int iKlwdc = YangdiMgr.GetKlwdcStatu(ydh);
        if (iTrdc == 0) ivTrdc.setImageResource(R.drawable.ic_status_0);
        if (iTrdc == 1) ivTrdc.setImageResource(R.drawable.ic_status_1);
        if (iTrdc == 2) ivTrdc.setImageResource(R.drawable.ic_status_2);
        if (iKlwdc == 0) ivKlwdc.setImageResource(R.drawable.ic_status_0);
        if (iKlwdc == 1) ivKlwdc.setImageResource(R.drawable.ic_status_1);
        if (iKlwdc == 2) ivKlwdc.setImageResource(R.drawable.ic_status_2);

        int dl = YangdiMgr.GetBqdl(ydh);
        btnZbdcjl.setEnabled(true);
        btnXmdcjl.setEnabled(true);
        if (!Qianqimgr.IsYangfang(ydh)) {
            ivZbdcjl.setImageResource(R.drawable.ic_status_2);
            ivXmdcjl.setImageResource(R.drawable.ic_status_2);
            btnZbdcjl.setEnabled(false);
            btnXmdcjl.setEnabled(false);
        } else if (dl > 0 && YangdiMgr.IsNeedYangfang(dl)) {
            ivZbdcjl.setImageResource(R.drawable.ic_status_2);
            ivXmdcjl.setImageResource(R.drawable.ic_status_2);
        }

        if (dl > 0 && !YangdiMgr.IsNeedWclzld(dl)) {
            ivWclzlddcjl.setImageResource(R.drawable.ic_status_2);
        }
        if (YangdiMgr.IsNeedWclzld(dl)) {
            btnWclzlddcjl.setEnabled(true);
        } else {
            btnWclzlddcjl.setEnabled(false);
        }

        if (dl > 0 && !YangdiMgr.IsNeedSgcl(dl)) {
            ivPjymdcjl.setImageResource(R.drawable.ic_status_2);
        }

        if (dl > 0 && !YangdiMgr.IsNeedSlzh(dl)) {
            ivSlzhdcjl.setImageResource(R.drawable.ic_status_2);
        }

        if (dl > 0 && !YangdiMgr.IsNeedTrgx(dl)) {
            ivTrgxdcjl.setImageResource(R.drawable.ic_status_2);
        }
    }

    class InitTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(MyDataInput.this);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在初始化前期数据...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            YangdiMgr.InitQqData(ydh);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            int qqdl = YangdiMgr.GetQqdl(ydh);
            int bqdl = YangdiMgr.GetBqdl(ydh);
            String qq = Resmgr.GetValueByCode("dl", qqdl);
            String bq = Resmgr.GetValueByCode("dl", bqdl);
            if (qq.equals("")) qq = "无";
            if (bq.equals("")) bq = "无";
            tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");
            resetStatus();
            dlg.cancel();
        }
    }
}
