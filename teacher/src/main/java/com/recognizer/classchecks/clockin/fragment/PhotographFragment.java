package com.recognizer.classchecks.clockin.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;

import com.alertdialog.AlertView;
import com.alertdialog.OnItemClickListener;
import com.library.mvp.BaseFragment;
import com.library.widget.LoadingDialogUtils;
import com.recognizer.R;
import com.recognizer.classchecks.clockin.ClockInActivity;
import com.recognizer.classchecks.clockin.model.PhotographModel;
import com.recognizer.classchecks.clockin.model.bean.ClockInResultBean;
import com.recognizer.classchecks.clockin.presenter.PhotographPresenter;
import com.recognizer.classchecks.clockin.view.PhotographView;
import com.recognizer.common.global.pref.PrefManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PhotographFragment extends BaseFragment<PhotographView, PhotographPresenter>
                implements PhotographView, OnItemClickListener, View.OnClickListener {
    private static final int TAKE_PHOTO = 1;
    private static final int OBTAIN_ALBUM = 2;
    private static final String IMAGE_TYPE = "image/*";

    private ClockInActivity mClockInActivity;

    private ImageView mIVDisplayImage; // 显示从系统相机返回的照片
    private ImageView mIVClockInOK;   //当系统拍照完成后，显示可以上传考勤‘√’图标

    private Uri mImageUri; // 从相册或相机获取的图片的Uri
    private String mImagePath; // 从相册或相机获取的图片的绝对路径

    private Dialog mDialog;

    public PhotographFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClockInActivity = (ClockInActivity) getActivity();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_photograph;
    }

    @Override
    public void initView() {
        mIVDisplayImage = (ImageView) mView.findViewById(R.id.iv_dispaly_image);
        mIVClockInOK = (ImageView) mView.findViewById(R.id.iv_clockin_ok);
        mIVClockInOK.setOnClickListener(this);
        // 获取相机选择对话框的配置信息（标题、选择项），如标题‘考勤照片’、选择项‘相机拍照’、‘相册选择’
        String [] clockinStr = getResources().getStringArray(R.array.clock_in_photograph);
        new AlertView(clockinStr[0], null, getString(R.string.cancel), null,
                new String[]{clockinStr[1], clockinStr[2]},
                mClockInActivity, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public PhotographPresenter createPresenter() {
        return new PhotographPresenter(new PhotographModel());
    }



    @Override
    public void onItemClick(Object o, int position) {
        //Toast.makeText(getContext(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();

        switch (position) {
            case -1 : // 点击取消
            {
                mClockInActivity.onBackPressed();
            }
            break;
            case 0 : // 拍照
            {
                toTakePhoto();
            }
            break;
            case 1 : // 从相机选择照片
            {
                toLocalAlbum();
            }
            break;
            default: break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO : // 调用系统相机请求code
            {
                if(resultCode == RESULT_OK) { //此处的 RESULT_OK 是系统自定义得一个常量
                    mIVDisplayImage.setImageBitmap(decodeUriAsBitmap(mImageUri));
                    mIVClockInOK.setVisibility(View.VISIBLE);
                } else {
                    mClockInActivity.onBackPressed();
                }
            }break;
            case OBTAIN_ALBUM : // 调用系统相册请求code
            {
                if(resultCode == RESULT_OK) { //此处的 RESULT_OK 是系统自定义得一个常量
                    mImageUri = data.getData();  //获得图片的uri
                    mImagePath = foundUriAsPath(mImageUri);
                    Bitmap bm = null;
                    bm = decodeUriAsBitmap(mImageUri);
                    if(null != bm) {
                        mIVDisplayImage.setImageBitmap(bm);
                    }
                    mIVClockInOK.setVisibility(View.VISIBLE);
                } else {
                    mClockInActivity.onBackPressed();
                }
            }break;
            default:
                break;
        }
    }

    /**
     * 调用系统相册
     */
    public void toLocalAlbum() {
        byWhat = 0;
        // Build.VERSION_CODES.KITKAT表示sdk版本为4.4
        // true的话就用新的方法，否则用老的方法
        //
//        boolean isKitKatO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        Intent albumIntent;
//        if(isKitKatO) {
//            // Intent.ACTION_OPEN_DOCUMENT 选择文件
//            albumIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // 新的方法
//        } else {
//            albumIntent = new Intent(Intent.ACTION_GET_CONTENT); // 旧的方法
//        }
        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        albumIntent.setType(IMAGE_TYPE);
        startActivityForResult(albumIntent, OBTAIN_ALBUM);
    }

    /**
     * 调用系统相机拍照
     */
    public void toTakePhoto() {
        File outputImage = new File(mClockInActivity.getExternalCacheDir(), "tmp_out_image.jpg");
        byWhat = 1;
        try {
            if(outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
            mImagePath = outputImage.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24) {
            mImageUri = FileProvider.getUriForFile(getContext(),
                    "com.facerecognizer.localcamera.fileprovider", outputImage);
        } else {
            mImageUri = Uri.fromFile(outputImage);
        }
        // 启动系统相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 以Uri获取图片的路径,由于Android在4.4以后，获取相册返回的URI经过多次封装，需要使用以下方法获取路径
     * @param uri
     * @return
     */
    private String foundUriAsPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String path = null;
        Cursor cursor = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // android多媒体数据库的封装接口，具体的看Android文档
            cursor = mClockInActivity.getContentResolver().query(uri, proj, null, null, null);
        } else {
            cursor = mClockInActivity.managedQuery(uri, proj, null, null, null);
        }
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        if(cursor.moveToFirst()) {
            // 获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //最后根据索引值获取图片路径
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    /**
     * 从Uri中获取bitmap
     * @param uri
     * @return
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mClockInActivity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clockin_ok : // 照片选择好后，点击‘√’按钮，上传
            {
                mPresenter.uploadAsClockIn();
            } break;
            default: break;
        }
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialogUtils.createLoadingDialog(mClockInActivity, "正在检测出勤信息...");
    }

    @Override
    public void hideLoading() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public String getImagePath() {
        return mImagePath;
    }

    @Override
    public void showMsg(String msg) {
        toast(mClockInActivity, msg, SHORT_SHOW);
    }

    @Override
    public void hanldResult(List<ClockInResultBean> clockinResults) {
        ClockInResultFragment crf = new ClockInResultFragment();
        crf.setListOfResultBean(clockinResults);
        replaceFragment(R.id.fl_fragment_clockin_container, crf);
    }

    @Override
    public double getLatitude() {
        return mClockInActivity.getLatitude();
    }

    @Override
    public double getLongitude() {
        return mClockInActivity.getLongitude();
    }

    @Override
    public String getLoginAccount() {
        return PrefManager.getString(getContext(), PrefManager.LOGIN_ACCOUNT, "");
    }

    @Override
    public String getJWAccount() {
        return PrefManager.getString(getContext(), PrefManager.JW_ACCOUNT, "");
    }

    @Override
    public void replaceFragment(@IdRes int resId, Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(resId, fragment);
        // 将fragment加入返回栈，当下一个fragment点击返回按钮时不会返回主界面，而是回到当前界面的Fragment
        ft.commit();
    }

    private int byWhat = 0;

    @Override
    public int getByWhat() {
        return byWhat;
    }
}
