package com.tdgeos.lib;

public class CoordTransform {
    //WGS84椭球参数
    public static final double a_gps = 6378137.00;//长半轴
    public static final double b_gps = 6356752.3142;//短半轴
    public static final double f_gps = 0.0033528106647475;//扁率：1 / 298.257223563
    public static final double e2_gps = 0.0066943799013;//第一偏心率的平方
    public static final double e22_gps = 0.006739496742227;//第二偏心率的平方

    //BJ54椭球参数
    public static final double a_bj54 = 6378245;//长半轴
    public static final double b_bj54 = 6356863.0188;//短半轴
    public static final double f_bj54 = 0.003352329869259;//扁率：1 / 298.3
    public static final double e2_bj54 = 0.006693421622965949;//第一偏心率的平方
    public static final double e22_bj54 = 0.006738525414683;//第二偏心率的平方
    public static final double c_bj54 = 6399698.90178271;//极曲率半径

    //XA80椭球参数
    public static final double a_xa80 = 6378140;//长半轴
    public static final double b_xa80 = 6356755.2882;//短半轴
    public static final double f_xa80 = 0.003352810664747;//扁率：1 / 298.257223563
    public static final double e2_xa80 = 0.00669438499959;//第一偏心率的平方
    public static final double e22_xa80 = 0.00673950181947;//第二偏心率的平方
    public static final double c_xa80 = 6399596.65198801;//极曲率半径


    //大地坐标转空间直角坐标
    public static MyCoord GeoToKjzj_wgs84(MyCoord coord) {
        double N = a_gps / Math.sqrt(1 - e2_gps * Math.sin(coord.y * Math.PI / 180.0) * Math.sin(coord.y * Math.PI / 180.0));
        double X = N * Math.cos(coord.y * Math.PI / 180.0) * Math.cos(coord.x * Math.PI / 180.0);
        double Y = N * Math.cos(coord.y * Math.PI / 180.0) * Math.sin(coord.x * Math.PI / 180.0);
        double Z = N * (1.0 - e2_gps) * Math.sin(coord.y * Math.PI / 180.0);
        return new MyCoord(X, Y, Z);
    }

    //大地坐标转空间直角坐标
    public static MyCoord GeoToKjzj_bj54(MyCoord coord) {
        double N = a_bj54 / Math.sqrt(1 - e2_bj54 * Math.sin(coord.y * Math.PI / 180.0) * Math.sin(coord.y * Math.PI / 180.0));
        double X = N * Math.cos(coord.y * Math.PI / 180.0) * Math.cos(coord.x * Math.PI / 180.0);
        double Y = N * Math.cos(coord.y * Math.PI / 180.0) * Math.sin(coord.x * Math.PI / 180.0);
        double Z = N * (1.0 - e2_bj54) * Math.sin(coord.y * Math.PI / 180.0);
        return new MyCoord(X, Y, Z);
    }

    //大地坐标转空间直角坐标
    public static MyCoord GeoToKjzj_xa80(MyCoord coord) {
        double N = a_xa80 / Math.sqrt(1 - e2_xa80 * Math.sin(coord.y * Math.PI / 180.0) * Math.sin(coord.y * Math.PI / 180.0));
        double X = N * Math.cos(coord.y * Math.PI / 180.0) * Math.cos(coord.x * Math.PI / 180.0);
        double Y = N * Math.cos(coord.y * Math.PI / 180.0) * Math.sin(coord.x * Math.PI / 180.0);
        double Z = N * (1.0 - e2_xa80) * Math.sin(coord.y * Math.PI / 180.0);
        return new MyCoord(X, Y, Z);
    }

    public static MyCoord KjzjToGeo_wgs84(MyCoord coord) {
        double b = Math.atan(coord.z / Math.sqrt(coord.x * coord.x + coord.y * coord.y));
        b = diedaiB_wgs84(coord.x, coord.y, coord.z, b);
        double B = b;
        double L = Math.atan(coord.y / coord.x);
        if (L < 0) L = (180.0 + L * 180.0 / Math.PI) * Math.PI / 180.0;
        double x = L * 180.0 / Math.PI;
        double y = B * 180.0 / Math.PI;
        return new MyCoord(x, y, coord.z);
    }

