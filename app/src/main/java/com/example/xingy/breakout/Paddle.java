package com.example.xingy.breakout;

import android.graphics.RectF;

public class Paddle {
    private RectF rect;

    private float length;
    private float height;

    private float x;
    private float y;

    private float rightBound;
    private float leftBound;

    private float paddleSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;

    public Paddle(int screenX, int screenY) {
        rightBound = screenX;
        leftBound = 0;

        length = 200;
        height = 40;

        x = (screenX / 2 ) - 50;
        y = screenY - 100;

        rect = new RectF(x, y, x + length, y + height);
        paddleSpeed = 700;
    }

    public void reset(int x, int y) {
        rect.left = x / 2 - length / 2;
        rect.top = y - 50;
        rect.right = rect.left + length;
        rect.bottom = rect.top + height;
    }

    public RectF getRect() {
        return rect;
    }

    public void setMovementState(int state) {
        //testing
        paddleMoving = state;
    }

    public void update(long fps) {

        if ((x + length) > rightBound ) {
            x = rightBound - length;
        }
        if ( x < leftBound) {
            x = leftBound;
        }
        if (paddleMoving == LEFT) {
            x = x - paddleSpeed / fps;
        }
        if (paddleMoving == RIGHT) {
            x = x + paddleSpeed / fps;
        }
        rect.left = x;
        rect.right = x + length;
    }
}
