package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BNaviPoint.CoordinateType;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams.Const.LayerMode;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.BaiduNaviManager;
import com.tdgeos.dlg.base.MyAboutDialog;
import com.tdgeos.dlg.base.MyMakeSureDialog;
import com.tdgeos.dlg.yddc.CompassDialog;
import com.tdgeos.dlg.yddc.ParamsDialog;
import com.tdgeos.dlg.yddc.TrackDialog;
import com.tdgeos.dlg.yddc.YdhDialog;
import com.tdgeos.lib.CoordTransform;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.lib.MyPoint;
import com.tdgeos.yangdi.Qianqimgr;
import com.tdgeos.yangdi.Resmgr;
import com.tdgeos.yangdi.Setmgr;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.LinearLayout.LayoutParams;

public class BDMap extends Activity implements View.OnClickListener, MKOfflineMapListener {
    private Button btnMenu = null;
    private PopupWindow popMenu = null;
    private ListView lvMenu = null;

    private TextView tvYdh = null;
    private Button btnChange = null;
    private Button btnInto = null;

    private MapView myMapView;
    private BaiduMap myBaiduMap;
    private LocationClient locClient = null;
    private MyLocationListenner bdListener = new MyLocationListenner();
    private boolean isFirstLoc = true;
    private BDLocation bdLoc = null;//坐标系为：bd0911

    private MKOfflineMap mOffline = null;

    private Button btnMyMap = null;
    private Button btnShpMap = null;
    private Button btnRstMap = null;
    private Button btnMyLoc = null;
    private ToggleButton btnTrk = null;
    private Button btnTrack = null;
    private Button btnCompass = null;
    private Button btnNavi = null;
    private Button btnYdloc = null;

    private int ydh = 0;
    private MyPoint ydLoc = null;

    private TextView tvLoc = null;
    private TextView tvDis = null;
    private MyPoint myLoc = null;
    private MyBroadcastReceiver myBroadcastReceiver = null;
    private MyHandler myHandler = null;

    private CompassDialog dlgCompass = null;
    private MyAboutDialog dlgAbout = null;

    private float dpiScale = 1;
    private int zone = MyConfig.GetParamsZone();
    int lon0 = MyConfig.GetParamsLon0();
    double dx = MyConfig.GetParamsDx();
    double dy = MyConfig.GetParamsDy();
    double dz = MyConfig.GetParamsDz();
    double rx = MyConfig.GetParamsRx();
    double ry = MyConfig.GetParamsRy();
    double rz = MyConfig.GetParamsRz();
    double k = MyConfig.GetParamsK();

    private boolean mIsEngineInitSuccess = false;
    private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
        public void engineInitSuccess() {
            mIsEngineInitSuccess = true;
            //System.out.println("导航初始化成功.");
        }

        public void engineInitStart() {
            //System.out.println("导航初始化开始.");
        }

