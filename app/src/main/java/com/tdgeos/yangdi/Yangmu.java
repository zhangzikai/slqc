package com.tdgeos.yangdi;

import com.tdgeos.lib.MyFuns;

public class Yangmu {
    public int ymh;
    public int lmlx;
    public int jclx;
    public String szmc;
    public int szdm;
    public float qqxj;
    public float bqxj;
    public int cfgllx;
    public int lc;
    public int kjdlxh;
    public float fwj;
    public float spj;
    public String bz;
    public int ckd;//0表示中心点，1表示西南角，2表示西北角，3表示东北角，4表示东南角
    public int szlx;//0表示正常，1表示丛生树[]，2表示分叉树()
    public int jczt;//0表示未检尺，1表示已检尺

    public float qxj;
    public float xj;

    public Yangmu() {
        ymh = -1;
        lmlx = -1;
        jclx = -1;
        szmc = "";
        szdm = -1;
        qqxj = -1;
        bqxj = -1;
        cfgllx = -1;
        lc = -1;
        kjdlxh = 0;
        fwj = -10;
        spj = -10;
        bz = "";
        ckd = YangdiMgr.YM_CKD_DEFAULT;
        szlx = 0;
        jczt = 0;

        qxj = -1;
        xj = -1;
    }

    public Yangmu(float x, float y) {
        ymh = -1;
        lmlx = -1;
        jclx = -1;
        szmc = "";
        szdm = -1;
        qqxj = -1;
        bqxj = -1;
        cfgllx = -1;
        lc = -1;
        kjdlxh = 0;
        bz = "";
        ckd = 1;
        szlx = 0;
        jczt = 0;
        qxj = -1;
        xj = -1;

        if (YangdiMgr.YM_ZB_TYPE == 0) {
            fwj = (float) (Math.atan((x) / (y)) * 180 / Math.PI);
            spj = (float) Math.sqrt((x) * (x) + (y) * (y));
            fwj = MyFuns.MyDecimal(fwj, 2);
            spj = MyFuns.MyDecimal(spj, 2);
        } else {
            fwj = x;
            spj = y;
        }
    }

