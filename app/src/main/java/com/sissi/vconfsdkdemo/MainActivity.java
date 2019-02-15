package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.view.View;

import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.IPaintBoard;
import com.kedacom.vconf.sdk.datacollaborate.IPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.BoardInfo;
//import com.kedacom.vconf.sdk.datacollaborate.bean.CreateConfResult;
import com.kedacom.vconf.sdk.datacollaborate.bean.DCMember;
import com.kedacom.vconf.sdk.datacollaborate.bean.DcConfInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.EConfType;
import com.kedacom.vconf.sdk.datacollaborate.bean.EDcMode;
import com.kedacom.vconf.sdk.datacollaborate.bean.ETerminalType;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;

import java.util.List;
//import com.kedacom.vconf.sdk.utils.KLog;

public class MainActivity extends AppCompatActivity {
    DataCollaborateManager dm;
    DataCollaborateManager dataCollaborateManager;
    IPaintFactory paintFactory;
    IPainter painter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void enter(View view) {

        dataCollaborateManager = DataCollaborateManager.getInstance(this.getApplication());

        dataCollaborateManager.enableSimulator(true);

        dataCollaborateManager.login("127.0.0.1", 6666, ETerminalType.TrueLinkAndroidPhone,
                new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("onSuccess");
                dataCollaborateManager.createDcConf("confE164", "confName", EDcMode.Auto, EConfType.MCC, "adminE164", null,
                        new IResultListener() {
                    @Override
                    public void onSuccess(Object result) {
                        startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));
                    }

                    @Override
                    public void onFailed(int errorCode) {

                    }

                    @Override
                    public void onTimeout() {

                    }
                });
            }

            @Override
            public void onFailed(int errorCode) {

                KLog.p("onFailed");
            }

            @Override
            public void onTimeout() {

                KLog.p("onTimeout");
            }
        }
        );


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        KLog.p("-->");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        KLog.p("-->");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        KLog.p("-->");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        KLog.p("-->");
//    }

}
