package com.ming.readmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(), "Press the correct key to move to the next note.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setTreble(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetTreble();
    }

    public void setBass(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetBass();
    }

    public void ShowHint(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.ShowHint();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        switch (item.getItemId()) {
            case R.id.treble_action:
                canvas.SetTreble();
                return true;
            case R.id.bass_action:
                canvas.SetBass();
                return true;
            case R.id.note_letter_action:
                canvas.ShowHint();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