    /*
    public Yangmu(float x, float y, int ckd)
    {
        ymh = -1;
        lmlx = -1;
        jclx = -1;
        szmc = "";
        szdm = -1;
        qqxj = -1;
        bqxj = -1;
        cfgllx = -1;
        lc = -1;
        kjdlxh = 0;
        bz = "";
        szlx = 0;
        jczt = 0;

        if(YangdiMgr.YM_ZB_TYPE == 0)
        {
            fwj = -10;
            spj = -10;
            this.ckd = ckd;
            if(ckd == 0)
            {
                x -= YangdiMgr.YD_SIZE / 2;
                y -= YangdiMgr.YD_SIZE / 2;
                if(x > 0 && y > 0)
                {
                    fwj = (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                    spj = (float)Math.sqrt((x)*(x)+(y)*(y));
                }
                if(x > 0 && y < 0)
                {
                    fwj = 180 + (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                    spj = (float)Math.sqrt((x)*(x)+(y)*(y));
                }
                if(x < 0 && y < 0)
                {
                    fwj = 180 + (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                    spj = (float)Math.sqrt((x)*(x)+(y)*(y));
                }
                if(x < 0 && y > 0)
                {
                    fwj = 360 + (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                    spj = (float)Math.sqrt((x)*(x)+(y)*(y));
                }
            }
            if(ckd == 2)
            {
                y = YangdiMgr.YD_SIZE - y;
                fwj = 180 - (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                spj = (float)Math.sqrt((x)*(x)+(y)*(y));
            }
            if(ckd == 1)
            {
                fwj = (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                spj = (float)Math.sqrt((x)*(x)+(y)*(y));
            }
            if(ckd == 3)
            {
                x = YangdiMgr.YD_SIZE - x;
                y = YangdiMgr.YD_SIZE - y;
                fwj = 180 + (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                spj = (float)Math.sqrt((x)*(x)+(y)*(y));
            }
            if(ckd == 4)
            {
                x = YangdiMgr.YD_SIZE - x;
                fwj = 360 - (float)(Math.atan((x)/(y)) * 180 / Math.PI);
                spj = (float)Math.sqrt((x)*(x)+(y)*(y));
            }
            fwj = MyFuns.MyDecimal(fwj, 2);
            spj = MyFuns.MyDecimal(spj, 2);
        }
        else
        {
            fwj = x;
            spj = y;
            this.ckd = ckd;
        }
    }
    */
    public float GetX() {
        if (YangdiMgr.YM_ZB_TYPE == 0) {
            float tmp = fwj;
            float x = -10;
            float y = -10;
            if (ckd == 0) {
                x = (float) Math.sin(fwj * Math.PI / 180) * spj;
                y = (float) Math.cos(fwj * Math.PI / 180) * spj;
                x += YangdiMgr.YD_SIZE / 2;
                y += YangdiMgr.YD_SIZE / 2;

            }
            if (ckd == 1) {
                x = (float) Math.sin(fwj * Math.PI / 180) * spj;
                y = (float) Math.cos(fwj * Math.PI / 180) * spj;

            }
            if (ckd == 2) {
                tmp = fwj - 90;
                y = (float) Math.sin(tmp * Math.PI / 180) * spj;
                x = (float) Math.cos(tmp * Math.PI / 180) * spj;
                y = YangdiMgr.YD_SIZE - y;
            }
            if (ckd == 3) {
                tmp = fwj - 180;
                x = (float) Math.sin(tmp * Math.PI / 180) * spj;
                y = (float) Math.cos(tmp * Math.PI / 180) * spj;
                x = YangdiMgr.YD_SIZE - x;
                y = YangdiMgr.YD_SIZE - y;
            }
            if (ckd == 4) {
                tmp = fwj - 270;
                y = (float) Math.sin(tmp * Math.PI / 180) * spj;
                x = (float) Math.cos(tmp * Math.PI / 180) * spj;
                x = YangdiMgr.YD_SIZE - x;
            }
            return x;
        } else {
            return fwj;
        }
    }

    public float GetY() {
        if (YangdiMgr.YM_ZB_TYPE == 0) {
            float tmp = fwj;
            float x = -10;
            float y = -10;
            if (ckd == 0) {
                x = (float) Math.sin(fwj * Math.PI / 180) * spj;
                y = (float) Math.cos(fwj * Math.PI / 180) * spj;
                x += YangdiMgr.YD_SIZE / 2;
                y += YangdiMgr.YD_SIZE / 2;

            }
            if (ckd == 1) {
                x = (float) Math.sin(fwj * Math.PI / 180) * spj;
                y = (float) Math.cos(fwj * Math.PI / 180) * spj;

            }
            if (ckd == 2) {
                tmp = fwj - 90;
                y = (float) Math.sin(tmp * Math.PI / 180) * spj;
                x = (float) Math.cos(tmp * Math.PI / 180) * spj;
                y = YangdiMgr.YD_SIZE - y;
            }
            if (ckd == 3) {
                tmp = fwj - 180;
                x = (float) Math.sin(tmp * Math.PI / 180) * spj;
                y = (float) Math.cos(tmp * Math.PI / 180) * spj;
                x = YangdiMgr.YD_SIZE - x;
                y = YangdiMgr.YD_SIZE - y;
            }
            if (ckd == 4) {
                tmp = fwj - 270;
                y = (float) Math.sin(tmp * Math.PI / 180) * spj;
                x = (float) Math.cos(tmp * Math.PI / 180) * spj;
                x = YangdiMgr.YD_SIZE - x;
            }
            return y;
        } else {
            return spj;
        }
    }

    public float GetErrorDis() {
        float x = GetX();
        float y = GetY();
        float dx = 0;
        float dy = 0;
        if (x < 0) {
            dx = -x;
        } else if (x > YangdiMgr.YD_SIZE) {
            dx = x - YangdiMgr.YD_SIZE;
        }
        if (y < 0) {
            dy = -y;
        } else if (y > YangdiMgr.YD_SIZE) {
            dy = y - YangdiMgr.YD_SIZE;
        }
        return dx > dy ? dx : dy;
    }

