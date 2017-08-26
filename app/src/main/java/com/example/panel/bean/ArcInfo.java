package com.example.panel.bean;

import android.database.Cursor;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by server on 17-8-26.
 */

public class ArcInfo {
    //public static final Uri CONTENT_URI = Uri.parse("content://com.Mileseey.iMeter.sketch3/arcInfoTable");
    //public static final Uri CONTENT_URI2 = Uri.parse("content://com.Mileseey.iMeter.sketch3/arcInfoTable2");
    private String arcId;
    private String bmp_url;
    private String bottom;
    private String circleAngle;
    private String description;
    private String endX;
    private String endY;
    private String gradient;
    private String left;
    private String length;
    private String lineAngle;
    private String name;
    private String right;
    private String sketchId;
    private String startX;
    private String startY;
    private String top;

    public ArcInfo() {
    }

    public ArcInfo(Cursor paramCursor) {
        this.arcId = paramCursor.getString(1);
        this.sketchId = paramCursor.getString(2);
        this.startX = paramCursor.getString(3);
        this.startY = paramCursor.getString(4);
        this.endX = paramCursor.getString(5);
        this.endY = paramCursor.getString(6);
        this.left = paramCursor.getString(7);
        this.top = paramCursor.getString(8);
        this.right = paramCursor.getString(9);
        this.bottom = paramCursor.getString(10);
        this.lineAngle = paramCursor.getString(11);
        this.circleAngle = paramCursor.getString(12);
        this.length = paramCursor.getString(13);
    }

    public ArcInfo(RectF paramRectF, float paramFloat1, float paramFloat2) {
        this.startX = String.valueOf(paramRectF.left);
        this.startY = String.valueOf(paramRectF.top);
        this.endX = String.valueOf(paramRectF.right);
        this.endY = String.valueOf(paramRectF.bottom);
        this.lineAngle = String.valueOf(paramFloat1);
        this.circleAngle = String.valueOf(paramFloat2);
    }

    public String getArcId() {
        return this.arcId;
    }

    public String getBmpUrl() {
        return this.bmp_url;
    }

    public String getBottom() {
        return this.bottom;
    }

    public String getCircleAngle() {
        return this.circleAngle;
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

    public String getLeft() {
        return this.left;
    }

    public String getLength() {
        return this.length;
    }

    public String getLineAngle() {
        return this.lineAngle;
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        Path localPath = new Path();
        RectF localRectF = new RectF(Float.parseFloat(this.left), Float.parseFloat(this.top), Float.parseFloat(this.right), Float.parseFloat(this.bottom));
        float f1 = Float.parseFloat(this.lineAngle);
        float f2 = Float.parseFloat(this.circleAngle);
        localPath.addArc(localRectF, f1 + (360.0F - f2), f2);
        return localPath;
    }

    public String getRight() {
        return this.right;
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

    public String getTop() {
        return this.top;
    }

    public void setArcId(String paramString) {
        this.arcId = paramString;
    }

    public void setBmpUrl(String paramString) {
        this.bmp_url = paramString;
    }

    public void setBottom(String paramString) {
        this.bottom = paramString;
    }

    public void setCircleAngle(String paramString) {
        this.circleAngle = paramString;
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

    public void setLeft(String paramString) {
        this.left = paramString;
    }

    public void setLength(String paramString) {
        this.length = paramString;
    }

    public void setLineAngle(String paramString) {
        this.lineAngle = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setRight(String paramString) {
        this.right = paramString;
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

    public void setTop(String paramString) {
        this.top = paramString;
    }
}
