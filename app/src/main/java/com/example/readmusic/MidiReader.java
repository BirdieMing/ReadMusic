package com.example.readmusic;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MidiReader {

    private ArrayList<NoteOn> notes;

    public MidiReader(InputStream input) {

        // 1. Open up a MIDI file
        MidiFile mf = null;
        //File input = new File("src/main/assets/HAPPY_BIRTHDAY.mid");

        try
        {
            mf = new MidiFile(input);
        }
        catch(IOException e)
        {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
            return;
        }

        // 2. Do some editing to the file
        // 2a. Strip out anything but notes from track 1
        MidiTrack T = mf.getTracks().get(0);

        // It's a bad idea to modify a set while iterating, so we'll collect
        // the events first, then remove them afterwards
        Iterator<MidiEvent> it = T.getEvents().iterator();
        //ArrayList<Long> ticks = new ArrayList<Long>();
        //ArrayList<Double> beats = new ArrayList<Double>();

        notes = new ArrayList<NoteOn>();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if (E.getClass().equals(NoteOn.class)) {
                NoteOn note = (NoteOn) E;
                if(note.getVelocity() != 0) {
                    notes.add(note);
                }
                //beats.add((double) (note.getTick() / 480));
                //notes.add(note.getNoteValue());
            }
        }
    }

    public ArrayList<NoteOn>  GetNotes() {
        return this.notes;
    }

    public static NoteOnDisplay GetNoteDisplay(NoteOn note) {
        //https://www.inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies
        switch (note.getNoteValue()) {
            case 48: //C
                return new NoteOnDisplay(-7,false);
            case 49: //C#
                return new NoteOnDisplay(-7,true);
            case 50: //D
                return new NoteOnDisplay(-6,false);
            case 51: //D#
                return new NoteOnDisplay(-6,true);
            case 52: //E
                return new NoteOnDisplay(-5,false);
            case 53: //F
                return new NoteOnDisplay(-4,false);
            case 54: //F#
                return new NoteOnDisplay(-4,true);
            case 55: //G
                return new NoteOnDisplay(-3,false);
            case 56: //G#
                return new NoteOnDisplay(-3,true);
            case 57: //A
                return new NoteOnDisplay(-2,false);
            case 58: //A#
                return new NoteOnDisplay(-2,true);
            case 59: //B
                return new NoteOnDisplay(-1,false);
            case 60: //C
                return new NoteOnDisplay(0, false);
            case 61: //C#
                return new NoteOnDisplay(0, true);
            case 62: //D
                return new NoteOnDisplay(1, false);
            case 63: //D#
                return new NoteOnDisplay(1, true);
            case 64: //E
                return new NoteOnDisplay(2, false);
            case 65: //F
                return new NoteOnDisplay(3, false);
            case 66: //F#
                return new NoteOnDisplay(3, true);
            case 67: //G
                return new NoteOnDisplay(4, false);
            case 68: //G#
                return new NoteOnDisplay(4, true);
            case 69: //A
                return new NoteOnDisplay(5, false);
            case 70: //A#
                return new NoteOnDisplay(5, true);
            case 71: //B
                return new NoteOnDisplay(6, false);
            case 72: //C
                return new NoteOnDisplay(7, false);
            default:
                return new NoteOnDisplay(0, false);
        }
    }
}
