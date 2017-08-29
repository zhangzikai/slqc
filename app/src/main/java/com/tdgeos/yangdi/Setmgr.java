package com.tdgeos.yangdi;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.lib.MyFile;
import com.tdgeos.tanhui.MyConfig;
import com.tdgeos.yangdi.WorkerInfo;
import com.tdgeos.yangdi.YDInfo;
import com.tdgeos.yangdi.YangdiMgr;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Setmgr {
    //将指定县对应的所有样地添加到任务列表
    public static void AddTaskByXian(int xiancode) {
        String sql = null;
        if (xiancode > 10) {
            sql = "select ydh, ydlb, x, y, gps_x, gps_y, xian, dm, hb, px, pw, pd, dl, qy, tdqs, linzh, zblx, yssz, f1, f3 from slqc_ydyz where xian = '" + xiancode + "' and (f2 not in ('1','2','3','4','5','6','7','8','9') or f2 is null)";
        } else {
            sql = "select ydh, ydlb, x, y, gps_x, gps_y, xian, dm, hb, px, pw, pd, dl, qy, tdqs, linzh, zblx, yssz, f1, f3 from slqc_ydyz where f2 = '" + xiancode + "'";
        }
        String[][] sss = Qianqimgr.SelectData(sql);
        if (sss == null) return;

        for (int i = 0; i < sss.length; i++) {
            YDInfo yd = new YDInfo();
            try {
                yd.ydh = Integer.parseInt(sss[i][0]);
            } catch (Exception e) {
            }
            try {
                yd.ydlb = Integer.parseInt(sss[i][1]);
            } catch (Exception e) {
            }
            try {
                yd.hzb = Integer.parseInt(sss[i][2]);
            } catch (Exception e) {
            }
            try {
                yd.zzb = Integer.parseInt(sss[i][3]);
            } catch (Exception e) {
            }
            try {
                yd.gpshzb = Integer.parseInt(sss[i][4]);
            } catch (Exception e) {
            }
            try {
                yd.gpszzb = Integer.parseInt(sss[i][5]);
            } catch (Exception e) {
            }
            try {
                yd.xian = Integer.parseInt(sss[i][6]);
            } catch (Exception e) {
            }
            try {
                yd.dm = Integer.parseInt(sss[i][7]);
            } catch (Exception e) {
            }
            try {
                yd.hb = Integer.parseInt(sss[i][8]);
            } catch (Exception e) {
            }
            try {
                yd.px = Integer.parseInt(sss[i][9]);
            } catch (Exception e) {
            }
            try {
                yd.pw = Integer.parseInt(sss[i][10]);
            } catch (Exception e) {
            }
            try {
                yd.pd = Integer.parseInt(sss[i][11]);
            } catch (Exception e) {
            }
            yd.dczt = 0;

            yd.dxttfh = sss[i][18];
            try {
                yd.sfyf = Integer.parseInt(sss[i][19]);
            } catch (Exception e) {
            }

            yd.xianju = Qianqimgr.GetXianJuNameByYdh(yd.ydh);
            int code = 0;
            String str = null;
            try {
                code = Integer.parseInt(sss[i][12]);
            } catch (Exception e) {
            }
            code = YangdiMgr.DlCodeOldToNew(code);
            str = Resmgr.GetValueByCode("dl", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.dl_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][13]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("qy", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.qy_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][14]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("tdqs", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.tdqs_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][15]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("linzh", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.linzh_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][16]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("zblx", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.zblx_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][17]);
                code = Resmgr.GetSzNewCodeByOldCode(code);
                String name = Resmgr.GetSzName(code);
                if (code > 0 && !name.equals("")) {
                    yd.yssz_q = code + " " + name;
                } else {
                    yd.yssz_q = "无";
                }
            } catch (Exception e) {
            }

            addTask(yd);
        }
        return;
    }

    //将指定样地添加到任务列表
    public static void AddTaskByYdh(int ydh) {
        String sql = "select ydh, ydlb, x, y, gps_x, gps_y, xian, dm, hb, px, pw, pd, dl, qy, tdqs, linzh, zblx, yssz, f1, f3 from slqc_ydyz where ydh = '" + ydh + "'";
        String[][] sss = Qianqimgr.SelectData(sql);
        if (sss == null) return;

        YDInfo yd = new YDInfo();
        try {
            yd.ydh = Integer.parseInt(sss[0][0]);
        } catch (Exception e) {
        }
        try {
            yd.ydlb = Integer.parseInt(sss[0][1]);
        } catch (Exception e) {
        }
        try {
            yd.hzb = Integer.parseInt(sss[0][2]);
        } catch (Exception e) {
        }
        try {
            yd.zzb = Integer.parseInt(sss[0][3]);
        } catch (Exception e) {
        }
        try {
            yd.gpshzb = Integer.parseInt(sss[0][4]);
        } catch (Exception e) {
        }
        try {
            yd.gpszzb = Integer.parseInt(sss[0][5]);
        } catch (Exception e) {
        }
        try {
            yd.xian = Integer.parseInt(sss[0][6]);
        } catch (Exception e) {
        }
        try {
            yd.dm = Integer.parseInt(sss[0][7]);
        } catch (Exception e) {
        }
        try {
            yd.hb = Integer.parseInt(sss[0][8]);
        } catch (Exception e) {
        }
        try {
            yd.px = Integer.parseInt(sss[0][9]);
        } catch (Exception e) {
        }
        try {
            yd.pw = Integer.parseInt(sss[0][10]);
        } catch (Exception e) {
        }
        try {
            yd.pd = Integer.parseInt(sss[0][11]);
        } catch (Exception e) {
        }
        yd.dczt = 0;

        yd.dxttfh = sss[0][18];
        try {
            yd.sfyf = Integer.parseInt(sss[0][19]);
        } catch (Exception e) {
        }

        yd.xianju = Qianqimgr.GetXianJuNameByYdh(yd.ydh);
        int code = 0;
        String str = null;
        try {
            code = Integer.parseInt(sss[0][12]);
        } catch (Exception e) {
        }
        code = YangdiMgr.DlCodeOldToNew(code);
        str = Resmgr.GetValueByCode("dl", code);
        if (str.equals("") || code <= 0) str = "无";
        yd.dl_q = str;
        code = 0;
        try {
            code = Integer.parseInt(sss[0][13]);
        } catch (Exception e) {
        }
        str = Resmgr.GetValueByCode("qy", code);
        if (str.equals("") || code <= 0) str = "无";
        yd.qy_q = str;
        code = 0;
        try {
            code = Integer.parseInt(sss[0][14]);
        } catch (Exception e) {
        }
        str = Resmgr.GetValueByCode("tdqs", code);
        if (str.equals("") || code <= 0) str = "无";
        yd.tdqs_q = str;
        code = 0;
        try {
            code = Integer.parseInt(sss[0][15]);
        } catch (Exception e) {
        }
        str = Resmgr.GetValueByCode("linzh", code);
        if (str.equals("") || code <= 0) str = "无";
        yd.linzh_q = str;
        code = 0;
        try {
            code = Integer.parseInt(sss[0][16]);
        } catch (Exception e) {
        }
        str = Resmgr.GetValueByCode("zblx", code);
        if (str.equals("") || code <= 0) str = "无";
        yd.zblx_q = str;
        code = 0;
        try {
            code = Integer.parseInt(sss[0][17]);
            code = Resmgr.GetSzNewCodeByOldCode(code);
            String name = Resmgr.GetSzName(code);
            if (code > 0 && !name.equals("")) {
                yd.yssz_q = code + " " + name;
            } else {
                yd.yssz_q = "无";
            }
        } catch (Exception e) {
        }
        addTask(yd);
    }

    public static void AddAllTask() {
        String sql = "select ydh, ydlb, x, y, gps_x, gps_y, xian, dm, hb, px, pw, pd, dl, qy, tdqs, linzh, zblx, yssz, f1, f3 from slqc_ydyz";
        String[][] sss = Qianqimgr.SelectData(sql);
        if (sss == null) return;

        for (int i = 0; i < sss.length; i++) {
            YDInfo yd = new YDInfo();
            try {
                yd.ydh = Integer.parseInt(sss[i][0]);
            } catch (Exception e) {
            }
            try {
                yd.ydlb = Integer.parseInt(sss[i][1]);
            } catch (Exception e) {
            }
            try {
                yd.hzb = Integer.parseInt(sss[i][2]);
            } catch (Exception e) {
            }
            try {
                yd.zzb = Integer.parseInt(sss[i][3]);
            } catch (Exception e) {
            }
            try {
                yd.gpshzb = Integer.parseInt(sss[i][4]);
            } catch (Exception e) {
            }
            try {
                yd.gpszzb = Integer.parseInt(sss[i][5]);
            } catch (Exception e) {
            }
            try {
                yd.xian = Integer.parseInt(sss[i][6]);
            } catch (Exception e) {
            }
            try {
                yd.dm = Integer.parseInt(sss[i][7]);
            } catch (Exception e) {
            }
            try {
                yd.hb = Integer.parseInt(sss[i][8]);
            } catch (Exception e) {
            }
            try {
                yd.px = Integer.parseInt(sss[i][9]);
            } catch (Exception e) {
            }
            try {
                yd.pw = Integer.parseInt(sss[i][10]);
            } catch (Exception e) {
            }
            try {
                yd.pd = Integer.parseInt(sss[i][11]);
            } catch (Exception e) {
            }
            yd.dczt = 0;

            yd.dxttfh = sss[i][18];
            try {
                yd.sfyf = Integer.parseInt(sss[i][19]);
            } catch (Exception e) {
            }

            yd.xianju = Qianqimgr.GetXianJuNameByYdh(yd.ydh);
            int code = 0;
            String str = null;
            try {
                code = Integer.parseInt(sss[i][12]);
            } catch (Exception e) {
            }
            code = YangdiMgr.DlCodeOldToNew(code);
            str = Resmgr.GetValueByCode("dl", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.dl_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][13]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("qy", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.qy_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][14]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("tdqs", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.tdqs_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][15]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("linzh", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.linzh_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][16]);
            } catch (Exception e) {
            }
            str = Resmgr.GetValueByCode("zblx", code);
            if (str.equals("") || code <= 0) str = "无";
            yd.zblx_q = str;
            code = 0;
            try {
                code = Integer.parseInt(sss[i][17]);
                code = Resmgr.GetSzNewCodeByOldCode(code);
                String name = Resmgr.GetSzName(code);
                if (code > 0 && !name.equals("")) {
                    yd.yssz_q = code + " " + name;
                } else {
                    yd.yssz_q = "无";
                }
            } catch (Exception e) {
            }

            addTask(yd);
        }
        return;
    }

    private static void addTask(YDInfo yd) {
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

        String sql1 = "select * from task where ydh='" + yd.ydh + "'";
        Cursor cursor = db.rawQuery(sql1, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return;
        } else {
            cursor.close();
        }

        String sql2 = "insert into task values('"
                + yd.ydh + "', '"
                + yd.ydlb + "', '"
                + yd.dxttfh + "', '"
                + yd.hzb + "', '"
                + yd.zzb + "', '"
                + yd.gpshzb + "', '"
                + yd.gpszzb + "', '"
                + yd.xian + "', '"
                + yd.dm + "', '"
                + yd.hb + "', '"
                + yd.px + "', '"
                + yd.pw + "', '"
                + yd.pd + "', '"
                + yd.sfyf + "', '"
                + yd.dczt + "', '"
                + yd.xianju + "', '"
                + yd.dl_q + "', '"
                + yd.qy_q + "', '"
                + yd.tdqs_q + "', '"
                + yd.linzh_q + "', '"
                + yd.zblx_q + "', '"
                + yd.yssz_q + "', '无', '无', '无', '无', '无', '无')";
        db.execSQL(sql2);
        db.close();

        String dataFile = YangdiMgr.getDbFile(yd.ydh);
        if (MyFile.Exists(dataFile)) {
            String sql = "select dl,qy,tdqs,linzh,zblx,yssz from ydyz where ydh = '" + yd.ydh + "'";
            String[][] sss = YangdiMgr.SelectData(yd.ydh, sql);
            if (sss != null) {
                int i_dl = -1;
                int i_qy = -1;
                int i_tdqs = -1;
                int i_linzh = -1;
                int i_zblx = -1;
                int i_yssz = -1;
                String dl = "";
                String qy = "";
                String tdqs = "";
                String linzh = "";
                String zblx = "";
                String yssz = "";
                try {
                    i_dl = Integer.parseInt(sss[0][0]);
                    dl = Resmgr.GetValueByCode("dl", i_dl);
                } catch (Exception e) {
                }
                try {
                    i_qy = Integer.parseInt(sss[0][1]);
                    qy = Resmgr.GetValueByCode("qy", i_qy);
                } catch (Exception e) {
                }
                try {
                    i_tdqs = Integer.parseInt(sss[0][2]);
                    tdqs = Resmgr.GetValueByCode("tdqs", i_tdqs);
                } catch (Exception e) {
                }
                try {
                    i_linzh = Integer.parseInt(sss[0][3]);
                    linzh = Resmgr.GetValueByCode("linzh", i_linzh);
                } catch (Exception e) {
                }
                try {
                    i_zblx = Integer.parseInt(sss[0][4]);
                    zblx = Resmgr.GetValueByCode("zblx", i_zblx);
                } catch (Exception e) {
                }
                try {
                    i_yssz = Integer.parseInt(sss[0][5]);
                    yssz = Resmgr.GetSzName(i_yssz);
                } catch (Exception e) {
                }
                String sdl = i_dl < 0 ? "无" : dl;
                String sqy = i_qy < 0 ? "无" : qy;
                String stdqs = i_tdqs < 0 ? "无" : tdqs;
                String slinzh = i_linzh < 0 ? "无" : linzh;
                String szblx = i_zblx < 0 ? "无" : zblx;
                String syssz = i_yssz < 0 ? "无" : i_yssz + " " + yssz;
                sql = "update task set "
                        + "dl_b = '" + sdl + "', "
                        + "qy_b = '" + sqy + "', "
                        + "tdqs_b = '" + stdqs + "', "
                        + "linzh_b = '" + slinzh + "', "
                        + "zblx_b = '" + szblx + "', "
                        + "yssz_b = '" + syssz + "' "
                        + "where ydh = '" + yd.ydh + "'";
                Setmgr.ExecSQL(sql);
            }

            sql = "select yangdi from dczt where ydh = '" + yd.ydh + "'";
            int a = YangdiMgr.QueryInt(yd.ydh, sql);
            sql = "update task set "
                    + "dczt = '" + a + "' "
                    + "where ydh = '" + yd.ydh + "'";
            Setmgr.ExecSQL(sql);
        }
    }

    //获取任务列表中的样地数量
    public static int GetTaskCount() {
        int n = 0;
        String sql = "select * from task";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            n = cursor.getCount();
            cursor.close();
        }
        db.close();
        return n;
    }

    public static List<YDInfo> GetTaskList(int pageIndex, int pageSize) {
        List<YDInfo> lst = new ArrayList<YDInfo>();
        String sql = "select * from task order by xian,ydh limit " + (pageSize * pageIndex) + ", " + pageSize;
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            for (int i = 0; i < sss.length; i++) {
                YDInfo yd = new YDInfo();
                try {
                    yd.ydh = Integer.parseInt(sss[i][0]);
                } catch (Exception e) {
                }
                try {
                    yd.ydlb = Integer.parseInt(sss[i][1]);
                } catch (Exception e) {
                }
                yd.dxttfh = sss[i][2];
                try {
                    yd.hzb = Integer.parseInt(sss[i][3]);
                } catch (Exception e) {
                }
                try {
                    yd.zzb = Integer.parseInt(sss[i][4]);
                } catch (Exception e) {
                }
                try {
                    yd.gpshzb = Integer.parseInt(sss[i][5]);
                } catch (Exception e) {
                }
                try {
                    yd.gpszzb = Integer.parseInt(sss[i][6]);
                } catch (Exception e) {
                }
                try {
                    yd.xian = Integer.parseInt(sss[i][7]);
                } catch (Exception e) {
                }
                try {
                    yd.dm = Integer.parseInt(sss[i][8]);
                } catch (Exception e) {
                }
                try {
                    yd.hb = Integer.parseInt(sss[i][9]);
                } catch (Exception e) {
                }
                try {
                    yd.px = Integer.parseInt(sss[i][10]);
                } catch (Exception e) {
                }
                try {
                    yd.pw = Integer.parseInt(sss[i][11]);
                } catch (Exception e) {
                }
                try {
                    yd.pd = Integer.parseInt(sss[i][12]);
                } catch (Exception e) {
                }
                try {
                    yd.sfyf = Integer.parseInt(sss[i][13]);
                } catch (Exception e) {
                }
                try {
                    yd.dczt = Integer.parseInt(sss[i][14]);
                } catch (Exception e) {
                }
                yd.xianju = sss[i][15];
                yd.dl_q = sss[i][16];
                yd.qy_q = sss[i][17];
                yd.tdqs_q = sss[i][18];
                yd.linzh_q = sss[i][19];
                yd.zblx_q = sss[i][20];
                yd.yssz_q = sss[i][21];
                yd.dl_b = sss[i][22];
                yd.qy_b = sss[i][23];
                yd.tdqs_b = sss[i][24];
                yd.linzh_b = sss[i][25];
                yd.zblx_b = sss[i][26];
                yd.yssz_b = sss[i][27];
                lst.add(yd);
            }
        }

        return lst;
    }

    public static List<YDInfo> GetTaskList() {
        List<YDInfo> lst = new ArrayList<YDInfo>();
        String sql = "select * from task";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            for (int i = 0; i < sss.length; i++) {
                YDInfo yd = new YDInfo();
                try {
                    yd.ydh = Integer.parseInt(sss[i][0]);
                } catch (Exception e) {
                }
                try {
                    yd.ydlb = Integer.parseInt(sss[i][1]);
                } catch (Exception e) {
                }
                yd.dxttfh = sss[i][2];
                try {
                    yd.hzb = Integer.parseInt(sss[i][3]);
                } catch (Exception e) {
                }
                try {
                    yd.zzb = Integer.parseInt(sss[i][4]);
                } catch (Exception e) {
                }
                try {
                    yd.gpshzb = Integer.parseInt(sss[i][5]);
                } catch (Exception e) {
                }
                try {
                    yd.gpszzb = Integer.parseInt(sss[i][6]);
                } catch (Exception e) {
                }
                try {
                    yd.xian = Integer.parseInt(sss[i][7]);
                } catch (Exception e) {
                }
                try {
                    yd.dm = Integer.parseInt(sss[i][8]);
                } catch (Exception e) {
                }
                try {
                    yd.hb = Integer.parseInt(sss[i][9]);
                } catch (Exception e) {
                }
                try {
                    yd.px = Integer.parseInt(sss[i][10]);
                } catch (Exception e) {
                }
                try {
                    yd.pw = Integer.parseInt(sss[i][11]);
                } catch (Exception e) {
                }
                try {
                    yd.pd = Integer.parseInt(sss[i][12]);
                } catch (Exception e) {
                }
                try {
                    yd.sfyf = Integer.parseInt(sss[i][13]);
                } catch (Exception e) {
                }
                try {
                    yd.dczt = Integer.parseInt(sss[i][14]);
                } catch (Exception e) {
                }
                yd.xianju = sss[i][15];
                yd.dl_q = sss[i][16];
                yd.qy_q = sss[i][17];
                yd.tdqs_q = sss[i][18];
                yd.linzh_q = sss[i][19];
                yd.zblx_q = sss[i][20];
                yd.yssz_q = sss[i][21];
                yd.dl_b = sss[i][22];
                yd.qy_b = sss[i][23];
                yd.tdqs_b = sss[i][24];
                yd.linzh_b = sss[i][25];
                yd.zblx_b = sss[i][26];
                yd.yssz_b = sss[i][27];
                lst.add(yd);
            }
        }

        return lst;
    }

    public static List<String> GetYdhList() {
        List<String> lst = new ArrayList<String>();
        String sql = "select ydh from task";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                lst.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static YDInfo GetTask(int ydh) {
        String sql = "select * from task where ydh = '" + ydh + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            YDInfo yd = new YDInfo();
            try {
                yd.ydh = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            try {
                yd.ydlb = Integer.parseInt(sss[0][1]);
            } catch (Exception e) {
            }
            yd.dxttfh = sss[0][2];
            try {
                yd.hzb = Integer.parseInt(sss[0][3]);
            } catch (Exception e) {
            }
            try {
                yd.zzb = Integer.parseInt(sss[0][4]);
            } catch (Exception e) {
            }
            try {
                yd.gpshzb = Integer.parseInt(sss[0][5]);
            } catch (Exception e) {
            }
            try {
                yd.gpszzb = Integer.parseInt(sss[0][6]);
            } catch (Exception e) {
            }
            try {
                yd.xian = Integer.parseInt(sss[0][7]);
            } catch (Exception e) {
            }
            try {
                yd.dm = Integer.parseInt(sss[0][8]);
            } catch (Exception e) {
            }
            try {
                yd.hb = Integer.parseInt(sss[0][9]);
            } catch (Exception e) {
            }
            try {
                yd.px = Integer.parseInt(sss[0][10]);
            } catch (Exception e) {
            }
            try {
                yd.pw = Integer.parseInt(sss[0][11]);
            } catch (Exception e) {
            }
            try {
                yd.pd = Integer.parseInt(sss[0][12]);
            } catch (Exception e) {
            }
            try {
                yd.sfyf = Integer.parseInt(sss[0][13]);
            } catch (Exception e) {
            }
            try {
                yd.dczt = Integer.parseInt(sss[0][14]);
            } catch (Exception e) {
            }
            yd.xianju = sss[0][15];
            yd.dl_q = sss[0][16];
            yd.qy_q = sss[0][17];
            yd.tdqs_q = sss[0][18];
            yd.linzh_q = sss[0][19];
            yd.zblx_q = sss[0][20];
            yd.yssz_q = sss[0][21];
            yd.dl_b = sss[0][22];
            yd.qy_b = sss[0][23];
            yd.tdqs_b = sss[0][24];
            yd.linzh_b = sss[0][25];
            yd.zblx_b = sss[0][26];
            yd.yssz_b = sss[0][27];
            return yd;
        }

        return null;
    }

    public static void RemoveTask(int ydh) {
        String sql = "delete from task where ydh = '" + ydh + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static void ClearTask() {
        String sql = "delete from task where 1=1";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static void ResetTaskStatus(int ydh, int iStatus) {
        String sql = "update task set dczt = '" + iStatus + "' where ydh = '" + ydh + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static void AddWorker(WorkerInfo worker) {
        String sql1 = "select * from worker where name='" + worker.name + "' and phone = '" + worker.phone + "'";
        String sql2 = "insert into worker(name, type, phone, company, address, notes) values('" + worker.name + "', '" + worker.type + "', '" + worker.phone + "', '" + worker.company + "', '" + worker.address + "', '" + worker.notes + "')";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = db.rawQuery(sql1, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return;
        }

        db.execSQL(sql2);
        cursor.close();
        db.close();
    }

    public static void UpdateWorker(WorkerInfo worker) {
        String sql = "update worker set name = '" + worker.name + "', type = '" + worker.type + "', phone = '" + worker.phone + "', company = '" + worker.company + "', address = '" + worker.address + "', notes = '" + worker.notes + "' where xh = '" + worker.id + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static WorkerInfo GetWorker(int id) {
        String sql = "select * from worker where xh = '" + id + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            WorkerInfo worker = new WorkerInfo();
            try {
                worker.id = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            worker.name = sss[0][1];
            try {
                worker.type = Integer.parseInt(sss[0][2]);
            } catch (Exception e) {
            }
            worker.phone = sss[0][3];
            worker.company = sss[0][4];
            worker.address = sss[0][5];
            worker.notes = sss[0][6];
            return worker;
        }

        return null;
    }

    public static WorkerInfo GetWorker(String name, int type) {
        String sql = "select * from worker where name = '" + name + "' and type = '" + type + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            WorkerInfo worker = new WorkerInfo();
            try {
                worker.id = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            worker.name = sss[0][1];
            try {
                worker.type = Integer.parseInt(sss[0][2]);
            } catch (Exception e) {
            }
            worker.phone = sss[0][3];
            worker.company = sss[0][4];
            worker.address = sss[0][5];
            worker.notes = sss[0][6];
            return worker;
        }

        return null;
    }

    public static List<WorkerInfo> GetWorkerList(int type) {
        List<WorkerInfo> lst = new ArrayList<WorkerInfo>();
        String sql = "select * from worker";
        if (type >= 0) sql += " where type = '" + type + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        if (sss != null) {
            for (int i = 0; i < sss.length; i++) {
                WorkerInfo worker = new WorkerInfo();
                try {
                    worker.id = Integer.parseInt(sss[i][0]);
                } catch (Exception e) {
                }
                worker.name = sss[i][1];
                try {
                    worker.type = Integer.parseInt(sss[i][2]);
                } catch (Exception e) {
                }
                worker.phone = sss[i][3];
                worker.company = sss[i][4];
                worker.address = sss[i][5];
                worker.notes = sss[i][6];
                lst.add(worker);
            }
        }

        return lst;
    }

    public static void RemoveWorker(int id) {
        String sql = "delete from worker where xh = '" + id + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }


    public static List<String> GetCyszList() {
        List<String> lst = new ArrayList<String>();
        String sql = "select name from cysz order by sycs desc";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return lst;
            }

            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                String str = cursor.getString(0);
                lst.add(str);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static void AddCysz(int dm, String mc) {
        String sql = "select name from cysz where name = '" + mc + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        int n = 0;
        if (cursor != null) {
            n = cursor.getCount();
            cursor.close();
        }
        if (n == 0) {
            sql = "insert into cysz values('" + dm + "', '" + mc + "', '0')";
            db.execSQL(sql);
        } else {
            sql = "update cysz set sycs = sycs+1 where name = '" + mc + "' and code = '" + dm + "'";
            db.execSQL(sql);
        }
        db.close();
    }

    public static void DelCysz(int dm) {
        String sql = "delete from cysz where code = '" + dm + "'";
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static void ExecSQL(String sql) {
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL(sql);
        db.close();
    }

    public static String[][] SelectData(String sql) {
        String dbFile = GetSetFile();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        String[][] sss = null;
        if (cursor != null) {
            int n = cursor.getCount();
            int m = cursor.getColumnCount();
            if (n == 0 || m == 0) {
                cursor.close();
                db.close();
                return null;
            }
            sss = new String[n][m];
            cursor.moveToFirst();
            for (int i = 0; i < n; i++) {
                sss[i] = new String[m];
                for (int j = 0; j < m; j++) {
                    sss[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return sss;
    }

    public static String GetSetFile() {
        return MyConfig.GetAppdir() + "/set.dat";
    }

}
