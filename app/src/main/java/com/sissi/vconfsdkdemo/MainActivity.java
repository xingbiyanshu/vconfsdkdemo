package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.kedacom.vconf.sdk.amulet.IResultListener;
import com.kedacom.vconf.sdk.common.type.SimulatedError;
import com.kedacom.vconf.sdk.common.type.SimulatedTimeout;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DcErrorCode;
import com.kedacom.vconf.sdk.datacollaborate.bean.BoardInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.DCMember;
import com.kedacom.vconf.sdk.datacollaborate.bean.DcConfInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.EConfType;
import com.kedacom.vconf.sdk.datacollaborate.bean.EDcMode;
import com.kedacom.vconf.sdk.datacollaborate.bean.ETerminalType;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;
import com.kedacom.vconf.sdk.utils.log.KLog;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class MainActivity extends FragmentActivity {

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
        dataCollaborateManager.enableSimulator(true);

//        dataCollaborateManager.feedSimulatedData("login", new SimulatedError(DataCollaborateManager.ErrCode_Failed));
        dataCollaborateManager.feedSimulatedData("login", null);

        // 登录数据协作
        dataCollaborateManager.login(ETerminalType.TrueLinkAndroidPhone,
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

                    dataCollaborateManager.feedSimulatedData("startCollaborate",
                            new DcConfInfo("123456", "dc conf", EDcMode.Auto, EConfType.MCC, true));
//                    dataCollaborateManager.feedSimulatedData("startCollaborate", new SimulatedError(DcErrorCode.BuildLink4ConfFailed));
                    // 创建数据协作
                    dataCollaborateManager.startCollaborate("confE164", "confName", EDcMode.Auto, EConfType.MCC, "adminE164", null,
                            new IResultListener() {

                                // 创建数据协作成功
                                @Override
                                public void onSuccess(Object result) {
                                    KLog.p("DcConfInfo=%s", result);
                                    // 跳转到数据协作界面
                                    startActivity(new Intent(MainActivity.this, DataCollaborateActivity.class));
                                }

                                @Override
                                public void onFailed(int errorCode) {
                                    KLog.p("createDcConf onFailed %s", errorCode);
                                }

                                @Override
                                public void onTimeout() {
                                    KLog.p("createDcConf onTimeout");
                                }
                            },
                            new DataCollaborateManager.IOnSynchronizeProgressListener() {
                                @Override
                                public void onProgress(String boardId, int percentage, boolean bFinished) {

                                }
                            },
                            new DataCollaborateManager.IOnSessionEventListener() { //FIXME 生命周期跟MainActivity绑定了，应该跟DataCollaborateActivity绑定。可以考虑抽取出同步接口？以及设置监听器接口，在同步前设置好监听器。
                                @Override
                                public void onDcFinished() {

                                }

                                @Override
                                public void onDCConfParaChanged(DcConfInfo dcConfInfo) {

                                }
                            },
                            new DataCollaborateManager.IOnOperatorEventListener() {
                                @Override
                                public void onUserJoined(DCMember member) {

                                }

                                @Override
                                public void onApplyOperator(DCMember member) {

                                }

                                @Override
                                public void onOperatorAdded(List<DCMember> members) {

                                }

                                @Override
                                public void onOperatorDeleted(List<DCMember> members) {

                                }
                            },
                            new DataCollaborateManager.IOnBoardOpListener() {
                                @Override
                                public void onBoardCreated(BoardInfo boardInfo) {

                                }

                                @Override
                                public void onBoardDeleted(String boardId) {

                                }

                                @Override
                                public void onBoardSwitched(String boardId) {

                                }

                                @Override
                                public void onAllBoardDeleted() {

                                }
                            },
                            new DataCollaborateManager.IOnPaintOpListener() {
                                @Override
                                public void onPaint(OpPaint op) {

                                }
                            });
                }

                @Override
                public void onFailed(int errorCode) {
                    KLog.p("login onFailed: %s", errorCode);
                }

                @Override
                public void onTimeout() {
                    KLog.p("login onTimeout");
                }
            }
        );


    }


}
