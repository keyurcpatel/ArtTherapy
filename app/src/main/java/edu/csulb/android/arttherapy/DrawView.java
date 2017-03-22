package edu.csulb.android.arttherapy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KEYUR on 20-03-2017.
 * Time: 18:29
 * Project: ArtTherapy
 */

public class DrawView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    Context context;
    Paint mPaint = new Paint();
        private Bitmap mBitmap;
        private Canvas mCanvas;
    private Path mPath = new Path();
        private Paint   mBitmapPaint;
        private float mX, mY;

        public DrawView(Context c) {
            super(c);
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(12);
            context=c;
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    break;
            }
            invalidate();
            return true;
        }



        private void touch_start(float x, float y) {
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void onClear() {
        mPath.reset();
        invalidate();
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
    }
    }