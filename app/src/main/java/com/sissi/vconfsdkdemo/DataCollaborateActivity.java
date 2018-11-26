package com.sissi.vconfsdkdemo;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.Msg;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPainter;
import com.kedacom.vconf.sdk.datacollaborate.IPaintBoard;
import com.kedacom.vconf.sdk.datacollaborate.IPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.BoardInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataCollaborateActivity extends AppCompatActivity
        implements DataCollaborateManager.IOnBoardOpListener, DataCollaborateManager.IOnPaintOpListener{

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
        dataCollaborateManager = DataCollaborateManager.getInstance();
        dataCollaborateManager.addBoardOpListener(this);
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


    public void download(View view) {
    }

    public void onCreatePaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCBoardCreatedNtf);
    }

    public void onSwitchPaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCBoardSwitchedNtf);
    }

    public void onDeletePaintBoardClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCBoardDeletedNtf);
    }

    public void onEraseClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCRectErasedNtf);
    }

    public void onZoominClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCFullScreenMatrixOpNtf);
    }

    public void onZoomoutClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCFullScreenMatrixOpNtf);
    }

    public void onClearScreenClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCScreenClearedNtf);
    }

    public void onDrawLineClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCLineDrawnNtf);
    }

    public void onDrawOvalClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCOvalDrawnNtf);
    }

    public void onDrawRectClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCRectDrawnNtf);
    }

    public void onDrawPathClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCPathDrawnNtf);
    }

    public void onInsertPicClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCPicInsertedNtf);
    }

    public void onDeletePicClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCPicDeletedNtf);
    }

    public void onMovePic(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCPicDraggedNtf);
    }

    public void onUndoClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCUndoneNtf);
    }

    public void onRedoClicked(View view) {
        dataCollaborateManager.ejectNtf(Msg.DCRedoneNtf);
    }

    @Override
    public void onBoardCreated(BoardInfo boardInfo) {
        IPaintBoard paintBoard = paintFactory.createPaintBoard(boardInfo);
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
