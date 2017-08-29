package com.tdgeos.yangdi;

import java.util.ArrayList;
import java.util.List;

import com.tdgeos.lib.MyCoord;
import com.tdgeos.lib.MyFile;
import com.tdgeos.tanhui.MyConfig;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Qianqimgr {
    //获取省代码对应的市名称列表
    public static List<String> GetShiList(int sheng_code) {
        if (!MyFile.Exists(GetDbFile())) {
            return new ArrayList<String>();
        }

        String sql = "select name from shi where parent='" + sheng_code + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            List<String> lst = new ArrayList<String>();
            for (int i = 0; i < sss.length; i++) {
                if (sss[i][0] != null) {
                    lst.add(sss[i][0].trim());
                }
            }
            return lst;
        }
        return new ArrayList<String>();
    }

    //根据市名称获取市代码
    public static int GetShiCode(String name) {
        String sql = "select code from shi where name='" + name + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            int i = -1;
            try {
                i = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            return i;
        }
        return -1;
    }

    //根据县代码获取市名称
    public static String GetShiName(int xian_code) {
        String sql = "select t2.name from xian as t1, shi as t2 where t1.code='" + xian_code + "' and t1.parent=t2.code";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            return sss[0][0];
        }
        return null;
    }

    //获取市代码对应的县名称列表
    public static List<String> GetXianList(int shi_code) {
        String sql = "select name from xian where parent='" + shi_code + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            List<String> lst = new ArrayList<String>();
            for (int i = 0; i < sss.length; i++) {
                if (sss[i][0] != null) {
                    lst.add(sss[i][0].trim());
                }
            }
            return lst;
        }
        return new ArrayList<String>();
    }

    //根据县名称获取县代码
    public static int GetXianCode(String name) {
        String sql = "select code from xian where name='" + name + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            int i = -1;
            try {
                i = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            return i;
        }
        return -1;
    }

    //根据县代码获取县名称
    public static String GetXianName(int code) {
        String sql = "select name from xian where code='" + code + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            return sss[0][0];
        }
        return null;
    }

    //根据样地号获取现代吗
    public static int GetXianCodeByYdh(int ydh) {
        String sql = "select xian from slqc_ydyz where ydh='" + ydh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null && sss.length > 0 && sss[0][0] != null) {
            int i = -1;
            try {
                i = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            return i;
        }
        return -1;
    }


    public static String GetXianJuNameByYdh(int ydh) {
        String sql = "select xian,f2 from slqc_ydyz where ydh = '" + ydh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            int xian = 0;
            try {
                xian = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            int ju = 0;
            try {
                ju = Integer.parseInt(sss[0][1]);
            } catch (Exception e) {
            }
            if (ju > 0 && ju < 10) {
                return Qianqimgr.GetXianName(ju);
            } else {
                return Qianqimgr.GetXianName(xian);
            }
        } else {
            return "";
        }
    }

    public static String GetJuNameByYdh(int ydh) {
        String sql = "select xian,f2 from slqc_ydyz where ydh = '" + ydh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            int ju = 0;
            try {
                ju = Integer.parseInt(sss[0][1]);
            } catch (Exception e) {
            }
            if (ju > 0 && ju < 10) {
                return Qianqimgr.GetXianName(ju);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    //判断是否是样方样地
    public static boolean IsYangfang(int ydh) {
        /*
        String sql = "select ydh from slqc_ydyz where f3 = '1' and ydh='" + ydh + "'";
		String[][] sss = SelectData(sql);
		if(sss != null && sss.length > 0)
		{
			return true;
		}
		return false;
		*/
        return true;
    }


    public static int GetQqdl(int ydh) {
        String sql = "select dl from slqc_ydyz where ydh = '" + ydh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            int dl = -1;
            try {
                dl = Integer.parseInt(sss[0][0]);
            } catch (Exception e) {
            }
            return YangdiMgr.DlCodeOldToNew(dl);
        }
        return -1;
    }

    public static MyCoord GetYdloc(int ydh) {
        MyCoord c = null;
        String dbFile = GetDbFile();
        String sql = "select gps_x, gps_y from slqc_ydyz where ydh = '" + ydh + "'";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                double lon = cursor.getDouble(0);
                double lat = cursor.getDouble(1);
                if (lon > 0 && lat > 0) c = new MyCoord(lon, lat, 0);
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
        }
        return c;
    }

    public static String[] GetQqYdyz(int ydh) {
        String sql = "select * from slqc_ydyz where ydh = '" + ydh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            return sss[0];
        }
        return null;
    }

    public static String[] GetQqKjl(int ydh, int xh) {
        String sql = "select * from slqc_kjl where ydh = '" + ydh + "' and xh = '" + xh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            return sss[0];
        }
        return null;
    }

    public static String[][] GetQqMmjc(int ydh) {
        String sql = "select * from slqc_mmjc where ydh = '" + ydh + "'";
        return SelectData(sql);
    }

    public static String GetQqYmsz(int ydh, int ymh) {
        String sql = "select szdm from slqc_mmjc where ydh = '" + ydh + "' and ymh = '" + ymh + "'";
        String[][] sss = SelectData(sql);
        if (sss != null) {
            String dm = sss[0][0];
            sql = "select name from sz_old where code_old = '" + dm + "'";
            sss = Resmgr.SelectData(sql);
            if (sss != null) {
                return dm + " " + sss[0][0];
            }
        }
        return null;
    }

    public static List<Kjl> GetQqKjlList(int ydh) {
        List<Kjl> lst = new ArrayList<Kjl>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_kjl where ydh = '" + ydh + "'";
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
                Kjl item = new Kjl();
                item.xh = cursor.getInt(1);
                item.mjbl = cursor.getFloat(2);
                item.dl = cursor.getInt(3);
                item.tdqs = cursor.getInt(4);
                item.lmqs = cursor.getInt(5);
                item.linzh = cursor.getInt(6);
                item.qy = cursor.getInt(7);
                item.yssz = cursor.getInt(8);
                item.lingz = cursor.getInt(9);
                item.ybd = cursor.getFloat(10);
                item.pjsg = cursor.getFloat(11);
                item.slqljg = cursor.getInt(12);
                item.szjg = cursor.getInt(13);
                item.spljydj = cursor.getInt(14);
                item.yssz = Resmgr.GetSzNewCodeByOldCode(item.yssz);
                item.mjbl /= 100.0f;
                item.ybd /= 100.0f;
                item.pjsg /= 10.0f;
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Sgcl> GetQqSgclList(int ydh) {
        List<Sgcl> lst = new ArrayList<Sgcl>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_sgcl where ydh = '" + ydh + "'";
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
                Sgcl item = new Sgcl();
                item.xh = i;
                item.ymh = cursor.getInt(1);
                item.sz = cursor.getInt(2);
                item.sz = Resmgr.GetSzNewCodeByOldCode(item.sz);
                item.xj = cursor.getFloat(3);
                item.sg = cursor.getFloat(4);
                item.zxg = cursor.getFloat(5);
                item.xj /= 10;
                item.sg /= 10;
                item.zxg /= 10;
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Slzh> GetQqSlzhList(int ydh) {
        List<Slzh> lst = new ArrayList<Slzh>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_slzh where ydh = '" + ydh + "'";
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
                Slzh item = new Slzh();
                item.xh = i;
                item.xh = cursor.getInt(1);
                item.zhlx = cursor.getInt(2);
                item.whbw = cursor.getString(3);
                item.szdj = cursor.getInt(4);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Trgx> GetQqTrgxList(int ydh) {
        List<Trgx> lst = new ArrayList<Trgx>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_trgx where ydh = '" + ydh + "'";
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
                Trgx item = new Trgx();
                item.xh = i;
                item.sz = cursor.getString(1);
                item.zs1 = cursor.getInt(2);
                item.zs2 = cursor.getInt(3);
                item.zs3 = cursor.getInt(4);
                item.jkzk = cursor.getString(5);
                item.phqk = cursor.getString(6);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Xm> GetQqXmList(int ydh) {
        List<Xm> lst = new ArrayList<Xm>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_xmdc where ydh = '" + ydh + "'";
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
                Xm item = new Xm();
                item.xh = i;
                item.mc = cursor.getString(1);
                item.gd = cursor.getFloat(2);
                item.xj = cursor.getFloat(3);
                item.gd /= 10;
                item.xj /= 10;
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Zb> GetQqZbList(int ydh) {
        List<Zb> lst = new ArrayList<Zb>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_zbdc where ydh = '" + ydh + "'";
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
                Zb item = new Zb();
                item.xh = i;
                item.zblx = cursor.getInt(1);
                item.mc = cursor.getString(2);
                item.pjgd = cursor.getFloat(3);
                item.fgd = cursor.getInt(4);
                item.zs = cursor.getInt(5);
                item.pjdj = cursor.getFloat(6);
                item.pjgd /= 10;
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Yddww> GetQqYindiandwwList(int ydh) {
        List<Yddww> lst = new ArrayList<Yddww>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_yindiandww where ydh = '" + ydh + "'";
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
                Yddww item = new Yddww();
                item.xh = i;
                item.mc = cursor.getString(1);
                item.bh = cursor.getInt(2);
                item.fwj = cursor.getFloat(3);
                item.spj = cursor.getFloat(4);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Yddww> GetQqYangdidwwList(int ydh) {
        List<Yddww> lst = new ArrayList<Yddww>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_yangdidww where ydh = '" + ydh + "'";
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
                Yddww item = new Yddww();
                item.xh = i;
                item.mc = cursor.getString(1);
                item.bh = cursor.getInt(2);
                item.fwj = cursor.getFloat(3);
                item.spj = cursor.getFloat(4);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Cljl> GetQqYxclList(int ydh) {
        List<Cljl> lst = new ArrayList<Cljl>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_yxcl where ydh = '" + ydh + "'";
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
                Cljl item = new Cljl();
                item.xh = i;
                item.cz = cursor.getString(1);
                item.fwj = cursor.getFloat(2);
                item.qxj = cursor.getFloat(3);
                item.xj = cursor.getFloat(4);
                item.spj = cursor.getFloat(5);
                item.lj = cursor.getFloat(6);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static List<Cljl> GetQqZjclList(int ydh) {
        List<Cljl> lst = new ArrayList<Cljl>();
        String dbFile = GetDbFile();
        String sql = "select * from slqc_zjcl where ydh = '" + ydh + "'";
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
                Cljl item = new Cljl();
                item.xh = i;
                item.cz = cursor.getString(1);
                item.fwj = cursor.getFloat(2);
                item.qxj = cursor.getFloat(3);
                item.xj = cursor.getFloat(4);
                item.spj = cursor.getFloat(5);
                item.lj = cursor.getFloat(6);
                lst.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return lst;
    }

    public static String[][] GetQqQtinfo(int ydh) {
        String sql = "select * from slqc_qt where ydh = '" + ydh + "'";
        return SelectData(sql);
    }

    public static String[][] GetQqKpfm(int ydh) {
        String sql = "select * from slqc_kpfm where ydh = '" + ydh + "'";
        return SelectData(sql);
    }

    public static String[][] GetQqWclzld(int ydh) {
        String sql = "select * from slqc_wclzld where ydh = '" + ydh + "'";
        return SelectData(sql);
    }

    public static String[][] SelectData(String sql) {
        String dbFile = GetDbFile();
        if (!MyFile.Exists(dbFile)) {
            return null;
        }
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

    public static String GetDbFile() {
        return MyConfig.GetAppdir() + "/qianqi.dat";
    }
}
