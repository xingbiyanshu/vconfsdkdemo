package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.bean.EConfType;
import com.kedacom.vconf.sdk.datacollaborate.bean.EDcMode;
import com.kedacom.vconf.sdk.datacollaborate.bean.ETerminalType;

public class MainActivity extends AppCompatActivity {

    DataCollaborateManager dataCollaborateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle(); // MainActivity为生命周期拥有者
    }


    public void enter(View view) {

        dataCollaborateManager = DataCollaborateManager.getInstance(this.getApplication());

        // 启用模拟器。启用后模拟器替代真实的底层模块接受请求并反馈通知/响应。
        // （需保证SimulatorOnOff.on==true，方能启用成功。）
        dataCollaborateManager.enableSimulator(true);

        // 登录数据协作
        dataCollaborateManager.login("127.0.0.1", 6666, ETerminalType.TrueLinkAndroidPhone,
            /* 结果监听器。
            此处创建的结果监听器的直接外部类是MainActivity且MainActivity为生命周期拥有者，
            所以该结果监听器的生命周期将自动绑定到该MainActivity实例——MainActivity销毁时该监听器自动注销。
            另外监听器返回结果后亦会自动注销（监听器的这两种生命周期机制是竞争关系）。
            所以此例中用户无需手动管理监听器的生命周期，SDK将自动管理。（手动管理的情形用户需在Activity销毁时
            调用dataCollaborateManager.delListener(resListener)，注销掉监听器）
            （SDK文档中有详细描述）
            */
            new IResultListener() {

                // 登录数据协作成功
                @Override
                public void onSuccess(Object result) {
                    KLog.p("login onSuccess");

                    // 创建数据协作
                    dataCollaborateManager.createDcConf("confE164", "confName", EDcMode.Auto, EConfType.MCC, "adminE164", null,
                        new IResultListener() {

                            // 创建数据协作成功
                            @Override
                            public void onSuccess(Object result) {
                                // 跳转到数据协作界面
                                startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));
                            }

                            @Override
                            public void onFailed(int errorCode) {
                                KLog.p("createDcConf onFailed");
                            }

                            @Override
                            public void onTimeout() {
                                KLog.p("createDcConf onTimeout");
                            }
                        });
                }

                @Override
                public void onFailed(int errorCode) {
                    KLog.p("login onFailed");
                }

                @Override
                public void onTimeout() {
                    KLog.p("login onTimeout");
                }
            }
        );


    }


}
