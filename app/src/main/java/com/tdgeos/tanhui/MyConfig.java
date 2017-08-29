package com.tdgeos.tanhui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.tdgeos.yangdi.YangdiMgr;

import android.os.Environment;

public class MyConfig {
    public static int iDpi = 0;
    public static int iScreenWidth = 0;
    public static int iScreenHeight = 0;
    public static int Dialog_Width_Large = 0;
    public static int Dialog_Width_Big = 0;
    public static int Dialog_Width_Middle = 0;
    public static int Dialog_Width_Small = 0;
    public static int Dialog_Height = 0;

    public static float DPI_SCALE = 1.0f;

    public static int MAP_VIEW_WIDTH = 0;
    public static int MAP_VIEW_HEIGHT = 0;

    public static String APP_DIR = "";
    public static String CUR_DIR = "";
    public static String WORK_DIR = "";
    public static String BACKUP_DIR = "";
    public static String EXPORT_DIR = "";

    public static boolean isOpenLoc = true;
    public static boolean isOpenTrk = true;
    public static boolean isShowQqTrk = true;
    public static boolean isShowTrk = true;
    public static int iTrkColor = 0xff00ff00;
    public static int iQqTrkColor = 0xff00ffff;

    public static int iCollectType = 0;//0表示按时间，1表示按距离
    public static int iCollectTime = 5;//秒
    public static int iCollectLen = 30;//米

    public static String DeviceId = "";
    public static String LastMapFile = "";

    public static int curyd = 0;
    public static int last_kjxh = 0;
    public static int last_szdm = 0;

    public static String CurTrkName = "";

    public static int lon0 = 120;
    public static int zone = 3;
    public static double dx = -17;
    public static double dy = -130;
    public static double dz = -40;
    public static double rx = 0;
    public static double ry = 0;
    public static double rz = 0;
    public static double k = 0;


    public static final String CONFIG_FILE = Environment.getExternalStorageDirectory().getPath() + "/" + YangdiMgr.APP_DIR_NAME + "/config.xml";
    public static final String LABEL_DPI = "dpi";
    public static final String LABEL_SCREEN_WIDTH = "screen_width";
    public static final String LABEL_SCREEN_HEIGHT = "screen_height";
    public static final String LABEL_DLG_WIDTH_LARGE = "dlg_width_large";
    public static final String LABEL_DLG_WIDTH_BIG = "dlg_width_big";
    public static final String LABEL_DLG_WIDTH_MIDDLE = "dlg_width_middle";
    public static final String LABEL_DLG_WIDTH_SMALL = "dlg_width_small";
    public static final String LABEL_DLG_HEIGHT = "dlg_height";
    public static final String LABEL_DPI_SCALE = "dpi_scale";
    public static final String LABEL_MAP_VIEW_WIDTH = "map_view_width";
    public static final String LABEL_MAP_VIEW_HEIGHT = "map_view_height";
    public static final String LABEL_APP_DIR = "app_dir";
    public static final String LABEL_CUR_DIR = "cur_dir";
    public static final String LABEL_WORK_DIR = "work_dir";
    public static final String LABEL_BACKUP_DIR = "backup_dir";
    public static final String LABEL_EXPORT_DIR = "export_dir";
    public static final String LABEL_OPEN_LOC = "open_loc";
    public static final String LABEL_OPEN_TRK = "open_trk";
    public static final String LABEL_COLLECT_TYPE = "collect_type";
    public static final String LABEL_COLLECT_TIME = "collect_time";
    public static final String LABEL_COLLECT_LEN = "collect_len";
    public static final String LABEL_SHOW_TRK = "show_trk";
    public static final String LABEL_SHOW_QQ_TRK = "show_qq_trk";
    public static final String LABEL_TRK_COLOR = "trk_color";
    public static final String LABEL_QQ_TRK_COLOR = "qq_trk_color";
    public static final String LABEL_DEVICE_ID = "device_id";
    public static final String LABEL_LAST_MAP_FILE = "last_map_file";
    public static final String LABEL_CUR_YD = "cur_yd";
    public static final String LABEL_CUR_TRK = "cur_trk";
    public static final String LABEL_LAST_KJXH = "last_kjxh";
    public static final String LABEL_LAST_SZDM = "last_szdm";
    public static final String LABEL_PARAMS_LON0 = "params_lon0";
    public static final String LABEL_PARAMS_ZONE = "params_zone";
    public static final String LABEL_PARAMS_DX = "params_dx";
    public static final String LABEL_PARAMS_DY = "params_dy";
    public static final String LABEL_PARAMS_DZ = "params_dz";
    public static final String LABEL_PARAMS_RX = "params_rx";
    public static final String LABEL_PARAMS_RY = "params_ry";
    public static final String LABEL_PARAMS_RZ = "params_rz";
    public static final String LABEL_PARAMS_K = "params_k";

