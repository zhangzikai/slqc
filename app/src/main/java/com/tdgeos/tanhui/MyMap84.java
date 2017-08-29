package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.tdgeos.dlg.base.MyAboutDialog;
import com.tdgeos.dlg.base.MyFileOpenDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.CompassDialog;
import com.tdgeos.dlg.yddc.ParamsDialog;
import com.tdgeos.dlg.yddc.TrackDialog;
import com.tdgeos.dlg.yddc.YdhDialog;
import com.tdgeos.lib.CoordTransform;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.lib.MyRect;
import com.tdgeos.lib.RasterLayer;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.TrackInfo;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.LinearLayout.LayoutParams;

public class MyMap84 extends Activity implements View.OnClickListener {
    private Button btnMenu = null;
    private PopupWindow popMenu = null;
    private ListView lvMenu = null;

    private TextView tvYdh = null;
    private Button btnChange = null;
    private Button btnInto = null;

    private Button btnBaiduMap = null;
    private Button btnMyLoc = null;
    private ToggleButton btnTrk = null;
    private Button btnTrack = null;
    private Button btnCompass = null;
    private Button btnAllmap = null;
    private Button btnZoomin = null;
    private Button btnZoomout = null;
    private Button btnYdloc = null;
    private Button btnYdloc2 = null;

    private TextView tvLoc = null;
    private TextView tvScale = null;
    private TextView tvDis = null;

    private LinearLayout layMyView = null;
    private MyView myView = null;
    private int ydh = 0;
    private boolean isHuicai = false;
    private boolean isBDBack = false;
    private List<YDInfo> lstYangdis = null;
    private List<MyPoint> lstYdlocs = null;
    private MyPoint ydLoc = null;

    private List<List<MyPoint>> lstTrkss = null;

    private MyPoint myLoc = null;
    private MyBroadcastReceiver myBroadcastReceiver = null;
    private MyHandler myHandler = null;

    private int dpi = MyConfig.GetDpi();
    private float dpiScale = dpi * 1.0f / 160;

    private CompassDialog dlgCompass = null;
    private MyAboutDialog dlgAbout = null;

    public final static double EARTH_RADIUS = 6378.137;
    public static final String IMAGE_TYPE = "img";

