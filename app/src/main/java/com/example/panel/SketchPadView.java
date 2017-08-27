package com.example.panel;


import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.panel.bean.ArcInfo;
import com.example.panel.bean.LineInfo;
import com.example.panel.bean.NoteInfo;
import com.example.panel.bean.SketchDataInfo;



public class SketchPadView extends View implements IUndoCommand {

    public static final int DRAW_ARC = 2;
    public static final int DRAW_LINE = 1;
    public static final int DRAW_NONE = 0;
	public static final int DRAW_TEXT = 3;

	public static final int STROKE_NONE = 0;
	public static final int STROKE_PEN = 1;
	public static final int STROKE_ERASER = 2;
	public static final int UNDO_SIZE = 20;

	private boolean m_isEnableDraw = true;
	private boolean m_isDirty = false;
	private boolean m_isTouchUp = false;
    private boolean m_isTouchMove = false;
	private boolean m_isSetForeBmp = false;
	private int m_bkColor = Color.WHITE;

	private int m_strokeType = STROKE_PEN;
	private int m_strokeColor = Color.BLACK;
	private int m_penSize = CommonDef.MIDDLE_PEN_WIDTH;
	private int m_eraserSize = CommonDef.MIDDLE_ERASER_WIDTH;
	private int m_canvasWidth = 100;
	private int m_canvasHeight = 100;
	private boolean m_canClear = true;

	private Paint m_bitmapPaint = null;
	private Bitmap m_foreBitmap = null;
	private SketchPadUndoStack m_undoStack = null;
	private Bitmap m_tempForeBitmap = null;
	private Bitmap m_bkBitmap;
	private Canvas m_canvas = null;
	private ISketchPadTool m_curTool = null;
	private ISketchPadCallback m_callback = null;
	private boolean isfirst = false;
	private Context context;
	private RectF mImgDesRect;
	private Paint paint;
	private Paint paintRect;
    public int gridHeightNum = 40;
    public int gridWidthNum = 35;

    private float pointX = 0.0F;
    private float pointY = 0.0F;

    private ArrayList<LineInfo> lineList = new ArrayList();
    private ArrayList<SketchDataInfo> dataList = new ArrayList();
    private ArrayList<ArcInfo> arcList = new ArrayList();
    private ArrayList<NoteInfo> noteList = new ArrayList();

    private SketchPadPen mPadPen;

    private int paintSwitch = 3;

    private String operateStatusString = "";

    private ReceiveActivity measureActivity;

	public SketchPadView(Context context) {
		this(context, null);
	}

	public SketchPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context =context;
		isfirst = true;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getAssets().open(
					"paper.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_bkBitmap = bitmap;
		initialize();
	}

	public SketchPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

    protected void onDraw(Canvas canvas) {
        draw(canvas, true);
    }

    private int getPaintSwitch(){
        return paintSwitch;
    }

    private void setPaintSwitch(int type){
        paintSwitch = type;
    }

    public void setMeasureActivity(ReceiveActivity activity) {
        Log.e("zwb", "settt ----" + activity);
        this.measureActivity = activity;
        Log.e("zwb", "settt -5555---" + measureActivity);
    }

	public boolean isDirty() {
		return m_isDirty;
	}

	public void setDrawStrokeEnable(boolean isEnable) {
		m_isEnableDraw = isEnable;
	}

	public void setBkColor(int color) {
		if (m_bkColor != color) {
			m_bkColor = color;
			invalidate();
		}
	}

	public void setForeBitmap(Bitmap foreBitmap) {
		Log.v("hef", "-----------setForeBitmap");
		if (foreBitmap != m_foreBitmap && null != foreBitmap) {
			// Recycle the bitmap.
			if (null != m_foreBitmap) {
				m_foreBitmap.recycle();
			}

			m_isSetForeBmp = true;

			// Remember the temporary fore bitmap.
			m_tempForeBitmap = BitmapUtil.duplicateBitmap(foreBitmap);

			// Here create a new fore bitmap to avoid crashing when set bitmap
			// to canvas.
			m_foreBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
			if (null != m_foreBitmap && null != m_canvas) {
				m_canvas.setBitmap(m_foreBitmap);
			}

			m_canClear = true;

			invalidate();
		}
	}

	public Bitmap getForeBitmap() {
		return m_foreBitmap;
	}

