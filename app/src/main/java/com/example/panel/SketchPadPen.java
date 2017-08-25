package com.example.panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.panel.bean.LineInfo;
import com.example.panel.bean.SketchDataInfo;

import java.util.ArrayList;


public class SketchPadPen implements ISketchPadTool
{
    private static final float TOUCH_TOLERANCE = 4.0f;

    protected static int GRID_SIZE = 10;
    protected static int GRID_WIDTH = 30;
    protected static int mStartX;
    protected static int mStartY;

    private float m_curX = 0.0f;
    private float m_curY = 0.0f;
    private boolean m_hasDrawn = false;
    private Path m_penPath = new Path();
    private Paint m_penPaint = new Paint();
    private Paint paintEndOutCircle = new Paint();
    private Paint paintFontBackground = new Paint();
    private Paint paintInCircle = new Paint();
    private Paint paintStartOutCircle = new Paint();
    private Paint pointPaint = new Paint();
    private Paint textLinePaint = new Paint();

    private SketchPadView sketchPadView;

    private float sX = 0.0F;
    private float sY = 0.0F;

    private float eX = 0.0F;
    private float eY = 0.0F;
    private float gX = 0.0F;
    private float gY = 0.0F;
    
    public SketchPadPen(int penSize, int penColor)
    {
        m_penPaint.setAntiAlias(true);
        m_penPaint.setDither(true);
        m_penPaint.setColor(penColor);
        m_penPaint.setStrokeWidth(penSize);
        m_penPaint.setStyle(Paint.Style.STROKE);
        m_penPaint.setStrokeJoin(Paint.Join.ROUND);
        m_penPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public SketchPadPen(int penSize, int penColor, SketchPadView sketchPadView) {
        m_penPaint.setAntiAlias(true);
        m_penPaint.setColor(Color.rgb(240, 131, 0));
        m_penPaint.setStrokeWidth(7.0F);
        m_penPaint.setStyle(Paint.Style.STROKE);
        m_penPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(5.0F);
        pointPaint.setColor(-65536);
        textLinePaint.setColor(Color.rgb(240, 131, 0));
        textLinePaint.setStrokeWidth(7.0F);
        textLinePaint.setAntiAlias(true);
        textLinePaint.setStyle(Paint.Style.STROKE);
        textLinePaint.setStrokeJoin(Paint.Join.ROUND);
        paintEndOutCircle.setStrokeWidth(5.0F);
        paintEndOutCircle.setStyle(Paint.Style.STROKE);
        paintEndOutCircle.setColor(0);
        paintInCircle.setStrokeWidth(5.0F);
        paintInCircle.setStyle(Paint.Style.STROKE);
        paintInCircle.setColor(Color.rgb(240, 131, 0));
        paintStartOutCircle.setStrokeWidth(5.0F);
        paintStartOutCircle.setStyle(Paint.Style.STROKE);
        paintStartOutCircle.setColor(0);
        paintFontBackground = new Paint();
        paintFontBackground.setAntiAlias(true);
        paintFontBackground.setColor(-1);
        paintFontBackground.setStyle(Paint.Style.STROKE);
        paintFontBackground.setStrokeWidth(4.0F);
        paintFontBackground.setTextSize(20.0F);
        this.sketchPadView = sketchPadView;
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
            canvas.drawPath(m_penPath, m_penPaint);
        }
    }
    
    @Override
    public boolean hasDraw()
    {
        return m_hasDrawn;
    }

    @Override
    public void cleanAll()
    {
        m_penPath.reset();
    }

    @Override
    public void touchDown(float x, float y)
    {
        /*m_penPath.reset();
        m_penPath.moveTo(x, y);
        m_curX = x;
        m_curY = y;*/
        changeXY(x, y);
        //setPaintMode(this.m_penPaint, 1);
        sX = (mStartX + (1.0F + gX) * GRID_WIDTH);
        sY = (mStartY + (1.0F + gY) * GRID_WIDTH);
        eX = (mStartX + (1.0F + gX) * GRID_WIDTH);
        eY = (mStartY + (1.0F + gY) * GRID_WIDTH);
        sketchPadView.setPointX(sX);
        sketchPadView.setPointY(sY);
        paintStartOutCircle.setColor(Color.rgb(240, 131, 0));
        paintEndOutCircle.setColor(Color.rgb(240, 131, 0));
    }

    @Override
    public void touchMove(float x, float y)
    {
        /*float dx = Math.abs(x - m_curX);
        float dy = Math.abs(y - m_curY);
        
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_penPath.quadTo(m_curX, m_curY, (x + m_curX) / 2, (y + m_curY) / 2);
            
            m_hasDrawn = true;
            m_curX = x;
            m_curY = y;
        }*/
        m_hasDrawn = true;
        changeXY(x, y);
        eX = (mStartX + (1.0F + gX) * GRID_WIDTH);
        eY = (mStartY + (1.0F + gY) * GRID_WIDTH);
        paintEndOutCircle.setColor(Color.rgb(240, 131, 0));
        paintStartOutCircle.setColor(0);
    }

