package com.sissi.vconfsdkdemo;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity
        implements  LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_frame, new LoginFragment());
        ft.commitAllowingStateLoss();
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
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        KLog.p("-->");
//    }
//
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
