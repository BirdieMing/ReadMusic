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
    private int spaceBetweenBeats = 100;
    private int spaceBetweenLines = 30;
    private int spaceBetweenHalfNotes = spaceBetweenLines / 2;
    private int noteSpace;
    private int notesPerLine;
    private int marginTop = 100;
    private Clef clef = Clef.Treble;
    private int numOfNotes = 10; // TODO: Enough for one line?
    private NoteMode noteMode = NoteMode.Note;
    private int spaceBetweenClefs = 60;
    private int keyboardStartX = 800;
    private int keyboardStartY = 400;
    private int white_key_width = 50;
    private int black_key_width = 25;
    private int white_key_height = 200;
    private int black_key_height = 130;
    private double currentTick = 480;
    //private double currentBeatNum;
    //private int currentLineNum;
    private boolean showHint = false;

    private int clickBoxWidth = spaceBetweenBeats;
    private int clickBoxHeight = 260;

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

        if (showHint)
            DrawSelectedNote(canvas);

        //DrawBoundingBox(canvas);
        DrawKeyboard(canvas, -2);
        DrawKeyboard(canvas, -1);
        DrawKeyboard(canvas, 0);
        DrawKeyboard(canvas, 1);
        DrawKeyboard(canvas, 2);
    }

    private void DrawBoundingBox(Canvas canvas) {
        Paint boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStyle(Paint.Style.STROKE);

        int currentLineNum = ((int) (this.currentTick / 480)) / notesPerLine;
        int middleY = marginTop + (spaceBetweenLines * 2) + (currentLineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        for (int i = 0; i < 10; i++) {
            int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + i * spaceBetweenBeats);

            canvas.drawRect(xPos - clickBoxWidth / 2, middleY - clickBoxHeight / 2, xPos + clickBoxWidth / 2, middleY + clickBoxHeight / 2, boxPaint);
        }

    }

    private void DrawSelectedNote(Canvas canvas) {

        int startX = keyboardStartX;
        int startY = keyboardStartY;

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
    }
    private void DrawKeyboard(Canvas canvas, int octaveNum) {

        int startX = keyboardStartX + octaveNum * 7 * white_key_width;
        int startY = keyboardStartY;

        Paint wk = new Paint();
        wk.setColor(Color.BLACK);
        wk.setStyle(Paint.Style.STROKE);
        wk.setStrokeWidth(5f);

        for (int i = 0; i < 7; i++) {
            canvas.drawRect(startX + i * white_key_width, startY, startX + (i + 1) * white_key_width, startY + white_key_height, wk);
        }

        Paint txt = new Paint();
        txt.setColor(Color.BLACK);
        txt.setStyle(Paint.Style.STROKE);
        txt.setStrokeWidth(3f);
        txt.setTextSize(30);

        if (octaveNum == 0) {
            canvas.drawText("C4", startX + 5, startY + white_key_height, txt);
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
        //currentBeatNum = ((double) this.currentTick / 480) % notesPerLine;
        //currentLineNum = ((int) (this.currentTick / 480)) / notesPerLine;
    }

    private void DrawNote(NoteOnDisplay note, Clef clef, Canvas canvas) {
        long noteTick = note.getTick();

        int lineNum = ((int) (noteTick / 480)) / notesPerLine;
        double beatNum = ((double) noteTick / 480) % notesPerLine;

        int middleC_Y;
        int middleLine_Y = marginTop + (spaceBetweenLines * 3) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        if (clef == Clef.Treble) {
            middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        } else {
            middleC_Y = marginTop - spaceBetweenLines + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        }

        int Ypos = middleC_Y + -1 * note.noteDelta * spaceBetweenHalfNotes;
        int Xpos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

        TailDirection tailDirection;
        if (Ypos > middleLine_Y) {
            tailDirection = TailDirection.Up;
        } else {
            tailDirection = TailDirection.Down;
        }

        boolean isSelected = noteTick == this.currentTick;

        if (noteMode == NoteMode.Note) {
            DrawNoteShape(canvas, Xpos, Ypos, 30, 40, isSelected, tailDirection);
        } else {
            DrawLetter(canvas, note.letter, Xpos, Ypos);
        }

        DrawShortLine(canvas, note, Xpos, Ypos);
        if (note.isSharp) {
            DrawSharp(canvas, Xpos, Ypos);
        }
    }

    private enum TailDirection {
        Up,
        Down
    }

    private void DrawNoteShape(Canvas canvas, int x, int y, int height, int width, boolean isSelected, TailDirection tailDirection) {
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

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        int tailLength = 80;

        if (tailDirection == TailDirection.Up)
            canvas.drawLine(stopX -2, y, stopX - 2, y - tailLength, p);
        else
            canvas.drawLine(stopX -2, y, stopX - 2, y + tailLength, p);
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

    private void DrawShortLine(Canvas canvas, NoteOnDisplay note, int noteX, int noteY) {
        int lineNum = 0;
        int lowerLine = marginTop + (spaceBetweenLines * 4) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
        int upperLine = marginTop + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);

        Paint p = new Paint();
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);

        int lineY = noteY;

        while (lineY < upperLine) {
            if (note.noteDelta % 2 == 0) {
                canvas.drawLine(noteX - 30, lineY, noteX + 30, lineY, p);
            } else {
                canvas.drawLine(noteX - 30, lineY + spaceBetweenLines / 2, noteX + 30, lineY + spaceBetweenLines / 2, p);
            }

            lineY = lineY + spaceBetweenLines;
        }

        while (lineY > lowerLine) {
            if (note.noteDelta % 2 == 0) {
                canvas.drawLine(noteX - 30, lineY, noteX + 30, lineY, p);
            } else {
                canvas.drawLine(noteX - 30, lineY - spaceBetweenLines / 2, noteX + 30, lineY - spaceBetweenLines / 2, p);
            }

            lineY = lineY - spaceBetweenLines;
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

    public void ShowHint() {
        this.showHint = true;
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

/*            if (clef == Clef.Treble) {
                middleC_Y = marginTop + (spaceBetweenLines * 5) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            } else {
                middleC_Y = marginTop - spaceBetweenLines + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            }

            int yPos = middleC_Y + -1 * this.notes.get(i).noteDelta * spaceBetweenHalfNotes;*/
            int xPos = (int) Math.ceil(lineSideMargins + noteSideMargins + clefWidth + beatNum * spaceBetweenBeats);

            int middleY = marginTop + (spaceBetweenLines * 2) + (lineNum) * (spaceBetweenLines *4 + 100 + spaceBetweenClefs);
            //Log.i("distance:", Double.toString(y - middleY));
            if (Math.abs(x - xPos) < clickBoxWidth / 2 && Math.abs(y - middleY) < clickBoxHeight / 2)
            {
                this.currentTick = noteTick;
                //this.currentBeatNum = ((double) this.currentTick / 480) % notesPerLine;
                //this.currentLineNum = ((int) (this.currentTick / 480)) / notesPerLine;
                this.invalidate();
            }

            if (isClickOnSelectNote(x, y))
            {
                this.currentTick = this.currentTick + 480;
                this.showHint = false;
                this.invalidate();
            }
        }

        return true;
    }

    private boolean isClickOnSelectNote(float clickX, float clickY) {
        int boardStartX = keyboardStartX;
        int boardStartY = keyboardStartY;

        for (int i = 0; i < this.notes.size(); i++) {
            NoteOnDisplay note = notes.get(i);
            if (note.getTick() == this.currentTick) {
                if (note.isSharp) {
                    float startX = boardStartX + (50 - 12.5f) + note.noteDelta * white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (50 - 12.5f) + black_key_width + note.noteDelta * white_key_width;
                    float endY = boardStartY + black_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                } else {
                    float startX = boardStartX + note.noteDelta * white_key_width;
                    float startY = boardStartY;
                    float endX = boardStartX + (note.noteDelta + 1) * white_key_width;
                    float endY = boardStartY + white_key_height;

                    if (startX < clickX && clickX < endX && startY < clickY && clickY < endY) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}