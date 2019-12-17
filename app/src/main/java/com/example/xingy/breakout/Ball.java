package com.example.xingy.breakout;

import android.graphics.RectF;
import java.util.Random;

public class Ball {
    private RectF rect;
    private float length = 25;
    private float height = 25;
    private float speedX = 700;
    private float speedY = 700;

    private float rightBound;
    private float bottomBound;


    public Ball(int screenX, int screenY) {
        rightBound = screenX;
        bottomBound = screenY;

        rect = new RectF();
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
        speedX += random.nextInt(100);
        speedY += random.nextInt(100);
    }


    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseX();
        }
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
        rect.left += speedX / fps;
        rect.top += speedY / fps;
        rect.right = rect.left + length;
        rect.bottom = rect.top - height;
    }
}
