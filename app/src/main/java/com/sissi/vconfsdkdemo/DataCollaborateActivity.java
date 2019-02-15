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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        boardContainer = findViewById(R.id.data_collaborate_content);

        KLog.p("dataCollaborateManager.setPainter");
        dataCollaborateManager = DataCollaborateManager.getInstance(this.getApplication());
        dataCollaborateManager.addBoardOpListener(this);
        dataCollaborateManager.addPaintOpListener(this);

        paintFactory = new DefaultPaintFactory(this);
        painter = paintFactory.createPainter();

        // DEBUG
//        RectF rect1 = new RectF(10, 10, 20, 30);
//        RectF rect2 = new RectF(10, 10, 20, 30);
//        RectF rect3 = new RectF(10, 10, 40, 70);
//        Matrix transMatrix1 = calcTransMatrix(rect1, rect2);
//        Matrix transMatrix2 = calcTransMatrix(rect1, rect3);
//        RectF rect12 = new RectF();
//        RectF rect13 = new RectF();
//        transMatrix1.mapRect(rect12, rect1);
//        transMatrix2.mapRect(rect13, rect1);
//        KLog.p("transMatrix1=%s, transMatrix2=%s, \nrect1=%s, rect2=%s, rect3=%s, rect12=%s, rect13=%s",
//                transMatrix1, transMatrix2, rect1, rect2, rect3, rect12, rect13);
//
//        PointF pointF = new PointF(448, 0); // 100%时的插入点
//        float[] mappedPoint = new float[]{pointF.x, pointF.y};
//        Matrix matrix1 = new Matrix();
//        Matrix matrix2 = new Matrix();
//        Matrix invertedMatrix1 = new Matrix();
//        Matrix invertedMatrix2 = new Matrix();
//        matrix1.postTranslate(10, 10);
//        matrix2.postTranslate(30, 30);
//        matrix1.mapRect(rect1);
//        matrix2.mapRect(rect2);
//        matrix1.invert(invertedMatrix1);
//        matrix2.postConcat(invertedMatrix1);
//        invertedMatrix1.mapRect(rect3);
//        KLog.p("matrix1=%s, matrix2=%s, invertedMatrix1=%s, rect1=%s, rect2=%s, rect3=%s",
//                matrix1, matrix2, invertedMatrix1, rect1, rect2, rect3);

//
//        matrix1.postScale(0.5f, 0.5f, 960, 540); // 全屏缩放为50%后的matrix
//        matrix1.invert(invertedMatrix1);
//        invertedMatrix1.invert(invertedMatrix2);
//        invertedMatrix1.mapPoints(mappedPoint);  // 适应全屏缩放50%后的插入点，使得实际插入的位置跟见到位置一致。
//        float[] vals = new float[]{0.6666667f, 0.0f, 161.33334f, 0.0f, 0.6666667f, 223.54288f, 0.0f, 0.0f, 1.0f};
//        matrix1.setValues(vals);
//        float[] x = new float[]{0, 0};
//        matrix1.mapPoints(x);
//        KLog.p("matrix1=%s, mappedPoints=[%s, %s]", matrix1, x[0], x[1]);
//        matrix1.mapPoints(mappedPoint);
//        KLog.p("after 0.5scaled matrix=%s, invertedMatrix1=%s, invert mappedPoints=[%s, %s]", matrix1, invertedMatrix1, mappedPoint[0], mappedPoint[1]);
//        matrix2.postTranslate(40, 40);
//        matrix2.postScale(2, 2);
//        matrix1.mapPoints(mappedPoint);
//        Matrix matrix2DivMatrix1 = new Matrix(matrix2);
//        matrix2DivMatrix1.postConcat(invertedMatrix1);
//        KLog.p("matrix1=%s, matrix2=%s, matrix2DivMatrix1=%s, \npoint=%s, matrix1MappedPoint=[%s,%s]\ninvertedMatrix1=%s, invertedMatrix2=%s",
//                matrix1, matrix2, matrix2DivMatrix1, pointF, mappedPoint[0], mappedPoint[1], invertedMatrix1, invertedMatrix2);
//        Matrix matrix1ConcatMatrix2 = new Matrix(matrix1);
//        matrix1ConcatMatrix2.postConcat(matrix2);
//        Matrix matrix2ConcatMatrix1 = new Matrix(matrix2);
//        matrix2ConcatMatrix1.postConcat(matrix1);
//        KLog.p("matrix1=%s, matrix2=%s, \nmatrix1ConcatMatrix2=%s, matrix2ConcatMatrix1=%s", matrix1, matrix2, matrix1ConcatMatrix2, matrix2ConcatMatrix1);
//
//        Matrix matrix1XMatrix2XInvertedMatrix2 = new Matrix(matrix1ConcatMatrix2);
//        matrix1XMatrix2XInvertedMatrix2.postConcat(invertedMatrix2);
//        Matrix matrix1XMatrix2XInvertedMatrix1 = new Matrix(matrix1ConcatMatrix2);
//        matrix1XMatrix2XInvertedMatrix1.postConcat(invertedMatrix1);
//        KLog.p("matrix1XMatrix2XInvertedMatrix2=%s, matrix1XMatrix2XInvertedMatrix1=%s", matrix1XMatrix2XInvertedMatrix2, matrix1XMatrix2XInvertedMatrix1);

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
        painter.getCurrentPaintBoard().save(new IPaintBoard.ISaveListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
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

    public void onCreatePaintBoardClicked(View view) {
        dataCollaborateManager.newBoard("mye164", new IResultListener() {
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
//        dataCollaborateManager.ejectNtf(Msg.DCFullScreenMatrixOpNtf);
        painter.getCurrentPaintBoard().zoom(100);
    }

    public void onZoomoutClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCFullScreenMatrixOpNtf);
        painter.getCurrentPaintBoard().zoom(50);
    }

    public void onClearScreenClicked(View view) {
//        dataCollaborateManager.ejectNtf(Msg.DCScreenClearedNtf);
//        painter.getCurrentPaintBoard().clearScreen();
        OpClearScreen opClearScreen = new OpClearScreen();
        opClearScreen.setBoardId("boardId");
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

    private void delBoard(String boardId){
        // 从painter删除该画板
        IPaintBoard paintBoard = painter.deletePaintBoard(boardId);
        if (null != paintBoard){
            // 从界面View树中移除该画板
            boardContainer.removeView(paintBoard.getBoardView());
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
    public void onPaintOp(List<OpPaint> ops) {
        painter.paint(ops);
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
