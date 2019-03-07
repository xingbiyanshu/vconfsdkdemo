package com.sissi.vconfsdkdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.Msg;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPainter;
import com.kedacom.vconf.sdk.datacollaborate.IPaintBoard;
import com.kedacom.vconf.sdk.datacollaborate.IPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.BoardInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpClearScreen;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataCollaborateActivity extends AppCompatActivity
        implements DataCollaborateManager.IOnBoardOpListener, DataCollaborateManager.IOnPaintOpListener, IPaintBoard.IPublisher/*, IPaintBoard.IOnRepealableStateChangedListener, IPaintBoard.IOnZoomRateChangedListener, IPaintBoard.IOnPictureCountChanged*/ {

    DataCollaborateManager dataCollaborateManager;
    IPaintFactory paintFactory;
    IPainter painter;

    ViewGroup boardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collaborate);

        boardContainer = findViewById(R.id.data_collaborate_content);

        dataCollaborateManager = DataCollaborateManager.getInstance(this.getApplication());
        // 监听画板操作通知（添加/删除/切换画板）
        dataCollaborateManager.addBoardOpListener(this);
        // 监听画板绘制操作
        dataCollaborateManager.addPaintOpListener(this);

        // 创建默认绘制工厂以创建画师和画板
        /*
        * 传入DefaultPaintFactory()的DataCollaborateActivity实例为生命周期拥有者（LifecycleOwner实例）
        * 所以工厂创建出的画师和画板生命周期和该DataCollaborateActivity实例绑定，用户无需手动管理。
        * （详见SDK说明）
        * */
        paintFactory = new DefaultPaintFactory(this);

        // 创建画师
        painter = paintFactory.createPainter();

    }


    static Bitmap bt;
    static String filePath;
    public void download(View view) {
//        bt = BitmapFactory.decodeFile("/data/local/tmp/wb.png");
//        if (null == bt) {
//            painter.getCurrentPaintBoard().getBoardView().setBackgroundColor(Color.BLUE);
//            bt = painter.getCurrentPaintBoard().snapshot(IPaintBoard.LAYER_ALL);
//        }else {
//            bt = painter.getCurrentPaintBoard().snapshot(IPaintBoard.LAYER_PIC_AND_SHAPE);
//        }
        painter.getCurrentPaintBoard().snapshot(IPaintBoard.AREA_ALL, 0, 0, new IPaintBoard.ISnapshotResultListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                bt = bitmap;
                File file = new File(Environment.getExternalStorageDirectory(), "snapshot.png");
                filePath = file.getPath();
                KLog.p("filePath=%s", filePath);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    //通过io流的方式来压缩保存图片
                    boolean isSuccess = bt.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    /**
     * 作为协作方主动创建画板
     * */
    public void onCreatePaintBoardClicked(View view) {
        dataCollaborateManager.newBoard("e164", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("newBoard success");
                createBoard((BoardInfo) result);
            }

            @Override
            public void onFailed(int errorCode) {
                KLog.p("newBoard fail");
            }

            @Override
            public void onTimeout() {
                KLog.p("newBoard timeout");
            }
        });
    }

    /**
     * 作为协作方主动删除画板
     * */
    public void onDeletePaintBoardClicked(View view) {
        dataCollaborateManager.delBoard("board", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("del board success");
                delBoard((String) result);
            }

            @Override
            public void onFailed(int errorCode) {
                KLog.p("del board success");

            }

            @Override
            public void onTimeout() {
                KLog.p("del board success");

            }
        });
    }

    /**
     * 作为协作方主动切换画板
     * */
    public void onSwitchPaintBoardClicked(View view) {
        dataCollaborateManager.switchBoard("board", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("switch board success");
                switchBoard((String) result);
            }

            @Override
            public void onFailed(int errorCode) {
                KLog.p("switch board failed");
            }

            @Override
            public void onTimeout() {
                KLog.p("switch board timeout");
            }
        });
    }

    public void onEraseClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCRectErasedNtf);
        painter.getCurrentPaintBoard().setTool(IPaintBoard.TOOL_RECT_ERASER);
    }

    public void onZoominClicked(View view) {
//        dataCollaborateManager.setDcServerAddr();
//        painter.getCurrentPaintBoard().zoom(100);
    }

    public void onZoomoutClicked(View view) {
//        Object obj = dataCollaborateManager.getDcServerAddr();
//        painter.getCurrentPaintBoard().zoom(50);
    }

    public void onClearScreenClicked(View view) {
        dataCollaborateManager.eject(Msg.DCScreenClearedNtf);
//        painter.getCurrentPaintBoard().clearScreen();
//        OpClearScreen opClearScreen = new OpClearScreen();
//        opClearScreen.setBoardId("boardId");
//        painter.paint(opClearScreen);
    }

    public void onDrawLineClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCLineDrawnNtf);
        painter.getCurrentPaintBoard().setTool(IPaintBoard.TOOL_LINE);
    }

    public void onDrawOvalClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCOvalDrawnNtf);
        painter.getCurrentPaintBoard().setTool(IPaintBoard.TOOL_OVAL);
    }

    public void onDrawRectClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCRectDrawnNtf);
        painter.getCurrentPaintBoard().setTool(IPaintBoard.TOOL_RECT);
    }

    public void onDrawPathClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCPathDrawnNtf);
        painter.getCurrentPaintBoard().setTool(IPaintBoard.TOOL_PENCIL);
    }

    public void onInsertPicClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCPicInsertedNtf);