    int zone = MyConfig.GetParamsZone();
    int lon0 = MyConfig.GetParamsLon0();
    double dx = MyConfig.GetParamsDx();
    double dy = MyConfig.GetParamsDy();
    double dz = MyConfig.GetParamsDz();
    double rx = MyConfig.GetParamsRx();
    double ry = MyConfig.GetParamsRy();
    double rz = MyConfig.GetParamsRz();
    double k = MyConfig.GetParamsK();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mymap);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ydh = getIntent().getIntExtra("ydh", 0);
        int i_trk = getIntent().getIntExtra("trk", 0);
        int i_back = getIntent().getIntExtra("back", 0);
        isHuicai = i_trk == 1;
        isBDBack = i_back == 1;
        MyConfig.SetCurYd(ydh);
        MyConfig.SetOpenLoc(true);
        MyConfig.SetOpenTrack(false);

        String trkname = MyConfig.GetCurTrk();
        List<String> lst = YangdiMgr.GetTrkNames(ydh);
        if (lst.size() > 0) {
            boolean b = false;
            for (int i = 0; i < lst.size(); i++) {
                if (trkname.equals(lst.get(i))) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                MyConfig.SetCurTrk(lst.get(0));
            }
        }

        lstYangdis = Setmgr.GetTaskList();
        if (lstYangdis == null) lstYangdis = new ArrayList<YDInfo>();
        lstYdlocs = new ArrayList<MyPoint>();
        double[] xx = new double[lstYangdis.size()];
        double[] yy = new double[lstYangdis.size()];
        for (int i = 0; i < lstYangdis.size(); i++) {
            YDInfo yd = lstYangdis.get(i);
            MyCoord pt = new MyCoord(yd.gpshzb, yd.gpszzb, 0);
            Bj54ToGps(pt, lon0, dx, dy, dz, rx, ry, rz, k);
            xx[i] = pt.x;
            yy[i] = pt.y;
            lstYdlocs.add(new MyPoint(pt.x, pt.y));
            if (yd.ydh == ydh) ydLoc = new MyPoint(pt.x, pt.y);
        }
        double left = MyFuns.dMin(xx);
        double right = MyFuns.dMax(xx);
        double top = MyFuns.dMax(yy);
        double bottom = MyFuns.dMin(yy);

        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        java.util.Map<String, String> item = new java.util.HashMap<String, String>();
        item.put("text", "加载地图");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "移除地图");
        list.add(item);
        item = new java.util.HashMap<String, String>();
        item.put("text", "参数设置");
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
                        MyFileOpenDialog dlg = new MyFileOpenDialog(MyMap84.this, Environment.getExternalStorageDirectory().getPath(), new String[]{"img"});
                        String str = dlg.showDialog();
                        if (str != null) {
                            MyConfig.SetLastMapFile(str);
                            myView.addRaster(str);
                        }
                        break;
                    }
                    case 1: {
                        MyConfig.SetLastMapFile("");
                        myView.removeRaster();
                        break;
                    }
                    case 2: {
                        ParamsDialog dlgParams = new ParamsDialog(MyMap84.this);
                        dlgParams.showDialog();
                        lon0 = MyConfig.GetParamsLon0();
                        dx = MyConfig.GetParamsDx();
                        dy = MyConfig.GetParamsDy();
                        dz = MyConfig.GetParamsDz();
                        rx = MyConfig.GetParamsRx();
                        ry = MyConfig.GetParamsRy();
                        rz = MyConfig.GetParamsRz();
                        k = MyConfig.GetParamsK();
                        double[] xx = new double[lstYangdis.size()];
                        double[] yy = new double[lstYangdis.size()];
                        for (int i = 0; i < lstYangdis.size(); i++) {
                            YDInfo yd = lstYangdis.get(i);
                            MyCoord pt = new MyCoord(yd.gpshzb, yd.gpszzb, 0);
                            Bj54ToGps(pt, lon0, dx, dy, dz, rx, ry, rz, k);
                            xx[i] = pt.x;
                            yy[i] = pt.y;
                            lstYdlocs.add(new MyPoint(pt.x, pt.y));
                            if (yd.ydh == ydh) ydLoc = new MyPoint(pt.x, pt.y);
                        }
                        double left = MyFuns.dMin(xx);
                        double right = MyFuns.dMax(xx);
                        double top = MyFuns.dMax(yy);
                        double bottom = MyFuns.dMin(yy);
                        myView.SetBound((float) left, (float) top, (float) right, (float) bottom);
                        myView.RefreshMap();
                        break;
                    }
                    case 3: {
                        dlgAbout.show();
                        break;
                    }
                }
            }
        });

        btnMenu = (Button) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(this);

        int qqdl = Qianqimgr.GetQqdl(ydh);
        int bqdl = YangdiMgr.GetBqdl(ydh);
        String qq = Resmgr.GetValueByCode("dl", qqdl);
        String bq = Resmgr.GetValueByCode("dl", bqdl);
        if (qq.equals("")) qq = "无";
        if (bq.equals("")) bq = "无";
        tvYdh = (TextView) findViewById(R.id.tv_ydh);
        tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");
        btnChange = (Button) findViewById(R.id.btn_change);
        btnInto = (Button) findViewById(R.id.btn_into);
        btnChange.setOnClickListener(this);
        btnInto.setOnClickListener(this);

        btnBaiduMap = (Button) findViewById(R.id.map_btn_tobd);
        btnMyLoc = (Button) findViewById(R.id.map_btn_myloc);
        btnTrk = (ToggleButton) findViewById(R.id.map_btn_trk);
        btnTrack = (Button) findViewById(R.id.map_btn_track);
        btnCompass = (Button) findViewById(R.id.map_btn_compass);
        btnAllmap = (Button) findViewById(R.id.map_btn_allmap);
        btnZoomin = (Button) findViewById(R.id.map_btn_zoomin);
        btnZoomout = (Button) findViewById(R.id.map_btn_zoomout);
        btnYdloc = (Button) findViewById(R.id.map_btn_ydloc);
        btnYdloc2 = (Button) findViewById(R.id.map_btn_ydloc2);
        btnBaiduMap.setOnClickListener(this);
        btnMyLoc.setOnClickListener(this);
        btnTrk.setOnClickListener(this);
        btnTrack.setOnClickListener(this);
        btnCompass.setOnClickListener(this);
        btnAllmap.setOnClickListener(this);
        btnZoomin.setOnClickListener(this);
        btnZoomout.setOnClickListener(this);
        btnYdloc.setOnClickListener(this);
        btnYdloc2.setOnClickListener(this);

        btnTrk.setChecked(false);

        tvLoc = (TextView) findViewById(R.id.map_tv_loc);
        tvScale = (TextView) findViewById(R.id.map_tv_scale);
        tvDis = (TextView) findViewById(R.id.map_tv_dis);

        myView = new MyView(this);
        myView.SetInitBound((float) left, (float) top, (float) right, (float) bottom);
        myView.SetBound((float) left, (float) top, (float) right, (float) bottom);
        layMyView = (LinearLayout) findViewById(R.id.lay_myview);
        layMyView.addView(myView);

        String str = MyConfig.GetLastMapFile();
        if (MyFile.Exists(str)) {
            myView.addRaster(str);
        }

        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, new IntentFilter("location_changed"));

        dlgCompass = new CompassDialog(this);
        dlgAbout = new MyAboutDialog(this);

        myHandler = new MyHandler();

        TrkTask task = new TrkTask();
        task.execute("");
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
        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu: {
                popMenu.showAsDropDown(btnMenu);
                break;
            }
            case R.id.btn_change: {
                YdhDialog dlg = new YdhDialog(this, ydh);
                int r = dlg.showDialog();
                if (r > 0 && r != ydh) {
                    ydh = r;
                    MyConfig.SetCurYd(ydh);
                    MyConfig.SetOpenTrack(false);
                    YangdiMgr.InitYangdi(ydh);

                    int qqdl = Qianqimgr.GetQqdl(ydh);
                    int bqdl = YangdiMgr.GetBqdl(ydh);
                    String qq = Resmgr.GetValueByCode("dl", qqdl);
                    String bq = Resmgr.GetValueByCode("dl", bqdl);
                    if (qq.equals("")) qq = "无";
                    if (bq.equals("")) bq = "无";
                    tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");
                    for (int i = 0; i < lstYangdis.size(); i++) {
                        if (lstYangdis.get(i).ydh == ydh) {
                            ydLoc = new MyPoint(lstYdlocs.get(i).x, lstYdlocs.get(i).y);
                            break;
                        }
                    }
                    myView.MoveToYangdi();
                    TrkTask task = new TrkTask();
                    task.execute("");
                }
                break;
            }
            case R.id.btn_into: {
                if (YangdiMgr.GetYdloc(ydh) == null) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "尚未采集样地西南角坐标，是否继续进入数据录入界面？", "是", "否");
                    if (!dlg.showDialog()) {
                        break;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(this, MyDataInput.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.map_btn_myloc: {
                if (myLoc != null) myView.MoveTo(myLoc);
                break;
            }
            case R.id.map_btn_trk: {
                if (btnTrk.isChecked()) {
                    List<String> lst = YangdiMgr.GetTrkNames(ydh);
                    if (lst.size() == 0) {
                        String trk = Qianqimgr.GetXianJuNameByYdh(ydh) + "-" + ydh + "-" + MyFuns.GetDateByNumberS();
                        YangdiMgr.CreateTrack(ydh, trk);
                        MyConfig.SetCurTrk(trk);
                        MyConfig.SetOpenTrack(true);
                    } else {
                        MyConfig.SetOpenTrack(true);
                        TrackDialog dlgTrack = new TrackDialog(this, ydh);
                        TrackInfo info = dlgTrack.showDialog();
                        if (info != null) {
                            btnTrk.setChecked(MyConfig.GetOpenTrack());
                        } else {
                            MyConfig.SetOpenTrack(false);
                            btnTrk.setChecked(false);
                        }
                    }
                } else {
                    MyConfig.SetOpenTrack(false);
                }
                break;
            }
            case R.id.map_btn_track: {
                if (ydh <= 0) {
                    Toast.makeText(this, "尚未设置当前样地号！", 1).show();
                    break;
                }
                TrackDialog dlgTrack = new TrackDialog(this, ydh);
                dlgTrack.showDialog();
                btnTrk.setChecked(MyConfig.GetOpenTrack());
                break;
            }
            case R.id.map_btn_compass: {
                dlgCompass.show();
                break;
            }
            case R.id.map_btn_allmap: {
                myView.ZoomToAll();
                break;
            }
            case R.id.map_btn_zoomin: {
                myView.ZoomIn();
                break;
            }
            case R.id.map_btn_zoomout: {
                myView.ZoomOut();
                break;
            }
            case R.id.map_btn_ydloc: {
                if (ydh <= 0) {
                    Toast.makeText(this, "尚未设置当前样地！", 1).show();
                    break;
                }
                if (myLoc == null) {
                    Toast.makeText(this, "GPS尚未定位成功！", 1).show();
                    break;
                }
                if (YangdiMgr.GetYdloc(ydh) != null) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "是否覆盖上次样地定位数据？", "覆盖", "取消");
                    if (!dlg.showDialog()) {
                        break;
                    }
                }
                MyPoint pt = new MyPoint(myLoc);
                double dis = GetDistance(ydLoc.y, ydLoc.x, pt.y, pt.x);
                dis = MyFuns.MyDecimal(dis, 2);
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "当前位置与样地相距 " + dis + " 米，是否继续保存当前坐标？", "保存", "取消");
                if (dlg.showDialog()) {
                    YangdiMgr.SetYdloc(ydh, pt);
                    Toast.makeText(this, "样地定位完成！", 1).show();
                }
                break;
            }
            case R.id.map_btn_ydloc2: {
                if (ydh <= 0) {
                    Toast.makeText(this, "尚未设置当前样地！", 1).show();
                    break;
                }
                if (myLoc == null) {
                    Toast.makeText(this, "GPS尚未定位成功！", 1).show();
                    break;
                }
                if (YangdiMgr.GetYdloc2(ydh) != null) {
                    MyMakeSureDialog dlg = new MyMakeSureDialog(this, "警告", "是否覆盖上次引点定位数据？", "覆盖", "取消");
                    if (!dlg.showDialog()) {
                        break;
                    }
                }
                MyPoint pt = new MyPoint(myLoc);
                MyCoord c = new MyCoord(myLoc.x, myLoc.y, 0);
                GpsToBj54(c, lon0, dx, dy, dz, rx, ry, rz, k);
                YangdiMgr.SetYdloc2(ydh, pt, c);
                Toast.makeText(this, "引点定位完成！", 1).show();
                break;
            }
            case R.id.map_btn_tobd: {
                Intent intent = new Intent();
                intent.putExtra("ydh", ydh);
                intent.setClass(MyMap84.this, BDMap.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    private void updateScale() {
        int iScale = myView.GetMapScale();
        String text = "";
        if (iScale < 0) {
            text = "N/A";
        } else if (iScale < 1000) {
            text = iScale + "米";
        } else {
            float s = iScale / 1000.0f;
            s = MyFuns.MyDecimal(s, 2);
            text = s + "公里";
        }
        tvScale.setText(text);
    }

    public static int Get6Daihao(int lon) {
        if (lon == 75) return 13;
        if (lon == 81) return 14;
        if (lon == 87) return 15;
        if (lon == 93) return 16;
        if (lon == 99) return 17;
        if (lon == 105) return 18;
        if (lon == 111) return 19;
        if (lon == 117) return 20;
        if (lon == 123) return 21;
        if (lon == 129) return 22;
        if (lon == 135) return 23;
        return 0;
    }

    public static int Get3Daihao(int lon) {
        return lon / 3;
    }

    private void Bj54ToGps(MyCoord pt, int lon0, double dx, double dy, double dz, double rx, double ry, double rz, double k) {
        pt.x = pt.x % 1000000;
        MyCoord c1 = CoordTransform.GaussToGeo_bj54(pt, lon0);
        MyCoord c2 = CoordTransform.GeoToKjzj_bj54(c1);
        MyCoord c3 = CoordTransform.Transform(c2, dx, dy, dz, -rx, -ry, -rz, k);
        MyCoord c4 = CoordTransform.KjzjToGeo_wgs84(c3);
        pt.x = c4.x;
        pt.y = c4.y;
    }

    private void GpsToBj54(MyCoord pt, int lon0, double dx, double dy, double dz, double rx, double ry, double rz, double k) {
        MyCoord c1 = CoordTransform.GeoToKjzj_wgs84(pt);
        MyCoord c2 = CoordTransform.Transform(c1, -dx, -dy, -dz, rx, ry, rz, k);
        MyCoord c3 = CoordTransform.KjzjToGeo_bj54(c2);
        MyCoord c4 = CoordTransform.GeoToGauss_bj54(c3, lon0);
        if (zone == 6) {
            pt.x = c4.x + Get6Daihao(lon0) * 1000000;
        } else if (zone == 3) {
            pt.x = c4.x + Get3Daihao(lon0) * 1000000;
        }
        pt.y = c4.y;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double dx = lng1 - lng2;
        double dy = lat1 - lat2;
        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;
        if (dx < 0.00000001 && dy < 0.00000001) return 0;
        lat1 = 90 - lat1;
        lat2 = 90 - lat2;
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        lng1 = lng1 * Math.PI / 180.0;
        lng2 = lng2 * Math.PI / 180.0;
        double c = Math.sin(lat1) * Math.sin(lat2) * Math.cos(lng1 - lng2) + Math.cos(lat1) * Math.cos(lat2);
        double s = EARTH_RADIUS * Math.acos(c);
        if (Double.isNaN(s)) return 0;
        else return s * 1000;
    }

    private void readQqTrack() {
        if (lstTrkss != null) lstTrkss.clear();
        try {
            String dbFile = MyConfig.GetWorkdir() + "/track/" + ydh + ".trk";
            if (!MyFile.Exists(dbFile)) {
                MyFile.CopyFile(MyConfig.GetAppdir() + "/yangdi.trk", dbFile);
                String zFile = MyConfig.GetAppdir() + "/trk" + YangdiMgr.DATA_EXNAME;
                String sql = "select * from track where ydh = '" + ydh + "'";
                SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(zFile, null);
                SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                Cursor cursor = db1.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        String name = cursor.getString(2);
                        double lon = cursor.getDouble(3);
                        double lat = cursor.getDouble(4);
                        String str = "insert into track(ydh,name,lon,lat) values('" + ydh + "', '" + name + "', '" + lon + "', '" + lat + "')";
                        db2.execSQL(str);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                db2.close();
                db1.close();
            }

            String sql = "select name from track where ydh = '" + ydh + "' group by name";
            List<String> lst = new ArrayList<String>();
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                int n = cursor.getCount();
                cursor.moveToFirst();
                for (int i = 0; i < n; i++) {
                    String str = cursor.getString(0);
                    lst.add(str);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            if (lst.size() > 0) {
                lstTrkss = new ArrayList<List<MyPoint>>();
                for (int i = 0; i < lst.size(); i++) {
                    sql = "select lon, lat from track where ydh = '" + ydh + "' and name = '" + lst.get(i) + "'";
                    cursor = db.rawQuery(sql, null);
                    if (cursor != null) {
                        List<MyPoint> lstTrks = new ArrayList<MyPoint>();
                        int n = cursor.getCount();
                        cursor.moveToFirst();
                        for (int j = 0; j < n; j++) {
                            double lon = cursor.getDouble(0);
                            double lat = cursor.getDouble(1);
                            lstTrks.add(new MyPoint(lon, lat));
                            cursor.moveToNext();
                        }
                        cursor.close();
                        lstTrkss.add(lstTrks);
                    }
                }
            }
            db.close();
        } catch (Exception e) {
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if (data.getBoolean("type")) {
                double lon = data.getDouble("lon");
                double lat = data.getDouble("lat");
                MyCoord c = new MyCoord(lon, lat, 0);
                GpsToBj54(c, lon0, dx, dy, dz, rx, ry, rz, k);
                int x = (int) c.x;
                int y = (int) c.y;
                tvLoc.setText("E：" + MyFuns.NumberToDegree(lon) + "  N：" + MyFuns.NumberToDegree(lat) + " (横坐标：" + x + ", 纵坐标：" + y + ")");
                if (myLoc == null) {
                    myLoc = new MyPoint(lon, lat);
                } else {
                    myLoc.x = (float) lon;
                    myLoc.y = (float) lat;
                }
                myView.RefreshOverlay();
            } else {
                tvLoc.setText("正在定位...");
                tvDis.setText("");
                myLoc = null;
                myView.RefreshOverlay();
            }
        }
    }

    class MyView extends View implements View.OnTouchListener {
        private GestureDetector mGestureDetector = null;
        private Paint paint = new Paint();
        private float xBegin = 0;
        private float yBegin = 0;
        private float xEnd = 0;
        private float yEnd = 0;
        private float x0 = 0;
        private float y0 = 0;
        private double disBegin = -1;
        private double disEnd = -1;
        private boolean isZoom = false;
        private boolean isMove = false;
        private Matrix matrixZoom = new Matrix();
        private Matrix matrixMove = new Matrix();

        private int view_width = 0;
        private int view_height = 0;
        private float mw;
        private MyRect initBound;
        private MyRect fBound;

        private float initScale;//初始比例尺，该比例尺表示屏幕上一个像素对应地图中一个单位的比例
        private float curScale;//当前比例尺，该比例尺表示屏幕上一个像素对应地图中一个单位的比例
        private float mapOriginX;
        private float mapOriginY;

        private int zoomSize = 20;
        private float tmpScale = 1;

        private Canvas tmpCanvas = null;
        private Bitmap bmpTempCanvas = null;
        private Bitmap bmpLocation = null;
        private Bitmap bmpTrknode = null;

        private Bitmap bmpCuryd = null;
        private Bitmap bmpYangdi0 = null;
        private Bitmap bmpYangdi1 = null;
        private Bitmap bmpYangdi2 = null;
        private Bitmap bmpYangfang0 = null;
        private Bitmap bmpYangfang1 = null;
        private Bitmap bmpYangfang2 = null;

        private boolean isFirstDraw = true;
        private boolean isRefresh = false;

        private RasterLayer rasterLayer = null;

        private int textSize = (int) (14 * dpiScale);

        public MyView(Context context) {
            super(context);
            mGestureDetector = new GestureDetector(context, new LearnGestureListener());
            this.setOnTouchListener(this);
            bmpLocation = BitmapFactory.decodeResource(getResources(), R.drawable.ic_loc);
            bmpCuryd = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_cur);
            bmpTrknode = BitmapFactory.decodeResource(getResources(), R.drawable.ic_track);
            bmpYangdi0 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_10);
            bmpYangdi1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_11);
            bmpYangdi2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_12);
            bmpYangfang0 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_13);
            bmpYangfang1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_14);
            bmpYangfang2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_yd_15);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            paint.reset();

            if (isFirstDraw) {
                view_width = this.getWidth();
                view_height = this.getHeight();
                MyConfig.SetMapViewSize(view_width, view_height);
                mw = (view_width * 254.0f) / (dpiScale * 160 * 10000.0f);
                bmpTempCanvas = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_4444);
                tmpCanvas = new Canvas(bmpTempCanvas);
                tmpCanvas.drawColor(Color.WHITE);
                float hscale = view_width / fBound.width();
                float vscale = view_height / fBound.height();
                initScale = (hscale < vscale) ? hscale : vscale;
                curScale = initScale;
                mapOriginX = view_width / 2 - fBound.center().x * initScale;
                mapOriginY = view_height + (fBound.center().y * initScale - view_height / 2);

                MoveToYangdi();

                isFirstDraw = false;
            }

            canvas.drawColor(Color.WHITE);

            if (isZoom) {
                canvas.drawBitmap(bmpTempCanvas, matrixZoom, paint);
            } else if (isMove) {
                canvas.drawBitmap(bmpTempCanvas, matrixMove, paint);
            } else if (isRefresh) {
                System.out.println("Main_MyView_onDraw : beginning draw...");
                long t1 = System.currentTimeMillis();
                tmpCanvas.drawColor(Color.WHITE);
                if (rasterLayer != null) {
                    MyPoint lt = new MyPoint(0, 0);
                    MyPoint rb = new MyPoint(view_width, view_height);
                    screenToMap(lt);
                    screenToMap(rb);
                    MyRect region = new MyRect(lt.x, lt.y, rb.x, rb.y);
                    region.adjust();
                    rasterLayer.Draw(tmpCanvas, paint, mapOriginX, mapOriginY, region, curScale);
                }
                canvas.drawBitmap(bmpTempCanvas, 0, 0, paint);
                drawOverlay(canvas);
                isRefresh = false;
                long t2 = System.currentTimeMillis();
                System.out.println("Main_MyView_onDraw : draw over. time = " + (t2 - t1));
            } else {
                canvas.drawBitmap(bmpTempCanvas, 0, 0, paint);
                drawOverlay(canvas);
            }
        }

        private void drawOverlay(Canvas canvas) {
            drawQqTrack(canvas);
            drawLoc(canvas);
            drawTrack(canvas);
            drawYangdi(canvas);
        }

        private void drawLoc(Canvas canvas) {
            if (myLoc != null) {
                MyPoint pt1 = new MyPoint(myLoc.x, myLoc.y);
                mapToScreen(pt1);
                canvas.drawBitmap(bmpLocation, pt1.x - bmpLocation.getWidth() / 2, pt1.y - bmpLocation.getHeight(), paint);
                double dis = MyMap.GetDistance(myLoc.y, myLoc.x, ydLoc.y, ydLoc.x);
                MyPoint pt2 = new MyPoint(ydLoc);
                mapToScreen(pt2);
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(3 * dpiScale);
                paint.setTextSize(textSize);
                canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
                String text = "";
                if (dis > 1000) {
                    dis /= 1000;
                    dis = MyFuns.MyDecimal(dis, 2);
                    text = "距离:" + dis + "公里";
                } else {
                    dis = MyFuns.MyDecimal(dis, 2);
                    text = "距离:" + dis + "米";
                }
                if (dis > 0) {
                    //canvas.drawText(text, pt2.x+bmpCuryd.getWidth()/2+5, pt2.y, paint);
                    tvDis.setText(text);
                }

                float f0 = 100;
                double fwj0 = Math.atan((pt1.x - pt2.x) / (pt2.y - pt1.y));
                double x0 = f0 * Math.sin(fwj0);
                double y0 = f0 * Math.cos(fwj0);
                if (pt2.y > pt1.y) {
                    x0 = pt1.x - x0;
                    y0 = pt1.y + y0;
                } else {
                    x0 = pt1.x + x0;
                    y0 = pt1.y - y0;
                }
                float f1 = 53.85f;
                double fwj1 = fwj0 - 22.0f / 180 * Math.PI;
                double x1 = f1 * Math.sin(fwj1);
                double y1 = f1 * Math.cos(fwj1);
                if (pt2.y > pt1.y) {
                    x1 = pt1.x - x1;
                    y1 = pt1.y + y1;
                } else {
                    x1 = pt1.x + x1;
                    y1 = pt1.y - y1;
                }
                float f2 = 53.85f;
                double fwj2 = fwj0 + 22.0f / 180 * Math.PI;
                double x2 = f2 * Math.sin(fwj2);
                double y2 = f2 * Math.cos(fwj2);
                if (pt2.y > pt1.y) {
                    x2 = pt1.x - x2;
                    y2 = pt1.y + y2;
                } else {
                    x2 = pt1.x + x2;
                    y2 = pt1.y - y2;
                }
                float f3 = 65;
                double x3 = f3 * Math.sin(fwj0);
                double y3 = f3 * Math.cos(fwj0);
                if (pt2.y > pt1.y) {
                    x3 = pt1.x - x3;
                    y3 = pt1.y + y3;
                } else {
                    x3 = pt1.x + x3;
                    y3 = pt1.y - y3;
                }
                Path path = new Path();
                path.moveTo((float) x0, (float) y0);
                path.lineTo((float) x1, (float) y1);
                path.lineTo((float) x3, (float) y3);
                path.lineTo((float) x2, (float) y2);
                path.close();
                paint.setStyle(Style.FILL);
                canvas.drawPath(path, paint);
            }
        }

        private void drawTrack(Canvas canvas) {
            if (MyConfig.GetShowTrack()) {
                String trkname = MyConfig.GetCurTrk();
                if (trkname.equals("")) return;
                List<MyPoint> lstTrack = YangdiMgr.GetTrkPoint(ydh, trkname);
                int count = lstTrack.size();
                if (count > 0) {
                    paint.setColor(MyConfig.GetTrackColor());
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(3 * dpiScale);

                    for (int i = 0; i < count; i++) {
                        mapToScreen(lstTrack.get(i));
                    }
                    canvas.drawBitmap(bmpTrknode, lstTrack.get(0).x - bmpTrknode.getWidth() / 2, lstTrack.get(0).y - bmpTrknode.getHeight() / 2, paint);
                    for (int i = 1; i < count; i++) {
                        canvas.drawLine(lstTrack.get(i - 1).x, lstTrack.get(i - 1).y, lstTrack.get(i).x, lstTrack.get(i).y, paint);
                        canvas.drawBitmap(bmpTrknode, lstTrack.get(i).x - bmpTrknode.getWidth() / 2, lstTrack.get(i).y - bmpTrknode.getHeight() / 2, paint);
                    }
                }
            }
        }

        private void drawQqTrack(Canvas canvas) {
            if (lstTrkss == null || lstTrkss.size() == 0) return;
            if (MyConfig.GetShowQqTrack()) {
                for (int k = 0; k < lstTrkss.size(); k++) {
                    List<MyPoint> lstTrack = lstTrkss.get(k);
                    int count = lstTrack.size();
                    if (count > 0) {
                        paint.setColor(MyConfig.GetQqTrackColor());
                        paint.setStyle(Style.FILL);
                        paint.setStrokeWidth(3 * dpiScale);

                        MyPoint pt1 = new MyPoint();
                        MyPoint pt2 = new MyPoint();
                        mapToScreen(lstTrack.get(0), pt1);
                        canvas.drawBitmap(bmpTrknode, pt1.x - bmpTrknode.getWidth() / 2, pt1.y - bmpTrknode.getHeight() / 2, paint);
                        for (int i = 1; i < count; i++) {
                            mapToScreen(lstTrack.get(i - 1), pt1);
                            mapToScreen(lstTrack.get(i), pt2);
                            canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
                            canvas.drawBitmap(bmpTrknode, pt2.x - bmpTrknode.getWidth() / 2, pt2.y - bmpTrknode.getHeight() / 2, paint);
                        }
                    }
                }
            }
        }

        private void drawYangdi(Canvas canvas) {
            int r = bmpYangdi0.getWidth() / 2;
            for (int i = 0; i < lstYangdis.size(); i++) {
                YDInfo yd = lstYangdis.get(i);
                MyPoint ptYdloc = new MyPoint(lstYdlocs.get(i));
                mapToScreen(ptYdloc);

                if (yd.dczt == 0) {
                    if (yd.sfyf == 0) {
                        canvas.drawBitmap(bmpYangdi0, ptYdloc.x - r, ptYdloc.y - r, paint);
                    } else {
                        canvas.drawBitmap(bmpYangfang0, ptYdloc.x - r, ptYdloc.y - r, paint);
                    }
                }
                if (yd.dczt == 1) {
                    if (yd.sfyf == 0) {
                        canvas.drawBitmap(bmpYangdi1, ptYdloc.x - r, ptYdloc.y - r, paint);
                    } else {
                        canvas.drawBitmap(bmpYangfang1, ptYdloc.x - r, ptYdloc.y - r, paint);
                    }
                }
                if (yd.dczt == 2) {
                    if (yd.sfyf == 0) {
                        canvas.drawBitmap(bmpYangdi2, ptYdloc.x - r, ptYdloc.y - r, paint);
                    } else {
                        canvas.drawBitmap(bmpYangfang2, ptYdloc.x - r, ptYdloc.y - r, paint);
                    }
                }

                if (yd.ydh == ydh) {
                    canvas.drawBitmap(bmpCuryd, ptYdloc.x - bmpCuryd.getWidth() / 2, ptYdloc.y - bmpCuryd.getHeight(), paint);
                }

                paint.setColor(Color.RED);
                paint.setTextSize(textSize);
                canvas.drawText(String.valueOf(yd.ydh), ptYdloc.x + r + 3, ptYdloc.y + 9, paint);
            }
        }

        //触摸事件处理
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int n = event.getPointerCount();
            if (n == 2) {
                if (!isZoom) {
                    disBegin = MyFuns.LineLen(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    isZoom = true;
                } else {
                    disEnd = MyFuns.LineLen(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    if (disEnd > disBegin) {
                        if (disEnd - disBegin > zoomSize) {
                            disBegin = disEnd;
                            tmpScale *= 1.1f;
                            matrixZoom.setScale(tmpScale, tmpScale, view_width / 2f, view_height / 2f);
                            RefreshOverlay();
                        }
                    } else {
                        if (disBegin - disEnd > zoomSize) {
                            disBegin = disEnd;
                            tmpScale *= 0.9f;
                            matrixZoom.setScale(tmpScale, tmpScale, view_width / 2f, view_height / 2f);
                            RefreshOverlay();
                        }
                    }
                }
                return true;
            }

            if (n < 2 && isZoom == true) {
                isZoom = false;
                isMove = false;
                disBegin = -1;
                disEnd = -1;
                ZoomTo(1.0f / tmpScale * GetPixScale());
                RefreshMap();
                tmpScale = 1;
                return true;
            }

            if (n == 1) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        xBegin = event.getX();
                        yBegin = event.getY();
                        x0 = xBegin;
                        y0 = yBegin;
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        if (isMove) {
                            isMove = false;
                            matrixMove.reset();
                            MyPoint pt = new MyPoint(MyConfig.GetMapViewWidth() / 2, MyConfig.GetMapViewHeight() / 2);
                            screenToMap(pt);
                            RefreshMap();
                        }
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                        xEnd = event.getX();
                        yEnd = event.getY();
                        float distanceX = xEnd > x0 ? xEnd - x0 : x0 - xEnd;
                        float distanceY = yEnd > y0 ? yEnd - y0 : y0 - yEnd;

                        if (distanceX > 10 || distanceY > 10) {
                            isMove = true;
                            Move(xEnd - x0, yEnd - y0);
                            matrixMove.postTranslate(xEnd - x0, yEnd - y0);
                            x0 = xEnd;
                            y0 = yEnd;
                            RefreshOverlay();
                        }
                    }
                    break;
                }
            }
            return mGestureDetector.onTouchEvent(event);
        }

        public void RefreshMap() {
            isRefresh = true;
            postInvalidate();
            updateScale();
        }

        public void RefreshOverlay() {
            isRefresh = false;
            postInvalidate();
        }

        public int GetMapScale() {
            MyPoint ptLeft = new MyPoint(0, view_height / 2);
            screenToMap(ptLeft);
            MyPoint ptRight = new MyPoint(view_width, view_height / 2);
            screenToMap(ptRight);
            double dis = GetDistance(ptLeft.y, ptLeft.x, ptRight.y, ptRight.x);
            dis = dis / mw / 100;
            if (Double.isInfinite(dis) || Double.isNaN(dis)) return -1;
            return (int) dis;
        }

        public float GetPixScale() {
            return curScale;
        }

        public void MoveToYangdi() {
            MoveTo(new MyPoint(ydLoc));
            ZoomTo(curScale * curScale / 21183.354f);
        }

        public void MoveTo(MyPoint pt) {
            mapToScreen(pt);
            Move(view_width / 2 - pt.x, view_height / 2 - pt.y);
        }

        public void ZoomIn() {
            ZoomTo(curScale * 0.5f);
        }

        public void ZoomOut() {
            ZoomTo(curScale * 2f);
        }

        public void ZoomToAll() {
            curScale = initScale;
            mapOriginX = view_width / 2 - fBound.center().x * initScale;
            mapOriginY = view_height + (fBound.center().y * initScale - view_height / 2);
            RefreshMap();
        }

        private void SetInitBound(float left, float top, float right, float bottom) {
            initBound = new MyRect(left, top, right, bottom);
        }

        private void SetBound(float left, float top, float right, float bottom) {
            fBound = new MyRect(left, top, right, bottom);

            float hscale = view_width / fBound.width();
            float vscale = view_height / fBound.height();
            initScale = (hscale < vscale) ? hscale : vscale;
            curScale = initScale;
            mapOriginX = view_width / 2 - fBound.center().x * initScale;
            mapOriginY = view_height + (fBound.center().y * initScale - view_height / 2);
            RefreshMap();
        }

        private void ZoomTo(float s) {
            float scale = curScale / s;
            if (scale * curScale < 175) scale = 175 / curScale;
            if (scale * curScale > 5795416) scale = 5795416 / curScale;
            MyPoint pt = new MyPoint(view_width / 2, view_height / 2);
            screenToMap(pt);
            float dx = (float) (pt.x * curScale * (1 - scale));
            float dy = (float) (pt.y * curScale * (scale - 1));
            Move(dx, dy);
            curScale *= scale;
            RefreshMap();
        }

        private void Move(float dx, float dy) {
            mapOriginX += dx;
            mapOriginY += dy;
            RefreshMap();
        }

        private void mapToScreen(MyPoint pt) {
            pt.x = pt.x * curScale + mapOriginX;
            pt.y = mapOriginY - pt.y * curScale;
        }

        private void mapToScreen(MyPoint ptIn, MyPoint ptOut) {
            ptOut.x = ptIn.x * curScale + mapOriginX;
            ptOut.y = mapOriginY - ptIn.y * curScale;
        }

        private void screenToMap(MyPoint pt) {
            pt.x = (pt.x - mapOriginX) / curScale;
            pt.y = (mapOriginY - pt.y) / curScale;
        }

        private void addRaster(String path) {
            rasterLayer = new RasterLayer();
            rasterLayer.Open(path);

            fBound.left = rasterLayer.GetBound().left;
            fBound.right = rasterLayer.GetBound().right;
            fBound.top = rasterLayer.GetBound().top;
            fBound.bottom = rasterLayer.GetBound().bottom;

            float hscale = view_width / fBound.width();
            float vscale = view_height / fBound.height();
            initScale = (hscale < vscale) ? hscale : vscale;
            curScale = initScale;
            mapOriginX = view_width / 2 - fBound.center().x * initScale;
            mapOriginY = view_height + (fBound.center().y * initScale - view_height / 2);
            RefreshMap();
        }

        private void removeRaster() {
            rasterLayer = null;
            fBound = new MyRect(initBound);
            float hscale = view_width / fBound.width();
            float vscale = view_height / fBound.height();
            initScale = (hscale < vscale) ? hscale : vscale;
            curScale = initScale;
            mapOriginX = view_width / 2 - fBound.center().x * initScale;
            mapOriginY = view_height + (fBound.center().y * initScale - view_height / 2);
            RefreshMap();
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
            return true;
        }

        //轻击事件
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

    class TrkTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(MyMap84.this);
            dlg.setCanceledOnTouchOutside(false);
            dlg.setMessage("正在加载前期航迹...");
            dlg.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            readQqTrack();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            myView.RefreshOverlay();
            dlg.cancel();
            if (isHuicai) {
                List<String> lst = YangdiMgr.GetTrkNames(ydh);
                if (lst.size() == 0) {
                    String trk = Qianqimgr.GetXianJuNameByYdh(ydh) + "-" + ydh + "-" + MyFuns.GetDateByNumberS() + "-hui";
                    YangdiMgr.CreateTrack(ydh, trk);
                    MyConfig.SetCurTrk(trk);
                    MyConfig.SetOpenTrack(true);
                    btnTrk.setChecked(true);
                } else {
                    String trk = Qianqimgr.GetXianJuNameByYdh(ydh) + "-" + ydh + "-" + MyFuns.GetDateByNumberS() + "-hui";
                    if (YangdiMgr.IsHaveTrkName(ydh, trk)) {
                        MyConfig.SetOpenTrack(true);
                        TrackDialog dlgTrack = new TrackDialog(MyMap84.this, ydh);
                        TrackInfo info = dlgTrack.showDialog();
                        if (info != null) {
                            btnTrk.setChecked(MyConfig.GetOpenTrack());
                        } else {
                            MyConfig.SetOpenTrack(false);
                            btnTrk.setChecked(false);
                        }
                    } else {
                        YangdiMgr.CreateTrack(ydh, trk);
                        MyConfig.SetCurTrk(trk);
                        MyConfig.SetOpenTrack(true);
                        btnTrk.setChecked(true);
                    }
                }
            } else if (isBDBack) {
                MyConfig.SetOpenTrack(true);
                btnTrk.setChecked(true);
            }
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(), 1000);
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);
            LocationManager locationManager = (LocationManager) MyMap84.this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps) {
                myHandler.sendEmptyMessage(1);
            }
        }
    }

    ;

    class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MyMakeSureDialog dlg = new MyMakeSureDialog(MyMap84.this, "提示", "设备GPS功能未开启，需手动开启，否则无法定位及采集航迹！", "去设置", "忽略");
                if (dlg.showDialog()) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MyMap84.this.startActivityForResult(intent, 0);
                }
            }
        }
    }

    ;
}
