package com.example.panel.bean;

import android.database.Cursor;

/**
 * Created by server on 17-8-14.
 */

public class SketchDataInfo {
    private String bmp_url;
    private String dataId;
    private String description;
    private String gradient;
    private String itemName;
    private String lineId;
    private String measureData;
    private String sketchId;

    public SketchDataInfo()
    {
    }

    public SketchDataInfo(Cursor paramCursor)
    {
        this.dataId = paramCursor.getString(1);
        this.lineId = paramCursor.getString(2);
        this.sketchId = paramCursor.getString(3);
        this.itemName = paramCursor.getString(4);
        this.description = paramCursor.getString(5);
        this.measureData = paramCursor.getString(6);
        this.gradient = paramCursor.getString(7);
        this.bmp_url = paramCursor.getString(8);
    }

    public String getBmpUrl()
    {
        return this.bmp_url;
    }

    public String getDataId()
    {
        return this.dataId;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getGradient()
    {
        return this.gradient;
    }

    public String getItemName()
    {
        return this.itemName;
    }

    public String getLineId()
    {
        return this.lineId;
    }

    public String getMeasureData()
    {
        return this.measureData;
    }

    public String getSketchId()
    {
        return this.sketchId;
    }

    public void setBmpUrl(String paramString)
    {
        this.bmp_url = paramString;
    }

    public void setDataId(String paramString)
    {
        this.dataId = paramString;
    }

    public void setDescription(String paramString)
    {
        this.description = paramString;
    }

    public void setGradient(String paramString)
    {
        this.gradient = paramString;
    }

    public void setItemName(String paramString)
    {
        this.itemName = paramString;
    }

    public void setLineId(String paramString)
    {
        this.lineId = paramString;
    }

    public void setMeasureData(String paramString)
    {
        this.measureData = paramString;
    }

    public void setSketchId(String paramString)
    {
        this.sketchId = paramString;
    }
}