    public void Move(float dx, float dy) {
        if (YangdiMgr.YM_ZB_TYPE == 0) {
            float x = GetX();
            float y = GetY();
            x += dx;
            y += dy;
            if (x > YangdiMgr.MAX_YM_ZB) x = YangdiMgr.MAX_YM_ZB;
            if (x < YangdiMgr.MIN_YM_ZB) x = YangdiMgr.MIN_YM_ZB;
            if (y > YangdiMgr.MAX_YM_ZB) y = YangdiMgr.MAX_YM_ZB;
            if (y < YangdiMgr.MIN_YM_ZB) y = YangdiMgr.MIN_YM_ZB;

            if (ckd == 0) {
                x = x - YangdiMgr.YD_SIZE / 2;
                y = y - YangdiMgr.YD_SIZE / 2;
                if (x > 0 && y > 0) {
                    fwj = (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    spj = (float) Math.sqrt((x) * (x) + (y) * (y));
                }
                if (x > 0 && y < 0) {
                    fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    spj = (float) Math.sqrt((x) * (x) + (y) * (y));
                }
                if (x < 0 && y < 0) {
                    fwj = 180 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    spj = (float) Math.sqrt((x) * (x) + (y) * (y));
                }
                if (x < 0 && y > 0) {
                    fwj = 360 + (float) (Math.atan((x) / (y)) * 180 / Math.PI);
                    spj = (float) Math.sqrt((x) * (x) + (y) * (y));
                }
            }
            if (ckd == 1) {
                if (y == 0) fwj = 90;
                else fwj = (float) (Math.atan(x / y) * 180 / Math.PI);
                spj = (float) Math.sqrt(x * x + y * y);
            }
            if (ckd == 2) {
                y = YangdiMgr.YD_SIZE - y;
                fwj = 180 - (float) (Math.atan(x / y) * 180 / Math.PI);
                spj = (float) Math.sqrt(x * x + y * y);
            }
            if (ckd == 3) {
                x = YangdiMgr.YD_SIZE - x;
                y = YangdiMgr.YD_SIZE - y;
                fwj = 180 + (float) (Math.atan(x / y) * 180 / Math.PI);
                spj = (float) Math.sqrt(x * x + y * y);
            }
            if (ckd == 4) {
                x = YangdiMgr.YD_SIZE - x;
                if (y == 0) fwj = 270;
                else fwj = 360 - (float) (Math.atan(x / y) * 180 / Math.PI);
                spj = (float) Math.sqrt(x * x + y * y);
            }
        } else {
            fwj += dx;
            spj += dy;
            if (fwj > YangdiMgr.MAX_YM_ZB) fwj = YangdiMgr.MAX_YM_ZB;
            if (fwj < YangdiMgr.MIN_YM_ZB) fwj = YangdiMgr.MIN_YM_ZB;
            if (spj > YangdiMgr.MAX_YM_ZB) spj = YangdiMgr.MAX_YM_ZB;
            if (spj < YangdiMgr.MIN_YM_ZB) spj = YangdiMgr.MIN_YM_ZB;
        }

        fwj = MyFuns.MyDecimal(fwj, 1);
        spj = MyFuns.MyDecimal(spj, 1);
    }

    public boolean Equals(Yangmu ym) {
        if (ym.ymh != ymh) return false;
        if (ym.lmlx != lmlx) return false;
        if (ym.jclx != jclx) return false;
        if (!ym.szmc.equals(szmc)) return false;
        if (ym.szdm != szdm) return false;
        if (ym.bqxj != bqxj) return false;
        if (ym.cfgllx != cfgllx) return false;
        if (ym.lc != lc) return false;
        if (ym.kjdlxh != kjdlxh) return false;
        if (ym.fwj != fwj) return false;
        if (ym.spj != spj) return false;
        if (!ym.bz.equals(bz)) return false;
        if (ym.ckd != ckd) return false;
        if (ym.szlx != szlx) return false;
        //if(ym.jczt != jczt) return false;

        return true;
    }
}