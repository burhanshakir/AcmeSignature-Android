package acme.burhanshakir.com.acmesignatureapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.Attributes;


public class DrawingView extends View {

    Paint mTextPaint;
    Paint mBitmapPaint;
    Canvas mPaintCanvas;
    Bitmap mScreenBitmap;
    Context mContext;
    Path mPath;
    Float mX, mY;
    boolean isSignatureDrawn = false;
    private static final float TOUCH_TOLERANCE = 4;
    DrawingListener mListener;

    public DrawingView(Context context, AttributeSet attrs){
        super(context,attrs);

        mContext = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mListener = (DrawingListener) context;

        // Initializing the paint object
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true); // flag to ensure drawing has smooth edges.
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setDither(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setStrokeWidth(15);
        mTextPaint.setStyle(Paint.Style.STROKE);

    }

    // Called when the view is created
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Creating canvas for screen size
        mScreenBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mPaintCanvas = new Canvas(mScreenBitmap);
    }

    // Automatically invoked when view is loaded
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mScreenBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mTextPaint);
    }

    //Called when drawing is started
    private void drawingStart(float x, float y){

        mPath.reset();
        mPath.moveTo(x,y);
        mX = x;
        mY = y;

        isSignatureDrawn = true;
    }

    // Called when the signature is being drawn
    private void drawingMove(float x, float y){

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){

            mPath.quadTo(mX,mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }


    }

    // Called when the signature drawing is finished
    private void drawingStopped(float x, float y){
        mPath.lineTo(x, y);

        mPaintCanvas.drawPath(mPath,mTextPaint);

        mPath.reset();

    }

    // Remove all drawings on click
    public void clearDrawingCanvas(){

        mPaintCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();

        isSignatureDrawn = false;
    }

    // Save signature
    public void saveSignature(Context context){

        if(isSignatureDrawn){
            new ImageSaver(context).
                    save(this);

            mListener.showMsgOnSave();
        }

        else{
            mListener.showErrorMsgOnSave();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                drawingStart(x,y);
                invalidate();

                break;


            case MotionEvent.ACTION_UP:

                drawingStopped(x,y);
                invalidate();

                break;


            case MotionEvent.ACTION_MOVE:

                drawingMove(x,y);
                invalidate();
                break;
        }

        return true;

    }
}
