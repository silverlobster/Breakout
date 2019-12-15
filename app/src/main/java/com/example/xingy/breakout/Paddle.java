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

        length = 130;
        height = 20;

        x = (screenX / 2 ) - 50;
        y = screenY - 30;

        rect = new RectF(x, y, x + length, y + height);
        paddleSpeed = 300;
    }

    public RectF getRect() {
        return rect;
    }


    public void setMovementState(int state) {
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
