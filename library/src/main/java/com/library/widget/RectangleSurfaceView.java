package com.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.library.R;

/*定义一个画矩形框的类*/
public class RectangleSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;
    private static Paint mPaint = new Paint();
    static {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    public RectangleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }
    void clearDraw()
    {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.BLUE);
        sh.unlockCanvasAndPost(canvas);
    }
    public void drawLine()
    {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        //canvas.drawPoint(100.0f, 100.0f, p);
        canvas.drawLine(0,110, 500, 110, p);
        canvas.drawCircle(110, 110, 10.0f, p);
        sh.unlockCanvasAndPost(canvas);

    }

    public void drawRect(Rect rect) {
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        //canvas.drawColor(Color.TRANSPARENT);
        /*Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);*/
        canvas.drawRect(rect, mPaint);
        sh.unlockCanvasAndPost(canvas);
    }

}