    private static void readConfig() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            MyHandler handler = new MyHandler();
            FileInputStream fis = new FileInputStream(file);
            parser.parse(fis, handler);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeConfig() {
        try {
            SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");    // 是否忽略XML声明

            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            handler.setResult(result);

            String uri = "";
            String localName = "";
            char[] ch = null;
            handler.startDocument();
            AttributesImpl attrs = new AttributesImpl();
            attrs.clear();
            handler.startElement(uri, localName, "config", attrs);

            handler.startElement(uri, localName, LABEL_DPI, null);
            ch = String.valueOf(iDpi).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DPI);

            handler.startElement(uri, localName, LABEL_SCREEN_WIDTH, null);
            ch = String.valueOf(iScreenWidth).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_SCREEN_WIDTH);

            handler.startElement(uri, localName, LABEL_SCREEN_HEIGHT, null);
            ch = String.valueOf(iScreenHeight).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_SCREEN_HEIGHT);

            handler.startElement(uri, localName, LABEL_DLG_WIDTH_LARGE, null);
            ch = String.valueOf(Dialog_Width_Large).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DLG_WIDTH_LARGE);

            handler.startElement(uri, localName, LABEL_DLG_WIDTH_BIG, null);
            ch = String.valueOf(Dialog_Width_Big).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DLG_WIDTH_BIG);

            handler.startElement(uri, localName, LABEL_DLG_WIDTH_MIDDLE, null);
            ch = String.valueOf(Dialog_Width_Middle).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DLG_WIDTH_MIDDLE);

            handler.startElement(uri, localName, LABEL_DLG_WIDTH_SMALL, null);
            ch = String.valueOf(Dialog_Width_Small).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DLG_WIDTH_SMALL);

            handler.startElement(uri, localName, LABEL_DLG_HEIGHT, null);
            ch = String.valueOf(Dialog_Height).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DLG_HEIGHT);

            handler.startElement(uri, localName, LABEL_DPI_SCALE, null);
            ch = String.valueOf(DPI_SCALE).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DPI_SCALE);

            handler.startElement(uri, localName, LABEL_MAP_VIEW_WIDTH, null);
            ch = String.valueOf(MAP_VIEW_WIDTH).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_MAP_VIEW_WIDTH);

            handler.startElement(uri, localName, LABEL_MAP_VIEW_HEIGHT, null);
            ch = String.valueOf(MAP_VIEW_HEIGHT).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_MAP_VIEW_HEIGHT);

            handler.startElement(uri, localName, LABEL_APP_DIR, null);
            ch = String.valueOf(APP_DIR).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_APP_DIR);

            handler.startElement(uri, localName, LABEL_CUR_DIR, null);
            ch = String.valueOf(CUR_DIR).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_CUR_DIR);

            handler.startElement(uri, localName, LABEL_WORK_DIR, null);
            ch = String.valueOf(WORK_DIR).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_WORK_DIR);

            handler.startElement(uri, localName, LABEL_BACKUP_DIR, null);
            ch = String.valueOf(BACKUP_DIR).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_BACKUP_DIR);

            handler.startElement(uri, localName, LABEL_EXPORT_DIR, null);
            ch = String.valueOf(EXPORT_DIR).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_EXPORT_DIR);

            handler.startElement(uri, localName, LABEL_OPEN_LOC, null);
            ch = String.valueOf(isOpenLoc ? 1 : 0).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_OPEN_LOC);

            handler.startElement(uri, localName, LABEL_OPEN_TRK, null);
            ch = String.valueOf(isOpenTrk ? 1 : 0).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_OPEN_TRK);

            handler.startElement(uri, localName, LABEL_COLLECT_TYPE, null);
            ch = String.valueOf(iCollectType).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_COLLECT_TYPE);

            handler.startElement(uri, localName, LABEL_COLLECT_TIME, null);
            ch = String.valueOf(iCollectTime).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_COLLECT_TIME);

            handler.startElement(uri, localName, LABEL_COLLECT_LEN, null);
            ch = String.valueOf(iCollectLen).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_COLLECT_LEN);

            handler.startElement(uri, localName, LABEL_SHOW_TRK, null);
            ch = String.valueOf(isShowTrk ? 1 : 0).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_SHOW_TRK);

            handler.startElement(uri, localName, LABEL_TRK_COLOR, null);
            ch = String.valueOf(iTrkColor).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_TRK_COLOR);

            handler.startElement(uri, localName, LABEL_SHOW_QQ_TRK, null);
            ch = String.valueOf(isShowQqTrk ? 1 : 0).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_SHOW_QQ_TRK);

            handler.startElement(uri, localName, LABEL_QQ_TRK_COLOR, null);
            ch = String.valueOf(iQqTrkColor).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_QQ_TRK_COLOR);

            handler.startElement(uri, localName, LABEL_DEVICE_ID, null);
            ch = String.valueOf(DeviceId).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_DEVICE_ID);

            handler.startElement(uri, localName, LABEL_LAST_MAP_FILE, null);
            ch = String.valueOf(LastMapFile).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_LAST_MAP_FILE);

            handler.startElement(uri, localName, LABEL_CUR_YD, null);
            ch = String.valueOf(curyd).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_CUR_YD);

            handler.startElement(uri, localName, LABEL_CUR_TRK, null);
            ch = String.valueOf(CurTrkName).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_CUR_TRK);

            handler.startElement(uri, localName, LABEL_LAST_KJXH, null);
            ch = String.valueOf(last_kjxh).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_LAST_KJXH);

            handler.startElement(uri, localName, LABEL_LAST_SZDM, null);
            ch = String.valueOf(last_szdm).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_LAST_SZDM);

            handler.startElement(uri, localName, LABEL_PARAMS_LON0, null);
            ch = String.valueOf(lon0).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_LON0);

            handler.startElement(uri, localName, LABEL_PARAMS_ZONE, null);
            ch = String.valueOf(zone).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_ZONE);

            handler.startElement(uri, localName, LABEL_PARAMS_DX, null);
            ch = String.valueOf(dx).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_DX);

            handler.startElement(uri, localName, LABEL_PARAMS_DY, null);
            ch = String.valueOf(dy).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_DY);

            handler.startElement(uri, localName, LABEL_PARAMS_DZ, null);
            ch = String.valueOf(dz).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_DZ);

            handler.startElement(uri, localName, LABEL_PARAMS_RX, null);
            ch = String.valueOf(rx).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_RX);

            handler.startElement(uri, localName, LABEL_PARAMS_RY, null);
            ch = String.valueOf(ry).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_RY);

            handler.startElement(uri, localName, LABEL_PARAMS_RZ, null);
            ch = String.valueOf(rz).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_RZ);

            handler.startElement(uri, localName, LABEL_PARAMS_K, null);
            ch = String.valueOf(k).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, LABEL_PARAMS_K);

            handler.endElement(uri, localName, "config");
            handler.endDocument();

            String str = writer.toString();
            byte[] bts = str.getBytes("utf-8");

            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            fos.write(bts);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SetScreenSize(int width, int height) {
        readConfig();
        iScreenWidth = width;
        iScreenHeight = height;
        Dialog_Width_Large = (int) (iScreenWidth * 0.95);
        Dialog_Width_Big = (int) (iScreenWidth * 0.7);
        Dialog_Width_Middle = iScreenWidth / 2;
        Dialog_Width_Small = iScreenWidth / 3;
        Dialog_Height = (int) (iScreenHeight * 0.8);
        writeConfig();
    }

    public static void SetDpi(int dpi) {
        readConfig();
        iDpi = dpi;
        DPI_SCALE = iDpi / 160.0f;
        writeConfig();
    }

    public static void SetMapViewSize(int width, int height) {
        readConfig();
        MAP_VIEW_WIDTH = width;
        MAP_VIEW_HEIGHT = height;
        writeConfig();
    }

    public static void SetAppdir(String dir) {
        readConfig();
        APP_DIR = dir;
        writeConfig();
    }

    public static void SetCurdir(String dir) {
        readConfig();
        CUR_DIR = dir;
        writeConfig();
    }

    public static void SetWorkdir(String dir) {
        readConfig();
        WORK_DIR = dir;
        writeConfig();
    }

    public static void SetBackupdir(String dir) {
        readConfig();
        BACKUP_DIR = dir;
        writeConfig();
    }

    public static void SetExportdir(String dir) {
        readConfig();
        EXPORT_DIR = dir;
        writeConfig();
    }

    public static void SetOpenLoc(boolean b) {
        readConfig();
        isOpenLoc = b;
        writeConfig();
    }

    public static void SetOpenTrack(boolean b) {
        readConfig();
        isOpenTrk = b;
        writeConfig();
    }

    public static void SetShowTrack(boolean b) {
        readConfig();
        isShowTrk = b;
        writeConfig();
    }

    public static void SetShowQqTrack(boolean b) {
        readConfig();
        isShowQqTrk = b;
        writeConfig();
    }

    public static void SetCollectType(int type) {
        readConfig();
        iCollectType = type;
        writeConfig();
    }

    public static void SetCollectTime(int time) {
        readConfig();
        iCollectTime = time;
        writeConfig();
    }

    public static void SetCollectLen(int len) {
        readConfig();
        iCollectLen = len;
        writeConfig();
    }

    public static void SetTrackColor(int color) {
        readConfig();
        iTrkColor = color;
        writeConfig();
    }

    public static void SetQqTrackColor(int color) {
        readConfig();
        iQqTrkColor = color;
        writeConfig();
    }

    public static void SetDeviceId(String id) {
        readConfig();
        DeviceId = id;
        writeConfig();
    }

    public static void SetLastMapFile(String file) {
        readConfig();
        LastMapFile = file;
        writeConfig();
    }

    public static void SetCurYd(int ydh) {
        readConfig();
        curyd = ydh;
        last_kjxh = 0;
        last_szdm = 0;
        writeConfig();
    }

    public static void SetLastKjxh(int xh) {
        readConfig();
        last_kjxh = xh;
        writeConfig();
    }

    public static void SetLastSzdm(int szdm) {
        readConfig();
        last_szdm = szdm;
        writeConfig();
    }

    public static void SetCurTrk(String trk) {
        readConfig();
        CurTrkName = trk;
        writeConfig();
    }

    public static void SetParamsLon0(int lon) {
        readConfig();
        lon0 = lon;
        writeConfig();
    }

    public static void SetParamsZone(int z) {
        readConfig();
        zone = z;
        writeConfig();
    }

    public static void SetParamsDx(double d) {
        readConfig();
        dx = d;
        writeConfig();
    }

    public static void SetParamsDy(double d) {
        readConfig();
        dy = d;
        writeConfig();
    }

    public static void SetParamsDz(double d) {
        readConfig();
        dz = d;
        writeConfig();
    }

    public static void SetParamsRx(double d) {
        readConfig();
        rx = d;
        writeConfig();
    }

    public static void SetParamsRy(double d) {
        readConfig();
        ry = d;
        writeConfig();
    }

    public static void SetParamsRz(double d) {
        readConfig();
        rz = d;
        writeConfig();
    }

    public static void SetParamsK(double d) {
        readConfig();
        k = d;
        writeConfig();
    }


    public static int GetLargeWidth() {
        readConfig();
        return Dialog_Width_Large;
    }

    public static int GetBigWidth() {
        readConfig();
        return Dialog_Width_Big;
    }

    public static int GetMiddleWidth() {
        readConfig();
        return Dialog_Width_Middle;
    }

    public static int GetSmallWidth() {
        readConfig();
        return Dialog_Width_Small;
    }

    public static int GetDlgHeight() {
        readConfig();
        return Dialog_Height;
    }

    public static int GetDpi() {
        readConfig();
        return iDpi;
    }

    public static int GetMapViewWidth() {
        readConfig();
        return MAP_VIEW_WIDTH;
    }

    public static int GetMapViewHeight() {
        readConfig();
        return MAP_VIEW_HEIGHT;
    }

    public static String GetAppdir() {
        readConfig();
        return APP_DIR;
    }

    public static String GetCurdir() {
        readConfig();
        return CUR_DIR;
    }

    public static String GetWorkdir() {
        readConfig();
        return WORK_DIR;
    }

    public static String GetBackupdir() {
        readConfig();
        return BACKUP_DIR;
    }

    public static String GetExportdir() {
        readConfig();
        return EXPORT_DIR;
    }

    public static boolean GetOpenLoc() {
        readConfig();
        return isOpenLoc;
    }

    public static boolean GetOpenTrack() {
        readConfig();
        return isOpenTrk;
    }

    public static boolean GetShowTrack() {
        readConfig();
        return isShowTrk;
    }

    public static boolean GetShowQqTrack() {
        readConfig();
        return isShowQqTrk;
    }

    public static int GetCollectType() {
        readConfig();
        return iCollectType;
    }

    public static int GetCollectTime() {
        readConfig();
        return iCollectTime;
    }

    public static int GetCollectLen() {
        readConfig();
        return iCollectLen;
    }

    public static int GetTrackColor() {
        readConfig();
        return iTrkColor;
    }

    public static int GetQqTrackColor() {
        readConfig();
        return iQqTrkColor;
    }

    public static String GetDeviceId() {
        readConfig();
        return DeviceId;
    }

    public static String GetLastMapFile() {
        readConfig();
        return LastMapFile;
    }

    public static int GetCurYd() {
        readConfig();
        return curyd;
    }

    public static int GetLastKjxh() {
        readConfig();
        return last_kjxh;
    }

    public static int GetLastSzdm() {
        readConfig();
        return last_szdm;
    }

    public static String GetCurTrk() {
        readConfig();
        return CurTrkName;
    }

    public static int GetParamsLon0() {
        readConfig();
        return lon0;
    }

    public static int GetParamsZone() {
        readConfig();
        return zone;
    }

    public static double GetParamsDx() {
        readConfig();
        return dx;
    }

    public static double GetParamsDy() {
        readConfig();
        return dy;
    }

    public static double GetParamsDz() {
        readConfig();
        return dz;
    }

    public static double GetParamsRx() {
        readConfig();
        return rx;
    }

    public static double GetParamsRy() {
        readConfig();
        return ry;
    }

    public static double GetParamsRz() {
        readConfig();
        return rz;
    }

    public static double GetParamsK() {
        readConfig();
        return k;
    }
}

