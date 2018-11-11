package com.sissi.vconfsdkdemo;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
//import com.google.gson.Gson;
import com.kedacom.vconf.sdk.base.AgentManager;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPainter;
import com.kedacom.vconf.sdk.datacollaborate.RawPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.DCEraseOp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataCollaborateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collaborate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        KLog.p("dataCollaborateManager.setPainter");
        DataCollaborateManager dataCollaborateManager = AgentManager.obtain(DataCollaborateManager.class);
//        RawPainter painter = new RawPainter(this);
        DefaultPainter painter = new DefaultPainter(this);
        dataCollaborateManager.setPainter(painter);
        ViewGroup vg = findViewById(R.id.data_collaborate_content);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(800, 800);

//        vg.addView(painter, params);
        View v = painter.getPaintView();
        v.setAlpha(0.2f);
        vg.setBackgroundColor(Color.GRAY);
        vg.addView(v, params);
//        wb.setBackgroundColor(Color.WHITE);
        new Handler().postDelayed(dataCollaborateManager::ejectNtfs, 3000);
//        dataCollaborateManager.ejectNtfs();

//        Gson gson = new Gson();
//        String json = gson.toJson(new DCEraseOp(1,2, 3, 4, 5));
//        KLog.p("json=%s", json);

    }

}
