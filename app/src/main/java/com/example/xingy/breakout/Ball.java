package com.example.xingy.breakout;

import android.graphics.RectF;

public class Ball {
    private RectF rect;
    private float length = 50;
    private float height = 50;
    private float speedX;
    private float speedY;

    private float rightBound;
    private float bottomBound;


    public Ball(int screenX, int screenY) {
        speedX = 700;
        speedY = 700;

        rightBound = screenX;
        bottomBound = screenY;

        rect = new RectF();

        rect.right = screenX / 2;
        rect.top = screenY / 2;
        rect.bottom = rect.top - height;
        rect.left = rect.right - length;
    }

    public RectF getRect() {
        return rect;
    }

    public void reverseX() {
        speedX = -speedX;
    }

    public void reverseY() {
        speedY = -speedY;
    }

    public void update(long fps) {
        if (rect.right >= rightBound || rect.left <= 0) {
            reverseX();
        }
        if (rect.top <= 0 || rect.bottom >= bottomBound) {
            reverseY();
        }
        rect.left += speedX / fps;
        rect.top += speedY / fps;
        rect.right = rect.left + length;
        rect.bottom = rect.top - height;
    }
}
