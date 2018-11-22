package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.kedacom.vconf.sdk.base.AgentManager;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.MsgConst;
import com.kedacom.vconf.sdk.base.CommonResultCode;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.bean.TerminalType;
//import com.kedacom.vconf.sdk.utils.KLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enter(View view) {
        DataCollaborateManager dataCollaborateManager = AgentManager.obtain(DataCollaborateManager.class);
        dataCollaborateManager.login("127.0.0.1", 6666, TerminalType.TrueLinkAndroidPhone, (i, o)->{
            KLog.p("#### CommonResultCode=%s, response=%s ", i, o);
            if (CommonResultCode.SUCCESS == i){
                dataCollaborateManager.createDcConf((rc, r)->{
                    if (CommonResultCode.SUCCESS == rc) startActivity(new Intent(this, DataCollaborateActivity.class));
                });
            }
//
//            startActivity(new Intent(this, DataCollaborateActivity.class));
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