    public static MyCoord KjzjToGeo_bj54(MyCoord coord) {
        double b = Math.atan(coord.z / Math.sqrt(coord.x * coord.x + coord.y * coord.y));
        b = diedaiB_bj54(coord.x, coord.y, coord.z, b);
        double B = b;
        double L = Math.atan(coord.y / coord.x);
        if (L < 0) L = (180.0 + L * 180.0 / Math.PI) * Math.PI / 180.0;
        double x = L * 180.0 / Math.PI;
        double y = B * 180.0 / Math.PI;
        return new MyCoord(x, y, coord.z);
    }

    public static MyCoord KjzjToGeo_xa80(MyCoord coord) {
        double b = Math.atan(coord.z / Math.sqrt(coord.x * coord.x + coord.y * coord.y));
        b = diedaiB_xa80(coord.x, coord.y, coord.z, b);
        double B = b;
        double L = Math.atan(coord.y / coord.x);
        if (L < 0) L = (180.0 + L * 180.0 / Math.PI) * Math.PI / 180.0;
        double x = L * 180.0 / Math.PI;
        double y = B * 180.0 / Math.PI;
        return new MyCoord(x, y, coord.z);
    }

    private static double diedaiB_wgs84(double x, double y, double z, double b) {
        double n = a_gps / Math.sqrt(1 - e2_gps * Math.sin(b) * Math.sin(b));
        double b1 = Math.atan((z + n * e2_gps * Math.sin(b)) / Math.sqrt(x * x + y * y));
        double d = b - b1;
        if (d < 0) d = -d;
        if (d < 0.0000001) {
            return b1;
        } else {
            return diedaiB_wgs84(x, y, z, b1);
        }
    }

    private static double diedaiB_bj54(double x, double y, double z, double b) {
        double n = a_bj54 / Math.sqrt(1 - e2_bj54 * Math.sin(b) * Math.sin(b));
        double b1 = Math.atan((z + n * e2_bj54 * Math.sin(b)) / Math.sqrt(x * x + y * y));
        double d = b - b1;
        if (d < 0) d = -d;
        if (d < 0.0000001) {
            return b1;
        } else {
            return diedaiB_bj54(x, y, z, b1);
        }
    }

    private static double diedaiB_xa80(double x, double y, double z, double b) {
        double n = a_xa80 / Math.sqrt(1 - e2_xa80 * Math.sin(b) * Math.sin(b));
        double b1 = Math.atan((z + n * e2_xa80 * Math.sin(b)) / Math.sqrt(x * x + y * y));
        double d = b - b1;
        if (d < 0) d = -d;
        if (d < 0.0000001) {
            return b1;
        } else {
            return diedaiB_xa80(x, y, z, b1);
        }
    }

    public static MyCoord Transform(MyCoord coord, double dx, double dy, double dz, double rx, double ry, double rz, double k) {
        double X = coord.x + dx + k * coord.x - ry / 3600 * Math.PI / 180 * coord.z + rz / 3600 * Math.PI / 180 * coord.y;
        double Y = coord.y + dy + k * coord.y + rx / 3600 * Math.PI / 180 * coord.z - rz / 3600 * Math.PI / 180 * coord.x;
        double Z = coord.z + dz + k * coord.z - rx / 3600 * Math.PI / 180 * coord.y + ry / 3600 * Math.PI / 180 * coord.x;
        return new MyCoord(X, Y, Z);
    }

