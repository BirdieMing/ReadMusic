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
import java.util.Random;

public class MidiReader {

    private ArrayList<NoteOn> notes;

    public MidiReader(InputStream input) {

        // 1. Open up a MIDI file
        MidiFile mf = null;
        //File input = new File("src/main/assets/HAPPY_BIRTHDAY.mid");

        try {
            mf = new MidiFile(input);
        } catch (IOException e) {
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

        while (it.hasNext()) {
            MidiEvent E = it.next();

            if (E.getClass().equals(NoteOn.class)) {
                NoteOn note = (NoteOn) E;
                if (note.getVelocity() != 0) {
                    notes.add(note);
                }
                //beats.add((double) (note.getTick() / 480));
                //notes.add(note.getNoteValue());
            }
        }
    }

    public ArrayList<NoteOn> GetNotes() {
        return this.notes;
    }

    public static NoteOnDisplay GetNoteDisplay(NoteOn note) {
        //https://www.inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies
        switch (note.getNoteValue()) {
            case 36: //C2
                return new NoteOnDisplay(-14, false);
            case 37: //C#2
                return new NoteOnDisplay(-14, true);
            case 38: //D2
                return new NoteOnDisplay(-13, false);
            case 39: //D#2
                return new NoteOnDisplay(-13, true);
            case 40: //E2
                return new NoteOnDisplay(-12, false);
            case 41: //F2
                return new NoteOnDisplay(-11, false);
            case 42: //F#2
                return new NoteOnDisplay(-11, true);
            case 43: //G2
                return new NoteOnDisplay(-10, false);
            case 44: //G#2
                return new NoteOnDisplay(-10, true);
            case 45: //A2
                return new NoteOnDisplay(-9, false);
            case 46: //A#2
                return new NoteOnDisplay(-9, true);
            case 47: //B2
                return new NoteOnDisplay(-8, false);
            case 48: //C3
                return new NoteOnDisplay(-7, false);
            case 49: //C#3
                return new NoteOnDisplay(-7, true);
            case 50: //D3
                return new NoteOnDisplay(-6, false);
            case 51: //D#3
                return new NoteOnDisplay(-6, true);
            case 52: //E3
                return new NoteOnDisplay(-5, false);
            case 53: //F3
                return new NoteOnDisplay(-4, false);
            case 54: //F#3
                return new NoteOnDisplay(-4, true);
            case 55: //G3
                return new NoteOnDisplay(-3, false);
            case 56: //G#3
                return new NoteOnDisplay(-3, true);
            case 57: //A3
                return new NoteOnDisplay(-2, false);
            case 58: //A#3
                return new NoteOnDisplay(-2, true);
            case 59: //B3
                return new NoteOnDisplay(-1, false);
            case 60: //C4 Middle C
                return new NoteOnDisplay(0, false);
            case 61: //C#4
                return new NoteOnDisplay(0, true);
            case 62: //D4
                return new NoteOnDisplay(1, false);
            case 63: //D#4
                return new NoteOnDisplay(1, true);
            case 64: //E4
                return new NoteOnDisplay(2, false);
            case 65: //F4
                return new NoteOnDisplay(3, false);
            case 66: //F#4
                return new NoteOnDisplay(3, true);
            case 67: //G4
                return new NoteOnDisplay(4, false);
            case 68: //G#4
                return new NoteOnDisplay(4, true);
            case 69: //A4
                return new NoteOnDisplay(5, false);
            case 70: //A#4
                return new NoteOnDisplay(5, true);
            case 71: //B4
                return new NoteOnDisplay(6, false);
            case 72: //C5
                return new NoteOnDisplay(7, false);
            case 73: //C#5
                return new NoteOnDisplay(7, true);
            case 74: //D5
                return new NoteOnDisplay(8, false);
            case 75: //D#5
                return new NoteOnDisplay(8, true);
            case 76: //E5
                return new NoteOnDisplay(9, false);
            case 77: //F5
                return new NoteOnDisplay(10, false);
            case 78: //F#5
                return new NoteOnDisplay(10, true);
            case 79: //G5
                return new NoteOnDisplay(11, false);
            case 80: //G#5
                return new NoteOnDisplay(11, true);
            case 81: //A5
                return new NoteOnDisplay(12, false);
            case 82: //A#5
                return new NoteOnDisplay(12, true);
            case 83: //B5
                return new NoteOnDisplay(13, false);
            default:
                return new NoteOnDisplay(0, false);
        }
    }

    public static ArrayList<NoteOn> GenerateRandomNotes(int numNotes) {
        ArrayList<NoteOn> randomNotes = new ArrayList<NoteOn>();
        for (int i = 0; i < numNotes; i++) {
            int low = 40;
            int high = 81;
            long tick = 480 * i;
            Random r = new Random();
            NoteOn note = new NoteOn(tick, 1, r.nextInt(high - low) + low, 100);
            randomNotes.add(note);
        }
        return randomNotes;
    }
}