	public void setBkBitmap(Bitmap bmp) {
		if (m_bkBitmap != bmp) {
			m_bkBitmap = bmp;
			invalidate();
		}
	}

	public Bitmap getBkBitmap() {
		return m_bkBitmap;
	}

	public void setStrokeType(int type) {
		switch (type) {
		case STROKE_PEN:
			m_curTool = new SketchPadPen(m_penSize, m_strokeColor, this);
			break;

		case STROKE_ERASER:
			m_curTool = new SketchPadEraser(m_eraserSize);
			break;
		}

		m_strokeType = type;
	}

	public void setStrokeSize(int size, int type) {
		switch (type) {
		case STROKE_PEN:
			m_penSize = size;
			break;

		case STROKE_ERASER:
			m_eraserSize = size;
			break;
		}
	}

	public void setStrokeColor(int color) {
		m_strokeColor = color;
	}

	public int getStrokeSize() {
		return m_penSize;
	}

	public int getStrokeColor() {
		return m_strokeColor;
	}

	public void clearAllStrokes() {
		if (m_canClear) {
			// Clear the undo stack.
			m_undoStack.clearAll();
			Constants.m_undoStack.clear();
			// Recycle the temporary fore bitmap
			if (null != m_tempForeBitmap) {
				m_tempForeBitmap.recycle();
				m_tempForeBitmap = null;
			}

			// Create a new fore bitmap and set to canvas.
			createStrokeBitmap(m_canvasWidth, m_canvasHeight);

			invalidate();
			m_isDirty = true;
			m_canClear = false;
		}
	}

	public Bitmap getCanvasSnapshot() {
		setDrawingCacheEnabled(true);
		buildDrawingCache(true);
		Bitmap bmp = getDrawingCache(true);

		if (null == bmp) {
			android.util.Log.d("leehong2",
					"getCanvasSnapshot getDrawingCache == null");
		}
		
		return BitmapUtil.duplicateBitmap(bmp);
	}
	public void cleanDrawingCache(){
		
		setDrawingCacheEnabled(false);
	}
	public void setCallback(ISketchPadCallback callback) {
		m_callback = callback;
	}

	public ISketchPadCallback getCallback() {
		return m_callback;
	}

	@Override
	public void onDeleteFromRedoStack() {
		// Do nothing currently.
	}

	@Override
	public void onDeleteFromUndoStack() {
		// Do nothing currently.
	}

	@Override
	public void redo() {
		if (null != m_undoStack) {
			m_undoStack.redo();
		}
	}
	@Override
	public void historydo() {
		if (null != m_undoStack) {
			m_undoStack.historydo();
		}
	}
	@Override
	public void undo() {
		Log.v("hef", m_undoStack+"m_undoStack");
		if (null != m_undoStack) {
			m_undoStack.undo();
		}
	}

	@Override
	public boolean canUndo() {
		if (null != m_undoStack) {
			return m_undoStack.canUndo();
		}

		return false;
	}

	@Override
	public boolean canRedo() {
		if (null != m_undoStack) {
			return m_undoStack.canRedo();
		}

		return false;
	}
	@Override
	public boolean canHistorydo() {
		if (null != m_undoStack) {
			return m_undoStack.canHistorydo();
		}

		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*if (null != m_callback) {
			int action = event.getAction();
			if (MotionEvent.ACTION_DOWN == action) {
				m_callback.onTouchDown(this, event);
			} else if (MotionEvent.ACTION_UP == action) {
				m_callback.onTouchUp(this, event);
			}
		}

		if (m_isEnableDraw) {
			m_isTouchUp = false;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// This will create a new stroke tool instance with specified
				// stroke type.
				// And the created tool will be added to undo stack.
				setStrokeType(m_strokeType);

				m_curTool.touchDown(event.getX(), event.getY());
				invalidate();
				break;

			case MotionEvent.ACTION_MOVE:
                m_curTool.touchMove(event.getX(), event.getY());
				// If current stroke type is eraser, draw strokes on bitmap hold
				// by m_canvas.
				if (STROKE_ERASER == m_strokeType) {
					m_curTool.draw(m_canvas);
				}

				invalidate();
				m_isDirty = true;
				m_canClear = true;
				break;

			case MotionEvent.ACTION_UP:
				m_isTouchUp = true;
				if (m_curTool.hasDraw()) {
					// Add to undo stack.
					m_undoStack.push(m_curTool);
				}

				m_curTool.touchUp(event.getX(), event.getY());
				// Draw strokes on bitmap which is hold by m_canvas.
				//m_curTool.draw(m_canvas);
                m_curTool.DrawOneLine(m_canvas);


				invalidate();
				m_isDirty = true;
				m_canClear = true;

                redo();

				break;
			}

		}