        public void engineInitFail() {
            //System.out.println("导航初始化失败.");
        }
    };
    private LBSAuthManagerListener mLBSAuthManagerListener = new LBSAuthManagerListener() {
        @Override
        public void onAuthResult(int status, String msg) {
            if (0 == status) {
                //System.out.println("key校验成功!");
            } else {
                //System.out.println("key校验失败, " + msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bdmap);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dpiScale = getResources().getDisplayMetrics().densityDpi * 1.0f / 160;

        ydh = getIntent().getIntExtra("ydh", 0);
        MyConfig.SetOpenTrack(MyConfig.GetOpenTrack());
        MyConfig.SetOpenLoc(true);

        myMapView = (MapView) findViewById(R.id.bmapView);
        myBaiduMap = myMapView.getMap();
        myBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(31.2334, 121.476389);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        myBaiduMap.animateMapStatus(u);

        mOffline = new MKOfflineMap();
        mOffline.init(this);

        loadYangdi();

        String dir = null;
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory().toString();
        }
        BaiduNaviManager.getInstance().initEngine(this, dir, mNaviEngineInitListener, mLBSAuthManagerListener);

        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, new IntentFilter("location_changed"));

        locClient = new LocationClient(this);
        locClient.registerLocationListener(bdListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        locClient.setLocOption(option);
        locClient.start();

        List<java.util.Map<String, String>> list = new ArrayList<java.util.Map<String, String>>();
        java.util.Map<String, String> item = null;
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
                        ParamsDialog dlgParams = new ParamsDialog(BDMap.this);
                        if (dlgParams.showDialog()) {
                            lon0 = MyConfig.GetParamsLon0();
                            dx = MyConfig.GetParamsDx();
                            dy = MyConfig.GetParamsDy();
                            dz = MyConfig.GetParamsDz();
                            rx = MyConfig.GetParamsRx();
                            ry = MyConfig.GetParamsRy();
                            rz = MyConfig.GetParamsRz();
                            k = MyConfig.GetParamsK();
                            myBaiduMap.clear();
                            loadYangdi();
                        }
                        break;
                    }
                    case 1: {
                        dlgAbout.show();
                        break;
                    }
                }
            }
        });

        btnMenu = (Button) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(this);

        tvYdh = (TextView) findViewById(R.id.tv_ydh);
        int qqdl = Qianqimgr.GetQqdl(ydh);
        int bqdl = YangdiMgr.GetBqdl(ydh);
        String qq = Resmgr.GetValueByCode("dl", qqdl);
        String bq = Resmgr.GetValueByCode("dl", bqdl);
        if (qq.equals("")) qq = "无";
        if (bq.equals("")) bq = "无";
        tvYdh.setText("当前样地：" + ydh + " (" + qq + " / " + bq + ")");
        btnChange = (Button) findViewById(R.id.btn_change);
        btnInto = (Button) findViewById(R.id.btn_into);
        btnChange.setOnClickListener(this);
        btnInto.setOnClickListener(this);

        tvLoc = (TextView) findViewById(R.id.map_tv_loc);
        tvDis = (TextView) findViewById(R.id.map_tv_dis);

        btnMyMap = (Button) findViewById(R.id.map_btn_mymap);
        btnMyLoc = (Button) findViewById(R.id.map_btn_myloc);
        btnTrk = (ToggleButton) findViewById(R.id.map_btn_trk);
        btnTrack = (Button) findViewById(R.id.map_btn_track);
        btnCompass = (Button) findViewById(R.id.map_btn_compass);
        btnShpMap = (Button) findViewById(R.id.map_btn_vector);
        btnRstMap = (Button) findViewById(R.id.map_btn_raster);
        btnNavi = (Button) findViewById(R.id.map_btn_navi);
        btnYdloc = (Button) findViewById(R.id.map_btn_ydloc);
        btnMyMap.setOnClickListener(this);
        btnMyLoc.setOnClickListener(this);
        btnTrk.setOnClickListener(this);
        btnTrack.setOnClickListener(this);
        btnCompass.setOnClickListener(this);
        btnShpMap.setOnClickListener(this);
        btnRstMap.setOnClickListener(this);
        btnNavi.setOnClickListener(this);
        btnYdloc.setOnClickListener(this);

        btnTrk.setChecked(MyConfig.GetOpenTrack());

        dlgCompass = new CompassDialog(this);
        dlgAbout = new MyAboutDialog(this);

        myHandler = new MyHandler();

        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 1000);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mOffline.importOfflineData();
    }

    @Override
    public void onStart() {
        super.onStart();
        mOffline.importOfflineData();
    }

    @Override
    public void onResume() {
        myMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        myMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        myBaiduMap.setMyLocationEnabled(false);
        myMapView.onDestroy();
        myMapView = null;
        //mOffline.destroy();
        locClient.stop();
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
            case R.id.map_btn_mymap: {
                if (YangdiMgr.MY_MAP_TYPE == 54) {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", ydh);
                    intent.putExtra("back", MyConfig.GetOpenTrack() ? 1 : 0);
                    intent.setClass(this, MyMap.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ydh", ydh);
                    intent.putExtra("back", MyConfig.GetOpenTrack() ? 1 : 0);
                    intent.setClass(this, MyMap84.class);
                    startActivity(intent);
                }
                finish();
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

                    myBaiduMap.clear();
                    loadYangdi();
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
                if (bdLoc == null) break;
                LatLng ll = new LatLng(bdLoc.getLatitude(), bdLoc.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                myBaiduMap.animateMapStatus(u);
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
                        dlgTrack.showDialog();
                        btnTrk.setChecked(MyConfig.GetOpenTrack());
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
            case R.id.map_btn_vector: {
                myBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            }
            case R.id.map_btn_raster: {
                myBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            }
            case R.id.map_btn_navi: {
                if (!mIsEngineInitSuccess) {
                    Toast.makeText(this, "导航初始化失败！", 1).show();
                    break;
                }
                BNaviPoint startPoint = null;//new BNaviPoint(116.307854,40.055878, "百度大厦", CoordinateType.BD09_MC);
                BNaviPoint endPoint = null;//new BNaviPoint(116.403875,39.915168, "天安门", CoordinateType.BD09_MC);
                double lon = -1;
                double lat = -1;
                if (myLoc != null) {
                    lon = myLoc.x;
                    lat = myLoc.y;
                    startPoint = new BNaviPoint(lon, lat, "起点", CoordinateType.WGS84);
                } else if (bdLoc != null) {
                    lon = bdLoc.getLongitude();
                    lat = bdLoc.getLatitude();
                    startPoint = new BNaviPoint(lon, lat, "起点", CoordinateType.BD09_MC);
                }
                if (lon < 0 || lat < 0) {
                    Toast.makeText(this, "尚未定位成功！", 1).show();
                    break;
                }
                if (ydLoc == null) {
                    return;
                }
                endPoint = new BNaviPoint(ydLoc.x, ydLoc.y, "终点", CoordinateType.WGS84);
                BaiduNaviManager.getInstance().launchNavigator(
                        this,
                        startPoint,
                        endPoint,
                        NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,
                        true,
                        BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
                        new OnStartNavigationListener() {
                            @Override
                            public void onJumpToNavigator(Bundle configParams) {
                                //BaiduNaviManager.getInstance().dismissWaitProgressDialog();
                                //BNMapController.getInstance().setLayerMode(LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
                                //mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance().getModel(ModelName.ROUTE_PLAN);
                                Intent intent = new Intent(BDMap.this, BDNavi.class);
                                intent.putExtras(configParams);
                                startActivity(intent);
                            }

                            @Override
                            public void onJumpToDownloader() {
                            }
                        }
                );
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
                double dis = MyMap.GetDistance(ydLoc.y, ydLoc.x, pt.y, pt.x);
                dis = MyFuns.MyDecimal(dis, 2);
                MyMakeSureDialog dlg = new MyMakeSureDialog(this, "提示", "当前位置与样地相距 " + dis + " 米，是否继续保存当前坐标？", "保存", "取消");
                if (dlg.showDialog()) {
                    YangdiMgr.SetYdloc(ydh, pt);
                    Toast.makeText(this, "样地定位完成！", 1).show();
                }
                break;
            }
        }
    }

    @Override
    public void onGetOfflineMapState(int arg0, int arg1) {

    }

    private void loadYangdi() {
        myBaiduMap.clear();

        if (ydh <= 0) return;
        YDInfo yd = Setmgr.GetTask(ydh);
        MyCoord ptYd = new MyCoord(yd.gpshzb, yd.gpszzb, 0);
        Bj54ToGps(ptYd, lon0, dx, dy, dz, rx, ry, rz, k);
        LatLng pt = new LatLng(ptYd.y, ptYd.x);
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.GPS);
        converter.coord(pt);
        LatLng desLatLng = converter.convert();

        ydLoc = new MyPoint(ptYd.x, ptYd.y);

        if (bdLoc != null && bdLoc.getLatitude() > 1 && bdLoc.getLongitude() > 1) {
            LatLng p2 = new LatLng(bdLoc.getLatitude(), bdLoc.getLongitude());
            List<LatLng> points = new ArrayList<LatLng>();
            points.add(desLatLng);
            points.add(p2);
            OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAA00FF00).points(points);
            myBaiduMap.addOverlay(ooPolyline);
        }

        OverlayOptions ooDot = new MarkerOptions().title(String.valueOf(ydh)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_yd_cur)).position(desLatLng).anchor(0.5f, 1.0f);
        myBaiduMap.addOverlay(ooDot);
        OverlayOptions ooText = new TextOptions().fontSize((int) (16 * dpiScale)).fontColor(0xFFFF0000).text("   " + ydh).position(desLatLng).align(TextOptions.ALIGN_LEFT, TextOptions.ALIGN_CENTER_VERTICAL);
        myBaiduMap.addOverlay(ooText);
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

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || myMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            myBaiduMap.setMyLocationData(locData);
            bdLoc = new BDLocation(location);
            loadYangdi();
            if (isFirstLoc && location.getLatitude() > 1 && location.getLongitude() > 1) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                myBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public class MyMarkerClickListener implements OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            TextView text = new TextView(getApplicationContext());
            text.setBackgroundResource(R.drawable.bd_marker_1);
            text.setPadding(5, 2, 5, 2);
            text.setText(marker.getTitle());
            InfoWindow infoWindow = new InfoWindow(text, marker.getPosition(), -30);
            myBaiduMap.showInfoWindow(infoWindow);
            return false;
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
                if (isFirstLoc && lat > 1 && lon > 1) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(lat, lon);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    myBaiduMap.animateMapStatus(u);
                }
                double dis = MyMap.GetDistance(ydLoc.y, ydLoc.x, lat, lon);
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
            } else {
                tvLoc.setText("正在定位...");
                tvDis.setText("");
                myLoc = null;
            }
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);
            LocationManager locationManager = (LocationManager) BDMap.this.getSystemService(Context.LOCATION_SERVICE);
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
                MyMakeSureDialog dlg = new MyMakeSureDialog(BDMap.this, "提示", "设备GPS功能未开启，需手动开启，否则无法定位及采集航迹！", "去设置", "忽略");
                if (dlg.showDialog()) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    BDMap.this.startActivityForResult(intent, 0);
                }
            }
        }
    }

    ;
}
