package com.sissi.vconfsdkdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.kedacom.vconf.sdk.base.IResultListener;
import com.kedacom.vconf.sdk.base.KLog;
import com.kedacom.vconf.sdk.base.Msg;
import com.kedacom.vconf.sdk.datacollaborate.DataCollaborateManager;
import com.kedacom.vconf.sdk.datacollaborate.DefaultPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPaintBoard;
import com.kedacom.vconf.sdk.datacollaborate.IPaintFactory;
import com.kedacom.vconf.sdk.datacollaborate.IPainter;
import com.kedacom.vconf.sdk.datacollaborate.bean.BoardInfo;
import com.kedacom.vconf.sdk.datacollaborate.bean.OpPaint;

//import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;


public class DataCollaborateActivity extends Activity // 继承的该Activity不是LifecycleOwner实例
        /* 针对未使用support库或androidx库中的已实现LifecycleOwner接口的Activity的情形，
        自己实现LifecycleOwner使得该Activity成为“生命周期拥有者”（LifecycleOwner实例），
        配合SDK用以自动管理监听器的生命周期*/
        implements LifecycleOwner,

        DataCollaborateManager.IOnBoardOpListener, DataCollaborateManager.IOnPaintOpListener, IPaintBoard.IPublisher {

    private DataCollaborateManager dm;
    private IPaintFactory paintFactory;
    private IPainter painter;

    private ViewGroup boardContainer;

    // 生命周期注册器
    private LifecycleRegistry lifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collaborate);

        // 需在注册监听器之前创建，否则绑定生命周期会失败
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE); // LifecycleObserver将收到onCreate回调

        boardContainer = findViewById(R.id.data_collaborate_content);

        dm = DataCollaborateManager.getInstance(this.getApplication());
        // 监听画板操作通知（添加/删除/切换画板）
        dm.addBoardOpListener(this);
        // 监听画板绘制操作
        dm.addPaintOpListener(this);

        // 创建默认绘制工厂以创建画师和画板
        /*
        * 传入DefaultPaintFactory()的DataCollaborateActivity实例为生命周期拥有者（LifecycleOwner实例）
        * 所以工厂创建出的画师和画板生命周期和该DataCollaborateActivity实例绑定，用户无需手动管理。
        * （详见SDK说明）
        * */
        paintFactory = new DefaultPaintFactory(this);
        painter = paintFactory.createPainter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME); // LifecycleObserver将收到onResume回调
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE); // LifecycleObserver将收到onPause回调
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY); // LifecycleObserver将收到onDestroy回调
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }


    /**
     * 创建画板
     * */
    public void onNewBoardClicked(View view) {
        dm.newBoard("e164", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("newBoard success");
                createBoard((BoardInfo) result);
                showToast("已创建画板"+((BoardInfo)result).getId());
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
     * 切换画板
     * */
    public void onSwitchBoardClicked(View view) {
        dm.switchBoard("board", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("switch board success");
                switchBoard((String) result);
                IPaintBoard curBoard = painter.getCurrentPaintBoard();
                String curBoardId = null != curBoard ? curBoard.getBoardId() : "null";
                showToast("已由画板"+curBoardId+"切换到画板"+result);
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


    /**
     * 删除画板
     * */
    public void onDelBoardClicked(View view) {
        dm.delBoard("board", new IResultListener() {

            @Override
            public void onSuccess(Object result) {
                KLog.p("del board success");
                delBoard((String) result);
                showToast("已删除画板"+result);
            }

            @Override
            public void onFailed(int errorCode) {
                KLog.p("del board failed");
            }

            @Override
            public void onTimeout() {
                KLog.p("del board timeout");

            }
        });
    }


    /**
     * 删除所有画板
     * */
    public void onDelAllBoardsClicked(View view) {
        dm.delAllBoard(new IResultListener() {
            @Override
            public void onSuccess(Object result) {
                KLog.p("del all boards success");
                for (IPaintBoard board : painter.getAllPaintBoards()){
                    delBoard(board.getBoardId());
                }
                showToast("已删除所有画板");
            }

            @Override
            public void onFailed(int errorCode) {
                KLog.p("del all boards failed");
            }

            @Override
            public void onTimeout() {
                KLog.p("del all boards timeout");
            }
        });
    }



    /**
     * 设置画线工具
     * */
    public void onLineClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.setTool(IPaintBoard.TOOL_LINE);
    }

    /**
     * 设置画圆工具
     * */
    public void onOvalClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.setTool(IPaintBoard.TOOL_OVAL);
    }

    /**
     * 设置画矩形工具
     * */
    public void onRectClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.setTool(IPaintBoard.TOOL_RECT);
    }

    /**
     * 设置画任意曲线工具
     * */
    public void onPathClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.setTool(IPaintBoard.TOOL_PENCIL);
    }

    /**
     * 设置擦除工具
     * */
    public void onEraserClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.setTool(IPaintBoard.TOOL_RECT_ERASER);
    }



    /**
     * 撤销
     * */
    public void onUndoClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.undo();
    }
    /**
     * 恢复
     * */
    public void onRedoClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.redo();
    }
    /**
     * 清屏
     * */
    public void onClearScreenClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.clearScreen();
    }



    /**
     * 插入图片
     * */
    public void onInsertPicClicked(View view) {
        KLog.p("filePath=%s", filePath);
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard
                && null != filePath
                && new File(filePath).exists()) curBoard.insertPic(filePath);
    }

    private static String filePath;
    /**
     * 快照
     * */
    public void onSnapshotClicked(View view) {
        IPaintBoard curBoard = painter.getCurrentPaintBoard();
        if (null != curBoard) curBoard.snapshot(IPaintBoard.AREA_WINDOW, 0, 0, new IPaintBoard.ISnapshotResultListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                File file = new File(Environment.getExternalStorageDirectory(), "snapshot.png");
                filePath = file.getPath();
                KLog.p("filePath=%s", filePath);
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    boolean bSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    showToast("截图保存在"+file.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    /**
     * 收到其他协作方创建画板的通知
     * */
    @Override
    public void onBoardCreated(BoardInfo boardInfo) {
        KLog.p("onBoardCreated");
        createBoard(boardInfo);
    }

    /**
     * 收到其他协作方删除画板的通知
     * */
    @Override
    public void onBoardDeleted(String boardId) {
        delBoard(boardId);
    }

    /**
     * 收到其他协作方切换画板的通知
     * */
    @Override
    public void onBoardSwitched(String boardId) {
        switchBoard(boardId);
    }

    /**
     * 收到其他协作方删除所有画板的通知
     * */
    @Override
    public void onAllBoardDeleted() {

    }

    /**
     * 发布绘制操作
     * */
    @Override
    public void publish(OpPaint op) {
        KLog.p("publish paint op %s", op);
//        dm.publishPaintOp(op);
    }

    /**
     * 收到绘制操作
     * */
    @Override
    public void onPaint(OpPaint op) {
        // 将绘制操作交由painter处理
        painter.paint(op);
    }

    @Override
    public void onBatchPaint(List<OpPaint> ops) {
        painter.batchPaint(ops);
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


    void showToast(String content){
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
