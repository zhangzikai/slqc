package com.tdgeos.lib;

public class MyCoord {
    public double x;
    public double y;
    public double z;

    public MyCoord() {
        x = 0;
        y = 0;
        z = 0;
    }

    public MyCoord(double tx, double ty, double tz) {
        x = tx;
        y = ty;
        z = tz;
    }

    public MyCoord(MyCoord pt) {
        if (pt != null) {
            x = pt.x;
            y = pt.y;
            z = pt.z;
        }
    }
}
