package com.sissi.vconfsdkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sissi.vconfsdk.base.AgentManager;
import com.sissi.vconfsdk.login.LoginManager;
import com.sissi.vconfsdk.login.MemberStateManager;
import com.sissi.vconfsdk.utils.KLog;

public class LoginActivity extends AppCompatActivity
        implements MemberStateManager.OnMemberStateChangedListener, LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_frame, new LoginFragment());
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        KLog.p("-->");
    }

    @Override
    protected void onResume() {
        super.onResume();
        KLog.p("-->");
    }

    @Override
    protected void onPause() {
        super.onPause();
        KLog.p("-->");
    }

    @Override
    protected void onStop() {
        super.onStop();
        KLog.p("-->");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.p("-->");
    }

    public void login(View view) {
        LoginManager loginManager = AgentManager.obtain(LoginManager.class);
        loginManager.login("server", "account", "passwd", (i, o) -> {
                KLog.p("#### resultCode=%s, response=%s ", i, o);
            startActivity(new Intent(this, MainActivity.class));
        });

        MemberStateManager memberStateManager = AgentManager.obtain(MemberStateManager.class);
        memberStateManager.addOnMemberStateChangedListener(this);
    }

    @Override
    public void onMemberStateChanged() {
        KLog.p("####");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
