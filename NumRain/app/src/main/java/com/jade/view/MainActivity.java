package com.jade.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jade.view.loadview.WavingView;
import com.jade.view.loadview.OnLoadProcess;
import com.jade.view.numrain.NumRain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WavingView wavingView = (WavingView) findViewById(R.id.load_view);

        wavingView.setListener(new OnLoadProcess() {
            @Override
            public void onLoad(int process) {
                Log.i("jade","process-->" + process);
                if (process == 99) {
                    NumRain numRain = new NumRain(MainActivity.this);

                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                            , ViewGroup.LayoutParams.MATCH_PARENT);

                    MainActivity.this.addContentView(numRain, params);
                }
            }
        });
    }
}
