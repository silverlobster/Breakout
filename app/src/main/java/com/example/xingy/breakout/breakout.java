package com.example.xingy.breakout;

import android.app.Activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

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

        Brick[] bricks = new Brick[200];
        int numBricks = 0;

        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;

        int score = 0;
        int lives = 3;

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

            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            try{
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                descriptor = assetManager.openFd("beep1.ogg");
                beep1ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep2.ogg");
                beep2ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep3.ogg");
                beep3ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("loseLife.ogg");
                loseLifeID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("explode.ogg");
                explodeID = soundPool.load(descriptor, 0);

            }catch(IOException e){
                Log.e("error", "failed to load sound files");
            }

            createBricksAndRestart();
        }

        public void createBricksAndRestart(){
            paddle.reset(screenX, screenY);
            ball.reset(screenX, screenY);

            int brickWidth = screenX / 10;
            int brickHeight = screenY / 20;

            numBricks = 0;

            for(int column = 0; column < 10; column++ ){
                for(int row = 0; row < 15; row++ ){
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }

            score = 0;
            lives = 3;
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

            // Check for ball colliding with a brick
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        bricks[i].setInvisible();
                        if ((ball.getRect().centerY() < bricks[i].getRect().top
                                && ball.getRect().centerY() > bricks[i].getRect().bottom)) {
                            ball.reverseX();
                        }
                        else if (ball.getRect().centerY() <= bricks[i].getRect().bottom
                                || ball.getRect().centerY() >= bricks[i].getRect().top) {
                            ball.reverseY();
                        }
                        score = score + 10;
                        soundPool.play(explodeID, 1, 1, 0, 0, 1);
                    }
                }
            }
            // Check for ball colliding with paddle
            if (RectF.intersects(paddle.getRect(), ball.getRect())) {
                ball.setRandomXVelocity();
                ball.reverseY();
                ball.clearObstacleY(paddle.getRect().top - 2);
                soundPool.play(beep1ID, 1, 1, 0, 0, 1);
            }

            // Bounce the ball back when it hits the bottom of screen
            if (ball.getRect().bottom > screenY) {
                ball.reverseY();
                ball.clearObstacleY(screenY - 2);

                // Lose a life
                lives--;
                soundPool.play(loseLifeID, 1, 1, 0, 0, 1);

                if (lives == 0) {
                    paused = true;
                    createBricksAndRestart();
                }
            }

            // Bounce the ball back when it hits the top of screen
            if (ball.getRect().top <= 0) {
                ball.getRect().top = 0;
                ball.reverseY();
                ball.clearObstacleY(12);
                soundPool.play(beep2ID, 1, 1, 0, 0, 1);
            }

            // If the ball hits left wall bounce
            if (ball.getRect().left <= 0) {
                ball.getRect().left = 0;
                ball.reverseX();
                ball.clearObstacleX(2);
                soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }

            // If the ball hits right wall bounce
            if (ball.getRect().right >= screenX) {
                ball.getRect().right = screenX;
                ball.reverseX();
                ball.clearObstacleX(screenX - 22);

                soundPool.play(beep3ID, 1, 1, 0, 0, 1);
            }

            // Pause if cleared screen
            if (score == numBricks * 10)

            {
                paused = true;
                createBricksAndRestart();
            }

        }

        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.argb(255, 230, 89, 73));
                paint.setColor(Color.argb(255, 39, 8, 133));
                //draw paddle
                canvas.drawRect(paddle.getRect(), paint);
                //change paint colour
                paint.setColor(Color.argb(255, 0, 0, 0));
                //draw ball
                canvas.drawRect(ball.getRect(), paint);

                paint.setColor(Color.argb(255,  177, 185, 199));
                for(int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        //Log.d("myTag", "This is my message");
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                }

                paint.setColor(Color.argb(255,  255, 255, 255));

                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

                if(score == numBricks * 10){
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE WON!", 10,screenY/2, paint);
                }

                if(lives <= 0) {
                    paint.setTextSize(90);
                    canvas.drawText("YOU HAVE LOST!", 10, screenY / 2, paint);
                }

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


