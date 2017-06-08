package com.recognizer.classchecks.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.library.basic.BasicActivity;
import com.library.location.GPSLocationListener;
import com.library.location.GPSLocationManager;
import com.library.location.GPSProviderStatus;
import com.library.mvp.BaseActivity;
import com.library.utils.CalendarUtils;
import com.library.widget.AlertDialogUtils;
import com.library.widget.util.StatusBarUtil;
import com.recognizer.R;
import com.library.widget.CustomToolbar;
import com.recognizer.classchecks.clockin.ClockInActivity;
import com.recognizer.classchecks.clockrecord.ClockRecordActivity;
import com.recognizer.classchecks.course.CourseActivity;
import com.recognizer.classchecks.course.ImportTableActivity;
import com.recognizer.classchecks.home.model.HomeModel;
import com.recognizer.classchecks.home.presenter.HomePresenter;
import com.recognizer.classchecks.home.view.HomeView;
import com.recognizer.classchecks.information.InformationActivity;
import com.recognizer.classchecks.report.GenerateReportActivity;
import com.recognizer.classchecks.setting.SettingActivity;
import com.recognizer.common.global.pref.PrefManager;

import java.util.Map;


public class HomeActivity extends BaseActivity<HomeView, HomePresenter>
        implements View.OnClickListener, OnPermissionCallback, HomeView {

    // 表示是否退出APP
    private static boolean IS_APP_EXIT = false;
    // 两次点击退出应用时间间隔
    private static final int REFRESH_DELAY = 1000;
    // handler用于检测退出的标志
    private final static int APP_EXIT_FLAG  = 1;
    // handler用于更新主界面电子时间的标志
    private final static int UPDATE_TIME = 2;
    // handler用于开启GPS定位的标志
    private final static int GPS_START = 3;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private NavigationView mNavigationView;

    private TextView mTextViewShowTime;
    private TextView mTextViewShowDateWeek;

    private ImageView mImageViewClockIn; // 打卡图标

    private Intent mIntent;

    private double mLongitude;
    private double mLatitude;

    //权限检测类(GPS)
    private PermissionHelper mPermissionHelper;
    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private GPSLocationManager gpsLocationManager;
    // 因为GPS定位会随时刷新，设置一个标志，在不是用户点击的情况下，不跳转到考勤页面
    private boolean hasClickClock = false;

    // 处理首页时钟
    private final Runnable mTimeRefresher = new Runnable()
    {

        @Override
        public void run()
        {
            Message msg = new Message();
            msg.what = UPDATE_TIME;
            handler.sendMessage(msg);
            handler.postDelayed(this, REFRESH_DELAY);
        }
    };
    // 更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case APP_EXIT_FLAG :  // 点击两次退出登录
                    // 没有点击第二次时， IS_APP_EXIT 变为false
                    IS_APP_EXIT = false;
                    break;
                case UPDATE_TIME :
                    mTextViewShowTime.setText(CalendarUtils.getCurrentTime(CalendarUtils.DATE_FORMAT_HMS));
                    break;
                case GPS_START :
                {
                    checkPermissions(); // 检查定位权限
                    gpsLocationManager.start(new MyListener());
                }break;
                default: break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstances();
        initLocationManager();
    }

    @Override
    protected void createToolbar() {

        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_home);

        mCustomToolbar.setMainTitleLeftText("");
        mCustomToolbar.setMainTitleLeftDrawable(R.drawable.button_more);
        mCustomToolbar.setMainTitle(getString(R.string.app_name));
        mCustomToolbar.setMainTitleRightText(getString(R.string.toolbar_right_title));
        // 因为DrawerLayout需要ActionBar的支持，所以这里必须将自定义的toolbar设置成ActionBar
        setSupportActionBar(mCustomToolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCustomToolbar.setMainTitleLeftOnClickListener(this);
        mCustomToolbar.setMainTitleRightOnClickListener(this);
        handler.post(mTimeRefresher);
    }

    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter(new HomeModel());
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(mTimeRefresher);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawerlayout_activity_home), mStatusBarColor);
    }

    private void initInstances() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_activity_home);
        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mCustomToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //
        drawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.addDrawerListener(drawerToggle);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_home);
        // 解决NavigationView的Item不显示icon的原始颜色
        mNavigationView.setItemIconTintList(null);
        //对NavigationView添加item的监听事件
        mNavigationView.setNavigationItemSelectedListener(naviListener);
        //左上角图标可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View view = mNavigationView.getHeaderView(0);
        view.findViewById(R.id.ll_nav_personal_info).setOnClickListener(this);
        // 获取显示时间的View控件，并使用多线程实现实时更新时间
        mTextViewShowTime = (TextView) findViewById(R.id.tv_time_show);
        // 显示 年月日 星期
        mTextViewShowDateWeek = (TextView) findViewById(R.id.tv_date_week_show);
        mTextViewShowDateWeek.setText(CalendarUtils.getCurrentDateAndWeek());

        mImageViewClockIn = (ImageView) findViewById(R.id.iv_clock_in);
        mImageViewClockIn.setOnClickListener(this);
    }

    private void checkPermissions() {
        mPermissionHelper = PermissionHelper.getInstance(HomeActivity.this);
        mPermissionHelper.request(MULTI_PERMISSIONS);
    }

    private void initLocationManager() {
        gpsLocationManager = GPSLocationManager.getInstances(HomeActivity.this);
        // checkPermissions();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
       drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(!IS_APP_EXIT) {
                Toast.makeText(getApplicationContext(), "再次点击退出程序", Toast.LENGTH_SHORT).show();
                IS_APP_EXIT = true;
                Message msg = new Message();
                msg.what = APP_EXIT_FLAG;
                handler.sendMessageDelayed(msg, 2000);
            } else {
                super.onBackPressed();
                System.exit(0);
            }
        }
    }

    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_item_record :
                    intent(getApplicationContext(), ClockRecordActivity.class);
                    break;
                case R.id.nav_item_course :
                    intent(getApplicationContext(), CourseActivity.class);
                    break;
                case R.id.nav_item_setting :
                    intent(getApplicationContext(), SettingActivity.class);
                    break;
                case R.id.nav_item_clockin :
                {

                }break;
                case R.id.nav_item_report :
                {
                    intent(getApplicationContext(), GenerateReportActivity.class);
                }
                break;
            }

            //关闭DrawerLayout回到主界面选中的tab的fragment页
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

    // 跳转页面
    private void intent(Context context, Class<?> clazz) {
        mIntent = new Intent(context, clazz);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View v) {
        if(mDrawerLayout.isDrawerOpen(mNavigationView)) mDrawerLayout.closeDrawer(mNavigationView);
        switch (v.getId()) {
            case R.id.ll_nav_personal_info : // 侧滑菜单上的用户界面部分
                intent(getApplicationContext(), InformationActivity.class);
                break;
            case com.library.R.id.lt_main_title_left : // 点击导航栏左边的文字或图标
                mDrawerLayout.openDrawer(mNavigationView);
                break;
            case com.library.R.id.lt_main_title_right: // 点击导航栏右边的文字或图标
                intent(getApplicationContext(), ClockRecordActivity.class);
                break;
            case R.id.iv_clock_in: // 点击页面照相机图标，进入打卡
                hasClickClock = true;
                presenter.checkHasCourse();
                break;
            default :
                break;
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {

    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {

    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {

    }

    @Override
    public void onNoPermissionNeeded() {

    }

    class MyListener implements GPSLocationListener {

        @Override
        public void UpdateLocation(Location location) {
            //toast("经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude(), LONG_SHOW);
            if (location != null) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();

                if(hasClickClock == true) {
                    hasClickClock = false;
                    Intent intent = new Intent(getApplicationContext(), ClockInActivity.class);
                    intent.putExtra("longitude", mLongitude);
                    intent.putExtra("latitude", mLatitude);
                    startActivity(intent);
                }
            } else {
                toast("请检查GPS或网络是否开启", LONG_SHOW);
            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps" == provider) {
                Toast.makeText(HomeActivity.this, "定位类型：" + provider, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {
            switch (gpsStatus) {
                case GPSProviderStatus.GPS_ENABLED:
                    Toast.makeText(HomeActivity.this, "GPS开启", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_DISABLED:
                    Toast.makeText(HomeActivity.this, "GPS关闭", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_OUT_OF_SERVICE:
                    Toast.makeText(HomeActivity.this, "GPS不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(HomeActivity.this, "GPS暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case GPSProviderStatus.GPS_AVAILABLE:
                    Toast.makeText(HomeActivity.this, "GPS可用啦", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    ////////////////////////////presenter相关//////////////////////////////
    @Override
    public String getJWAccount() {
        String jwAccount = PrefManager.getString(getApplicationContext(), PrefManager.JW_ACCOUNT, "");
        return jwAccount;
    }

    @Override
    public void showResult(int what, String msg) {
        if(CAN_OK_CLOCKIN == what) { // 当前有课，可以考勤
            handler.obtainMessage(GPS_START).sendToTarget();
        } else if(CURR_NO_COURSE == what) { // 没有课程需要考勤
            customToast(msg, SHORT_SHOW);
        } else if(NOT_IMPORT_COURSE == what) { // 检测到没有导入课表
            AlertDialogUtils.DialogListener cancel = new AlertDialogUtils.DialogListener() {
                @Override
                public void callback(String[] array) {

                }
            };
            AlertDialogUtils.DialogListener ensure = new AlertDialogUtils.DialogListener() {
                @Override
                public void callback(String[] array) {
                    intent(getApplicationContext(), ImportTableActivity.class);
                }
            };
            AlertDialogUtils.alert(HomeActivity.this, AlertDialogUtils.TYPE_INFO, "提示", msg,
                    ensure, "确定", cancel, "取消");
        } else if(SHOW_MSG == what) {
            toast(msg, SHORT_SHOW);
        }
    }
}
