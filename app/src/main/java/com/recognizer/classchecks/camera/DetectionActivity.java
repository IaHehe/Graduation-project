package com.recognizer.classchecks.camera;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.library.mvp.BaseActivity;
import com.library.opencv.DetectionBasedTracker;
import com.library.utils.LogUtil;
import com.library.widget.CustomToolbar;
import com.library.widget.LoadingDialogUtils;
import com.library.widget.RectangleSurfaceView;
import com.recognizer.R;
import com.recognizer.classchecks.camera.model.DetectionModel;
import com.recognizer.classchecks.camera.model.bean.StudentClockinBean;
import com.recognizer.classchecks.camera.presenter.DetectionPresenter;
import com.recognizer.classchecks.camera.view.DetectionView;
import com.recognizer.common.global.pref.PrefManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class DetectionActivity extends BaseActivity<DetectionView, DetectionPresenter>
        implements TextureView.SurfaceTextureListener, View.OnClickListener, DetectionView{

    private static final String TAG = "Detection-OpenCV";
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    static {
//        ORIENTATION.append(Surface.ROTATION_0, 90);
//        ORIENTATION.append(Surface.ROTATION_90, 0);
//        ORIENTATION.append(Surface.ROTATION_180, 270);
//        ORIENTATION.append(Surface.ROTATION_270, 180);
        ORIENTATION.append(Surface.ROTATION_0, 270);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 90);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    private TextureView mTextureView;
    private RectangleSurfaceView mRectSurfaceView;
    private TextView mTextViewShowMsg;
    private TextView mTextViewShowInfo;
    private SurfaceTexture mSurfaceTexture;

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;

    private CaptureRequest mCaptureRequest;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;

    private ImageReader mImageReader;

    private Size mPreviewSize;
    private Size mCaptureSize;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Handler childHandler;

    private String mCameraID;

    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;
    private File mCascadeFile;
    private Mat mRgba;
    private Mat mGray;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private int checkedCounter = 1; // 检测到人脸的次数
    private byte[] mCaptureByte; // 捕获的相机图片

    private double mLongitude;
    private double mLatitude;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");//lbpcascade_frontalface
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            // 当相机开启后，再开启预览
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            if(null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    private CameraCaptureSession.StateCallback mCaptureSessionStateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {
                // 创建捕获请求
                mCaptureRequest = mCaptureRequestBuilder.build();
                mCameraCaptureSession = session;
                // 设置反复捕获数据的请求， 这样预览界面就会一直有数据显示
                mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Toast.makeText(DetectionActivity.this, "配置失败！"
                    , Toast.LENGTH_SHORT).show();
        }
    };

    //这个回调接口用于拍照结束时重启预览，因为拍照会导致预览停止
    CameraCaptureSession.CaptureCallback mCameraCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
            // 重启预览
            restartPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLongitude = intent.getDoubleExtra("longitude", -0.0);
        mLatitude = intent.getDoubleExtra("latitude", -0.0);

        initViews();
    }

    @Override
    public void onBackPressed() {
        if(againClockin == true) {
            //againClockin = false;
            restartPreview();
            checkedCounter = 1;
            mTextViewShowMsg.setVisibility(View.GONE);
            mTextViewShowInfo.setVisibility(View.GONE);
        } else if(againClockin == false) {
            super.onBackPressed();
        }
    }

    private void initViews() {

        mTextViewShowMsg = (TextView) findViewById(R.id.tv_show_msg);
        mTextViewShowInfo = (TextView) findViewById(R.id.tv_show_info);

        // 画矩形
        mRectSurfaceView = (RectangleSurfaceView) findViewById(R.id.faces_rect_activity_capture);
        // 显示预览图像
        mTextureView = (TextureView) findViewById(R.id.capture_texture);
        mTextureView.setSurfaceTextureListener(this);

        mHandlerThread = new HandlerThread("Camera2");
        mHandlerThread.start();
        mHandler = new Handler(getMainLooper());
        childHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_detection);
        mCustomToolbar.setMainTitle(getString(R.string.activity_detection_activity));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detection;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        }
    }

    @Override
    protected void onPause() {
        super.onResume();
        if(null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
            mCameraCaptureSession.close();
        }
        if(null != mSurfaceTexture) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if(null != mImageReader) {
            mImageReader.close();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
            mCameraCaptureSession.close();
        }
        if(null != mSurfaceTexture) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if(null != mImageReader) {
            mImageReader.close();
        }
    }

    @Override
    public DetectionPresenter createPresenter() {
        return new DetectionPresenter(new DetectionModel());
    }

    /**
     * 设置相机参数
     * @param width
     * @param height
     */
    private void setupCamera(int width, int height) {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        // 遍历所有摄像头
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (String id : mCameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics =
                            mCameraManager.getCameraCharacteristics(id);
                    // 默认打开后置摄像头
                    // LENS_FACING_FRONT 前置标志
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                        continue;
                    }
                    // 获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    // 根据TextureView的尺寸设置预览尺寸
                    mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                    mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                        @Override
                        public int compare(Size lhs, Size rhs) {
                            return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                        }
                    });
                    mCameraID = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size getOptimalSize(Size[] outputSizes, int width, int height) {
        return outputSizes[0];
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        try {
            // 检查权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开相机，第一个参数指示打开哪个摄像头，第二个参数stateCallback为相机的状态回调接口，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行

            mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启预览
     */
    private void startPreview() {

        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        // 设置TextureView的缓冲区大小
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        // 获取Surface显示预览数据
        Surface surface = new Surface(surfaceTexture);
        setupImageReader();
        // 获取ImageReader的Surface
        Surface imageReaderSurface = mImageReader.getSurface();

        // 创建CaptureRequestBuilder，TEMPLATE_PREVIEW表示预览请求
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(surface);
            // CaptureRequest添加imageReaderSurface，不加的话就会导致ImageReader的onImageAvailable()方法不会回调
            //mCaptureRequestBuilder.addTarget(imageReaderSurface);
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，
            // 当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice.createCaptureSession(Arrays.asList(surface, imageReaderSurface), mCaptureSessionStateCallback, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupImageReader() {
        //前三个参数分别是需要的尺寸和格式，最后一个参数代表每次最多获取几帧数据，本例的2代表ImageReader中最多可以获取两帧图像流
        mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                ImageFormat.JPEG, 20);
        //监听ImageReader的事件，当有图像流数据可用时会回调onImageAvailable方法，它的参数就是预览帧数据，可以对这帧数据进行处理
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                //我们可以将这帧数据转成字节数组，类似于Camera1的PreviewCallback回调的预览帧数据
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                image.close();
                LogUtil.i("mImageReader.setOnImageAvailableListener...");
                mCaptureByte = bytes;
                presenter.clockin();

            }
        }, mHandler);
    }

    private void capture() {

        try {
            // 首先创建请求拍照的CaptureRequest
            final CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 获取屏幕方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 设置CaptureRequest输出到mImageReader
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 设置拍照方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));
            // 停止预览
            mCameraCaptureSession.stopRepeating();
            //开始拍照，然后回调上面的接口重启预览，因为mCaptureBuilder设置ImageReader作为target，
            // 所以会自动回调ImageReader的onImageAvailable()方法保存图片
            mCameraCaptureSession.capture(captureRequestBuilder.build(), null, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启预览
     */
    private void restartPreview() {
        try {
            //执行setRepeatingRequest方法就行了，注意mCaptureRequest是之前开启预览设置的请求
            mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 当SurfaceTexture可用的时候，设置相机参数并打开相机
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
        mSurfaceTexture = surface;
        setupCamera(width, height);
        openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(null != mSurfaceTexture) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
            mTextureView = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Bitmap bitmap = mTextureView.getBitmap();
        Utils.bitmapToMat(bitmap, mRgba);
        bitmap.recycle();
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_BGR2GRAY);

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();
        if (mNativeDetector != null)
            mNativeDetector.detect(mGray, faces);
        Rect[] facesArray = faces.toArray();
        //Toast.makeText(getApplicationContext(), "faceNumber:"+facesArray.length, Toast.LENGTH_SHORT).show();
        android.graphics.Rect darwRect = new android.graphics.Rect();
        if(facesArray.length > 0) {
            Rect r = facesArray[0];
            darwRect = new android.graphics.Rect(
                    r.x, r.y, r.width + r.x, r.height+r.y
            );
            checkedCounter ++;
            if(checkedCounter == 20) {
                LogUtil.i("onSurfaceTextureUpdated->checkedCounter");
                capture();
            }
        } else {
            darwRect = new android.graphics.Rect(0, 0, 0, 0);
        }
        mRectSurfaceView.setVisibility(View.VISIBLE);
        mRectSurfaceView.drawRect(darwRect);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public byte[] getCaptureByte() {
        return mCaptureByte;
    }

    @Override
    public String getJWAccount() {
        return PrefManager.getString(getApplicationContext(), PrefManager.JW_ACCOUNT, "");
    }

    @Override
    public String getLoginAccount() {
        return PrefManager.getString(getApplicationContext(), PrefManager.LOGIN_ACCOUNT, "");
    }

    @Override
    public double getLng() {
        return mLongitude;
    }

    @Override
    public double getLat() {
        return mLatitude;
    }

    @Override
    public void showResult(int what, String msg) {
        if(what == CLOCK_IN_FAILED) {
            msgHandler.obtainMessage(DISPLAY_MSG_INFO_CLOCK_FAILED, msg).sendToTarget();
        } else if(what == SHOW_MSG){
            customToast(msg, SHORT_SHOW);
        } else {
            toast(msg, SHORT_SHOW);
        }
    }

    @Override
    public void showResult(int what, Object obj) {
        if(what == CLOCK_IN_SUCCESS) {
            msgHandler.obtainMessage(DISPLAY_MSG_INFO_CLOCK_SUCCESS, obj).sendToTarget();
            //msgHandler.obtainMessage(DISPLAY_MSG_INFO_CLOCK_SUCCESS, "签到时间：05:28:03").sendToTarget();
        }
    }

    private Dialog mDialog;

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(DetectionActivity.this, "人脸比对中...");
    }

    @Override
    public void hideLoading() {
        LogUtil.i("hideLoading execute...");
        LoadingDialogUtils.closeDialog(mDialog);
    }

    private static final int DISPLAY_MSG_INFO_CLOCK_SUCCESS = 1;
    private static final int DISPLAY_MSG_INFO_CLOCK_FAILED = 2;

    private boolean againClockin = false;

    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISPLAY_MSG_INFO_CLOCK_SUCCESS :
                {
                    againClockin = false;
                    mTextViewShowMsg.setVisibility(View.VISIBLE);
                    mTextViewShowMsg.setTextColor(getResources().getColor(R.color.colorPrimary));
                    StudentClockinBean scb = (StudentClockinBean) msg.obj;
                    mTextViewShowMsg.setText("签到完成："+scb.getStuName());
                    mTextViewShowInfo.setVisibility(View.VISIBLE);
                    mTextViewShowInfo.setText("签到时间："+scb.getCurTime());
                } break;
                case DISPLAY_MSG_INFO_CLOCK_FAILED : {
                    againClockin = true;
                    mTextViewShowMsg.setVisibility(View.VISIBLE);
                    mTextViewShowMsg.setTextColor(getResources().getColor(R.color.colorBloodOrange));
                    mTextViewShowMsg.setText(msg.obj.toString());
                    mTextViewShowInfo.setVisibility(View.VISIBLE);
                    mTextViewShowInfo.setText("请重新签到");
                } break;
                default: break;
            }
        }
    };
}

