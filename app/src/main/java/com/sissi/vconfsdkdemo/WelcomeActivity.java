package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sissi.vconfsdk.base.AgentManager;
import com.sissi.vconfsdk.startup.StartManager;
import com.sissi.vconfsdk.utils.KLog;

public class WelcomeActivity extends AppCompatActivity {
    private int dd = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        StartManager startManager = (StartManager) AgentManager.obtain(StartManager.class);
        startManager.startup(0, (i, o)->{
            KLog.p("#### dd=%s, resultCode=%s, response=%s ", dd, i, o);
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