    public static MyCoord GeoToGauss_bj54(MyCoord coord, int lon0) {
        double L = coord.x * Math.PI / 180;
        double B = coord.y * Math.PI / 180;
        double sinB = Math.sin(B);
        double Xlen = 111134.8611 * B * 180 / Math.PI - (32005.7799 * sinB + 133.9238 * sinB * sinB * sinB + 0.6976 * sinB * sinB * sinB * sinB * sinB + 0.0039 * sinB * sinB * sinB * sinB * sinB * sinB * sinB) * Math.cos(B);
        double t = Math.tan(B);
        double ng2 = Math.cos(B) * Math.cos(B) * e22_bj54;
        double m = Math.cos(B) * Math.PI / 180.0 * (L * 180 / Math.PI - lon0);
        double NN = c_bj54 / Math.sqrt(1 + ng2);

        double Y = Xlen + NN * t * (m * m / 2.0 + (5.0 - t * t + 9 * ng2 + 4 * ng2 * ng2) * m * m * m * m / 24.0 + (61.0 - 58.0 * t * t + t * t * t * t) * m * m * m * m * m * m / 720.0);
        double X = NN * (m + (1.0 - t * t + ng2) * m * m * m / 6.0 + (5.0 - 18.0 * t * t + t * t * t * t + 14.0 * ng2 - 58.0 * ng2 * t * t) * m * m * m * m * m / 120.0);
        X += 500000;
        return new MyCoord(X, Y, 0);
    }

    public static MyCoord GeoToGauss_xa80(MyCoord coord, int lon0) {
        double L = coord.x * Math.PI / 180;
        double B = coord.y * Math.PI / 180;
        double sinB = Math.sin(B);
        double Xlen = 111133.0047 * B * 180 / Math.PI - (32009.8575 * sinB + 133.9602 * sinB * sinB * sinB + 0.6976 * sinB * sinB * sinB * sinB * sinB + 0.0039 * sinB * sinB * sinB * sinB * sinB * sinB * sinB) * Math.cos(B);
        double t = Math.tan(B);
        double ng2 = Math.cos(B) * Math.cos(B) * e22_xa80;
        double m = Math.cos(B) * Math.PI / 180.0 * (L * 180 / Math.PI - lon0);
        double NN = c_xa80 / Math.sqrt(1 + ng2);

        double Y = Xlen + NN * t * (m * m / 2.0 + (5.0 - t * t + 9 * ng2 + 4 * ng2 * ng2) * m * m * m * m / 24.0 + (61.0 - 58.0 * t * t + t * t * t * t) * m * m * m * m * m * m / 720.0);
        double X = NN * (m + (1.0 - t * t + ng2) * m * m * m / 6.0 + (5.0 - 18.0 * t * t + t * t * t * t + 14.0 * ng2 - 58.0 * ng2 * t * t) * m * m * m * m * m / 120.0);
        X += 500000;
        return new MyCoord(X, Y, 0);
    }

    public static void GpsToPcs(MyCoord coord) {
        double N = a_gps / Math.sqrt(1 - e2_gps * Math.sin(coord.y * Math.PI / 180.0) * Math.sin(coord.y * Math.PI / 180.0));
        double X = N * Math.cos(coord.y * Math.PI / 180.0) * Math.cos(coord.x * Math.PI / 180.0);
        double Y = N * Math.cos(coord.y * Math.PI / 180.0) * Math.sin(coord.x * Math.PI / 180.0);
        double Z = N * (1.0 - e2_gps) * Math.sin(coord.y * Math.PI / 180.0);
        coord.x = X;
        coord.y = Y;
        coord.z = Z;
    }

