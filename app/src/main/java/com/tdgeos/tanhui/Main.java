package com.tdgeos.tanhui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tdgeos.dlg.base.MyAboutDialog;
import com.tdgeos.dlg.base.MyFileOpenDialog;
import com.tdgeos.dlg.base.MyListDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.DataImportDialog;
import com.tdgeos.dlg.yddc.MutilExportDialog;
import com.tdgeos.dlg.yddc.MutilUploadDialog;
import com.tdgeos.dlg.yddc.SyssetDialog;
import com.tdgeos.dlg.yddc.TaskAddDialog;
import com.tdgeos.dlg.yddc.WorkerMgrDialog;
import com.tdgeos.lib.MyFile;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class Main extends Activity implements View.OnClickListener, View.OnLongClickListener {
    private WakeLock mWakeLock = null;

    private Button btnMenu = null;
    private PopupWindow popMenu = null;
    private ListView lvMenu = null;

    private Button btnMap = null;
    private Button btnDataInput = null;

    private EditText etYdh = null;
    private Button btnSearch = null;

    private EditText etPage = null;
    private Button btnGo = null;

    private TextView tvYdcount = null;
    private TextView tvPage = null;
    private Button btnFirst = null;
    private Button btnLast = null;
    private Button btnPrev = null;
    private Button btnNext = null;
    private int iPageCount = 0;
    private int iCurPage = 0;
    private final int iPageSize = 10;

    private GestureDetector mGestureDetector = null;

    private LinearLayout layItem1 = null;
    private ImageView ivStatus1 = null;
    private TextView tvYdh1 = null;
    private TextView tvXian1 = null;
    private TextView tvDl1 = null;
    private TextView tvQy1 = null;
    private TextView tvTdqs1 = null;
    private TextView tvLinzh1 = null;

    private LinearLayout layItem2 = null;
    private ImageView ivStatus2 = null;
    private TextView tvYdh2 = null;
    private TextView tvXian2 = null;
    private TextView tvDl2 = null;
    private TextView tvQy2 = null;
    private TextView tvTdqs2 = null;
    private TextView tvLinzh2 = null;

    private LinearLayout layItem3 = null;
    private ImageView ivStatus3 = null;
    private TextView tvYdh3 = null;
    private TextView tvXian3 = null;
    private TextView tvDl3 = null;
    private TextView tvQy3 = null;
    private TextView tvTdqs3 = null;
    private TextView tvLinzh3 = null;

    private LinearLayout layItem4 = null;
    private ImageView ivStatus4 = null;
    private TextView tvYdh4 = null;
    private TextView tvXian4 = null;
    private TextView tvDl4 = null;
    private TextView tvQy4 = null;
    private TextView tvTdqs4 = null;
    private TextView tvLinzh4 = null;

    private LinearLayout layItem5 = null;
    private ImageView ivStatus5 = null;
    private TextView tvYdh5 = null;
    private TextView tvXian5 = null;
    private TextView tvDl5 = null;
    private TextView tvQy5 = null;
    private TextView tvTdqs5 = null;
    private TextView tvLinzh5 = null;

    private LinearLayout layItem6 = null;
    private ImageView ivStatus6 = null;
    private TextView tvYdh6 = null;
    private TextView tvXian6 = null;
    private TextView tvDl6 = null;
    private TextView tvQy6 = null;
    private TextView tvTdqs6 = null;
    private TextView tvLinzh6 = null;

    private LinearLayout layItem7 = null;
    private ImageView ivStatus7 = null;
    private TextView tvYdh7 = null;
    private TextView tvXian7 = null;
    private TextView tvDl7 = null;
    private TextView tvQy7 = null;
    private TextView tvTdqs7 = null;
    private TextView tvLinzh7 = null;

    private LinearLayout layItem8 = null;
    private ImageView ivStatus8 = null;
    private TextView tvYdh8 = null;
    private TextView tvXian8 = null;
    private TextView tvDl8 = null;
    private TextView tvQy8 = null;
    private TextView tvTdqs8 = null;
    private TextView tvLinzh8 = null;

    private LinearLayout layItem9 = null;
    private ImageView ivStatus9 = null;
    private TextView tvYdh9 = null;
    private TextView tvXian9 = null;
    private TextView tvDl9 = null;
    private TextView tvQy9 = null;
    private TextView tvTdqs9 = null;
    private TextView tvLinzh9 = null;

    private LinearLayout layItem10 = null;
    private ImageView ivStatus10 = null;
    private TextView tvYdh10 = null;
    private TextView tvXian10 = null;
    private TextView tvDl10 = null;
    private TextView tvQy10 = null;
    private TextView tvTdqs10 = null;
    private TextView tvLinzh10 = null;

    private int curYdh = 0;
    private MyAboutDialog dlgAbout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        float dpiScale = getResources().getDisplayMetrics().densityDpi * 1.0f / 160;

        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        java.util.Map<String, String> item = new java.util.HashMap<String, String>();
        item.put("text", "导入任务");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "添加样地");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "人员管理");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "数据导入");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "数据导出");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "数据上传");
        list.add(item);
