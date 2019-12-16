package com.example.xingy.breakout;

import android.graphics.RectF;
import java.util.Random;

public class Ball {
    private RectF rect;
    private float length = 50;
    private float height = 50;
    private float speedX = 700;
    private float speedY = 700;

    private float rightBound;
    private float bottomBound;


    public Ball(int screenX, int screenY) {
        rightBound = screenX;
        bottomBound = screenY;

        rect = new RectF();

        //rect.right = screenX / 2;
        //rect.top = screenY / 2;
        //rect.bottom = rect.top - height;
        //rect.left = rect.right - length;
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

    public void setRandomSpeed() {
        Random random = new Random();
        speedX += random.nextInt(50);
        speedY += random.nextInt(50);
    }

    public void clearObstacleY(float y) {
        rect.bottom = y;
        rect.top = y - height;
    }

    public void clearObstacleX(float x) {
        rect.left = x;
        rect.right = x + length;
    }

    public void reset(int x, int y) {
        rect.left = x / 2;
        rect.top = y / 2;
        rect.right = x / 2 + length;
        rect.bottom = y / 2 - height;
    }

    public void update(long fps) {
        if (rect.right >= rightBound || rect.left <= 0) {
            setRandomSpeed();
            reverseX();
        }
        if (rect.top <= 0 || rect.bottom >= bottomBound) {
            setRandomSpeed();
            reverseY();
        }
        rect.left += speedX / fps;
        rect.top += speedY / fps;
        rect.right = rect.left + length;
        rect.bottom = rect.top - height;
    }
}
