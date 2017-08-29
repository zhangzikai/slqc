package com.tdgeos.dlg.yddc;

import java.util.Iterator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdgeos.lib.CoordTransform;
import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.tanhui.MyMap;
import com.tdgeos.tanhui.R;

public class CompassDialog extends Dialog implements OnClickListener {
    private Context ctt;
    private SensorManager sensorManager = null;
    private MySensorEventListener sensorEventListener = null;
    private LinearLayout layCompassView = null;
    private ImageView ivCompass = null;
    private TextView tvFwj = null;
    private TextView tvQxj = null;


    private LocationManager locationManager = null;
    private MyLocationListener locListener = null;
    private MyGpsListener gpsListener = null;
    private LinearLayout layGpsView = null;
    private LinearLayout layView = null;
    private MyGpsView myGpsView = null;
    private TextView tvNum = null;
    private TextView tvJingdu = null;
    private TextView tvLon = null;
    private TextView tvLat = null;
    private TextView tvAlt = null;
    private TextView tvHzb = null;
    private TextView tvZzb = null;

    private Button btnOk;
    private Button btnCancel;

    private int dpi = MyConfig.GetDpi();

    public CompassDialog(Context context) {
        super(context);
        ctt = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_yddc_compass);
        setTitle("星历罗盘");
        if (dpi == 320) this.getWindow().getAttributes().width = 1240;
        else if (dpi == 240) this.getWindow().getAttributes().width = 830;
        else if (dpi == 160) this.getWindow().getAttributes().width = 620;
        else this.getWindow().getAttributes().width = 620;
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

            }
        });

        layCompassView = (LinearLayout) findViewById(R.id.lay_1);
        ivCompass = (ImageView) findViewById(R.id.iv_compass);
        tvFwj = (TextView) findViewById(R.id.tv_fwj);
        tvQxj = (TextView) findViewById(R.id.tv_qxj);
        //layCompassView.setVisibility(8);

        myGpsView = new MyGpsView(ctt);
        layGpsView = (LinearLayout) findViewById(R.id.lay_2);
        layView = (LinearLayout) findViewById(R.id.lay_view);
        layView.addView(myGpsView);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvJingdu = (TextView) findViewById(R.id.tv_jingdu);
        tvLon = (TextView) findViewById(R.id.tv_lon);
        tvLat = (TextView) findViewById(R.id.tv_lat);
        tvAlt = (TextView) findViewById(R.id.tv_alt);
        tvHzb = (TextView) findViewById(R.id.tv_hzb);
        tvZzb = (TextView) findViewById(R.id.tv_zzb);
        //layGpsView.setVisibility(8);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        sensorEventListener = new MySensorEventListener();
        sensorManager = (SensorManager) ctt.getSystemService(Activity.SENSOR_SERVICE);
        //sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);

        locListener = new MyLocationListener();
        gpsListener = new MyGpsListener();
        locationManager = (LocationManager) ctt.getSystemService(Context.LOCATION_SERVICE);
        //locationManager.addGpsStatusListener(gpsListener);
        //locationManager.requestLocationUpdates("gps", 3000, 0, locListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        locationManager.addGpsStatusListener(gpsListener);
        locationManager.requestLocationUpdates("gps", 3000, 0, locListener);
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(sensorEventListener);
        locationManager.removeUpdates(locListener);
        locationManager.removeGpsStatusListener(gpsListener);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                this.cancel();
                break;
            case R.id.btn_cancel:
                this.cancel();
                break;
        }
    }


    class MySensorEventListener implements SensorEventListener {
        private float currentDegree = 0f;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float azimuth_angle = event.values[0];
            float pitch_angle = event.values[1];
            if (pitch_angle < 0) pitch_angle = -pitch_angle;
            //float roll_angle = event.values[2];
            azimuth_angle = MyFuns.MyDecimal(azimuth_angle, 2);
            pitch_angle = MyFuns.MyDecimal(pitch_angle, 2);
            tvFwj.setText("方位角：" + MyFuns.NumberToDegree(azimuth_angle));
            tvQxj.setText("倾斜角：" + MyFuns.NumberToDegree(pitch_angle));
            /*
            RotateAnimation类：旋转变化动画类 
			参数说明:
            fromDegrees：旋转的开始角度。 
            toDegrees：旋转的结束角度。 
            pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。 
            pivotXValue：X坐标的伸缩值。 
            pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。 
            pivotYValue：Y坐标的伸缩值 
            */
            RotateAnimation ra = new RotateAnimation(currentDegree, -azimuth_angle,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            //旋转过程持续时间  
            int t = (int) (currentDegree + azimuth_angle);
            if (t < 0) t = -t;
            t = t / 360 * 1000;
            if (t < 200) t = 200;
            ra.setDuration(t);
            //罗盘图片使用旋转动画  
            ivCompass.startAnimation(ra);
            currentDegree = -azimuth_angle;
        }
    }

    class MyGpsView extends View {
        private Paint paint = new Paint();
        private int vw = 0;
        private int vh = 0;
        private int r = 0;
        private boolean isFirst = true;

        private GpsStatus gpsStatus = null;
        private Bitmap bmpSate = null;

        public MyGpsView(Context context) {
            super(context);
            bmpSate = BitmapFactory.decodeResource(getResources(), R.drawable.ic_satellite_0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float dpiScale = dpi * 1.0f / 160;
            if (isFirst) {
                paint.setAntiAlias(true);
                vw = this.getWidth();
                vh = this.getHeight();
                r = (int) (vw - 50 * dpiScale) / 6;

                isFirst = false;
            }

            paint.reset();
            paint.setStyle(Style.FILL);
            paint.setColor(0xffd0d0d0);
            canvas.drawCircle(vw / 2, vh / 2, 15 * dpiScale, paint);

            paint.reset();
            paint.setStyle(Style.STROKE);
            paint.setColor(0xffd0d0d0);
            float r1 = r;
            canvas.drawCircle(vw / 2, vh / 2, r1, paint);
            float r2 = r + r;
            canvas.drawCircle(vw / 2, vh / 2, r2, paint);
            float r3 = r + r + r;
            canvas.drawCircle(vw / 2, vh / 2, r3, paint);

            if (gpsStatus != null) {
                paint.reset();
                paint.setStyle(Style.FILL);
                paint.setColor(0xff00ff00);
                paint.setTextSize(28);
                int maxSatellites = gpsStatus.getMaxSatellites();
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) {
                    GpsSatellite s = iters.next();
                    float a = s.getAzimuth();//方位角
                    float e = s.getElevation();//高度角
                    int id = s.getPrn();
                    e = 90 - e;
                    float len = e / 90 * r * 3;
                    float x = (float) (Math.sin(a * Math.PI / 180) * len);
                    float y = (float) (Math.cos(a * Math.PI / 180) * len);
                    canvas.drawBitmap(bmpSate, vw / 2 + x - bmpSate.getWidth() / 2, vh / 2 + y - bmpSate.getHeight() / 2, paint);
                    canvas.drawText(String.valueOf(id), vw / 2 + x + bmpSate.getWidth() / 2 + 3, vh / 2 + y + 14, paint);
                    count++;
                }
                tvNum.setText("卫星数量：" + count);

            }
        }

        public void ResetGpsStatus(GpsStatus gps) {
            gpsStatus = gps;
            this.postInvalidate();
        }
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude();
            double lat = location.getLatitude();
            double alt = location.getAltitude();
            tvLon.setText("经　　度：" + MyFuns.NumberToDegree(lon));
            tvLat.setText("纬　　度：" + MyFuns.NumberToDegree(lat));
            tvAlt.setText("海　　拔：" + MyFuns.MyDecimal(alt, 2));
            double jd = location.getAccuracy();
            tvJingdu.setText("坐标精度：" + MyFuns.MyDecimal(jd, 2));
            MyCoord c = new MyCoord(lon, lat, 0);
            int zone = MyConfig.GetParamsZone();
            int lon0 = MyConfig.GetParamsLon0();
            double dx = MyConfig.GetParamsDx();
            double dy = MyConfig.GetParamsDy();
            double dz = MyConfig.GetParamsDz();
            double rx = MyConfig.GetParamsRx();
            double ry = MyConfig.GetParamsRy();
            double rz = MyConfig.GetParamsRz();
            double k = MyConfig.GetParamsK();
            MyCoord c1 = CoordTransform.GeoToKjzj_wgs84(c);
            MyCoord c2 = CoordTransform.Transform(c1, -dx, -dy, -dz, rx, ry, rz, k);
            MyCoord c3 = CoordTransform.KjzjToGeo_bj54(c2);
            MyCoord c4 = CoordTransform.GeoToGauss_bj54(c3, lon0);
            if (zone == 6) {
                c.x = c4.x + MyMap.Get6Daihao(lon0) * 1000000;
            } else if (zone == 3) {
                c.x = c4.x + MyMap.Get3Daihao(lon0) * 1000000;
            }
            c.y = c4.y;
            tvHzb.setText("横  坐  标：" + (int) c.x);
            tvZzb.setText("纵  坐  标：" + (int) c.y);
        }

        @Override
        public void onProviderDisabled(String provider) {
            //设备GPS功能被关闭时触发
        }

        @Override
        public void onProviderEnabled(String provider) {
            //设备GPS功能打开时触发
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //GPS状态变化时触发
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }
        }
    }

    class MyGpsListener implements GpsStatus.Listener {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX: //第一次定位
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS: //卫星状态改变
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    myGpsView.ResetGpsStatus(gpsStatus);
                    break;
                case GpsStatus.GPS_EVENT_STARTED: //定位启动
                    break;
                case GpsStatus.GPS_EVENT_STOPPED: //定位结束
                    break;
            }
        }
    }
}
