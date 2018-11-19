package com.sissi.vconfsdkdemo;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
//import com.google.gson.Gson;
import com.kedacom.vconf.sdk.base.AgentManager;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.Msg;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPainter;
import com.kedacom.vconf.sdk.datacollaborate.IPaintBoard;
import com.kedacom.vconf.sdk.datacollaborate.IPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;
import com.kedacom.vconf.sdk.datacollaborate.bean.PaintBoardInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataCollaborateActivity extends AppCompatActivity
        implements DataCollaborateManager.IPaintBoardLifecycleListener, DataCollaborateManager.IOnPaintOpListener{

    DataCollaborateManager dataCollaborateManager;
    IPaintFactory paintFactory;
    IPainter painter;

    ViewGroup boardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collaborate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        boardContainer = findViewById(R.id.data_collaborate_content);

        KLog.p("dataCollaborateManager.setPainter");
        dataCollaborateManager = AgentManager.obtain(DataCollaborateManager.class);
        dataCollaborateManager.addPaintBoardLifecycleListener(this);
        dataCollaborateManager.addPaintOpListener(this);
//        RawPainter painter = new RawPainter(this);
//        DefaultPainter painter = new DefaultPainter(this);

//        dataCollaborateManager.setPainter(painter);
//        ViewGroup vg = findViewById(R.id.data_collaborate_content);
////        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        vg.addView(painter, params);

//        View v = painter.getPaintView();
//        v.setAlpha(0.2f);
//        v.setBackgroundColor(Color.BLUE);
//        vg.addView(v, params);

//        vg.setBackgroundColor(Color.BLUE);

//        new Handler().postDelayed(dataCollaborateManager::ejectNtfs, 1000);

        paintFactory = new DefaultPaintFactory(this);
        painter = paintFactory.createPainter();


    }

    public void onCreatePaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsNewWhiteBoard_Ntf);
    }

    public void onSwitchPaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsSwitch_Ntf);
    }

    public void onDeletePaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsDelWhiteBoard_Ntf);
    }

    public void onEraseClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperEraseOperInfo_Ntf);
    }

    public void onZoominClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperFullScreen_Ntf);
    }

    public void onZoomoutClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperFullScreen_Ntf);
    }

    public void onClearScreenClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperClearScreen_Ntf);
    }

    public void onDrawLineClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperLineOperInfo_Ntf);
    }

    public void onDrawOvalClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperCircleOperInfo_Ntf);
    }

    public void onDrawRectClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperRectangleOperInfo_Ntf);
    }

    public void onDrawPathClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperPencilOperInfo_Ntf);
    }

    public void onInsertPicClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperInsertPic_Ntf);
    }

    public void onDeletePicClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperPitchPicDel_Ntf);
    }

    public void onMovePic(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperPitchPicDrag_Ntf);
    }

    public void onUndoClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperUndo_Ntf);
    }

    public void onRedoClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DcsOperRedo_Ntf);
    }

    @Override
    public void onBoardCreated(PaintBoardInfo paintBoardInfo) {
        IPaintBoard paintBoard = paintFactory.createPaintBoard(paintBoardInfo);
        painter.addPaintBoard(paintBoard);
    }

    @Override
    public void onBoardDeleted(String boardId) {
        IPaintBoard paintBoard = painter.getPaintBoard(boardId);
        if (null != paintBoard){
            boardContainer.removeView(paintBoard.getBoardView());
            painter.deletePaintBoard(boardId);
        }
    }

    @Override
    public void onBoardSwitched(String boardId) {
        IPaintBoard curPaintBoard = painter.switchPaintBoard(boardId);
        if (null != curPaintBoard){
            View v = curPaintBoard.getBoardView();
            boardContainer.addView(v,  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }


    @Override
    public void onPaintOp(OpPaint opPaint) {
        KLog.p("UI onPaintOp:%s", opPaint);
        painter.paint(opPaint);
    }

}
