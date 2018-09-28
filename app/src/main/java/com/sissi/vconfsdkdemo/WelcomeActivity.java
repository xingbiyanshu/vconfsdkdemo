package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sissi.vconfsdk.base.RequestAgent;
import com.sissi.vconfsdk.startup.StartManager;

public class WelcomeActivity extends AppCompatActivity
        implements StartManager.OnStartupResultListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        StartManager startManager = (StartManager) RequestAgent.instance(StartManager.class);
        startManager.startup(0, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStartupSuccess() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onStartupFailed(int errCode) {

    }
}