//        bt = BitmapFactory.decodeFile("/data/local/tmp/wb.png");
//        painter.getCurrentPaintBoard().insertPic("/data/local/tmp/Penguins.jpg");
        KLog.p("filePath=%s", filePath);
        painter.getCurrentPaintBoard().insertPic(filePath);
    }

    public void onDeletePicClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCPicDeletedNtf);
    }

    public void onMovePic(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCPicDraggedNtf);
    }

    public void onUndoClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCUndoneNtf);
        painter.getCurrentPaintBoard().undo();
    }

    public void onRedoClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCRedoneNtf);
        painter.getCurrentPaintBoard().redo();
    }

    private void createBoard(BoardInfo boardInfo){
        IPaintBoard paintBoard = paintFactory.createPaintBoard(boardInfo);
        paintBoard.setPublisher(DataCollaborateActivity.this);
        painter.addPaintBoard(paintBoard); // 创建画板并添加给painter管理。
    }

    private void delBoard(String boardId){
        // 从painter删除该画板
        IPaintBoard paintBoard = painter.deletePaintBoard(boardId);
        if (null != paintBoard){
            // 从界面View树中移除该画板
            boardContainer.removeView(paintBoard.getBoardView());
        }
    }

    private void switchBoard(String boardId){
        IPaintBoard curPaintBoard = painter.getCurrentPaintBoard();
        // painter切换到目标画板
        IPaintBoard switchToPaintBoard = painter.switchPaintBoard(boardId);
        if (null != switchToPaintBoard){
            // view 树中删除当前画板 NOTE:这个取决于界面的具体需求，此例界面仅展示一张画板
            if (null != curPaintBoard) {
                boardContainer.removeView(curPaintBoard.getBoardView());
            }
            // view树中添加目标画板
            boardContainer.addView(switchToPaintBoard.getBoardView(),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onBoardCreated(BoardInfo boardInfo) {
        KLog.p("onBoardCreated");
        createBoard(boardInfo);
    }

    @Override
    public void onBoardDeleted(String boardId) {
        delBoard(boardId);
    }

    @Override
    public void onBoardSwitched(String boardId) {
        switchBoard(boardId);
    }

    @Override
    public void onAllBoardDeleted() {

    }


    @Override
    public void publish(OpPaint op) {
        KLog.p(KLog.WARN,"publish paint op %s", op);
//        dataCollaborateManager.publishPaintOp(op);
    }

    @Override
    public void onPaint(OpPaint op) {
        painter.paint(op);
    }

    @Override
    public void onBatchPaint(List<OpPaint> ops) {
        painter.batchPaint(ops);
    }

//
//    @Override
//    public void onZoomRateChanged(int percentage) {
//
//    }
//
//    @Override
//    public void onPictureCountChanged(int count) {
//
//    }
//
//    @Override
//    public void onRepealableStateChanged(int repealedOpsCount, int remnantOpsCount) {
//
//    }
}
