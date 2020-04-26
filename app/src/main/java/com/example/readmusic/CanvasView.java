package com.example.readmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.leff.midi.event.NoteOn;

import java.io.InputStream;
import java.util.ArrayList;

public class CanvasView extends View {

    //public int width;
    //public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private ArrayList<NoteOn> notes;
    private int sideMargins = 30;
    private int clefWidth = 90;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

        InputStream stream;

        try {
            stream = getContext().getAssets().open("HAPPY_BIRTHDAY.mid");
            MidiReader reader = new MidiReader(stream);
            notes = reader.GetNotes();

        } catch(final Throwable tx) {

        }
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        DrawClefsAndLines(0, canvas);
        DrawClefsAndLines(1, canvas);
        DrawClefsAndLines(2, canvas);
        //NoteOn note = notes.get(0);

/*        for (int i = 0; i < notes.size(); i++) {
            DrawNote(notes.get(i), canvas);
        }*/
        DrawNote(notes.get(0), canvas);
    }

    public void DrawNote(NoteOn note, Canvas canvas) {
        int noteValue = note.getNoteValue();
        long noteTick = note.getTick();
        //noteValue = 61;

        int noteSpace = canvas.getWidth() - sideMargins - clefWidth;
        int spaceBetweenBeats = 30;
        int spaceBetweenLines = 30;
        int notesPerLine = noteSpace / spaceBetweenBeats;
        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        int beatNum = ((int) (noteTick / 480)) % notesPerLine;
        int spaceBetweenHalfNotes = spaceBetweenLines / 2;

        int spaceBetweenClefs = 60;
        int middleC_Y = 100 + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + spaceBetweenClefs + spaceBetweenLines * 5 + 100);
        int Ypos = middleC_Y + -1 * (noteValue - 60) * spaceBetweenHalfNotes;
        int Xpos = sideMargins + clefWidth + beatNum * spaceBetweenBeats;
        DrawOvalWithCenter(canvas, Xpos, Ypos, 30, 35);
    }

    private void DrawClefsAndLines(int num, Canvas canvas) {

        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        int spaceBetweenClefs = 60;
        int spaceBetweenLines = 30;
        int startY = 100 + num * (spaceBetweenLines *4 + spaceBetweenClefs + spaceBetweenLines * 4 + 100);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(sideMargins, startY + spaceBetweenLines * i, canvas.getWidth() - sideMargins, startY + spaceBetweenLines * i, p);
        }

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(sideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, canvas.getWidth() - sideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, p);
        }

        Drawable t = getResources().getDrawable(R.drawable.treble_clef, null);
        t.setBounds(sideMargins, startY, clefWidth + sideMargins, startY + 30 * 4);
        t.draw(canvas);

        Drawable b = getResources().getDrawable(R.drawable.bass_clef, null);
        b.setBounds(sideMargins, startY + 30 * 4 + spaceBetweenClefs, clefWidth + sideMargins, startY + 30 * 4 + 30 * 4 + spaceBetweenClefs);
        b.draw(canvas);
    }

    private void DrawOvalWithCenter(Canvas canvas, int x, int y, int height, int width) {
        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);

        int startX = x - width / 2;
        int stopX = x + width / 2;
        int startY = y - height / 2;
        int stopY = y + height / 2;
        canvas.drawOval(startX, startY, stopX, stopY, p);
    }
    // when ACTION_DOWN start touch according to the x,y values
/*    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }*/

/*    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }*/

/*
    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }
*/

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

/*        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }*/
        return true;
    }
}