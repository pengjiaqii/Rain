package com.jade.gesture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GuestureLayout guestureLayout = (GuestureLayout) findViewById(R.id.guesture);
        guestureLayout.setSelectListener(new onSelectDone() {
            @Override
            public void onSelectEd(String content) {
                //获取哪些按钮被选中了,index拼接之后的
                Log.w("jade","content--->" + content);
            }
        });
    }
}
