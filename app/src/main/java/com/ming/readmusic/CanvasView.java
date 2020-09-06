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

// cmd + [ and cmd + ]  go back and forth in code

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private ArrayList<NoteOnDisplay> notes = new ArrayList<NoteOnDisplay>();
    private int lineSideMargins = 30;
    private int clefWidth = 90;
    private int noteSideMargins = 50;
    private int spaceBetweenBeats = 70;
    private int spaceBetweenLines = 30;
    private int spaceBetweenHalfNotes = spaceBetweenLines / 2;
    private int noteSpace;
    private int notesPerLine;
    private int marginTop = 100;
    private Clef clef = Clef.Treble;
    private int numOfNotes = 10; // TODO: Enough for one line?
    private NoteMode noteMode = NoteMode.Note;
    private double currentTick = 480;
    private int spaceBetweenClefs = 60;

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

        clef = Clef.Treble;
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
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

        int numClef = GetNumClefs(canvas);

        for (int i = 0; i < numClef; i++) {
            DrawClefsAndLines(i, clef, canvas);
        }

        DrawVerticalLine(canvas);

        for (int i = 0; i < notes.size(); i++) {
            DrawNote(notes.get(i), clef, canvas);
        }

        DrawKeyboard(canvas);
    }

    private void DrawKeyboard(Canvas canvas) {
        int startX = 800;
        int startY = 400;
        int white_key_width = 50;
        int black_key_width = 25;
        int white_key_height = 200;
        int black_key_height = 130;

        Paint wk = new Paint();
        wk.setColor(Color.BLACK);
        wk.setStyle(Paint.Style.STROKE);
        wk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            canvas.drawRect(startX + i * white_key_width, startY, startX + (i + 1) * white_key_width, startY + white_key_height, wk);
        }

        Paint selected = new Paint();
        selected.setColor(Color.RED);
        selected.setStyle(Paint.Style.FILL);
        selected.setStrokeWidth(5f);

        for (int i = 0; i < notes.size(); i++) {
            NoteOnDisplay note = notes.get(i);
            if (note.getTick() == this.currentTick) {
                if (note.isSharp) {
                    canvas.drawRect(startX + (50 - 12.5f) +  note.noteDelta * white_key_width, startY, startX + (50 - 12.5f) + black_key_width + note.noteDelta * white_key_width, startY + black_key_height, selected);
                } else {
                    canvas.drawRect(startX + note.noteDelta * white_key_width, startY, startX + (note.noteDelta + 1) * white_key_width, startY + white_key_height, selected);
                }
            }
        }

        Paint bk = new Paint();
        bk.setColor(Color.BLACK);
        bk.setStyle(Paint.Style.FILL);
        bk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            if (i == 2 || i == 6) {
                continue;
            }
            canvas.drawRect(startX + (50 - 12.5f) +  i * white_key_width, startY, startX + (50 - 12.5f) + black_key_width + i * white_key_width, startY + black_key_height, bk);
        }
    }

    private void DrawVerticalLine(Canvas canvas) {

        int lineNum = ((int) (currentTick / 480)) / notesPerLine;
        double beatNum = ((double) currentTick / 480) % notesPerLine;

        int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        int middleY = marginTop + (spaceBetweenLines * 2) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        canvas.drawLine(xPos, middleY - spaceBetweenLines * 3.5f, xPos, middleY + spaceBetweenLines * 3.5f, p);
    }

    private int GetNumClefs(Canvas canvas) {
        if (notes.size() == 0) { return 1; }

        NoteOnDisplay lastNote = notes.get(notes.size() - 1);
        long noteTick = lastNote.getTick();
        int lineNum = ((int) (noteTick / 480)) / notesPerLine;

        return lineNum + 1;
    }

    private void CalNoteSpaces(Canvas canvas) {
        noteSpace = canvas.getWidth() - (lineSideMargins * 2) - clefWidth - (noteSideMargins * 2);
        notesPerLine = (int) Math.ceil((double) noteSpace / spaceBetweenBeats);
    }

    private void DrawNote(NoteOnDisplay note, Clef clef, Canvas canvas) {
        int noteValue = note.getNoteValue();
        long noteTick = note.getTick();

        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        double beatNum = ((double) noteTick / 480) % notesPerLine;

        int middleC_Y;
        if (clef == Clef.Treble) {
            middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        } else {
            middleC_Y = marginTop - spaceBetweenLines + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        }

        int Ypos = middleC_Y + -1 * note.noteDelta * spaceBetweenHalfNotes;
        int Xpos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

        boolean isSelected = noteTick == this.currentTick;

        if (noteMode == NoteMode.Note) {
            DrawOvalWithCenter(canvas, Xpos, Ypos, 30, 35, isSelected);
        } else {
            DrawLetter(canvas, note.letter, Xpos, Ypos);
        }

        DrawShortLine(canvas, note, Xpos, Ypos);
        if (note.isSharp) {
            DrawSharp(canvas, Xpos, Ypos);
        }
    }

    private void DrawOvalWithCenter(Canvas canvas, int x, int y, int height, int width, boolean isSelected) {
        Paint p = new Paint();
        if (isSelected) {
            p.setColor(Color.RED);
        } else {
            p.setColor(Color.BLACK);
        }
        p.setStyle(Paint.Style.FILL);

        int startX = x - width / 2;
        int stopX = x + width / 2;
        int startY = y - height / 2;
        int stopY = y + height / 2;
        canvas.drawOval(startX, startY, stopX, stopY, p);
    }

    private void DrawLetter(Canvas canvas, String letter, int x, int y) {
        Paint textPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(14f);
        textPaint.setTextSize(60);

        Rect bounds = new Rect();
        textPaint.getTextBounds(letter, 0, letter.length(), bounds);
        int startX = x - (bounds.width() / 2);
        int startY = y + (bounds.height() / 2);
        canvas.drawText(letter, startX, startY, textPaint);
    }

    private void DrawShortLine(Canvas canvas, NoteOnDisplay note, int x, int y) {
        // TODO: More than one line?
        if (!MidiReader.RequireShortLine(note)) {
            return;
        }

        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);

        canvas.drawLine(x - 30, y, x + 30, y, p);
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

    private void DrawClefsAndLines(int num, Clef clef, Canvas canvas) {
        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
        int spaceBetweenClefs = 60;
        int spaceBetweenLines = 30;
        int startY = marginTop + num * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i, p);
        }

        /*for (int i = 0; i < 5; i++) {
            canvas.drawLine(lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, canvas.getWidth() - lineSideMargins, startY + spaceBetweenLines * i + spaceBetweenLines * 4 + spaceBetweenClefs, p);
        }*/

        if (clef == Clef.Treble) {
            Drawable t = getResources().getDrawable(R.drawable.treble_clef, null);
            t.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            t.draw(canvas);
        } else {
            Drawable b = getResources().getDrawable(R.drawable.bass_clef, null);
            b.setBounds(lineSideMargins, startY, clefWidth + lineSideMargins, startY + 30 * 4);
            b.draw(canvas);
        }
    }

    public void SetTreble() {
        clef = Clef.Treble;
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
        this.invalidate();
    }

    public void SetBass() {
        clef = Clef.Bass;
        this.notes = MidiReader.GenerateRandomNoteDisplays(numOfNotes, clef);
        this.invalidate();
    }

    public void SwitchNoteMode() {
        if (noteMode == NoteMode.Note) {
            noteMode = NoteMode.Letter;
        } else {
            noteMode = NoteMode.Note;
        }
        this.invalidate();
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        for (int i = 0; i < this.notes.size(); i++) {
            long noteTick = this.notes.get(i).getTick();

            int lineNum = ((int) (noteTick / 480)) / notesPerLine;
            double beatNum = ((double) noteTick / 480) % notesPerLine;
            int middleC_Y;

            if (clef == Clef.Treble) {
                middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            } else {
                middleC_Y = marginTop - spaceBetweenLines + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            }

            int yPos = middleC_Y + -1 * this.notes.get(i).noteDelta * spaceBetweenHalfNotes;
            int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

            int middleY = marginTop + (spaceBetweenLines * 2) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            Log.i("distance:", Double.toString(y - middleY));
            if (Math.abs(x - xPos) < 20 && Math.abs(y - middleY) < 110)
            {
                this.currentTick = noteTick;
                this.invalidate();
            }
        }

        return true;
    }
}