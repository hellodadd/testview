package com.example.panel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.example.panel.bean.ArcInfo;
import com.example.panel.bean.LineInfo;
import com.example.panel.bean.NoteInfo;
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

    private Point OldArcPoint = new Point();
    private Point OldCirclePoint = new Point();
    private float OldCircleRadius;
    private float cX = 0.0F;
    private float cY = 0.0F;
    private float circleCircleAngle = 0.0F;
    private float circleLineAngle = 0.0F;
    private RectF circleRectF = new RectF();

    private String lineText;
    
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

    public float getsX() {
        return this.sX;
    }

    public float getsY() {
        return this.sY;
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
    public void touchDown(float x, float y) {
        changeXY(x, y);
        setPaintMode(m_penPaint, 1);
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
    public void touchMove(float x, float y) {
        changeXY(x, y);
        //Log.e("ZWB","touch move------");
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
        float f1 = this.eX;
        float f2 = this.eY;
        eX = (mStartX + (1.0F + gX) * GRID_WIDTH);
        eY = (mStartY + (1.0F + gY) * GRID_WIDTH);
        paintEndOutCircle.setColor(0);
        if ((sX == eX) && (sY == eY)) {
            paintEndOutCircle.setColor(Color.rgb(240, 131, 0));
            paintStartOutCircle.setColor(0);
        }

        if ("Paint Line".equals(this.sketchPadView.getOperateStatus())) {
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
        }else if("Paint Arc".equals(this.sketchPadView.getOperateStatus())){
            Point localPoint1 = new Point(0,0);
            Point localPoint2 = new Point();
            Point localPoint3 = new Point();
            localPoint2.x = ((int)this.sX);
            localPoint2.y = ((int)this.sY);
            localPoint3.x = ((int)this.eX);
            localPoint3.y = ((int)this.eY);
            ArcInfo localArcInfo = new ArcInfo();
            float f3 = GetLineLength(this.sX, this.sY, this.eX, this.eY) / 2.0F;
            Point localPoint4 = Circle_Center2(localPoint2, localPoint3, f3, 1);
            if ((localPoint4.x != 0) || (localPoint4.y != 0)) {
                localPoint1.x = localPoint4.x;
                localPoint1.y = localPoint4.y;
            }
            this.circleLineAngle = GetLineAngle(this.sX, this.sY, this.eX, this.eY);
            this.circleRectF = Circle_Rect(localPoint1, f3);
            this.circleCircleAngle = getCircleAngle(this.sX, this.sY, this.eX, this.eY, f3);
            localArcInfo.setStartX(String.valueOf(sX));
            localArcInfo.setStartY(String.valueOf(this.sY));
            localArcInfo.setLeft(String.valueOf(this.circleRectF.left));
            localArcInfo.setTop(String.valueOf(this.circleRectF.top));
            localArcInfo.setRight(String.valueOf(this.circleRectF.right));
            localArcInfo.setBottom(String.valueOf(this.circleRectF.bottom));
            localArcInfo.setLineAngle(String.valueOf(this.circleLineAngle));
            localArcInfo.setCircleAngle(String.valueOf(this.circleCircleAngle));
            localArcInfo.setLength("");
            ArrayList localArrayList2 = this.sketchPadView.getArcList();
            localArrayList2.add(localArcInfo);
            SketchDataInfo localSketchDataInfo2 = new SketchDataInfo();
            this.sketchPadView.getDataList().add(localSketchDataInfo2);
            localArcInfo.setEndX(String.valueOf(this.eX));
            localArcInfo.setEndY(String.valueOf(this.eY));
        }
    }

    public void changeXY(float X, float Y){
        //Log.e("zwb","change  X = " + X + " Y = " + Y + " mSartX = " + mStartX);
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

    public void DrawOneArc(Canvas canvas){
        Point localPoint1 = new Point(0,0);
        Point localPoint2 = new Point();
        Point localPoint3 = new Point();
        localPoint2.x = (int)this.sX;
        localPoint2.y = (int)this.sY;
        localPoint3.x = (int)this.eX;
        localPoint3.y = (int)this.eY;

        if (canvas != null){
            if ((this.sX == this.eX) && (this.sY == this.eY)) {
                //canvas.drawCircle(this.sX, this.sY, 1 + GRID_WIDTH, this.paintStartOutCircle);
                //this.sketchPadView.setOneLineComplete("Î´»­Íê");
            }
            float f = GetLineLength(this.sX, this.sY, this.eX, this.eY) / 2.0F;
            Point localPoint4 = Circle_Center2(localPoint2, localPoint3, f, 1);
            //Log.e("zwb","drawonearc------ localPoint4.x = " + localPoint4.x + " localPoint4.y= " + localPoint4.y);
            if ((localPoint4.x != 0) || (localPoint4.y != 0)) {
                localPoint1.x = localPoint4.x;
                localPoint1.y = localPoint4.y;
            }
            this.circleLineAngle = GetLineAngle(this.sX, this.sY, this.eX, this.eY);
            this.circleRectF = Circle_Rect(localPoint1, f);
            this.circleCircleAngle = getCircleAngle(this.sX, this.sY, this.eX, this.eY, f);

            Log.e("zwb", "circleRectF = " + circleRectF.left + " circleRectF = " + circleRectF.top +
                  " circleRectF = " + circleRectF.right + " circleRectF = " + circleRectF.bottom);
            float d2 = 360.0F - this.circleCircleAngle + this.circleLineAngle;
            Log.e("zwb", " 222222 =  " + d2 + " circleCircleAngle = " + circleCircleAngle);
            canvas.drawArc(this.circleRectF, 360.0F - this.circleCircleAngle + this.circleLineAngle,
                    this.circleCircleAngle, false, this.m_penPaint);
            //this.sketchPadView.setOneLineComplete("»­Íê");
        }
    }

    public void DrawFinishArc(Canvas canvas, int paramInt, ArrayList<ArcInfo> infoArrayList){
        for(int i = 0; i < infoArrayList.size(); i++){
            ArcInfo localArcInfo = (ArcInfo)infoArrayList.get(i);
            Float localFloat1 = Float.valueOf(Float.parseFloat(localArcInfo.getStartX()));
            Float localFloat2 = Float.valueOf(Float.parseFloat(localArcInfo.getStartY()));
            Float localFloat3 = Float.valueOf(Float.parseFloat(localArcInfo.getEndX()));
            Float localFloat4 = Float.valueOf(Float.parseFloat(localArcInfo.getEndY()));
            String str1 = localArcInfo.getBmpUrl();
            String str2 = localArcInfo.getLeft();
            String str3 = localArcInfo.getTop();
            String str4 = localArcInfo.getRight();
            String str5 = localArcInfo.getBottom();
            RectF localRectF = new RectF(Float.parseFloat(str2), Float.parseFloat(str3), Float.parseFloat(str4), Float.parseFloat(str5));
            float f1 = Float.parseFloat(localArcInfo.getLineAngle());
            float f2 = Float.parseFloat(localArcInfo.getCircleAngle());
            String str6 = localArcInfo.getLength();
            String str7 = "";
            String str8 = "";
            if ((!"".equals(str6)) && (str6 != null)) {
                str7 = str6;
            }
            if ((!"".equals(localArcInfo.getName())) && (localArcInfo.getName() != null)) {
                str8 = localArcInfo.getName();
            }
            float f3 = (float)Math.hypot(localFloat1.floatValue() - localFloat3.floatValue(), localFloat2.floatValue() - localFloat4.floatValue());
            float f4 = GetLineAngle(localFloat1.floatValue(), localFloat2.floatValue(), localFloat3.floatValue(), localFloat4.floatValue());
            Point localPoint = getArcCenter(localRectF, f1, f2);
            GraphicsUtils.LineFloat localLineFloat = GetQieLine(canvas, localPoint.x, localPoint.y, f3 / 2.0F, f4);
            Path localPath = new Path();
            localPath.setLastPoint(localLineFloat.x1, localLineFloat.y1);
            localPath.lineTo(localLineFloat.x2, localLineFloat.y2);
            if ((!"".equals(str7))) {
                Paint localPaint3 = new Paint();
                localPaint3.setAntiAlias(true);
                localPaint3.setStrokeWidth(2.0F);
                localPaint3.setTextSize(20.0F);
                localPaint3.setColor(-16777216);
                /*
                float f6 = this.sketchPadView.getHOffset(str7, localPaint3, f3);
                paramCanvas.drawTextOnPath(str7, localPath, f6, -10.0F, this.paintFontBackground);
                paramCanvas.drawTextOnPath(str7, localPath, f6, -10.0F, localPaint3);
                */
            }

            if ((!"".equals(str8))) {
                Paint localPaint1 = new Paint();
                localPaint1.setAntiAlias(true);
                localPaint1.setTextSize(20.0F);
                localPaint1.setColor(-16777216);
                SketchPadView localSketchPadView = this.sketchPadView;
                double d = f3;
                /*float f5 = localSketchPadView.getHOffset(str8, localPaint1, d);
                Paint localPaint2 = this.paintFontBackground;
                paramCanvas.drawTextOnPath(str8, localPath, f5, 25.0F, localPaint2);
                paramCanvas.drawTextOnPath(str8, localPath, f5, 25.0F, localPaint1);*/
            }

            if (paramInt == i) {
                setPaintMode(this.m_penPaint, 1);
            }else{
                setPaintMode(this.m_penPaint, 0);
            }

            canvas.drawArc(localRectF, f1 + (360.0F - f2), f2, false, this.m_penPaint);
            //DrawPathImage(canvas, localLineFloat.x1, localLineFloat.y1, localLineFloat.x2, localLineFloat.y2, str1);
        }
    }

    public void DrawFinishNote(Canvas canvas){
        if(canvas != null){
            Paint localPaint = new Paint();
            localPaint.setAntiAlias(true);
            localPaint.setStrokeWidth(2.0F);
            localPaint.setTextSize(30.0F);
            localPaint.setColor(-16777216);
            ArrayList localArrayList = sketchPadView.getNoteList();
            for (int i = 0; i < localArrayList.size(); i++) {
                NoteInfo localNoteInfo = (NoteInfo)localArrayList.get(i);
                float f1 = Float.parseFloat(localNoteInfo.getStartX());
                float f2 = Float.parseFloat(localNoteInfo.getStartY());
                canvas.drawText(localNoteInfo.getNote(), f1, f2, localPaint);
            }
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


    public String getLineText() {
        return lineText;
    }

    public void setLineText(String text) {
        lineText = text;
    }

    private float GetLineLength(float x1, float y1, float x2, float y2) {
        return (float)Math.hypot(x1 - x2, y1 - y2);
    }

    private Point Circle_Center2(Point paramPoint1, Point paramPoint2, double paramDouble, int paramInt) {
        Point localPoint = new Point();
        localPoint.set(paramPoint1.x + (paramPoint2.x - paramPoint1.x) / 2, paramPoint1.y + (paramPoint2.y - paramPoint1.y) / 2);
        return localPoint;
    }

    private RectF Circle_Rect(Point paramPoint, double paramDouble) {
        RectF localRectF = new RectF();
        localRectF.left = (int)(paramPoint.x - paramDouble);
        localRectF.right = (int)(paramDouble + paramPoint.x);
        localRectF.top = (int)(paramPoint.y - paramDouble);
        localRectF.bottom = (int)(paramDouble + paramPoint.y);
        return localRectF;
    }

    public float GetLineAngle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4){
        double d = Math.atan((paramFloat4 - paramFloat2) / (paramFloat3 - paramFloat1));
        if ((paramFloat3 >= paramFloat1) && (paramFloat4 >= paramFloat2)) {
            d = 180.0D * (d / 3.141592653589793D);
        }else if ((paramFloat3 < paramFloat1) && (paramFloat4 >= paramFloat2)){
            d = 180.0D + 180.0D * (d / 3.141592653589793D);
        }else if ((paramFloat3 < paramFloat1) && (paramFloat4 < paramFloat2)){
            d = 180.0D + 180.0D * (d / 3.141592653589793D);
        }else if ((paramFloat3 < paramFloat1) || (paramFloat4 >= paramFloat2)){
            d = 360.0D + 180.0D * (d / 3.141592653589793D);
        }

        return (float) d;
    }

    private float getCircleAngle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, double paramDouble) {
        double d1 = Math.hypot(paramFloat3 - paramFloat1, paramFloat4 - paramFloat2);
        double d2 = (paramDouble * paramDouble + paramDouble * paramDouble - d1 * d1) / (paramDouble * (2.0D * paramDouble));

        if (d2 > 1.0D) {
            d2 = 1.0D;
        }
        if (d2 < -1.0D){
            d2 = -1.0D;
        }

        float f = (float)(180.0F * (float)Math.acos(d2) / 3.141592653589793D);

        return f;
    }

    public Point getArcCenter(RectF paramRectF, float paramFloat1, float paramFloat2) {
        Point localPoint = new Point(0, 0);
        int i = (int)((paramRectF.right - paramRectF.left) / 2.0F);
        int j = (int)((paramRectF.bottom - paramRectF.top) / 2.0F);
        int k = (int)(i * Math.cos(angletoArc(paramFloat1 + (360.0F - paramFloat2) + paramFloat2 / 2.0F)));
        int m = (int)(j * Math.sin(angletoArc(paramFloat1 + (360.0F - paramFloat2) + paramFloat2 / 2.0F)));
        int n = (int)(paramRectF.left + i + k);
        int i1 = (int)(paramRectF.top + j + m);
        localPoint.set(n, i1);
        return localPoint;
    }

    private float angletoArc(float paramFloat) {
        return (float)(3.141592653589793D * paramFloat / 180.0D);
    }

    public GraphicsUtils.LineFloat GetQieLine(Canvas paramCanvas, float paramFloat1,
                                              float paramFloat2, float paramFloat3, float paramFloat4)
    {
        GraphicsUtils.LineFloat localLineFloat = new GraphicsUtils.LineFloat();
        float f1 = (float)(paramFloat1 - paramFloat3 * Math.cos(angletoArc(paramFloat4)));
        float f2 = (float)(paramFloat2 - paramFloat3 * Math.sin(angletoArc(paramFloat4)));
        float f3 = (float)(paramFloat1 + paramFloat3 * Math.cos(angletoArc(paramFloat4)));
        float f4 = (float)(paramFloat2 + paramFloat3 * Math.sin(angletoArc(paramFloat4)));
        localLineFloat.x1 = f1;
        localLineFloat.y1 = f2;
        localLineFloat.x2 = f3;
        localLineFloat.y2 = f4;
        return localLineFloat;
    }

}
