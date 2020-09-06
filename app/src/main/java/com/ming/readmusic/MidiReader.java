package com.ming.readmusic;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import java.io.IOException;
import java.io.InputStream;
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
                return new NoteOnDisplay(-14, false, "C");
            case 37: //C#2
                return new NoteOnDisplay(-14, true, "C#");
            case 38: //D2
                return new NoteOnDisplay(-13, false, "D");
            case 39: //D#2
                return new NoteOnDisplay(-13, true, "D#");
            case 40: //E2
                return new NoteOnDisplay(-12, false, "E");
            case 41: //F2
                return new NoteOnDisplay(-11, false, "F");
            case 42: //F#2
                return new NoteOnDisplay(-11, true, "F#");
            case 43: //G2
                return new NoteOnDisplay(-10, false, "G");
            case 44: //G#2
                return new NoteOnDisplay(-10, true, "G#");
            case 45: //A2
                return new NoteOnDisplay(-9, false, "A");
            case 46: //A#2
                return new NoteOnDisplay(-9, true, "A#");
            case 47: //B2
                return new NoteOnDisplay(-8, false, "B");
            case 48: //C3
                return new NoteOnDisplay(-7, false, "C");
            case 49: //C#3
                return new NoteOnDisplay(-7, true, "C#");
            case 50: //D3
                return new NoteOnDisplay(-6, false, "D");
            case 51: //D#3
                return new NoteOnDisplay(-6, true, "D#");
            case 52: //E3
                return new NoteOnDisplay(-5, false, "E");
            case 53: //F3
                return new NoteOnDisplay(-4, false, "F");
            case 54: //F#3
                return new NoteOnDisplay(-4, true, "F#");
            case 55: //G3
                return new NoteOnDisplay(-3, false, "G");
            case 56: //G#3
                return new NoteOnDisplay(-3, true, "G#");
            case 57: //A3
                return new NoteOnDisplay(-2, false, "A");
            case 58: //A#3
                return new NoteOnDisplay(-2, true, "A#");
            case 59: //B3
                return new NoteOnDisplay(-1, false, "B");
            case 60: //C4 Middle C
                return new NoteOnDisplay(0, false, "C");
            case 61: //C#4
                return new NoteOnDisplay(0, true, "C#");
            case 62: //D4
                return new NoteOnDisplay(1, false, "D");
            case 63: //D#4
                return new NoteOnDisplay(1, true, "D#");
            case 64: //E4
                return new NoteOnDisplay(2, false, "E");
            case 65: //F4
                return new NoteOnDisplay(3, false, "F");
            case 66: //F#4
                return new NoteOnDisplay(3, true, "F#");
            case 67: //G4
                return new NoteOnDisplay(4, false, "G");
            case 68: //G#4
                return new NoteOnDisplay(4, true, "G#");
            case 69: //A4
                return new NoteOnDisplay(5, false, "A");
            case 70: //A#4
                return new NoteOnDisplay(5, true, "A#");
            case 71: //B4
                return new NoteOnDisplay(6, false, "B");
            case 72: //C5
                return new NoteOnDisplay(7, false, "C");
            case 73: //C#5
                return new NoteOnDisplay(7, true, "C#");
            case 74: //D5
                return new NoteOnDisplay(8, false, "D");
            case 75: //D#5
                return new NoteOnDisplay(8, true, "D#");
            case 76: //E5
                return new NoteOnDisplay(9, false, "E");
            case 77: //F5
                return new NoteOnDisplay(10, false, "F");
            case 78: //F#5
                return new NoteOnDisplay(10, true, "F#");
            case 79: //G5
                return new NoteOnDisplay(11, false, "G");
            case 80: //G#5
                return new NoteOnDisplay(11, true, "G#");
            case 81: //A5
                return new NoteOnDisplay(12, false, "A");
            case 82: //A#5
                return new NoteOnDisplay(12, true, "A#");
            case 83: //B5
                return new NoteOnDisplay(13, false, "B");
            case 84: //C5
                return new NoteOnDisplay(14, false, "C");
            default:
                return new NoteOnDisplay(0, false, "");
        }
    }

    public static ArrayList<Integer> GetTrebleNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();

        notes.add(60); //C4 Middle C
        notes.add(62); //D4
        notes.add(64); //E4
        notes.add(65); //F4
        notes.add(67); //G4
        notes.add(69); //A4
        notes.add(71); //B4
        notes.add(72); //C5
        notes.add(74); //D5
        notes.add(76); //E5
        notes.add(79); //G5
        notes.add(77); //F5
        notes.add(81); //A5
        notes.add(83); //B5
        notes.add(84); //C5
        return notes;
    }

    public static ArrayList<Integer> GetBassNotes() {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        notes.add(41); //F2
        notes.add(43); //G2
        notes.add(45); //A2
        notes.add(47); //B2
        notes.add(48); //C3
        notes.add(50); //D3
        notes.add(52); //E3
        notes.add(53); //F3
        notes.add(55); //G3
        notes.add(57); //A3
        notes.add(59); //B3
        notes.add(60); //C4 Middle C
        return notes;
    }

    public static ArrayList<Integer> GetNonSharp(Clef clef) {
        ArrayList<Integer> notes = new ArrayList<Integer>();
        if (clef == Clef.Treble) {
            notes = GetTrebleNotes();
        } else {
            notes = GetBassNotes();
        }

        return notes;
    }

    public static ArrayList<NoteOnDisplay> GenerateRandomNoteDisplays(int numNotes, Clef clef) {
        return GetNoteDisplays(GenerateRandomNotes(numNotes, clef));
    }

    public static ArrayList<NoteOn> GenerateRandomNotes(int numNotes, Clef clef) {
        ArrayList<NoteOn> randomNotes = new ArrayList<NoteOn>();
        ArrayList<Integer> bagOfNotes = GetNonSharp(clef);
        for (int i = 0; i < numNotes; i++) {
            long tick = 480 * i;
            Random r = new Random();
            Integer randomIndex = r.nextInt(bagOfNotes.size());
            NoteOn note = new NoteOn(tick, 1, bagOfNotes.get(randomIndex), 100);
            randomNotes.add(note);
        }
        return randomNotes;
    }

    public static ArrayList<NoteOnDisplay> GetNoteDisplays(ArrayList<NoteOn> notes) {
        ArrayList<NoteOnDisplay> noteDisplayList = new ArrayList<NoteOnDisplay>();
        for (int i = 0; i < notes.size(); i++) {
            NoteOnDisplay noteDisplay = GetNoteDisplay(notes.get(i));
            noteDisplay.init(notes.get(i));
            noteDisplayList.add(noteDisplay);
        }

        return noteDisplayList;
    }
}
