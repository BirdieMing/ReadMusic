package com.ming.readmusic;

public class NoteOnDisplay {
    public int noteDelta;
    public boolean isSharp;

    public int GetDelta() {
        return this.noteDelta;
    }

    public boolean IsSharp() {
        return isSharp;
    }

    public NoteOnDisplay(int noteDelta, boolean isSharp)
    {
        this.noteDelta = noteDelta;
        this.isSharp = isSharp;
    }
}