//        item = new java.util.HashMap<String, String>();
//        item.put("text", "调查细则");
//        list.add(item);
//        item = new java.util.HashMap<String, String>(); 
//        item.put("text", "使用手册");
//        list.add(item);
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
                        MyFileOpenDialog dlg = new MyFileOpenDialog(Main.this, MyConfig.GetCurdir(), new String[]{"dat"});
                        String dat = dlg.showDialog();
                        if (dat != null) {
                            String tmpFile = MyConfig.GetAppdir() + "/dat.tmp";
                            try {
                                MyFile.DeleteFile(tmpFile);
                                MyFile.CopyFile(dat, tmpFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (!YangdiMgr.decodeFile(tmpFile)) {
                                    MyFile.DeleteFile(tmpFile);
                                    Toast.makeText(Main.this, "导入失败，无法解密任务文件。", 1).show();
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!MyFile.Exists(tmpFile)) {
                                Toast.makeText(Main.this, "导入失败，无效的任务文件。", 1).show();
                            }

                            String dbFile = MyConfig.GetAppdir() + "/qianqi.dat";
                            if (MyFile.Exists(dbFile)) {
                                MyMakeSureDialog dlg2 = new MyMakeSureDialog(Main.this, "提示", "是否保留原有任务数据？\n该操作并不会删除任何已经录入的数据！", "不保留", "保留");
                                if (dlg2.showDialog()) {
                                    MyFile.DeleteFile(dbFile);
                                    MyFile.Rename(tmpFile, dbFile);
                                    Setmgr.ClearTask();
                                    new LoadTask().execute("");
                                    Toast.makeText(Main.this, "导入完成。", 1).show();
                                    break;
                                } else {
                                    CombineTask task = new CombineTask();
                                    task.execute(tmpFile, dbFile);
                                }
                            } else {
                                MyFile.Rename(tmpFile, dbFile);
                                Toast.makeText(Main.this, "导入完成。", 1).show();
                            }
                        }
                        break;
                    }
                    case 1: {
                        TaskAddDialog dlg = new TaskAddDialog(Main.this);
                        int[] re = dlg.showDialog();
                        if (re != null) {
                            iCurPage = 0;
                            if (re[2] == 1) {
                                Setmgr.ClearTask();
                            }
                            new InitTask().execute(re[0], re[1]);
                        } else {
                            int n = Setmgr.GetTaskCount();
                            if (n == 0) {
                                iCurPage = 0;
                                curYdh = 0;
                            }
                            setPageInfo();
                            new LoadTask().execute("");
                        }
                        break;
                    }
                    case 2: {
                        WorkerMgrDialog dlg = new WorkerMgrDialog(Main.this);
                        dlg.show();
                        break;
                    }
                    case 3: {
                        DataImportDialog dlg = new DataImportDialog(Main.this);
                        dlg.showDialog();
                        setPageInfo();
                        setYdcount();
                        new LoadTask().execute("");
                        break;
                    }
                    case 4: {
                        MutilExportDialog dlg = new MutilExportDialog(Main.this);
                        dlg.show();
                        break;
                    }
                    case 5: {
                        MutilUploadDialog dlg = new MutilUploadDialog(Main.this);
                        dlg.show();
                        break;
                    }
//		    	case 6:
//		    	{
//		    		String filePath = MyConfig.GetWorkdir() + "/doc/xize.pdf";
//					String type = "pdf";
//					openDoc(filePath, type);
//		    		break;
//		    	}
//		    	case 7:
//		    	{
//		    		String filePath = MyConfig.GetWorkdir() + "/doc/shouce.pdf";
//					String type = "pdf";
//					openDoc(filePath, type);
//		    		break;
//		    	}
                    case 6: {
                        SyssetDialog dlgSysset = new SyssetDialog(Main.this);
                        dlgSysset.show();
                        break;
                    }
                    case 7: {
                        dlgAbout.show();
                        break;
                    }
                }
            }
        });

        dlgAbout = new MyAboutDialog(this);

        btnMenu = (Button) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(this);

        btnMap = (Button) findViewById(R.id.btn_map);
        btnMap.setOnClickListener(this);
        btnDataInput = (Button) findViewById(R.id.btn_datainput);
        btnDataInput.setOnClickListener(this);

        etYdh = (EditText) findViewById(R.id.et_ydh);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        etPage = (EditText) findViewById(R.id.et_page);
        btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(this);

        tvYdcount = (TextView) findViewById(R.id.tv_ydcount);
        tvPage = (TextView) findViewById(R.id.tv_page);

        mGestureDetector = new GestureDetector(this, new LearnGestureListener());

        btnFirst = (Button) findViewById(R.id.btn_first);
        btnFirst.setOnClickListener(this);
        btnLast = (Button) findViewById(R.id.btn_last);
        btnLast.setOnClickListener(this);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        layItem1 = (LinearLayout) findViewById(R.id.lay_1);
        ivStatus1 = (ImageView) findViewById(R.id.iv_status_1);
        tvYdh1 = (TextView) findViewById(R.id.tv_ydh_1);
        tvXian1 = (TextView) findViewById(R.id.tv_xian_1);
        tvDl1 = (TextView) findViewById(R.id.tv_dl_1);
        tvQy1 = (TextView) findViewById(R.id.tv_qy_1);
        tvTdqs1 = (TextView) findViewById(R.id.tv_tdqs_1);
        tvLinzh1 = (TextView) findViewById(R.id.tv_linzh_1);

        layItem2 = (LinearLayout) findViewById(R.id.lay_2);
        ivStatus2 = (ImageView) findViewById(R.id.iv_status_2);
        tvYdh2 = (TextView) findViewById(R.id.tv_ydh_2);
        tvXian2 = (TextView) findViewById(R.id.tv_xian_2);
        tvDl2 = (TextView) findViewById(R.id.tv_dl_2);
        tvQy2 = (TextView) findViewById(R.id.tv_qy_2);
        tvTdqs2 = (TextView) findViewById(R.id.tv_tdqs_2);
        tvLinzh2 = (TextView) findViewById(R.id.tv_linzh_2);

        layItem3 = (LinearLayout) findViewById(R.id.lay_3);
        ivStatus3 = (ImageView) findViewById(R.id.iv_status_3);
        tvYdh3 = (TextView) findViewById(R.id.tv_ydh_3);
        tvXian3 = (TextView) findViewById(R.id.tv_xian_3);
        tvDl3 = (TextView) findViewById(R.id.tv_dl_3);
        tvQy3 = (TextView) findViewById(R.id.tv_qy_3);
        tvTdqs3 = (TextView) findViewById(R.id.tv_tdqs_3);
        tvLinzh3 = (TextView) findViewById(R.id.tv_linzh_3);

        layItem4 = (LinearLayout) findViewById(R.id.lay_4);
        ivStatus4 = (ImageView) findViewById(R.id.iv_status_4);
        tvYdh4 = (TextView) findViewById(R.id.tv_ydh_4);
        tvXian4 = (TextView) findViewById(R.id.tv_xian_4);
        tvDl4 = (TextView) findViewById(R.id.tv_dl_4);
        tvQy4 = (TextView) findViewById(R.id.tv_qy_4);
        tvTdqs4 = (TextView) findViewById(R.id.tv_tdqs_4);
        tvLinzh4 = (TextView) findViewById(R.id.tv_linzh_4);

        layItem5 = (LinearLayout) findViewById(R.id.lay_5);
        ivStatus5 = (ImageView) findViewById(R.id.iv_status_5);
        tvYdh5 = (TextView) findViewById(R.id.tv_ydh_5);
        tvXian5 = (TextView) findViewById(R.id.tv_xian_5);
        tvDl5 = (TextView) findViewById(R.id.tv_dl_5);
        tvQy5 = (TextView) findViewById(R.id.tv_qy_5);
        tvTdqs5 = (TextView) findViewById(R.id.tv_tdqs_5);
        tvLinzh5 = (TextView) findViewById(R.id.tv_linzh_5);

        layItem6 = (LinearLayout) findViewById(R.id.lay_6);
        ivStatus6 = (ImageView) findViewById(R.id.iv_status_6);
        tvYdh6 = (TextView) findViewById(R.id.tv_ydh_6);
        tvXian6 = (TextView) findViewById(R.id.tv_xian_6);
        tvDl6 = (TextView) findViewById(R.id.tv_dl_6);
        tvQy6 = (TextView) findViewById(R.id.tv_qy_6);
        tvTdqs6 = (TextView) findViewById(R.id.tv_tdqs_6);
        tvLinzh6 = (TextView) findViewById(R.id.tv_linzh_6);

        layItem7 = (LinearLayout) findViewById(R.id.lay_7);
        ivStatus7 = (ImageView) findViewById(R.id.iv_status_7);
        tvYdh7 = (TextView) findViewById(R.id.tv_ydh_7);
        tvXian7 = (TextView) findViewById(R.id.tv_xian_7);
        tvDl7 = (TextView) findViewById(R.id.tv_dl_7);
        tvQy7 = (TextView) findViewById(R.id.tv_qy_7);
        tvTdqs7 = (TextView) findViewById(R.id.tv_tdqs_7);
        tvLinzh7 = (TextView) findViewById(R.id.tv_linzh_7);

        layItem8 = (LinearLayout) findViewById(R.id.lay_8);
        ivStatus8 = (ImageView) findViewById(R.id.iv_status_8);
        tvYdh8 = (TextView) findViewById(R.id.tv_ydh_8);
        tvXian8 = (TextView) findViewById(R.id.tv_xian_8);
        tvDl8 = (TextView) findViewById(R.id.tv_dl_8);
        tvQy8 = (TextView) findViewById(R.id.tv_qy_8);
        tvTdqs8 = (TextView) findViewById(R.id.tv_tdqs_8);
        tvLinzh8 = (TextView) findViewById(R.id.tv_linzh_8);

        layItem9 = (LinearLayout) findViewById(R.id.lay_9);
        ivStatus9 = (ImageView) findViewById(R.id.iv_status_9);
        tvYdh9 = (TextView) findViewById(R.id.tv_ydh_9);
        tvXian9 = (TextView) findViewById(R.id.tv_xian_9);
        tvDl9 = (TextView) findViewById(R.id.tv_dl_9);
        tvQy9 = (TextView) findViewById(R.id.tv_qy_9);
        tvTdqs9 = (TextView) findViewById(R.id.tv_tdqs_9);
        tvLinzh9 = (TextView) findViewById(R.id.tv_linzh_9);

        layItem10 = (LinearLayout) findViewById(R.id.lay_10);
        ivStatus10 = (ImageView) findViewById(R.id.iv_status_10);
        tvYdh10 = (TextView) findViewById(R.id.tv_ydh_10);
        tvXian10 = (TextView) findViewById(R.id.tv_xian_10);
        tvDl10 = (TextView) findViewById(R.id.tv_dl_10);
        tvQy10 = (TextView) findViewById(R.id.tv_qy_10);
        tvTdqs10 = (TextView) findViewById(R.id.tv_tdqs_10);
        tvLinzh10 = (TextView) findViewById(R.id.tv_linzh_10);

        layItem1.setOnClickListener(this);
        layItem2.setOnClickListener(this);
        layItem3.setOnClickListener(this);
        layItem4.setOnClickListener(this);
        layItem5.setOnClickListener(this);
        layItem6.setOnClickListener(this);
        layItem7.setOnClickListener(this);
        layItem8.setOnClickListener(this);
        layItem9.setOnClickListener(this);
        layItem10.setOnClickListener(this);

        layItem1.setOnLongClickListener(this);
        layItem2.setOnLongClickListener(this);
        layItem3.setOnLongClickListener(this);
        layItem4.setOnLongClickListener(this);
        layItem5.setOnLongClickListener(this);
        layItem6.setOnLongClickListener(this);
        layItem7.setOnLongClickListener(this);
        layItem8.setOnLongClickListener(this);
        layItem9.setOnLongClickListener(this);
        layItem10.setOnLongClickListener(this);

        MyConfig.SetCurYd(-1);
        MyConfig.SetOpenLoc(false);

        new LoadTask().execute("");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        MyConfig.SetCurYd(-1);
        MyConfig.SetOpenTrack(false);
        MyConfig.SetOpenLoc(false);
        new LoadTask().execute("");
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent iLoc = new Intent(Main.this, LocationService.class);
        startService(iLoc);
        acquireWakeLock();
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
        Intent iLoc = new Intent(Main.this, LocationService.class);
        stopService(iLoc);
        releaseWakeLock();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出");
            builder.setMessage("确定退出程序吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Main.this.finish();
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    return;
                }
            });
            builder.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu: {
                popMenu.showAsDropDown(btnMenu);
                break;
            }
            case R.id.btn_map: {
                if (curYdh == 0) {
                    Toast.makeText(this, "没有指定当前样地！", 1).show();
                    break;
                }
                YangdiMgr.InitYangdi(curYdh);
                if (YangdiMgr.MY_MAP_TYPE == 54) {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", curYdh);
                    intent.setClass(this, MyMap.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", curYdh);
                    intent.setClass(this, MyMap84.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.btn_datainput: {
                if (curYdh == 0) {
                    Toast.makeText(this, "没有指定当前样地！", 1).show();
                    break;
                }
                YangdiMgr.InitYangdi(curYdh);
                Intent intent = new Intent();
                intent.putExtra("ydh", curYdh);
                intent.setClass(this, MyDataInput.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_search: {
                int ydh = 0;
                String sydh = etYdh.getText().toString();
                try {
                    ydh = Integer.parseInt(sydh);
                } catch (Exception e) {
                }
                if (Setmgr.GetTask(ydh) == null) {
                    Toast.makeText(this, "无效样地号！", 1).show();
                    break;
                }
                for (int i = 0; i < iPageCount; i++) {
                    List<YDInfo> lst = Setmgr.GetTaskList(i, iPageSize);
                    boolean b = false;
                    for (int j = 0; j < lst.size(); j++) {
                        if (lst.get(j).ydh == ydh) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        iCurPage = i;
                        curYdh = ydh;
                        setPageInfo();
                        //setData();
                        new LoadTask().execute("");
                        break;
                    }
                }
                break;
            }
            case R.id.btn_go: {
                int page = iCurPage;
                String sp = etPage.getText().toString();
                try {
                    page = Integer.parseInt(sp);
                } catch (Exception e) {
                }
                page--;
                if (page >= iPageCount || page < 0) {
                    Toast.makeText(this, "无效页码！", 1).show();
                    break;
                }
                if (iCurPage == page) break;
                iCurPage = page;
                curYdh = 0;
                setPageInfo();
                //setData();
                new LoadTask().execute("");
                break;
            }
            case R.id.btn_first: {
                if (iCurPage == 0) break;
                iCurPage = 0;
                curYdh = 0;
                setPageInfo();
                //setData();
                new LoadTask().execute("");
                break;
            }
            case R.id.btn_last: {
                if (iCurPage == iPageCount - 1) break;
                iCurPage = iPageCount - 1;
                curYdh = 0;
                setPageInfo();
                //setData();
                new LoadTask().execute("");
                break;
            }
            case R.id.btn_prev: {
                if (iCurPage == 0) break;
                iCurPage--;
                curYdh = 0;
                setPageInfo();
                //setData();
                new LoadTask().execute("");
                break;
            }
            case R.id.btn_next: {
                if (iCurPage == iPageCount - 1) break;
                iCurPage++;
                curYdh = 0;
                setPageInfo();
                //setData();
                new LoadTask().execute("");
                break;
            }
            case R.id.lay_1: {
                try {
                    curYdh = Integer.parseInt(tvYdh1.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_2: {
                try {
                    curYdh = Integer.parseInt(tvYdh2.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_3: {
                try {
                    curYdh = Integer.parseInt(tvYdh3.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_4: {
                try {
                    curYdh = Integer.parseInt(tvYdh4.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_5: {
                try {
                    curYdh = Integer.parseInt(tvYdh5.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_6: {
                try {
                    curYdh = Integer.parseInt(tvYdh6.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_7: {
                try {
                    curYdh = Integer.parseInt(tvYdh7.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_8: {
                try {
                    curYdh = Integer.parseInt(tvYdh8.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_9: {
                try {
                    curYdh = Integer.parseInt(tvYdh9.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_listcheck);
                layItem10.setBackgroundResource(R.drawable.xml_bg_list);
                break;
            }
            case R.id.lay_10: {
                try {
                    curYdh = Integer.parseInt(tvYdh10.getText().toString());
                } catch (Exception e) {
                }
                layItem1.setBackgroundResource(R.drawable.xml_bg_list);
                layItem2.setBackgroundResource(R.drawable.xml_bg_list);
                layItem3.setBackgroundResource(R.drawable.xml_bg_list);
                layItem4.setBackgroundResource(R.drawable.xml_bg_list);
                layItem5.setBackgroundResource(R.drawable.xml_bg_list);
                layItem6.setBackgroundResource(R.drawable.xml_bg_list);
                layItem7.setBackgroundResource(R.drawable.xml_bg_list);
                layItem8.setBackgroundResource(R.drawable.xml_bg_list);
                layItem9.setBackgroundResource(R.drawable.xml_bg_list);
                layItem10.setBackgroundResource(R.drawable.xml_bg_listcheck);
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        v.setBackgroundResource(R.drawable.bg_item_11);
        int ydh = 0;
        switch (v.getId()) {
            case R.id.lay_1: {
                try {
                    ydh = Integer.parseInt(tvYdh1.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_2: {
                try {
                    ydh = Integer.parseInt(tvYdh2.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_3: {
                try {
                    ydh = Integer.parseInt(tvYdh3.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_4: {
                try {
                    ydh = Integer.parseInt(tvYdh4.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_5: {
                try {
                    ydh = Integer.parseInt(tvYdh5.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_6: {
                try {
                    ydh = Integer.parseInt(tvYdh6.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_7: {
                try {
                    ydh = Integer.parseInt(tvYdh7.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_8: {
                try {
                    ydh = Integer.parseInt(tvYdh8.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_9: {
                try {
                    ydh = Integer.parseInt(tvYdh9.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
            case R.id.lay_10: {
                try {
                    ydh = Integer.parseInt(tvYdh10.getText().toString());
                } catch (Exception e) {
                }
                break;
            }
        }
        List<String> lst = new ArrayList<String>();
        lst.add("删除");
        MyListDialog dlg = new MyListDialog(this, "选项", lst);
        int r = dlg.showDialog();
        if (r == 0) {
            MyMakeSureDialog msdlg = new MyMakeSureDialog(this, "删除", "将指定样地从列表中移除，该操作不会删除任何已调查数据，是否继续移除该样地？", "移除", "取消");
            if (msdlg.showDialog()) {
                Setmgr.RemoveTask(ydh);
                if (curYdh == ydh) curYdh = 0;
                List<YDInfo> yds = Setmgr.GetTaskList(iCurPage, iPageSize);
                if (yds.size() == 0 || iCurPage > 0) iCurPage--;
                new LoadTask().execute("");
            } else {
                if (ydh == curYdh) v.setBackgroundResource(R.drawable.xml_bg_listcheck);
                else v.setBackgroundResource(R.drawable.xml_bg_list);
            }
        } else {
            if (ydh == curYdh) v.setBackgroundResource(R.drawable.xml_bg_listcheck);
            else v.setBackgroundResource(R.drawable.xml_bg_list);
        }

        return false;
    }

    private void combineData(String tmpFile, String dbFile) {
        SQLiteDatabase dbSrc = null;
        try {
            dbSrc = SQLiteDatabase.openOrCreateDatabase(tmpFile, null);
        } catch (Exception e) {
        }
        if (dbSrc == null) return;

        SQLiteDatabase dbDst = null;
        try {
            dbDst = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } catch (Exception e) {
        }
        if (dbDst == null) return;

        String sql = "select * from shi";
        Cursor sor = dbSrc.rawQuery(sql, null);
        if (sor != null) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int code = sor.getInt(0);
                String name = sor.getString(1);
                int parent = sor.getInt(2);
                sql = "delete from shi where code = '" + code + "'";
                dbDst.execSQL(sql);
                sql = "insert into shi values('" + code + "', '" + name + "', '" + parent + "')";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }
        sql = "select * from xian";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int code = sor.getInt(0);
                String name = sor.getString(1);
                int parent = sor.getInt(2);
                sql = "delete from xian where code = '" + code + "'";
                dbDst.execSQL(sql);
                sql = "insert into xian values('" + code + "', '" + name + "', '" + parent + "')";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_ydyz";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                sql = "delete from slqc_ydyz where ydh = '" + ydh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_ydyz values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_kjl";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int xh = sor.getInt(1);
                sql = "delete from slqc_kjl where ydh = '" + ydh + "' and xh = '" + xh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_kjl values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_mmjc";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int ymh = sor.getInt(1);
                sql = "delete from slqc_mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_mmjc values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_kpfm";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                sql = "delete from slqc_kpfm where ydh = '" + ydh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_kpfm values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_qt";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                sql = "delete from slqc_qt where ydh = '" + ydh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_qt values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_sgcl";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int ymh = sor.getInt(1);
                sql = "delete from slqc_sgcl where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_sgcl values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_slzh";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int xh = sor.getInt(1);
                sql = "delete from slqc_slzh where ydh = '" + ydh + "' and xh = '" + xh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_slzh values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_trgx";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(1);
                sql = "delete from slqc_trgx where ydh = '" + ydh + "' and szmc = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_trgx values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_wclzld";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(11);
                sql = "delete from slqc_wclzld where ydh = '" + ydh + "' and sz = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_wclzld values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_xmdc";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(1);
                sql = "delete from slqc_xmdc where ydh = '" + ydh + "' and mc = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_xmdc values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_yangdidww";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int xh = sor.getInt(1);
                sql = "delete from slqc_yangdidww where ydh = '" + ydh + "' and bh = '" + xh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_yangdidww values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_yindiandww";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int xh = sor.getInt(1);
                sql = "delete from slqc_yindiandww where ydh = '" + ydh + "' and bh = '" + xh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_yindiandww values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_ydbh";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                int xh = sor.getInt(1);
                sql = "delete from slqc_ydbh where ydh = '" + ydh + "' and yz = '" + xh + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_ydbh values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_yxcl";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(1);
                sql = "delete from slqc_yxcl where ydh = '" + ydh + "' and cz = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_yxcl values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_zjcl";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(1);
                sql = "delete from slqc_zjcl where ydh = '" + ydh + "' and cz = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_zjcl values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        sql = "select * from slqc_zbdc";
        sor = dbSrc.rawQuery(sql, null);
        if (sor != null && sor.getCount() > 0) {
            sor.moveToFirst();
            for (int i = 0; i < sor.getCount(); i++) {
                int ydh = sor.getInt(0);
                String mc = sor.getString(2);
                sql = "delete from slqc_zbdc where ydh = '" + ydh + "' and mc = '" + mc + "'";
                dbDst.execSQL(sql);
                sql = "insert into slqc_zbdc values('" + ydh + "'";
                for (int j = 1; j < sor.getColumnCount(); j++) {
                    sql += ", '" + sor.getString(j) + "'";
                }
                sql += ")";
                dbDst.execSQL(sql);
                sor.moveToNext();
            }
            sor.close();
        }

        MyFile.DeleteFile(tmpFile);
    }

    private void openDoc(String filePath, String type) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        if ("png".equals(type) || "jpg".equals(type) || "jpeg".equals(type)
                || "gif".equals(type) || "bmp".equals(type)) {
            intent.setDataAndType(uri, "image/*");
        } else if ("doc".equals(type) || "docx".equals(type)
                || "ppt".equals(type) || "pptx".equals(type)
                || "xls".equals(type) || "xlsx".equals(type)) {
            intent.setDataAndType(uri, "application/msword");
        } else if ("txt".equals(type)) {
            intent.setDataAndType(uri, "text/plain");
        } else if ("mp3".equals(type) || ("amr").equals(type) || ("MP3").equals(type)) {
            intent.setDataAndType(uri, "audio/*");
        } else if ("mp4".equals(type) || "rmvb".equals(type) || ("MP4").equals(type) || ("m4v").equals(type)) {
            intent.setDataAndType(uri, "video/*");
        } else if ("pdf".equals(type)) {
            intent.setDataAndType(uri, "application/pdf");
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有找到对应的应用。", 1).show();
        }
    }

    private void setYdcount() {
        List<YDInfo> lst = Setmgr.GetTaskList();
        if (lst == null) {
            tvYdcount.setText("");
            return;
        }
        int a = lst.size();
        int b = 0;
        for (int i = 0; i < a; i++) {
            if (lst.get(i).dczt == 2) b++;
        }
        tvYdcount.setText("样地总数量：" + a + "， 已完成：" + b + "， 剩余：" + (a - b));
    }

    private void setPageInfo() {
        int yd_count = Setmgr.GetTaskCount();
        int m = yd_count % iPageSize;
        iPageCount = yd_count / iPageSize;
        if (m > 0) iPageCount++;
        tvPage.setText("当前第" + (iCurPage + 1) + "页，共" + iPageCount + "页");
    }

    private void clearData() {
        ivStatus1.setImageResource(R.drawable.ic_yd_0);
        tvYdh1.setText("");
        tvXian1.setText("");
        tvDl1.setText("");
        tvQy1.setText("");
        tvTdqs1.setText("");
        tvLinzh1.setText("");
        layItem1.setVisibility(4);
        ivStatus2.setImageResource(R.drawable.ic_yd_0);
        tvYdh2.setText("");
        tvXian2.setText("");
        tvDl2.setText("");
        tvQy2.setText("");
        tvTdqs2.setText("");
        tvLinzh2.setText("");
        layItem2.setVisibility(4);
        ivStatus3.setImageResource(R.drawable.ic_yd_0);
        tvYdh3.setText("");
        tvXian3.setText("");
        tvDl3.setText("");
        tvQy3.setText("");
        tvTdqs3.setText("");
        tvLinzh3.setText("");
        layItem3.setVisibility(4);
        ivStatus4.setImageResource(R.drawable.ic_yd_0);
        tvYdh4.setText("");
        tvXian4.setText("");
        tvDl4.setText("");
        tvQy4.setText("");
        tvTdqs4.setText("");
        tvLinzh4.setText("");
        layItem4.setVisibility(4);
        ivStatus5.setImageResource(R.drawable.ic_yd_0);
        tvYdh5.setText("");
        tvXian5.setText("");
        tvDl5.setText("");
        tvQy5.setText("");
        tvTdqs5.setText("");
        tvLinzh5.setText("");
        layItem5.setVisibility(4);
        ivStatus6.setImageResource(R.drawable.ic_yd_0);
        tvYdh6.setText("");
        tvXian6.setText("");
        tvDl6.setText("");
        tvQy6.setText("");
        tvTdqs6.setText("");
        tvLinzh6.setText("");
        layItem6.setVisibility(4);
        ivStatus7.setImageResource(R.drawable.ic_yd_0);
        tvYdh7.setText("");
        tvXian7.setText("");
        tvDl7.setText("");
        tvQy7.setText("");
        tvTdqs7.setText("");
        tvLinzh7.setText("");
        layItem7.setVisibility(4);
        ivStatus8.setImageResource(R.drawable.ic_yd_0);
        tvYdh8.setText("");
        tvXian8.setText("");
        tvDl8.setText("");
        tvQy8.setText("");
        tvTdqs8.setText("");
        tvLinzh8.setText("");
        layItem8.setVisibility(4);
        ivStatus9.setImageResource(R.drawable.ic_yd_0);
        tvYdh9.setText("");
        tvXian9.setText("");
        tvDl9.setText("");
        tvQy9.setText("");
        tvTdqs9.setText("");
        tvLinzh9.setText("");
        layItem9.setVisibility(4);
        ivStatus10.setImageResource(R.drawable.ic_yd_0);
        tvYdh10.setText("");
        tvXian10.setText("");
        tvDl10.setText("");
        tvQy10.setText("");
        tvTdqs10.setText("");
        tvLinzh10.setText("");
        layItem10.setVisibility(4);

        layItem1.setBackgroundResource(R.drawable.xml_bg_list);
        layItem2.setBackgroundResource(R.drawable.xml_bg_list);
        layItem3.setBackgroundResource(R.drawable.xml_bg_list);
        layItem4.setBackgroundResource(R.drawable.xml_bg_list);
        layItem5.setBackgroundResource(R.drawable.xml_bg_list);
        layItem6.setBackgroundResource(R.drawable.xml_bg_list);
        layItem7.setBackgroundResource(R.drawable.xml_bg_list);
        layItem8.setBackgroundResource(R.drawable.xml_bg_list);
        layItem9.setBackgroundResource(R.drawable.xml_bg_list);
        layItem10.setBackgroundResource(R.drawable.xml_bg_list);
    }

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent ev) {
        }

        @Override
        public void onLongPress(MotionEvent ev) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 10) {
                if (iCurPage == iPageCount - 1) return false;
                iCurPage++;
                curYdh = 0;
                setPageInfo();
                new LoadTask().execute("");
            }
            if (e1.getY() - e2.getY() < 100 && Math.abs(velocityY) > 10) {
                if (iCurPage == 0) return false;
                iCurPage--;
                curYdh = 0;
                setPageInfo();
                new LoadTask().execute("");
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent ev) {
            return true;
        }
    }

    class CombineTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(Main.this);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在导入任务数据...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            combineData(params[0], params[1]);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dlg.cancel();
            Toast.makeText(Main.this, "导入完成。", 1).show();
            setPageInfo();
            new LoadTask().execute("");
        }
    }

    class InitTask extends AsyncTask<Integer, Integer, Boolean> {
        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(Main.this);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在初始化数据...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int a1 = params[0];
            int a2 = params[1];
            if (a1 == 1) {
                Setmgr.AddTaskByXian(a2);
            }
            if (a1 == 2) {
                Setmgr.AddTaskByYdh(a2);
            }
            if (a1 == 3) {
                Setmgr.AddAllTask();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dlg.cancel();
            setPageInfo();
            new LoadTask().execute("");
        }
    }

    class LoadTask extends AsyncTask<String, Integer, Boolean> {
        private List<YDInfo> lst = null;
        private int sleeptime = 100;

        @Override
        protected void onPreExecute() {
            clearData();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            lst = Setmgr.GetTaskList(iCurPage, iPageSize);
            if (lst == null) {
                return false;
            }
            int n = lst.size();
            if (n >= 1) {
                this.publishProgress(1);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 2) {
                this.publishProgress(2);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 3) {
                this.publishProgress(3);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 4) {
                this.publishProgress(4);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 5) {
                this.publishProgress(5);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 6) {
                this.publishProgress(6);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 7) {
                this.publishProgress(7);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 8) {
                this.publishProgress(8);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 9) {
                this.publishProgress(9);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (n >= 10) {
                this.publishProgress(10);
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (lst == null) return;
            int n = values[0];
            if (n == 1) {
                YDInfo yd = lst.get(0);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus1.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus1.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus1.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus1.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus1.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus1.setImageResource(R.drawable.ic_yd_2);
                tvYdh1.setText(String.valueOf(yd.ydh));
                tvXian1.setText(yd.xianju);
                tvDl1.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl1.setTextColor(Color.RED);
                } else {
                    tvDl1.setTextColor(Color.BLACK);
                }
                tvQy1.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy1.setTextColor(Color.RED);
                } else {
                    tvQy1.setTextColor(Color.BLACK);
                }
                tvTdqs1.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs1.setTextColor(Color.RED);
                } else {
                    tvTdqs1.setTextColor(Color.BLACK);
                }
                tvLinzh1.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh1.setTextColor(Color.RED);
                } else {
                    tvLinzh1.setTextColor(Color.BLACK);
                }
                layItem1.setVisibility(1);
                if (yd.ydh == curYdh) layItem1.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 2) {
                YDInfo yd = lst.get(1);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus2.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus2.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus2.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus2.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus2.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus2.setImageResource(R.drawable.ic_yd_2);
                tvYdh2.setText(String.valueOf(yd.ydh));
                tvXian2.setText(yd.xianju);
                tvDl2.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl2.setTextColor(Color.RED);
                } else {
                    tvDl2.setTextColor(Color.BLACK);
                }
                tvQy2.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy2.setTextColor(Color.RED);
                } else {
                    tvQy2.setTextColor(Color.BLACK);
                }
                tvTdqs2.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs2.setTextColor(Color.RED);
                } else {
                    tvTdqs2.setTextColor(Color.BLACK);
                }
                tvLinzh2.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh2.setTextColor(Color.RED);
                } else {
                    tvLinzh2.setTextColor(Color.BLACK);
                }
                layItem2.setVisibility(1);
                if (yd.ydh == curYdh) layItem2.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 3) {
                YDInfo yd = lst.get(2);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus3.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus3.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus3.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus3.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus3.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus3.setImageResource(R.drawable.ic_yd_2);
                tvYdh3.setText(String.valueOf(yd.ydh));
                tvXian3.setText(yd.xianju);
                tvDl3.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl3.setTextColor(Color.RED);
                } else {
                    tvDl3.setTextColor(Color.BLACK);
                }
                tvQy3.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy3.setTextColor(Color.RED);
                } else {
                    tvQy3.setTextColor(Color.BLACK);
                }
                tvTdqs3.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs3.setTextColor(Color.RED);
                } else {
                    tvTdqs3.setTextColor(Color.BLACK);
                }
                tvLinzh3.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh3.setTextColor(Color.RED);
                } else {
                    tvLinzh3.setTextColor(Color.BLACK);
                }
                layItem3.setVisibility(1);
                if (yd.ydh == curYdh) layItem3.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 4) {
                YDInfo yd = lst.get(3);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus4.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus4.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus4.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus4.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus4.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus4.setImageResource(R.drawable.ic_yd_2);
                tvYdh4.setText(String.valueOf(yd.ydh));
                tvXian4.setText(yd.xianju);
                tvDl4.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl4.setTextColor(Color.RED);
                } else {
                    tvDl4.setTextColor(Color.BLACK);
                }
                tvQy4.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy4.setTextColor(Color.RED);
                } else {
                    tvQy4.setTextColor(Color.BLACK);
                }
                tvTdqs4.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs4.setTextColor(Color.RED);
                } else {
                    tvTdqs4.setTextColor(Color.BLACK);
                }
                tvLinzh4.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh4.setTextColor(Color.RED);
                } else {
                    tvLinzh4.setTextColor(Color.BLACK);
                }
                layItem4.setVisibility(1);
                if (yd.ydh == curYdh) layItem4.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 5) {
                YDInfo yd = lst.get(4);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus5.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus5.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus5.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus5.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus5.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus5.setImageResource(R.drawable.ic_yd_2);
                tvYdh5.setText(String.valueOf(yd.ydh));
                tvXian5.setText(yd.xianju);
                tvDl5.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl5.setTextColor(Color.RED);
                } else {
                    tvDl5.setTextColor(Color.BLACK);
                }
                tvQy5.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy5.setTextColor(Color.RED);
                } else {
                    tvQy5.setTextColor(Color.BLACK);
                }
                tvTdqs5.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs5.setTextColor(Color.RED);
                } else {
                    tvTdqs5.setTextColor(Color.BLACK);
                }
                tvLinzh5.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh5.setTextColor(Color.RED);
                } else {
                    tvLinzh5.setTextColor(Color.BLACK);
                }
                layItem5.setVisibility(1);
                if (yd.ydh == curYdh) layItem5.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 6) {
                YDInfo yd = lst.get(5);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus6.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus6.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus6.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus6.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus6.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus6.setImageResource(R.drawable.ic_yd_2);
                tvYdh6.setText(String.valueOf(yd.ydh));
                tvXian6.setText(yd.xianju);
                tvDl6.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl6.setTextColor(Color.RED);
                } else {
                    tvDl6.setTextColor(Color.BLACK);
                }
                tvQy6.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy6.setTextColor(Color.RED);
                } else {
                    tvQy6.setTextColor(Color.BLACK);
                }
                tvTdqs6.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs6.setTextColor(Color.RED);
                } else {
                    tvTdqs6.setTextColor(Color.BLACK);
                }
                tvLinzh6.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh6.setTextColor(Color.RED);
                } else {
                    tvLinzh6.setTextColor(Color.BLACK);
                }
                layItem6.setVisibility(1);
                if (yd.ydh == curYdh) layItem6.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 7) {
                YDInfo yd = lst.get(6);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus7.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus7.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus7.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus7.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus7.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus7.setImageResource(R.drawable.ic_yd_2);
                tvYdh7.setText(String.valueOf(yd.ydh));
                tvXian7.setText(yd.xianju);
                tvDl7.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl7.setTextColor(Color.RED);
                } else {
                    tvDl7.setTextColor(Color.BLACK);
                }
                tvQy7.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy7.setTextColor(Color.RED);
                } else {
                    tvQy7.setTextColor(Color.BLACK);
                }
                tvTdqs7.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs7.setTextColor(Color.RED);
                } else {
                    tvTdqs7.setTextColor(Color.BLACK);
                }
                tvLinzh7.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh7.setTextColor(Color.RED);
                } else {
                    tvLinzh7.setTextColor(Color.BLACK);
                }
                layItem7.setVisibility(1);
                if (yd.ydh == curYdh) layItem7.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 8) {
                YDInfo yd = lst.get(7);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus8.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus8.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus8.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus8.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus8.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus8.setImageResource(R.drawable.ic_yd_2);
                tvYdh8.setText(String.valueOf(yd.ydh));
                tvXian8.setText(yd.xianju);
                tvDl8.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl8.setTextColor(Color.RED);
                } else {
                    tvDl8.setTextColor(Color.BLACK);
                }
                tvQy8.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy8.setTextColor(Color.RED);
                } else {
                    tvQy8.setTextColor(Color.BLACK);
                }
                tvTdqs8.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs8.setTextColor(Color.RED);
                } else {
                    tvTdqs8.setTextColor(Color.BLACK);
                }
                tvLinzh8.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh8.setTextColor(Color.RED);
                } else {
                    tvLinzh8.setTextColor(Color.BLACK);
                }
                layItem8.setVisibility(1);
                if (yd.ydh == curYdh) layItem8.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 9) {
                YDInfo yd = lst.get(8);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus9.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus9.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus9.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus9.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus9.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus9.setImageResource(R.drawable.ic_yd_2);
                tvYdh9.setText(String.valueOf(yd.ydh));
                tvXian9.setText(yd.xianju);
                tvDl9.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl9.setTextColor(Color.RED);
                } else {
                    tvDl9.setTextColor(Color.BLACK);
                }
                tvQy9.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy9.setTextColor(Color.RED);
                } else {
                    tvQy9.setTextColor(Color.BLACK);
                }
                tvTdqs9.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs9.setTextColor(Color.RED);
                } else {
                    tvTdqs9.setTextColor(Color.BLACK);
                }
                tvLinzh9.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh9.setTextColor(Color.RED);
                } else {
                    tvLinzh9.setTextColor(Color.BLACK);
                }
                layItem9.setVisibility(1);
                if (yd.ydh == curYdh) layItem9.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
            if (n == 10) {
                YDInfo yd = lst.get(9);
                if (yd.dczt == 0 && yd.sfyf == 1) ivStatus10.setImageResource(R.drawable.ic_yd_3);
                if (yd.dczt == 0 && yd.sfyf == 0) ivStatus10.setImageResource(R.drawable.ic_yd_0);
                if (yd.dczt == 1 && yd.sfyf == 1) ivStatus10.setImageResource(R.drawable.ic_yd_4);
                if (yd.dczt == 1 && yd.sfyf == 0) ivStatus10.setImageResource(R.drawable.ic_yd_1);
                if (yd.dczt == 2 && yd.sfyf == 1) ivStatus10.setImageResource(R.drawable.ic_yd_5);
                if (yd.dczt == 2 && yd.sfyf == 0) ivStatus10.setImageResource(R.drawable.ic_yd_2);
                tvYdh10.setText(String.valueOf(yd.ydh));
                tvXian10.setText(yd.xianju);
                tvDl10.setText(yd.dl_q + " / " + yd.dl_b);
                if (!yd.dl_q.equals("无") && !yd.dl_b.equals("无") && !yd.dl_q.equals(yd.dl_b)) {
                    tvDl10.setTextColor(Color.RED);
                } else {
                    tvDl10.setTextColor(Color.BLACK);
                }
                tvQy10.setText(yd.qy_q + " / " + yd.qy_b);
                if (!yd.qy_q.equals("无") && !yd.qy_b.equals("无") && !yd.qy_q.equals(yd.qy_b)) {
                    tvQy10.setTextColor(Color.RED);
                } else {
                    tvQy10.setTextColor(Color.BLACK);
                }
                tvTdqs10.setText(yd.tdqs_q + " / " + yd.tdqs_b);
                if (!yd.tdqs_q.equals("无") && !yd.tdqs_b.equals("无") && !yd.tdqs_q.equals(yd.tdqs_b)) {
                    tvTdqs10.setTextColor(Color.RED);
                } else {
                    tvTdqs10.setTextColor(Color.BLACK);
                }
                tvLinzh10.setText(yd.linzh_q + " / " + yd.linzh_b);
                if (!yd.linzh_q.equals("无") && !yd.linzh_b.equals("无") && !yd.linzh_q.equals(yd.linzh_b)) {
                    tvLinzh10.setTextColor(Color.RED);
                } else {
                    tvLinzh10.setTextColor(Color.BLACK);
                }
                layItem10.setVisibility(1);
                if (yd.ydh == curYdh) layItem10.setBackgroundResource(R.drawable.xml_bg_listcheck);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setYdcount();
            setPageInfo();
        }
    }
}
