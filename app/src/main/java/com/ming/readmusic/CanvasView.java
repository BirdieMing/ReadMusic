package com.ming.readmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.leff.midi.event.NoteOn;
import java.util.ArrayList;

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private ArrayList<NoteOn> notes = new ArrayList<NoteOn>();
    private int lineSideMargins = 30;
    private int clefWidth = 90;
    private int noteSideMargins = 50;
    private int spaceBetweenBeats = 70;
    private int spaceBetweenLines = 30;
    private int spaceBetweenHalfNotes = spaceBetweenLines / 2;
    private int noteSpace;
    private int notesPerLine;
    Button b = new Button(200, 100, "Go");
    private int marginTop = 300;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        this.setWillNotDraw(false);
        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

/*        InputStream stream;

        try {
            stream = getContext().getAssets().open("HAPPY_BIRTHDAY.mid");
            MidiReader reader = new MidiReader(stream);
            notes = reader.GetNotes();

        } catch(final Throwable tx) {

        }*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        setMeasuredDimension(widthMeasureSpec, 3000);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("size info: ", Integer.toString(w) + " "  + Integer.toString(h));
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, 2000, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        CalNoteSpaces(mCanvas);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        b.DrawButton(canvas);

        int numClef = GetNumClefs(canvas);

        for (int i = 0; i < numClef; i++) {
            DrawClefsAndLines(i, canvas);
        }

        for (int i = 0; i < notes.size(); i++) {
            DrawNote(notes.get(i), canvas);
            NoteOn note = notes.get(i);
            Log.i("Note Value", "Value: " + Integer.toString(note.getNoteValue()) + " " + "Tick: "+ note.getTick());
        }
    }
    private int GetNumClefs(Canvas canvas) {
        if (notes.size() == 0) { return 1; }

        NoteOn lastNote = notes.get(notes.size() - 1);
        long noteTick = lastNote.getTick();
        int lineNum = ((int) (noteTick / 480)) / notesPerLine;

        return lineNum + 1;
    }

    private void CalNoteSpaces(Canvas canvas) {
        noteSpace = canvas.getWidth() - (lineSideMargins * 2) - clefWidth - (noteSideMargins * 2);
        notesPerLine = (int) Math.ceil((double) noteSpace / spaceBetweenBeats);
    }

    private void DrawNote(NoteOn note, Canvas canvas) {
        int noteValue = note.getNoteValue();
        long noteTick = note.getTick();
        NoteOnDisplay noteDisplay = MidiReader.GetNoteDisplay(note);

        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        double beatNum = ((double) noteTick / 480) % notesPerLine;

        int spaceBetweenClefs = 60;
        int middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + spaceBetweenClefs + spaceBetweenLines * 4 + 100);
        int Ypos = middleC_Y + -1 * noteDisplay.noteDelta * spaceBetweenHalfNotes;
        int Xpos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);
        //Log.i("X:", Integer.toString(Xpos));
        DrawOvalWithCenter(canvas, Xpos, Ypos, 30, 35);

        if (noteDisplay.isSharp) {
            DrawSharp(canvas, Xpos, Ypos);
        }
    }

    private void DrawSharp(Canvas canvas, int x, int y) {

        Paint textPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(14f);
        textPaint.setTextSize(60);

        Rect bounds = new Rect();
        textPaint.getTextBounds("#", 0, "#".length(), bounds);
        int startX = x - (bounds.width() / 2) - 40;
        int startY = y + (bounds.height() / 2);
        canvas.drawText("#", startX, startY, textPaint);
    }

    private void DrawClefsAndLines(int num, Canvas canvas) {

        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        int spaceBetweenClefs = 60;
        int spaceBetweenLines = 30;
        int startY = marginTop + num * (spaceBetweenLines *4 + spaceBetweenClefs + spaceBetweenLines * 4 + 100);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i, p);
        }

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, p);
        }

        Drawable t = getResources().getDrawable(R.drawable.treble_clef, null);
        t.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
        t.draw(canvas);

        Drawable b = getResources().getDrawable(R.drawable.bass_clef, null);
        b.setBounds(lineSideMargins, startY + 30 * 4 + spaceBetweenClefs, clefWidth + lineSideMargins, startY + 30 * 4 + 30 * 4 + spaceBetweenClefs);
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

/*        int tailLength = 100;
        Paint tailPaint = new Paint();
        tailPaint = new Paint();
        tailPaint.setColor(Color.BLACK);
        tailPaint.setStyle(Paint.Style.STROKE);
        tailPaint.setStrokeWidth(8f);

        canvas.drawLine(stopX, y, stopX, y - tailLength, tailPaint);*/
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
        if (b.IsButtonPressed((int) x, (int) y)) {
            this.notes = MidiReader.GenerateRandomNotes(10);
            this.invalidate();
        }
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