package com.ming.readmusic;

public class NoteOnDisplay {
    public int noteDelta;
    public boolean isSharp;
    public String letter;

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
}
