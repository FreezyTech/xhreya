package com.example.xhreya;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class HiddenSOSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_hidden_sosactivity);

        findViewById(R.id.btnCancel)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, AlertSentActivity.class)));
    }
}