class MyHandler extends DefaultHandler {
    private StringBuilder builder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        builder.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals(MyConfig.LABEL_DPI)) {
            try {
                MyConfig.iDpi = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_SCREEN_WIDTH)) {
            try {
                MyConfig.iScreenWidth = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_SCREEN_HEIGHT)) {
            try {
                MyConfig.iScreenHeight = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DLG_WIDTH_LARGE)) {
            try {
                MyConfig.Dialog_Width_Large = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DLG_WIDTH_BIG)) {
            try {
                MyConfig.Dialog_Width_Big = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DLG_WIDTH_MIDDLE)) {
            try {
                MyConfig.Dialog_Width_Middle = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DLG_WIDTH_SMALL)) {
            try {
                MyConfig.Dialog_Width_Small = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DLG_HEIGHT)) {
            try {
                MyConfig.Dialog_Height = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.DPI_SCALE)) {
            try {
                MyConfig.DPI_SCALE = Float.parseFloat(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_MAP_VIEW_WIDTH)) {
            try {
                MyConfig.MAP_VIEW_WIDTH = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_MAP_VIEW_HEIGHT)) {
            try {
                MyConfig.MAP_VIEW_HEIGHT = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_APP_DIR)) {
            try {
                MyConfig.APP_DIR = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_CUR_DIR)) {
            try {
                MyConfig.CUR_DIR = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_WORK_DIR)) {
            try {
                MyConfig.WORK_DIR = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_BACKUP_DIR)) {
            try {
                MyConfig.BACKUP_DIR = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_EXPORT_DIR)) {
            try {
                MyConfig.EXPORT_DIR = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_OPEN_LOC)) {
            try {
                int i = Integer.parseInt(builder.toString());
                MyConfig.isOpenLoc = (i == 1);
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_OPEN_TRK)) {
            try {
                int i = Integer.parseInt(builder.toString());
                MyConfig.isOpenTrk = (i == 1);
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_COLLECT_TYPE)) {
            try {
                MyConfig.iCollectType = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_COLLECT_TIME)) {
            try {
                MyConfig.iCollectTime = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_COLLECT_LEN)) {
            try {
                MyConfig.iCollectLen = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_SHOW_TRK)) {
            try {
                int i = Integer.parseInt(builder.toString());
                MyConfig.isShowTrk = (i == 1);
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_TRK_COLOR)) {
            try {
                MyConfig.iTrkColor = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_SHOW_QQ_TRK)) {
            try {
                int i = Integer.parseInt(builder.toString());
                MyConfig.isShowQqTrk = (i == 1);
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_QQ_TRK_COLOR)) {
            try {
                MyConfig.iQqTrkColor = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_DEVICE_ID)) {
            try {
                MyConfig.DeviceId = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_LAST_MAP_FILE)) {
            try {
                MyConfig.LastMapFile = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_CUR_YD)) {
            try {
                MyConfig.curyd = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_LAST_KJXH)) {
            try {
                MyConfig.last_kjxh = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_LAST_SZDM)) {
            try {
                MyConfig.last_szdm = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_CUR_TRK)) {
            try {
                MyConfig.CurTrkName = builder.toString();
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_LON0)) {
            try {
                MyConfig.lon0 = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_ZONE)) {
            try {
                MyConfig.zone = Integer.parseInt(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_DX)) {
            try {
                MyConfig.dx = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_DY)) {
            try {
                MyConfig.dy = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_DZ)) {
            try {
                MyConfig.dz = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_RX)) {
            try {
                MyConfig.rx = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_RY)) {
            try {
                MyConfig.ry = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_RZ)) {
            try {
                MyConfig.rz = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        } else if (localName.equals(MyConfig.LABEL_PARAMS_K)) {
            try {
                MyConfig.k = Double.parseDouble(builder.toString());
            } catch (NumberFormatException e) {
            }
        }
    }
}
