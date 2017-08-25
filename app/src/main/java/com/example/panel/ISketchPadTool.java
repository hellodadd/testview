package com.example.panel;


import android.graphics.Canvas;

import com.example.panel.bean.LineInfo;

import java.util.ArrayList;

public interface ISketchPadTool
{
    public void draw(Canvas canvas);
    public boolean hasDraw();
    public void cleanAll();
    public void touchDown(float x, float y);
    public void touchMove(float x, float y);
    public void touchUp(float x, float y);
    public void DrawOneLine(Canvas canvas);
    public void DrawFinishLine(Canvas canvas, ArrayList<LineInfo> arrayList);
}
