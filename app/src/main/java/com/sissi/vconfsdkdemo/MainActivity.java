package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sissi.vconfsdk.base.AgentManager;
import com.sissi.vconfsdk.base.MsgConst;
import com.sissi.vconfsdk.base.ResultCode;
import com.sissi.vconfsdk.datacollaborate.DataCollaborateManager;
import com.sissi.vconfsdk.utils.KLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enter(View view) {
        DataCollaborateManager dataCollaborateManager = AgentManager.obtain(DataCollaborateManager.class);
        dataCollaborateManager.login("127.0.0.1", 6666, MsgConst.EmDcsType.emTypeTrueTouchPhoneAndroid, (i, o)->{
            KLog.p("#### resultCode=%s, response=%s ", i, o);
            if (ResultCode.SUCCESS == i){
                startActivity(new Intent(this, DataCollaborateActivity.class));
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
