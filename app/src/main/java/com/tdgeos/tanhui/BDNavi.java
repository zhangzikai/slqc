package com.tdgeos.tanhui;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.tts.BNTTSPlayer;
import com.baidu.navisdk.comapi.tts.BNavigatorTTSPlayer;
import com.baidu.navisdk.comapi.tts.IBNTTSPlayerListener;
import com.baidu.navisdk.model.datastruct.LocData;
import com.baidu.navisdk.model.datastruct.SensorData;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.routeguide.IBNavigatorListener;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.ui.widget.RoutePlanObserver.IJumpToDownloadListener;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class BDNavi extends Activity {
    private IBNavigatorListener mBNavigatorListener = new IBNavigatorListener() {
        @Override
        public void onYawingRequestSuccess() {
        }

        @Override
        public void onYawingRequestStart() {
        }

        @Override
        public void onPageJump(int jumpTiming, Object arg) {
            if (IBNavigatorListener.PAGE_JUMP_WHEN_GUIDE_END == jumpTiming) {
                finish();
            } else if (IBNavigatorListener.PAGE_JUMP_WHEN_ROUTE_PLAN_FAIL == jumpTiming) {
                finish();
            }
        }

        @Override
        public void notifyGPSStatusData(int arg0) {
        }

        @Override
        public void notifyLoacteData(LocData arg0) {
        }

        @Override
        public void notifyNmeaData(String arg0) {
        }

        @Override
        public void notifySensorData(SensorData arg0) {
        }

        @Override
        public void notifyStartNav() {
            BaiduNaviManager.getInstance().dismissWaitProgressDialog();
        }

        @Override
        public void notifyViewModeChanged(int arg0) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 14) {
            BaiduNaviManager.getInstance().destroyNMapView();
        }
        MapGLSurfaceView nMapView = BaiduNaviManager.getInstance().createNMapView(this);
        View navigatorView = BNavigator.getInstance().init(this, getIntent().getExtras(), nMapView);

        setContentView(navigatorView);

        BNavigator.getInstance().setListener(mBNavigatorListener);
        BNavigator.getInstance().startNav();

        BNTTSPlayer.initPlayer();

        BNavigatorTTSPlayer.setTTSPlayerListener(new IBNTTSPlayerListener() {
            @Override
            public int playTTSText(String arg0, int arg1) {
                return BNTTSPlayer.playTTSText(arg0, arg1);
            }

            @Override
            public void phoneHangUp() {
            }

            @Override
            public void phoneCalling() {
            }

            @Override
            public int getTTSState() {
                return BNTTSPlayer.getTTSState();
            }
        });

        BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, new IJumpToDownloadListener() {
            @Override
            public void onJumpToDownloadOfflineData() {
            }
        }));

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
        BNavigator.getInstance().resume();
        super.onResume();
        BNMapController.getInstance().onResume();
    }

    @Override
    public void onPause() {
        BNavigator.getInstance().pause();
        super.onPause();
        BNMapController.getInstance().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        BNavigator.destory();
        BNRoutePlaner.getInstance().setObserver(null);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        BNavigator.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {
        BNavigator.getInstance().onBackPressed();
    }
}
