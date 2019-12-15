package com.example.xingy.breakout;

import android.app.Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class breakout extends Activity {

    BreakoutView breakoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);
    }

    class BreakoutView extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;
        int screenX;
        int screenY;

        Paddle paddle;
        Ball ball;

        public BreakoutView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;

            paddle = new Paddle(screenX, screenY);
            ball = new Ball(screenX, screenY);

        }

        @Override
        public void run() {
            while (playing) {
                long startFrameTime = System.currentTimeMillis();
                if (!paused) {
                    update();
                }
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        public void update() {
            paddle.update(fps);
            ball.update(fps);
        }

        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.argb(255, 230, 89, 73));
                paint.setColor(Color.argb(255, 39, 8, 133));
                canvas.drawRect(paddle.getRect(), paint);
                paint.setColor(Color.argb(255, 0, 0, 0));
                canvas.drawRect(ball.getRect(), paint);
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if (motionEvent.getX() > screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT);
                    }
                    else {
                        paddle.setMovementState(paddle.LEFT);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }
            return true;
        }
    }

        @Override
        protected void onResume() {
            super.onResume();
            breakoutView.resume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            breakoutView.pause();
        }
    }


