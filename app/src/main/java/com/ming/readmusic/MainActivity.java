package com.ming.readmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //NestedScrollView view = (NestedScrollView) findViewById(R.id.nestedView);
        //view.setNestedScrollingEnabled(true);
    }

    public void setTreble(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetTreble();
    }

    public void setBass(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SetBass();
    }

    public void switchNoteMode(View view) {
        CanvasView canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.SwitchNoteMode();
    }
}
