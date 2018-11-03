package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.kedacom.vconf.sdk.base.AgentManager;
import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.startup.StartManager;
import com.kedacom.vconf.sdk.utils.KLog;

public class WelcomeActivity extends AppCompatActivity {
    private int dd = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void printInfo(){
        KLog.p("test");
    }

    public void enter(View view) {
        StartManager startManager = AgentManager.obtain(StartManager.class);
        startManager.startup(0, /*(i, o)->{
//            KLog.p("#### resultCode=%s, response=%s ", i, o);
            startActivity(new Intent(this, LoginActivity.class));
//            printInfo();
        }*/
        new IResultListener() {
            @Override
            public void onResponse(int resultCode, Object response) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        }
        );
    }
}
