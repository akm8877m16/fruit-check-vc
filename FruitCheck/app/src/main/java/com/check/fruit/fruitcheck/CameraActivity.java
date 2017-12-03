package com.check.fruit.fruitcheck;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.check.fruit.fruitcheck.util.AppConstant;
import com.check.fruit.fruitcheck.util.BitmapUtils;
import com.check.fruit.fruitcheck.util.CameraUtil;
import com.check.fruit.fruitcheck.util.SystemUtils;
import com.check.fruit.fruitcheck.views.CameraFrameView;
import com.check.fruit.fruitcheck.views.circleView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    public static final int SCAN_UPDATE = 11;
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private ImageView flash_light;
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    private RelativeLayout homecamera_bottom_relative;
    private Button img_camera;
    private int picHeight;
    private CameraFrameView cameraFrameView;
    private FrameLayout frameLayout;
    private TimerTask task=null;
    public Timer mTimer=null;
    private Handler myUiHandler;
    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;


    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = (Button) findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);
        //闪光灯
        flash_light = (ImageView) findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);

        homecamera_bottom_relative = (RelativeLayout) findViewById(R.id.homecamera_bottom_relative);
        this.cameraFrameView = (CameraFrameView)findViewById(R.id.cameraFrameView);

    }

    private void initData() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        //menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        //animHeight = (screenHeight - screenWidth - menuPopviewHeight - SystemUtils.dp2px(context, 44)) / 2;

        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
        //RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
        //bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        //homecamera_bottom_relative.setLayoutParams(bottomParam);
        this.frameLayout = (FrameLayout)findViewById(R.id.cameraFrameViewLayout);
    }

    private void initTimer(){
        myUiHandler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == SCAN_UPDATE){
                    cameraFrameView.invalidate();
                }
            }
        };

        task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事:发送消息
                int scanStartPosition = cameraFrameView.getScanStartPosition();
                if(scanStartPosition > 0){
                    scanStartPosition = scanStartPosition + 10;
                    if(scanStartPosition > cameraFrameView.getFramePositionY()+ cameraFrameView.getFrameWidth()-10){
                        scanStartPosition = cameraFrameView.getFramePositionY();
                    }
                    cameraFrameView.setScanStartPosition(scanStartPosition);
                    Message message = new Message();
                    message.what = SCAN_UPDATE;
                    myUiHandler.sendMessage(message);
                }
            }
        };

        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(task, 1000, 100); // 1s后执行task,经过1s再次执行
        }

    }

    public void showCamera() {
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();

        } else {
            initView();
            initData();
            initTimer();
            if (mCamera == null) {
                mCamera = getCamera(mCameraId);
                if (mHolder != null) {
                    startPreview(mCamera, mHolder);
                }
            }
        }
        // END_INCLUDE(camera_permission)

    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestCameraPermission() {
        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
               ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);

        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                //CAMERA permission has now been granted. Showing preview.
                initView();
                initData();
                initTimer();
                if (mCamera == null) {
                    mCamera = getCamera(mCameraId);
                    if (mHolder != null) {
                        startPreview(mCamera, mHolder);
                    }
                }

            } else {
             //CAMERA permission was NOT granted.

            }
            // END_INCLUDE(permission_result)

        }  else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                switch (light_num) {
                    case 0:
                        //关闭
                        CameraUtil.getInstance().turnLightOff(mCamera);
                        break;
                    case 1:
                        CameraUtil.getInstance().turnLightOn(mCamera);
                        break;
                    case 2:
                        //自动
                        CameraUtil.getInstance().turnLightAuto(mCamera);
                        break;
                }
                captrue();
                break;

            //闪光灯
            case R.id.flash_light:
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        flash_light.setImageResource(R.drawable.camera_icon_flashlight_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //自动
                        light_num = 2;
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.camera_icon_flashlight_auto);
                        break;
                    case 2:
                        //关闭
                        light_num = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.camera_icon_flashlight_off);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        mCamera.takePicture(null, null,new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);

                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);
                saveBitmap = Bitmap.createBitmap(saveBitmap, cameraFrameView.getFramePositionX(),cameraFrameView.getFramePositionY(),cameraFrameView.getFrameWidth(),cameraFrameView.getFrameWidth());

                String dir_path = getCacheDir().getPath();
                String img_path = dir_path +
                        File.separator + "fruit_check" + System.currentTimeMillis() + ".jpeg";

                //BitmapUtils.saveJPGE_After(context, saveBitmap, img_path, 100);

                File myCaptureFile = new File(img_path);
                try{
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    saveBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                }catch (Exception e){
                    Log.d("content:",e.toString());
                }


                if(!bitmap.isRecycled()){
                    bitmap.recycle();
                }

                if(!saveBitmap.isRecycled()){
                    saveBitmap.recycle();
                }

                Intent intent = new Intent();
                intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth);
                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
                setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
                finish();

                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
//                Log.d("bitmapWidth==", bitmap.getWidth() + "");
//                Log.d("bitmapHeight==", bitmap.getHeight() + "");
            }

        });

    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            camera.cancelAutoFocus();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 1000);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 1000);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);
        //camera.autoFocus(autoFocusCallback);
        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */

        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
        //params.gravity = Gravity.CENTER;
        surfaceView.setLayoutParams(params);
        this.cameraFrameView.setLayoutParams(params);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


}
