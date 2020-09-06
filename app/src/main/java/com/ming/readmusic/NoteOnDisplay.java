package com.ming.readmusic;

import com.leff.midi.event.NoteOn;

public class NoteOnDisplay {
    public int noteDelta;
    public boolean isSharp;
    public String letter;
    private long tick;
    private int noteValue;
    private int xPos;
    private int yPos;
    private boolean isBlackKey;
    private int keyDelta;

    public int GetDelta() {
        return this.noteDelta;
    }

    public boolean IsSharp() {
        return isSharp;
    }

    public NoteOnDisplay(int noteDelta, boolean isSharp, String letter)
    {
        this.letter = letter;
        this.noteDelta = noteDelta;
        this.isSharp = isSharp;
    }

    public int getNoteValue() {
        return noteValue;
    }

    public long getTick() {
        return tick;
    }

    public void init(NoteOn note) {
        this.noteValue = note.getNoteValue();
        this.tick = note.getTick();
    }
}