		// Here must return true if enable to draw, otherwise the stroke may NOT
		// be drawn.
		return true;
		*/

        if(getPaintSwitch() == 1){
            drawLine(event);
        }else if(getPaintSwitch() == 2){
            drawArc(event);
        }else if(getPaintSwitch() == 3){
            drawNote(event);
        }
        return  true;
	}

	private void drawLine(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                set_TouchUp(false);
                set_TouchMove(false);
                setOperateStatus("Paint Line");
                mPadPen.touchDown(event.getX(), event.getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                set_TouchUp(false);
                set_TouchMove(true);
                mPadPen.touchMove(event.getX(), event.getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                set_TouchUp(true);
                set_TouchMove(false);
                mPadPen.touchUp(event.getX(), event.getY());
                mPadPen.DrawOneLine(m_canvas);
                invalidate();
                addOneLine();
                redo();
                break;
            }
        }
	}

    private void drawArc(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                set_TouchUp(false);
                set_TouchMove(false);
                setOperateStatus("Paint Arc");
                mPadPen.touchDown(event.getX(), event.getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                set_TouchMove(true);
                set_TouchUp(false);
                mPadPen.touchMove(event.getX(), event.getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                set_TouchUp(true);
                set_TouchMove(false);
                mPadPen.touchUp(event.getX(),event.getY());
                mPadPen.DrawOneArc(m_canvas);
                invalidate();
                redo();
                break;
            }
        }
    }

    private void drawNote(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                set_TouchMove(false);
                set_TouchUp(false);
                setOperateStatus("Paint Text");
                mPadPen.touchDown(event.getX(), event.getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                set_TouchMove(true);
                set_TouchUp(false);
                mPadPen.touchMove(event.getX(), event.getY());
                break;
            }
            case MotionEvent.ACTION_UP:{
                set_TouchMove(false);
                set_TouchUp(true);
                Log.e("zwb","----ativity = " + measureActivity);
                measureActivity.AddOneNote(this.mPadPen.getsX(), this.mPadPen.getsY());
                break;
            }
        }
    }

    public void set_TouchMove(boolean paramBoolean)
    {
        m_isTouchMove = paramBoolean;
    }

    public void set_TouchUp(boolean paramBoolean)
    {
        m_isTouchUp = paramBoolean;
    }

    public void setOperateStatus(String drawstatus) {
        operateStatusString = drawstatus;
    }

    public String getOperateStatus() {
        return operateStatusString;
    }

	protected void setCanvasSize(int width, int height) {
		if (width > 0 && height > 0) {
			if (m_canvasWidth != width || m_canvasHeight != height) {
				m_canvasWidth = width;
				m_canvasHeight = height;

				createStrokeBitmap(m_canvasWidth, m_canvasHeight);
			}
		}
	}

    public void setPointX(float x) {
        this.pointX = x;
    }

    public void setPointY(float y) {
        this.pointY = y;
    }

    public ArrayList<LineInfo> getLineList() {
        return lineList;
    }

    public ArrayList<SketchDataInfo> getDataList() {
        return dataList;
    }

    public ArrayList<ArcInfo> getArcList() {
        return arcList;
    }

    public ArrayList<NoteInfo> getNoteList() {
        return this.noteList;
    }


	protected void draw(Canvas canvas, boolean save) {

        canvas.drawColor(m_bkColor);

        if (null != m_bkBitmap) {
            RectF dst = new RectF(getLeft(), getTop(), getRight(), getBottom());
            Rect  rst = new Rect(0, 0, m_bkBitmap.getWidth(), m_bkBitmap.getHeight());
            canvas.drawBitmap(m_bkBitmap, rst, dst, m_bitmapPaint);
        }

        if (null != m_foreBitmap) {
            canvas.drawBitmap(m_foreBitmap, 0, 0, m_bitmapPaint);
        }

        if(isfirst){
            historydo();
            isfirst = false;
        }

        if (mPadPen != null) {
            if ((!m_isTouchUp) && (getPaintSwitch() == 1)) {
                mPadPen.DrawOneLine(canvas);
                mPadPen.setLineText("");
            }
            if ((!m_isTouchUp) && (getPaintSwitch() == 2)) {
                mPadPen.DrawOneArc(canvas);
                mPadPen.setLineText("");
            }

            /*if ((!m_isTouchUp) && (getPaintSwitch() == 0) && (this.measureMove))
                this.mPadPen.DrawMoveResult(paramCanvas, this.measure_text.getSelectedText(), this.measureX, this.measureY);

            if ((!m_isTouchUp) && (getPaintSwitch() == 0))
            {
                this.mPadPen.DrawMoveLine(paramCanvas);
                this.mPadPen.DrawMoveArc(paramCanvas);
            }*/
        }


        for(int w = 0; w < gridWidthNum; w++){
            for(int h = 0; h < gridHeightNum; h++){
                int l = 0 + w * 30;
                int i1 = 0 + h * 30;
                int i2 = l + 30;
                int i3 = i1 + 30;
                canvas.drawRect(l, i1, i2, i3, paintRect);
            }
        }

    }


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// If NOT set fore bitmap, call setCanvasSize() method to set canvas
		// size, it will
		// create a new fore bitmap and set to canvas.

        gridWidthNum = (2 * (int)(0.5D + Math.floor(w / 30)));
        gridHeightNum = (2 * (int)(1.0D + Math.floor(h / 30)));

		
		if (!m_isSetForeBmp) {
			//setCanvasSize(2048, 1080);
		}

        m_canvasWidth = w;
        m_canvasHeight = h;


		m_isSetForeBmp = false;
	}

	protected void setTempForeBitmap(Bitmap tempForeBitmap) {
		if (null != tempForeBitmap) {
			if (null != m_foreBitmap) {
				m_foreBitmap.recycle();
			}

			m_foreBitmap = BitmapUtil.duplicateBitmap(tempForeBitmap);

			if (null != m_foreBitmap && null != m_canvas) {
				m_canvas.setBitmap(m_foreBitmap);
				invalidate();
			}
		}
	}

	protected void initialize() {
        m_canvas = new Canvas();
        m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_undoStack = new SketchPadUndoStack(this, UNDO_SIZE);

        mPadPen = new SketchPadPen(m_penSize, m_strokeColor, this);

        paintRect = new Paint();
        paintRect.setColor(-7829368);
        paintRect.setStrokeWidth(1.0F);
        paintRect.setStyle(Paint.Style.STROKE);
	}

	protected void createStrokeBitmap(int w, int h) {
		m_canvasWidth = w;
		m_canvasHeight = h;
		System.out.println("m_canvasWidth"+m_canvasWidth+":"+"m_canvasHeight"+m_canvasHeight);
		Bitmap bitmap = Bitmap.createBitmap(m_canvasWidth, m_canvasHeight,
				Bitmap.Config.ARGB_8888);
//		Bitmap bitmap = null;
//		try {
//			bitmap = BitmapFactory.decodeStream(context.getAssets().open("a.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (null != bitmap) {
			m_foreBitmap = bitmap;
			// Set the fore bitmap to m_canvas to be as canvas of strokes.
			m_canvas.setBitmap(m_foreBitmap);
		}
	}

	private void addOneLine(){

	}

	public class SketchPadUndoStack {
		private int m_stackSize = 0;
		private SketchPadView m_sketchPad = null;
		private ArrayList<ISketchPadTool> m_undoStack = new ArrayList<ISketchPadTool>();
		private ArrayList<ISketchPadTool> m_redoStack = new ArrayList<ISketchPadTool>();
		private ArrayList<ISketchPadTool> m_removedStack = new ArrayList<ISketchPadTool>();

		public SketchPadUndoStack(SketchPadView sketchPad, int stackSize) {
			m_sketchPad = sketchPad;
			m_stackSize = stackSize;
		}

		public boolean canHistorydo() {
			// TODO Auto-generated method stub
			Log.v("hef", Constants.m_undoStack.size()+"------");
			return (Constants.m_undoStack.size() > 0);
		}

		public int size() {
			// TODO Auto-generated method stub
			return m_undoStack.size();
		}

		public void push(ISketchPadTool sketchPadTool) {
			if (null != sketchPadTool) {
//				if (m_undoStack.size() == m_stackSize && m_stackSize > 0) {
//					ISketchPadTool removedTool = m_undoStack.get(0);
//					m_removedStack.add(removedTool);
//					m_undoStack.remove(0);
//				}

				m_undoStack.add(sketchPadTool);
				Constants.m_undoStack.add(sketchPadTool);
			}
		}

		public void clearAll() {
			m_redoStack.clear();
			m_undoStack.clear();
			m_removedStack.clear();
		}

		public void undo() {
			if (canUndo() && null != m_sketchPad) {
				ISketchPadTool removedTool = m_undoStack
						.get(m_undoStack.size() - 1);
				m_redoStack.add(removedTool);
				m_undoStack.remove(m_undoStack.size() - 1);

				if (null != m_tempForeBitmap) {
					// Set the temporary fore bitmap to canvas.
					m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
				} else {
					// Create a new bitmap and set to canvas.
					m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth,
							m_sketchPad.m_canvasHeight);
				}

				Canvas canvas = m_sketchPad.m_canvas;

				// First draw the removed tools from undo stack.
				for (ISketchPadTool sketchPadTool : m_removedStack) {
					sketchPadTool.draw(canvas);
				}

				for (ISketchPadTool sketchPadTool : m_undoStack) {
					sketchPadTool.draw(canvas);
				}

				m_sketchPad.invalidate();
			}
		}
		
