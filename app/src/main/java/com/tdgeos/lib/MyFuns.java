package com.tdgeos.lib;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//通用函数
public class MyFuns {
    public static Bitmap GetBitmap(String filePath, int size) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        int bw = opt.outWidth;
        int bh = opt.outHeight;

        float a = bw > bh ? bw : bh;
        float f = a / size;
        int n = 1;
        if (f >= 2) n = (int) f;

        opt.inJustDecodeBounds = false;
        opt.inSampleSize = n;
        opt.inInputShareable = true;
        opt.inPurgeable = true;
        bitmap = BitmapFactory.decodeFile(filePath, opt);
        //bw = bitmap.getWidth();
        //bh = bitmap.getHeight();
        return bitmap;
    }

    public static String NumberToDegree(double num) {
        int du = (int) num;
        double d_fen = (num - du) * 60;
        int fen = (int) d_fen;
        double miao = (d_fen - fen) * 60;
        miao = MyDecimal(miao, 2);
        String degree = du + "° " + fen + "' " + miao + "\"";
        return degree;
    }

    public static void sort_bubble(int[] a, int n) {
        int tmp = 0;
        for (int i = 0; i < n - 1; ++i) {
            boolean flag = true;
            for (int j = 0; j < n - i - 1; ++j) {
                if (a[j] > a[j + 1]) {
                    tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    flag = false;
                }
            }
            if (flag) break;
        }
    }

    public static int[] Sort(int[] data, int count) {
        sort_bubble(data, count);
        return data;
    }

    public static float min(float f1, float f2) {
        return f1 < f2 ? f1 : f2;
    }

    public static float max(float f1, float f2) {
        return f1 > f2 ? f1 : f2;
    }

    /**
     * 求最大值
     *
     * @param arData
     * @return
     */
    public static double dMax(double[] arData) {
        double temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp < arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最小值
     *
     * @param arData
     * @return
     */
    public static double dMin(double[] arData) {
        double temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp > arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最大值
     *
     * @param arData
     * @return
     */
    public static float dMax(float[] arData) {
        float temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp < arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最小值
     *
     * @param arData
     * @return
     */
    public static int dMin(int[] arData) {
        int temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp > arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最大值
     *
     * @param arData
     * @return
     */
    public static int dMax(int[] arData) {
        int temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp < arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最小值
     *
     * @param arData
     * @return
     */
    public static float dMin(float[] arData) {
        float temp = arData[0];
        for (int i = 1; i < arData.length; i++) {
            if (temp > arData[i]) temp = arData[i];
        }
        return temp;
    }

    /**
     * 求最小值
     *
     * @param lstData
     * @return
     */
    public static int iMin(List<Integer> lstData) {
        int t = lstData.get(0);
        for (int i = 1; i < lstData.size(); i++) {
            if (t > lstData.get(i)) t = lstData.get(i);
        }
        return t;
    }

    /**
     * 求最大值
     *
     * @param lstData
     * @return
     */
    public static int iMax(List<Integer> lstData) {
        int t = lstData.get(0);
        for (int i = 1; i < lstData.size(); i++) {
            if (t < lstData.get(i)) t = lstData.get(i);
        }
        return t;
    }

    public static String[] Split(String str, char c) {
        if (str == null) return null;
        if (str.equals("")) {
            String[] ss = new String[1];
            ss[0] = "";
            return ss;
        }
        int size = 0;
        List<String> lst = new ArrayList<String>();
        char[] ccStr = str.toCharArray();
        char[] cc = new char[ccStr.length];
        for (int i = 0; i < ccStr.length; i++) {
            if (ccStr[i] != c) {
                cc[size] = ccStr[i];
                size++;
            }
            if (ccStr[i] == c) {
                String s = new String(cc, 0, size);
                lst.add(s);
                size = 0;
            }
            if (i == ccStr.length - 1) {
                String s = new String(cc, 0, size);
                lst.add(s);
                size = 0;
            }
        }
        int count = lst.size();
        String[] ss = new String[count];
        for (int i = 0; i < count; i++) {
            ss[i] = lst.get(i);
        }
        return ss;
    }

    /**
     * 去除整型链表中的重复元素，构造一个新链表返回
     *
     * @param lst
     * @return
     */
    public static List<Integer> RemoveRepeat(List<Integer> lst) {
        if (lst == null) return lst;
        if (lst.size() == 0) return lst;
        Set<Integer> set = new HashSet<Integer>();
        List<Integer> tmp = new ArrayList<Integer>();
        for (int i = 0; i < lst.size(); i++) {
            set.add(lst.get(i));
        }
        Iterator<Integer> it = set.iterator();
        for (int i = 0; i < set.size(); i++) {
            if (it.hasNext()) tmp.add(it.next());
        }
        return tmp;
    }

    /**
     * 去除字符串链表中的重复元素，构造一个新链表返回
     *
     * @param lst
     * @return
     */
    public static List<String> RemoveRepeat_String(List<String> lst) {
        if (lst == null) return lst;
        if (lst.size() == 0) return lst;
        Set<String> set = new HashSet<String>();
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < lst.size(); i++) {
            set.add(lst.get(i));
        }
        Iterator<String> it = set.iterator();
        for (int i = 0; i < set.size(); i++) {
            if (it.hasNext()) tmp.add(it.next());
        }
        return tmp;
    }

    /**
     * 整型链表排序
     *
     * @param lst     排序前的链表
     * @param isToBig 为true从小到大排，为false从大道小排
     * @return 返回排序后的链表
     */
    public static List<Integer> SortList(List<Integer> lst, boolean isToBig) {
        if (lst == null || lst.size() == 0) return null;
        List<Integer> tmp = new ArrayList<Integer>();
        tmp.add(lst.get(0));

        for (int i = 1; i < lst.size(); i++) {
            if (lst.get(i) >= tmp.get(tmp.size() - 1)) {
                tmp.add(lst.get(i));
                continue;
            } else {
                for (int j = 0; j < tmp.size(); j++) {
                    if (lst.get(i) <= tmp.get(j)) {
                        tmp.add(j, lst.get(i));
                        break;
                    }
                }
            }
        }
        return tmp;
    }

    /**
     * 计算小数位数
     *
     * @param data
     * @return
     */
    public static int DecimalPlace(String data) {
        if (data == null) return -1;
        byte[] bts = data.getBytes();
        int pos = -1;
        for (int i = 0; i < bts.length; i++) {
            if (bts[i] == '.') {
                pos = i;
                break;
            }
        }
        if (pos == 0 || pos == bts.length - 1) return -1;
        return bts.length - 1 - pos;
    }

    public static String GetDateTimeByNumber() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        String shour;
        String sminute;
        String ssecond;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = syear + smonth + sday + shour + sminute + ssecond;
        return time;
    }

    //2012-04-10 17:38:42
    public static String GetDateTimeByString() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        String shour;
        String sminute;
        String ssecond;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = syear + "-" + smonth + "-" + sday + " " + shour + ":" + sminute + ":" + ssecond;
        return time;
    }

    //2012-04-10_17-38-42
    public static String GetDateTimeByString_() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        String shour;
        String sminute;
        String ssecond;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = syear + "-" + smonth + "-" + sday + "_" + shour + "-" + sminute + "-" + ssecond;
        return time;
    }

    public static String GetDateByNumberS() {
        Calendar c = Calendar.getInstance();
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        year = year % 100;
        String syear;
        String smonth;
        String sday;
        if (year < 10) syear = "0" + year;
        else syear = String.valueOf(year);
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        String date = syear + smonth + sday;
        return date;
    }

    public static String GetDateByString() {
        Calendar c = Calendar.getInstance();
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        String date = syear + "-" + smonth + "-" + sday;
        return date;
    }

    public static String GetTimeByString() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String shour;
        String sminute;
        String ssecond;
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = shour + ":" + sminute + ":" + ssecond;
        return time;
    }

    public static String GetTimeByStringS() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String shour;
        String sminute;
        String ssecond;
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = shour + ":" + sminute;
        return time;
    }

    //2012_04_10
    public static String GetDateByString_() {
        Calendar c = Calendar.getInstance();
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        String time = syear + "_" + smonth + "_" + sday;
        return time;
    }

    public static String GetTimeByNumber() {
        Calendar c = Calendar.getInstance();
        int second;
        int minute;
        int hour;
        int day;
        int month;
        int year;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        String syear = String.valueOf(year);
        String smonth;
        String sday;
        String shour;
        String sminute;
        String ssecond;
        if (month < 10) smonth = "0" + month;
        else smonth = String.valueOf(month);
        if (day < 10) sday = "0" + day;
        else sday = String.valueOf(day);
        if (hour < 10) shour = "0" + hour;
        else shour = String.valueOf(hour);
        if (minute < 10) sminute = "0" + minute;
        else sminute = String.valueOf(minute);
        if (second < 10) ssecond = "0" + second;
        else ssecond = String.valueOf(second);
        String time = syear + smonth + sday + shour + sminute + ssecond;
        return time;
    }

    //"yyyy-MM-dd hh:mm:ss" To "hh:mm"
    public static String DateTimeToTimeS(String str) {
        if (str == null || str.equals("")) return "";
        String[] ss = Split(str, ' ');
        if (ss == null || ss.length != 2) return "";
        String s = ss[1];
        String[] ss2 = Split(s, ':');
        if (ss2 == null || ss2.length != 3) return "";
        return ss2[0] + ":" + ss2[1];
    }

    public static int bigToLittle(int a) {
        return byteToLittleInt(bigIntToByte(a));
    }

    public static int littleToBig(int a) {
        return byteToBigInt(intToByte(a));
    }

    public static int byteToBigInt(byte[] w) {
        return ((w[0]) << 24 | (w[1] & 0xff) << 16 | (w[2] & 0xff) << 8 | (w[3] & 0xff));
    }

    public static int byteToLittleInt(byte[] w)//read little data
    {
        return (w[3]) << 24 | (w[2] & 0xff) << 16 | (w[1] & 0xff) << 8 | (w[0] & 0xff);
    }

    public static short byteToLittleShort(byte[] w) {
        return (short) ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    //无符号单字节转换为short
    public static short usbyteToShort(byte b) {
        byte[] sh = new byte[2];
        sh[1] = 0;
        sh[0] = b;
        return (short) ((sh[1] & 0xff) << 8 | (sh[0] & 0xff));
    }

    public static float byteToLittleFloat(byte[] w) {
        int value = (w[3]) << 24 | (w[2] & 0xff) << 16 | (w[1] & 0xff) << 8 | (w[0] & 0xff);
        return Float.intBitsToFloat(value);
    }

    public static double byteToLittleDouble(byte[] w) {
        long lvalue = (long) (w[7]) << 56 | (long) (w[6] & 0xff) << 48 |
                (long) (w[5] & 0xff) << 40 | (long) (w[4] & 0xff) << 32 |
                (long) (w[3] & 0xff) << 24 | (long) (w[2] & 0xff) << 16 |
                (long) (w[1] & 0xff) << 8 | (long) (w[0] & 0xff);
        return Double.longBitsToDouble(lvalue);
    }

    public static long byteToLittleLong(byte[] w) {
        long lvalue = (long) (w[7]) << 56 | (long) (w[6] & 0xff) << 48 |
                (long) (w[5] & 0xff) << 40 | (long) (w[4] & 0xff) << 32 |
                (long) (w[3] & 0xff) << 24 | (long) (w[2] & 0xff) << 16 |
                (long) (w[1] & 0xff) << 8 | (long) (w[0] & 0xff);
        return lvalue;
    }


    public static byte[] bigIntToByte(int w) {
        byte[] b = new byte[4];
        b[3] = (byte) w;
        b[2] = (byte) (w >> 8);
        b[1] = (byte) (w >> 16);
        b[0] = (byte) (w >> 24);
        return b;
    }

    public static byte[] shortToByte(short w) {
        byte[] b = new byte[2];
        b[0] = (byte) w;
        b[1] = (byte) (w >> 8);
        return b;
    }

    //short转换为无符号单字节
    public static byte shortToUSbyte(short s) {
        return (byte) s;
    }

    public static byte[] intToByte(int w) {
        byte[] b = new byte[4];
        b[0] = (byte) w;
        b[1] = (byte) (w >> 8);
        b[2] = (byte) (w >> 16);
        b[3] = (byte) (w >> 24);
        return b;
    }

    public static byte[] floatToByte(float w) {
        byte[] b = new byte[4];
        int t = Float.floatToIntBits(w);
        b[0] = (byte) t;
        b[1] = (byte) (t >> 8);
        b[2] = (byte) (t >> 16);
        b[3] = (byte) (t >> 24);
        return b;
    }

    public static byte[] doubleToByte(double w) {
        byte[] b = new byte[8];
        long t = Double.doubleToLongBits(w);
        b[0] = (byte) t;
        b[1] = (byte) (t >> 8);
        b[2] = (byte) (t >> 16);
        b[3] = (byte) (t >> 24);
        b[4] = (byte) (t >> 32);
        b[5] = (byte) (t >> 40);
        b[6] = (byte) (t >> 48);
        b[7] = (byte) (t >> 56);
        return b;
    }

    public static byte[] longToByte(long t) {
        byte[] b = new byte[8];
        b[0] = (byte) t;
        b[1] = (byte) (t >> 8);
        b[2] = (byte) (t >> 16);
        b[3] = (byte) (t >> 24);
        b[4] = (byte) (t >> 32);
        b[5] = (byte) (t >> 40);
        b[6] = (byte) (t >> 48);
        b[7] = (byte) (t >> 56);
        return b;
    }

    //比较
    public static boolean ArrayCompare(String[] a1, String[] a2) {
        if (a1 == null || a2 == null) return false;
        if (a1.length != a2.length) return false;
        if (a1.equals(a2)) return true;

        int size = a1.length;
        boolean b = true;
        for (int i = 0; i < size; i++) {
            boolean tmp = false;
            for (int j = 0; j < size; j++) {
                if (a1[i].equals(a2[j])) {
                    tmp = true;
                    break;
                }
            }
            if (!tmp) {
                b = false;
                break;
            }
        }
        return b;
    }

    public static float MyDecimal(float value, int n) {
        BigDecimal b = new BigDecimal(value);
        float f = b.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
        return f;
    }

    public static double MyDecimal(double value, int n) {
        BigDecimal b = new BigDecimal(value);
        double f = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f;
    }

    public static boolean IsSameNumber(float f1, float f2) {
        float t = f1 - f2;
        if (t > -0.0000001 && t < 0.0000001) return true;
        else return false;
    }

    public static boolean IsSameNumber(double f1, double f2) {
        double t = f1 - f2;
        if (t > -0.0000001 && t < 0.0000001) return true;
        else return false;
    }

    public static double LineLen(MyPoint pt1, MyPoint pt2) {
        if (pt1 == null || pt2 == null) return -1;
        return Math.sqrt((pt1.x - pt2.x) * (pt1.x - pt2.x) + (pt1.y - pt2.y) * (pt1.y - pt2.y));
    }

    //计算线段长度
    public static float LineLen(float x1, float y1, float x2, float y2) {
        float len = (float) Math.sqrt((double) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
        return len;
    }

    public static boolean IsPointInRing(float x, float y, float[] faX, float[] faY) {
        float fMinX = MyFuns.dMin(faX);
        float fMaxX = MyFuns.dMax(faX);
        float fMinY = MyFuns.dMin(faY);
        float fMaxY = MyFuns.dMax(faY);
        boolean b = x >= fMinX && y <= fMaxY && x <= fMaxX && y >= fMinY;
        if (!b) return false;
        int nCross = 0;
        for (int i = 0; i < faX.length; i++) {
            float x1 = faX[i];
            float y1 = faY[i];
            float x2 = faX[(i + 1) % faX.length];
            float y2 = faY[(i + 1) % faY.length];

            if (y1 == y2) continue;
            if (y < ((y1 < y2) ? y1 : y2)) continue;
            if (y >= ((y1 > y2) ? y1 : y2)) continue;

            float a = (y - y1) * (x2 - x1) / (y2 - y1) + x1;

            if (a > x) nCross++;
        }
        return (nCross % 2 == 1);
    }

    public static boolean IsPointInRing(MyPoint pt, List<MyPoint> lstRing) {
        float x = pt.x;
        float y = pt.y;
        float[] faX = new float[lstRing.size()];
        float[] faY = new float[lstRing.size()];
        for (int i = 0; i < lstRing.size(); i++) {
            faX[i] = lstRing.get(i).x;
            faY[i] = lstRing.get(i).y;
        }

        float fMinX = MyFuns.dMin(faX);
        float fMaxX = MyFuns.dMax(faX);
        float fMinY = MyFuns.dMin(faY);
        float fMaxY = MyFuns.dMax(faY);
        boolean b = x >= fMinX && y <= fMaxY && x <= fMaxX && y >= fMinY;
        if (!b) return false;
        int nCross = 0;
        for (int i = 0; i < faX.length; i++) {
            float x1 = faX[i];
            float y1 = faY[i];
            float x2 = faX[(i + 1) % faX.length];
            float y2 = faY[(i + 1) % faY.length];

            if (y1 == y2) continue;
            if (y < ((y1 < y2) ? y1 : y2)) continue;
            if (y >= ((y1 > y2) ? y1 : y2)) continue;

            float a = (y - y1) * (x2 - x1) / (y2 - y1) + x1;

            if (a > x) nCross++;
        }
        return (nCross % 2 == 1);
    }
}
