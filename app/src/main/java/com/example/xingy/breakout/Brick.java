package com.example.xingy.breakout;

import android.graphics.RectF;

public class Brick {
    private RectF rect;
    private boolean isVisible;

    public Brick(int row, int column, int width, int height){
        isVisible = true;
        int padding = 2;
        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

}
/*
                        if (ball.getRect().centerX() > bricks[i].getRect().left
                                && ball.getRect().centerX() < bricks[i].getRect().right) {
                            ball.reverseY();
                            if (ball.getRect().top > bricks[i].getRect().centerY()) {
                                ball.clearObstacleY(bricks[i].getRect().bottom - ball.getRect().height());
                            }
                            else {
                                ball.clearObstacleY(bricks[i].getRect().top + ball.getRect().height());
                            }
                        }
                        else {
                            ball.reverseX();
                            if(ball.getRect().left > bricks[i].getRect().centerX()) {
                                ball.clearObstacleX(bricks[i].getRect().right + ball.getRect().width());
                            }
                            else {
                                ball.clearObstacleX(bricks[i].getRect().left - ball.getRect().width());
                            }
                        }
 */