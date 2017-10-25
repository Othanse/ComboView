package com.example.eagleweb.combobutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ComboTextView combo = (ComboTextView) findViewById(R.id.combo);
        combo.setType(1);
        combo.setComboListener(new ComboTextView.ComboListener() {
            @Override
            public void click(boolean isCombo, int comboCount) {
                System.out.println("点击了  isCombo:" + isCombo + "  count:" + comboCount);
            }

            @Override
            public void comboOver(int comboCount) {
                System.out.println("连击结束  comboCount:" + comboCount);
            }
        });
    }
}
