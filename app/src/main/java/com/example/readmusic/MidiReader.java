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
}
