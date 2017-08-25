package com.example.panel.bean;

import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;

/**
 * Created by server on 17-8-14.
 */

public class LineInfo {
    public static final Uri CONTENT_URI = Uri.parse("content://com.Mileseey.iMeter.sketch3/lineInfoTable");
    public static final Uri CONTENT_URI2 = Uri.parse("content://com.Mileseey.iMeter.sketch3/lineInfoTable2");
    private String bmp_url;
    private String description;
    private String endX;
    private String endY;
    private String gradient;
    private String length;
    private String lineId;
    private String name;
    private String sketchId;
    private String startX;
    private String startY;

    public LineInfo() {
    }

    public LineInfo(Cursor cursor) {
        this.lineId = cursor.getString(1);
        this.sketchId = cursor.getString(2);
        this.startX = cursor.getString(3);
        this.startY = cursor.getString(4);
        this.endX = cursor.getString(5);
        this.endY = cursor.getString(6);
        this.length = cursor.getString(7);
    }

    public String getBmpUrl() {
        return this.bmp_url;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEndX() {
        return this.endX;
    }

    public String getEndY() {
        return this.endY;
    }

    public String getGradient() {
        return this.gradient;
    }

    public String getLength() {
        return this.length;
    }

    public String getLineId() {
        return this.lineId;
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        Path path = new Path();
        float f1 = Float.parseFloat(this.startX);
        float f2 = Float.parseFloat(this.startY);
        float f3 = Float.parseFloat(this.endX);
        float f4 = Float.parseFloat(this.endY);
        if ((f1 == f3) && (f2 < f4)) {
            path.moveTo(f3, f4);
            path.lineTo(f1, f2);
        }else if ((f1 == f3) && (f2 >= f4)) {
            path.moveTo(f1, f2);
            path.lineTo(f3, f4);
        }else if ((f1 >= f3) && (f2 >= f4)) {
            path.moveTo(f3, f4);
            path.lineTo(f1, f2);
        }else if ((f1 < f3) && (f2 > f4)) {
            path.moveTo(f1, f2);
            path.lineTo(f3, f4);
        }else if ((f1 >= f3) && (f2 <= f4)) {
            path.moveTo(f3, f4);
            path.lineTo(f1, f2);
        }else {
            path.moveTo(f1, f2);
            path.lineTo(f3, f4);
        }

        return path;
    }

    public String getSketchId() {
        return this.sketchId;
    }

    public String getStartX() {
        return this.startX;
    }

    public String getStartY() {
        return this.startY;
    }

    public void setBmpUrl(String paramString) {
        this.bmp_url = paramString;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public void setEndX(String paramString) {
        this.endX = paramString;
    }

    public void setEndY(String paramString) {
        this.endY = paramString;
    }

    public void setGradient(String paramString) {
        this.gradient = paramString;
    }

    public void setLength(String paramString) {
        this.length = paramString;
    }

    public void setLineId(String paramString) {
        this.lineId = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setSketchId(String paramString) {
        this.sketchId = paramString;
    }

    public void setStartX(String paramString) {
        this.startX = paramString;
    }

    public void setStartY(String paramString) {
        this.startY = paramString;
    }
}
