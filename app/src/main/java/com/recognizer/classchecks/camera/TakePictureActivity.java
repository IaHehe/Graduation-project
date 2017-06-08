package com.recognizer.classchecks.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.basic.BasicActivity;
import com.library.opencv.DetectionBasedTracker;
import com.library.utils.ImageUtils;
import com.library.widget.AlertDialogUtils;
import com.library.widget.CustomToolbar;
import com.library.widget.RectangleSurfaceView;
import com.recognizer.R;
import com.recognizer.classchecks.login.LoginActivity;

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


public class TakePictureActivity extends BasicActivity
        implements TextureView.SurfaceTextureListener, View.OnClickListener{

    private static final String TAG = "CaptureActivity-OpenCV";
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
    //private RectangleSurfaceView mRectSurfaceView;
    private TextView mTVDetectResult;
    private SurfaceTexture mSurfaceTexture;
    private ImageView mIVTakeCapture;

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
    private Handler mChildHandler;

    private String mCameraID;

    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;
    private File                   mCascadeFile;
    private Mat mRgba;
    private Mat mGray;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private int checkedCounter = 1; // 检测到人脸的次数

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

//                    mOpenCvCameraView.enableView();
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
            Toast.makeText(TakePictureActivity.this, "配置失败！"
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
        init();

        AlertDialogUtils.alert(TakePictureActivity.this, AlertDialogUtils.TYPE_INFO, "提示",
                "在您注册之前，我们需要采集5张您的脸部照片用于后期打卡检测，请您根据提示从不同角度进行拍摄",
                null, "确定", null, null);
    }

    private void init() {
        mTextureView = (TextureView) findViewById(R.id.texture_take_picture_activity);
        mTextureView.setSurfaceTextureListener(this);

        mTVDetectResult = (TextView) findViewById(R.id.tv_face_detection_result);

        mIVTakeCapture = (ImageView) findViewById(R.id.iv_take_picture);
        mIVTakeCapture.setOnClickListener(this);

        mHandlerThread = new HandlerThread("Camera2");
        mHandlerThread.start();
        mHandler = new Handler(getMainLooper());
        mChildHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_take_picture);
        mCustomToolbar.setMainTitle(getString(R.string.activity_take_picture_main_title));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_take_picture;
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
        // 实例化ImageReader
        setupImageReader();
        // 获取ImageReader的Surface
        Surface imageReaderSurface = mImageReader.getSurface();
        // 创建CaptureRequestBuilder，TEMPLATE_PREVIEW表示预览请求
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(surface);
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，
            // 当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice.createCaptureSession(Arrays.asList(surface, imageReaderSurface), mCaptureSessionStateCallback, mChildHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String [] faceInfoSavePath = new String[5];
    private int  faceCount = 0;
    private Handler mCollectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                {
                    restartPreview();
                }
                break;
                default:
                    break;
            }
        }
    };

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

                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(faceDetection(bitmap)) {
                    faceInfoSavePath[faceCount] = ImageUtils.saveImageToGallery(TakePictureActivity.this, bitmap);
                    bitmap.recycle();
                    faceCount ++;
                    if(faceCount > 4) {
                        Intent intent = new Intent(TakePictureActivity.this, LoginActivity.class);
                        intent.putExtra("from", "TakePictureActivity");
                        intent.putExtra("faceInfoSavePath", faceInfoSavePath);
                        startActivity(intent);
                        finish();
                    } else {
                        mTVDetectResult.setText("人脸检测通过\n请拍摄第" + (faceCount+1) +"张照片");
                        mTVDetectResult.setVisibility(View.VISIBLE);
                        Message msg = new Message();
                        msg.what = 1;
                        mCollectHandler.sendMessageDelayed(msg, 1000);
                    }
                } else {
                    customToast("人脸检测未通过，请重新拍照", SHORT_SHOW);
                    restartPreview();
                }
            }
        }, mHandler);
    }

    private void capture() {

        try {
            // 首先创建请求拍照的CaptureRequest
            final CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 获取屏幕方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // CaptureRequest添加imageReaderSurface，不加的话就会导致ImageReader的onImageAvailable()方法不会回调
            // 设置CaptureRequest输出到mImageReader
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 设置拍照方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));
            // 停止预览
            mCameraCaptureSession.stopRepeating();
            //开始拍照，然后回调上面的接口重启预览，因为mCaptureBuilder设置ImageReader作为target，
            // 所以会自动回调ImageReader的onImageAvailable()方法保存图片
            mCameraCaptureSession.capture(captureRequestBuilder.build(), null, mChildHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启预览
     */
    private void restartPreview() {
        try {
            mTVDetectResult.setVisibility(View.GONE);
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

    private boolean faceDetection(Bitmap bitmap) {
        Utils.bitmapToMat(bitmap, mRgba);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_BGR2GRAY);

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();
        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new org.opencv.core.Size(mAbsoluteFaceSize, mAbsoluteFaceSize),
                    new org.opencv.core.Size());
        Rect[] facesArray = faces.toArray();
        if(facesArray.length > 0) return true;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_take_picture) {
            capture();
        }
    }
}