    public static MyCoord GaussToGeo_bj54(MyCoord coord, int lon0) {
        double t, Itp, X0, Bf = 0, N, v, ll, W, L0;

        double iPI = 0.0174532925199433; ////3.1415926535898/180.0;

        double x = coord.y;
        double y = coord.x;
        L0 = lon0 * iPI;
        X0 = x * 0.000001;
        y = y - 500000;

        double a = a_bj54;
        double ep = e2_bj54;
        double ep1 = e22_bj54;
        if (X0 < 3)
            Bf = 9.04353301294 * X0 - 0.00000049604 * X0 * X0 - 0.00075310733 * X0 * X0 * X0 - 0.00000084307 * X0 * X0 * X0 * X0 - 0.00000426055 * X0 * X0 * X0 * X0 * X0 - 0.00000010148 * X0 * X0 * X0 * X0 * X0 * X0;
        else if (X0 < 6)
            Bf = 27.11115372595 + 9.02468257083 * (X0 - 3) - 0.00579740442 * (X0 - 3) * (X0 - 3) - 0.00043532572 * (X0 - 3) * (X0 - 3) * (X0 - 3) + 0.00004857285 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) + 0.00000215727 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) - 0.00000019399 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3);

        Bf = Bf * Math.PI / 180;
        t = Math.tan(Bf);
        Itp = ep1 * Math.cos(Bf) * Math.cos(Bf);
        W = Math.sqrt(1 - ep * Math.sin(Bf) * Math.sin(Bf));
        v = Math.sqrt(1 + ep1 * Math.cos(Bf) * Math.cos(Bf));
        N = a / W;
        double Lat = Bf - 0.5 * v * v * t * ((y / N) * (y / N) - (5 + 3 * t * t + Itp - 9 * Itp * t * t) * (y / N) * (y / N) * (y / N) * (y / N) / 12 + (61 + 90 * t * t + 45 * t * t * t * t) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) / 360);
        Lat = Lat / iPI;
        ll = ((y / N) - (1 + 2 * t * t + Itp) * (y / N) * (y / N) * (y / N) / 6 + (5 + 28 * t * t + 24 * t * t * t * t + 6 * Itp + 8 * Itp * t * t) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) / 120) / Math.cos(Bf);
        double Lon = (L0 + ll) / iPI;

        return new MyCoord(Lon, Lat, 0);
    }

    public static MyCoord GaussToGeo_xa80(MyCoord coord, int lon0) {
        double t, Itp, X0, Bf = 0, N, v, ll, W, L0;

        double iPI = 0.0174532925199433; ////3.1415926535898/180.0;

        double x = coord.y;
        double y = coord.x;
        L0 = lon0 * iPI;
        X0 = x * 0.000001;
        y = y - 500000;

        double a = a_xa80;
        double ep = e2_xa80;
        double ep1 = e22_xa80;
        if (X0 < 3)
            Bf = 9.04369066313 * X0 - 0.00000049618 * X0 * X0 - 0.00075325505 * X0 * X0 * X0 - 0.0000008433 * X0 * X0 * X0 * X0 - 0.00000426157 * X0 * X0 * X0 * X0 * X0 - 0.0000001015 * X0 * X0 * X0 * X0 * X0 * X0;
        else if (X0 < 6)
            Bf = 27.11162289465 + 9.02483657729 * (X0 - 3) - 0.00579850656 * (X0 - 3) * (X0 - 3) - 0.00043540029 * (X0 - 3) * (X0 - 3) * (X0 - 3) + 0.00004858357 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) + 0.00000215769 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) - 0.00000019404 * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3) * (X0 - 3);

        Bf = Bf * Math.PI / 180;
        t = Math.tan(Bf);
        Itp = ep1 * Math.cos(Bf) * Math.cos(Bf);
        W = Math.sqrt(1 - ep * Math.sin(Bf) * Math.sin(Bf));
        v = Math.sqrt(1 + ep1 * Math.cos(Bf) * Math.cos(Bf));
        N = a / W;
        double Lat = Bf - 0.5 * v * v * t * ((y / N) * (y / N) - (5 + 3 * t * t + Itp - 9 * Itp * t * t) * (y / N) * (y / N) * (y / N) * (y / N) / 12 + (61 + 90 * t * t + 45 * t * t * t * t) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) / 360);
        Lat = Lat / iPI;
        ll = ((y / N) - (1 + 2 * t * t + Itp) * (y / N) * (y / N) * (y / N) / 6 + (5 + 28 * t * t + 24 * t * t * t * t + 6 * Itp + 8 * Itp * t * t) * (y / N) * (y / N) * (y / N) * (y / N) * (y / N) / 120) / Math.cos(Bf);
        double Lon = (L0 + ll) / iPI;

        return new MyCoord(Lon, Lat, 0);
    }
}
