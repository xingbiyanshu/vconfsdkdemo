package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.view.View;

import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.basement.EmulationModeOnOff;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.bean.ETerminalType;
//import com.kedacom.vconf.sdk.utils.KLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmulationModeOnOff.enable(true);
    }

    public void enter(View view) {
//        startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));


        DataCollaborateManager dataCollaborateManager = DataCollaborateManager.getInstance(this.getApplication());
        dataCollaborateManager.login("127.0.0.1", 6666, ETerminalType.TrueLinkAndroidPhone, new IResultListener() {
//            @Override
//            public LifecycleOwner getLifecycleOwner() {
//                KLog.p("getLifecycleOwner");
//                return MainActivity.this;
//            }

            @Override
            public void onArrive() {
                KLog.p("onResultArrived");
                startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));
            }

            @Override
            public void onSuccess(Object result) {
                KLog.p("onSuccess");
//                dataCollaborateManager.createDcConf(new IResultListener() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));
//                    }
//
//                    @Override
//                    public void onFailed(int errorCode) {
//
//                    }
//
//                    @Override
//                    public void onTimeout() {
//
//                    }
//                });
            }

            @Override
            public void onFailed(int errorCode) {

                KLog.p("onFailed");
            }

            @Override
            public void onTimeout() {

                KLog.p("onTimeout");
            }
        });
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
