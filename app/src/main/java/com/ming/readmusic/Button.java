package com.ming.readmusic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Button {
    private int x;
    private int y;
    private String text;
    private int width = 200;
    private int height = 100;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Button(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
        startX = x - width / 2;
        endX = x + width / 2;
        startY = y - height / 2;
        endY = y + height / 2;
    }

    public void DrawButton(Canvas canvas) {

        Paint borderPaint = new Paint();
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLUE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10f);
        canvas.drawRect(startX, startY, endX, endY, borderPaint);

        Paint textPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(10f);
        textPaint.setTextSize(60);

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int startX = x - (bounds.width() / 2);
        int startY = y + (bounds.height() / 2);
        canvas.drawText(this.text, startX, startY, textPaint);
    }

    public boolean IsButtonPressed(int pressedX, int pressedY) {
        if (startX < pressedX && pressedX < endX && startY < pressedY && pressedY < endY) {
            Log.i("Button", "Pressed!");
            return true;
        } else {
            return false;
        }
    }

}
