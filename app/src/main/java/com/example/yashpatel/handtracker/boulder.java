package com.example.yashpatel.handtracker;

import android.graphics.Canvas;
import android.graphics.Paint;

public class boulder {


    float x, y, dx, dy, diameter;
    float width, height;

    public void update()
    {
        x += dx;
        y += dy;
        //if (x < 0) dx = -dx;
        if (x < 0) x = 0;
        if (y < 0) dy = -dy;
       // if (x > width) dx = -dx;
        if (x > width) x = width;
        //if (y > height) dy = -dy;
    }

    public void draw(Canvas canvas, Paint paint)
    {
        canvas.drawCircle(x, y, diameter, paint);
    }
}