    @Override
    public void touchUp(float x, float y) {
        //m_penPath.lineTo(x, y);
        changeXY(x, y);
        setPaintMode(m_penPaint, 0);
        eX = (mStartX + (1.0F + gX) * GRID_WIDTH);
        eY = (mStartY + (1.0F + gY) * GRID_WIDTH);
        paintEndOutCircle.setColor(0);
        if ((sX == eX) && (sY == eY)) {
            paintEndOutCircle.setColor(Color.rgb(240, 131, 0));
            paintStartOutCircle.setColor(0);
        }

        LineInfo localLineInfo = new LineInfo();
        localLineInfo.setStartX(String.valueOf(sX));
        localLineInfo.setStartY(String.valueOf(sY));
        localLineInfo.setEndX(String.valueOf(eX));
        localLineInfo.setEndY(String.valueOf(eY));
        localLineInfo.setLength("");
        ArrayList localArrayList1 = sketchPadView.getLineList();
        localArrayList1.add(localLineInfo);
        SketchDataInfo localSketchDataInfo1 = new SketchDataInfo();
        sketchPadView.getDataList().add(localSketchDataInfo1);
    }

    public void changeXY(float X, float Y){
        float f1 = GRID_WIDTH - (X - mStartX) % GRID_WIDTH;
        float f2 = GRID_WIDTH - (Y - mStartY) % GRID_WIDTH;
        if (f1 < GRID_WIDTH / 2){
            gX = (int)((X - mStartX) / GRID_WIDTH);
        }else{
            gX = (-1 + (int)((X - mStartX) / GRID_WIDTH));
        }

        if(f2 >= GRID_WIDTH / 2 ){
            gY = (int)((Y - mStartY) / GRID_WIDTH);
        }else{
            gY = (-1 + (int)((Y - mStartY) / GRID_WIDTH));
        }
    }

    @Override
    public void DrawOneLine(Canvas canvas) {
        if(canvas != null){
            if ((sX == eX) && (sY == eY)){
                canvas.drawCircle(sX, this.sY, 1 + GRID_WIDTH, paintStartOutCircle);
            }else{
                canvas.drawLine(sX, sY, eX, eY, m_penPaint);
                canvas.drawCircle(sX, sY, 8.0F, paintInCircle);
                canvas.drawCircle(eX, eY, 8.0F, paintInCircle);
                canvas.drawCircle(sX, sY, 1 + GRID_WIDTH, paintStartOutCircle);
                canvas.drawCircle(eX, eY, 1 + GRID_WIDTH, paintEndOutCircle);
            }
        }
    }

    public void DrawFinishLine(Canvas canvas, ArrayList<LineInfo> arrayList){
        int size = arrayList.size();
        for(int i = 0; i < size; i++){
            LineInfo lineInfo = (LineInfo)arrayList.get(i);
            Float startX = Float.valueOf(Float.parseFloat(lineInfo.getStartX()));
            Float startY = Float.valueOf(Float.parseFloat(lineInfo.getStartY()));
            Float endX = Float.valueOf(Float.parseFloat(lineInfo.getEndX()));
            Float endY = Float.valueOf(Float.parseFloat(lineInfo.getEndY()));
            String str1 = lineInfo.getBmpUrl();
            String str2 = lineInfo.getLength();
            String str3 = lineInfo.getGradient();
            String str4 = "";
            String str5 = "";
            if ((!"".equals(str2)) && (str2 != null))
                str4 = str2;
            if ((!"".equals(str3)) && (str3 != null))
                str4 = str4 + " " + str3;
            if ((!"".equals(lineInfo.getName())) && (lineInfo.getName() != null))
                str5 = lineInfo.getName();

            Path localPath2;
            if ((!"".equals(str4))){
                localPath2 = new Path();
                Paint localPaint3 = new Paint();
                localPaint3.setAntiAlias(true);
                localPaint3.setStrokeWidth(2.0F);
                if (startX <= endX){
                    localPath2.setLastPoint(startX, startY);
                    localPath2.lineTo(endX, endY);
                }else{
                    localPath2.setLastPoint(endX, endY);
                    localPath2.lineTo(startX, startY);
                }
                Double localDouble2 = Double.valueOf(Math.hypot(startX - endX, startY - endY));
                localPaint3.setTextSize(20.0F);
                localPaint3.setColor(-16777216);
            }

            setPaintMode(m_penPaint, 0);
            canvas.drawLine(startX, startY, endX, endY, this.m_penPaint);
            canvas.drawCircle(startX, startY, 8.0F, this.paintInCircle);
            canvas.drawCircle(endX, endY, 8.0F, this.paintInCircle);

        }
    }

    private void setPaintMode(Paint paint, int mode){
        if (mode == 0) {
            paint.setPathEffect(new DashPathEffect(new float[] { 20.0F, 7.0F }, 0.0F));
            paint.setColor(-16777216);
            paint.setStrokeWidth(7.0F);
        }else{
            paint.setPathEffect(new DashPathEffect(new float[] { 20.0F, 0.0F }, 0.0F));
            paint.setColor(Color.rgb(240, 131, 0));
            paint.setStrokeWidth(7.0F);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }
    }
}