//		public void drawHistory(){
//			Canvas canvas = m_sketchPad.m_canvas;
//			for (ISketchPadTool sketchPadTool : m_undoStack) {
//				sketchPadTool.draw(canvas);
//			}
//		}
        public void reDraw() {
            m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
            Canvas canvas = m_sketchPad.m_canvas;
            SketchPadPen sketchPadPen = new SketchPadPen(SketchPadView.this.m_penSize,
                    SketchPadView.this.m_strokeColor, this.m_sketchPad);
            sketchPadPen.DrawFinishLine(canvas, SketchPadView.this.lineList);
            sketchPadPen.DrawFinishArc(canvas, -1, SketchPadView.this.arcList);
            sketchPadPen.DrawFinishNote(canvas);
            m_sketchPad.invalidate();
        }

		public void redo() {
//			if (canRedo() && null != m_sketchPad) {
			/*if (canUndo() && null != m_sketchPad) {
//				ISketchPadTool removedTool = m_redoStack
//						.get(m_redoStack.size() - 1);
//				m_undoStack.add(removedTool);
//				m_redoStack.remove(m_redoStack.size() - 1);

				if (null != m_tempForeBitmap) {
					// Set the temporary fore bitmap to canvas.
					m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
				} else {
					// Create a new bitmap and set to canvas.
					m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth,
							m_sketchPad.m_canvasHeight);
				}

				Canvas canvas = m_sketchPad.m_canvas;

				// First draw the removed tools from undo stack.
//				for (ISketchPadTool sketchPadTool : m_removedStack) {
//					sketchPadTool.draw(canvas);
//				}
				for (ISketchPadTool sketchPadTool : Constants.m_undoStack) {
					sketchPadTool.draw(canvas);
				}

				m_sketchPad.invalidate();
			}
			*/

            reDraw();
            SketchPadView.this.destroyDrawingCache();
		}
		public void historydo() {
//			if (canRedo() && null != m_sketchPad) {
			if (canHistorydo() && null != m_sketchPad) {
//				ISketchPadTool removedTool = m_redoStack
//						.get(m_redoStack.size() - 1);
//				m_undoStack.add(removedTool);
//				m_redoStack.remove(m_redoStack.size() - 1);

				if (null != m_tempForeBitmap) {
					// Set the temporary fore bitmap to canvas.
					m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
				} else {
					// Create a new bitmap and set to canvas.
					m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth,
							m_sketchPad.m_canvasHeight);
				}

				Canvas canvas = m_sketchPad.m_canvas;

				// First draw the removed tools from undo stack.
//				for (ISketchPadTool sketchPadTool : m_removedStack) {
//					sketchPadTool.draw(canvas);
//				}
				for (ISketchPadTool sketchPadTool : Constants.m_undoStack) {
					sketchPadTool.draw(canvas);
				}

				m_sketchPad.invalidate();
			}
		}
		public boolean canUndo() {
			return (m_undoStack.size() > 0);
			
		}
		
		public boolean canRedo() {
			return (m_redoStack.size() > 0);
		}
		
	}
}
