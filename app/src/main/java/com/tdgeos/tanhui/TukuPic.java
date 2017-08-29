package com.tdgeos.tanhui;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.lib.MyFile;
import com.tdgeos.lib.MyFuns;
import com.tdgeos.yangdi.Resmgr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class TukuPic extends Activity {

    private String szmc;
    private String enmc;
    private int szdm;
    private final String TUKU_DIR = MyConfig.GetWorkdir() + "/tuku";

    private ViewPager viewPager;
    private MyAdapter adapterPic;

    private TextView tvMc;
    private TextView tvDm;
    private TextView tvKe;
    private TextView tvShu;
    private TextView tvSbyd;
    private TextView tvFb;

    private List<Bitmap> lstBitmap = new ArrayList<Bitmap>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuku_pic);

        Intent intent = this.getIntent();
        szmc = intent.getStringExtra("szmc");
        szdm = intent.getIntExtra("szdm", 0);
        enmc = intent.getStringExtra("enmc");

        viewPager = (ViewPager) findViewById(R.id.vp_pic);
        adapterPic = new MyAdapter(getViews());
        viewPager.setAdapter(adapterPic);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(0);

        tvMc = (TextView) findViewById(R.id.tv_mc);
        tvDm = (TextView) findViewById(R.id.tv_dm);
        tvShu = (TextView) findViewById(R.id.tv_shu);
        tvKe = (TextView) findViewById(R.id.tv_ke);
        tvSbyd = (TextView) findViewById(R.id.tv_sbyd);
        tvFb = (TextView) findViewById(R.id.tv_fb);
        //tvXttz.setMovementMethod(ScrollingMovementMethod.getInstance());

        String sql = "select * from tuku where mc_cn = '" + szmc + "' or dm = '" + szdm + "' or mc_en = '" + enmc + "'";
        String[][] sss = Resmgr.SelectData(sql);
        if (sss != null) {
            tvMc.setText(sss[0][0]);
            tvDm.setText(sss[0][1]);
            tvKe.setText(sss[0][3]);
            tvShu.setText(sss[0][4]);
            tvSbyd.setText(sss[0][5]);
            tvFb.setText(sss[0][6]);
        }
    }

    @Override
    public void onDestroy() {
        for (int i = 0; i < lstBitmap.size(); i++) {
            if (lstBitmap.get(i) != null) {
                lstBitmap.get(i).recycle();
            }
        }
        super.onDestroy();
    }


    private List<String> getImageFiles(String dir) {
        List<String> lst = new ArrayList<String>();

        String s1 = dir + "/" + szdm + "_1.jpg";
        if (MyFile.Exists(s1)) lst.add(s1);
        String s2 = dir + "/" + szdm + "_2.jpg";
        if (MyFile.Exists(s2)) lst.add(s2);
        String s3 = dir + "/" + szdm + "_3.jpg";
        if (MyFile.Exists(s3)) lst.add(s3);
        String s4 = dir + "/" + szdm + "_4.jpg";
        if (MyFile.Exists(s4)) lst.add(s4);
        String s5 = dir + "/" + szdm + "_5.jpg";
        if (MyFile.Exists(s5)) lst.add(s5);

        String s6 = dir + "/" + szmc + "_1.jpg";
        if (MyFile.Exists(s6)) lst.add(s6);
        String s7 = dir + "/" + szmc + "_2.jpg";
        if (MyFile.Exists(s7)) lst.add(s7);
        String s8 = dir + "/" + szmc + "_3.jpg";
        if (MyFile.Exists(s8)) lst.add(s8);
        String s9 = dir + "/" + szmc + "_4.jpg";
        if (MyFile.Exists(s9)) lst.add(s9);
        String s10 = dir + "/" + szmc + "_5.jpg";
        if (MyFile.Exists(s10)) lst.add(s10);

        String s11 = dir + "/" + enmc + "_1.jpg";
        if (MyFile.Exists(s11)) lst.add(s11);
        String s12 = dir + "/" + enmc + "_2.jpg";
        if (MyFile.Exists(s12)) lst.add(s12);
        String s13 = dir + "/" + enmc + "_3.jpg";
        if (MyFile.Exists(s13)) lst.add(s13);
        String s14 = dir + "/" + enmc + "_4.jpg";
        if (MyFile.Exists(s14)) lst.add(s14);
        String s15 = dir + "/" + enmc + "_5.jpg";
        if (MyFile.Exists(s15)) lst.add(s15);
        return lst;
    }

    private List<View> getViews() {
        List<View> lst = new ArrayList<View>();
        List<String> pics = getImageFiles(TUKU_DIR);
        for (int i = 0; i < pics.size(); i++) {
            ImageView imageView = new ImageView(this);
            Bitmap bm = MyFuns.GetBitmap(pics.get(i), 1024);
            if (bm != null) {
                imageView.setImageBitmap(bm);
                //imageView.setScaleType(ScaleType.CENTER_INSIDE);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                lst.add(imageView);
                lstBitmap.add(bm);
            }
        }
        return lst;
    }

    class MyAdapter extends PagerAdapter {
        private List<View> lstView;

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(lstView.get(position), 0);
            return lstView.get(position);
        }

        @Override
        public int getCount() {
            return lstView.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        public MyAdapter(List<View> lst) {
            lstView = lst;
        }
    }

    class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

        }
    }
}
