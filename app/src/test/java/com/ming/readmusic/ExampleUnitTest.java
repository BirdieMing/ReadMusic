package com.ming.readmusic;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_midi_library() {
        // 1. Open up a MIDI file
        MidiFile mf = null;
        File input = new File("src/main/assets/HAPPY_BIRTHDAY.mid");
        String test = System.getProperty("user.dir");
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
        ArrayList<MidiEvent> eventsToRemove = new ArrayList<MidiEvent>();
        ArrayList<Long> ticks = new ArrayList<Long>();
        ArrayList<Double> beats = new ArrayList<Double>();
        ArrayList<Integer> notes = new ArrayList<Integer>();
        ArrayList<String> classes = new ArrayList<>();
        ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(!E.getClass().equals(NoteOn.class) && !E.getClass().equals(NoteOff.class))
            {
                eventsToRemove.add(E);
            } else {
                events.add(E);
            }

            classes.add(E.getClass().getName());
            if (E.getClass().equals(NoteOn.class)) {
                NoteOn note = (NoteOn) E;
                beats.add((double) (note.getTick() / 480));
                notes.add(note.getNoteValue());
                ticks.add(note.getTick());
            }
        }
        int pause = 1;
/*
        for(MidiEvent E : eventsToRemove)
        {
            T.removeEvent(E);
        }
*/

        // 2b. Completely remove track 2
/*
        mf.removeTrack(2);
*/

/*
        // 2c. Reduce the tempo by half
        T = mf.getTracks().get(0);

        it = T.getEvents().iterator();
        while(it.hasNext())
        {
            MidiEvent E = it.next();
        }
*/

/*
        // 3. Save the file back to disk
        try
        {
            mf.writeToFile(input);
        }
        catch(IOException e)
        {
            System.err.println("Error writing MIDI file:");
            e.printStackTrace();
        }
*/

    }
}