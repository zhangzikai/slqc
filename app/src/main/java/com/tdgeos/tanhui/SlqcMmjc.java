package com.tdgeos.tanhui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.Yangmu2Dialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.yangdi.YangdiMgr;
import com.tdgeos.yangdi.Yangmu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SlqcMmjc extends Activity implements View.OnClickListener {
    private int ydh = 0;
    private int iStatus = 0;

    private Button btnClose = null;
    private Button btnAdd = null;
    private Button btnSave = null;
    private Button btnFinish = null;

    private ListView lvList = null;
    private List<Integer> lstYms = new ArrayList<Integer>();

    private MyHandler myHandler;
    private Yangmu2Dialog ymDialog = null;
    private int picType = 1;
    private int picYmh = 0;
    private String picInfo = null;
    private String picPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slqc_mmjc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = this.getIntent();
        ydh = intent.getIntExtra("ydh", 0);
        int[] ii = YangdiMgr.GetDczt(ydh);
        iStatus = ii[7];

        myHandler = new MyHandler();
        ymDialog = new Yangmu2Dialog(this, myHandler, ydh);

        btnClose = (Button) findViewById(R.id.btn_close);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnClose.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        lvList = (ListView) findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int ymh = lstYms.get(arg2);
                Yangmu ym = YangdiMgr.GetYangmu(ydh, ymh);
                ymDialog.SetData(ym);
                ymDialog.show();
                /*
                YangmuDialog dlgYangmu = new YangmuDialog(SlqcMmjc.this, ym, ydh);
				Yangmu item = dlgYangmu.showDialog();
				if(item != null)
				{
					YangdiMgr.UpdateYangmu(ydh, item);
					initData();
				}
				*/
            }
        });
        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                List<String> lst = new ArrayList<String>();
                lst.add("删除");
                MyListDialog dlg = new MyListDialog(SlqcMmjc.this, "选项", lst);
                int r = dlg.showDialog();
                if (r == 0) {
                    MyMakeSureDialog msdlg = new MyMakeSureDialog(SlqcMmjc.this, "删除", "删除后将不可恢复，是否继续删除该样木？", "删除", "取消");
                    if (msdlg.showDialog()) {
                        int ymh = lstYms.get(arg2);
                        YangdiMgr.DelYangmu(ydh, ymh);
                        initData();
                    }
                }
                return false;
            }
        });
        lvList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

            }
        });

        initData();
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
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
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
                    ymDialog.show();
                    break;
                }
                default:
                    break;
            }
        } else {
            switch (requestCode) {
                case 0: {
                    ymDialog.show();
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
                List<String> lstError = new ArrayList<String>();
                List<String> lstWarn = new ArrayList<String>();
                YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
                if (iStatus != 2) {
                    iStatus = 1;
                } else if (lstError.size() > 0) {
                    iStatus = 1;
                }
                String sql = "update dczt set mmjc = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                break;
            }
            case R.id.btn_finish: {
                iStatus = 1;
                List<String> lstError = new ArrayList<String>();
                List<String> lstWarn = new ArrayList<String>();
                YangdiMgr.CheckMmjc(ydh, lstError, lstWarn);
                if (lstError.size() == 0) {
                    iStatus = 2;
                }
                String sql = "update dczt set mmjc = '" + iStatus + "' where ydh = '" + ydh + "'";
                YangdiMgr.ExecSQL(ydh, sql);
                int all = YangdiMgr.GetYangmuCount(ydh);
                int pass = YangdiMgr.GetPassYangmuCount(ydh);
                if (pass < all) {
                    String str = YangdiMgr.GetResidueYangmu(ydh);
                    str += "，是否仍然离开该界面？";
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", str, "是", "否");
                    if (dlg.showDialog()) {
                        this.finish();
                    }
                } else {
                    this.finish();
                }
                break;
            }
            case R.id.btn_add: {
                ymDialog.SetData(null);
                ymDialog.show();
            /*
			YangmuDialog dlgYangmu = new YangmuDialog(this, null, ydh);
			Yangmu ym = dlgYangmu.showDialog();
			if(ym != null)
			{
				YangdiMgr.AddYangmu(ydh, ym);
				initData();
			}
			*/
                break;
            }
        }
    }

    private void initData() {
        int pos = lvList.getFirstVisiblePosition();

        lstYms.clear();
        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        List<Yangmu> lst = YangdiMgr.GetYangmus(ydh);
        for (int i = 0; i < lst.size(); i++) {
            Yangmu ym = lst.get(i);
            java.util.Map<String, String> item = new java.util.HashMap<String, String>();
            item.put("ymh", String.valueOf(ym.ymh));
            //item.put("lmlx", Resmgr.GetValueByCode("lmlx", ym.lmlx));
            item.put("lmlx", getLmlx(ym.lmlx));
            //item.put("jclx", Resmgr.GetValueByCode("jclx", ym.jclx));
            item.put("jclx", getJclx(ym.jclx));
            if (ym.szdm > 0) item.put("sz", ym.szdm + " " + ym.szmc);
            else item.put("sz", "");
            String sxj = "";
            if (ym.bqxj > 0) sxj = String.valueOf(ym.bqxj);
            item.put("xj", sxj);
            //item.put("cflx", Resmgr.GetValueByCode("cfgllx", ym.cfgllx));
            item.put("cflx", getCflx(ym.cfgllx));
            item.put("kjxh", String.valueOf(ym.kjdlxh));
            item.put("hzb", String.valueOf(ym.fwj));
            item.put("zzb", String.valueOf(ym.spj));
            list.add(item);
            lstYms.add(ym.ymh);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.slqc_mmjc_item,
                new String[]{"ymh", "lmlx", "jclx", "sz", "xj", "cflx", "kjxh", "hzb", "zzb"},
                new int[]{R.id.tv_ymh, R.id.tv_lmlx, R.id.tv_jclx, R.id.tv_sz, R.id.tv_xj, R.id.tv_cflx, R.id.tv_kjxh, R.id.tv_hzb, R.id.tv_zzb});
        lvList.setAdapter(adapter);

        lvList.setSelection(pos);
    }

    private String getLmlx(int code) {
        if (code == 11) return "11 林木-乔木林地";
        if (code == 12) return "12 林木-疏林地";
        if (code == 21) return "21 散生木-乔木幼中林内的霸王木";
        if (code == 22) return "22 散生木-其它地类";
        if (code == 30) return "30 四旁树";
        return "";
    }

    private String getJclx(int code) {
        if (code == 10) return "10 新植大苗";
        if (code == 11) return "11 保留木";
        if (code == 12) return "12 进界木";
        if (code == 13) return "13 枯立木";
        if (code == 14) return "14 采伐木";
        if (code == 15) return "15 枯倒木";
        if (code == 16) return "16 漏测木";
        if (code == 17) return "17 多测木";
        if (code == 18) return "18 胸径错测木";
        if (code == 19) return "19 树种错测木";
        if (code == 20) return "20 类型错测木";
        if (code == 21) return "21 经济乔木树种及乔木型灌木树种";
        if (code == 1) return "1 改设样地的样木和未复位的非经济乔木树种保留木";
        return "";
    }

    private String getCflx(int code) {
        if (code == 11) return "11 林业部门管理林木";
        if (code == 12) return "12 非林业部门管理林木";
        if (code == 20) return "20 不纳入采伐限额管理林木";
        return "";
    }

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                initData();
            }
            if (msg.what == 101) {
                Bundle data = msg.getData();
                picType = data.getInt("picType", 0);
                picYmh = data.getInt("picYmh", 0);
                picInfo = data.getString("picInfo", "");
                picPath = data.getString("picPath", "");
                MyFile.DeleteFile(picPath);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picPath)));
                startActivityForResult(intent, 0);
            }
        }
    }

    ;
}
