package com.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.library.R;
import com.library.widget.adapter.PopupViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/8 00:42
 */

public class PopupView extends LinearLayout{

    private Context context;

    private LinearLayout mParentLinearLayout; // 当前类中放置一个线性布局，父LinearLayout， 放置多个子线性布局
    private List<LinearLayout> mChildLinearLayout; // 父LinearLayout放置的子LinearLayout，用于放置具体的下拉选择内容

    private int mPopupNum; // 下拉选择列表的个数
    private CharSequence[] defaultText; // 用于下拉列表默认显示的值
    @ColorInt
    private int defaultTextColor; // 页面顶部下拉选择菜单默认字体颜色
    @ColorInt
    private int selectedTextColor; // 下拉选择菜单被选中后的字体颜色


    private ListView mPopupListView; // ListView用于放置下拉列表的选项

    private List<PopupViewAdapter> mPopupViewAdapters; // ListView对应的Adapter

    private View mPopupView; // 对应PopupWindow的布局(R.layout.popwin_supplier_list)
    private PopupWindow mPopupWindow;

    private int menuIndex = -1; // 记录当前选择的下拉列表的菜单索引，如果有3个下拉选项，则其值为0~2
    private String [] currentSelectItem; // 存储点击每个下拉选择项后的值

    private OnItemSelectedListener mItemSelectedListener;

    public PopupView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 获取用户设置的属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PopupView);
        mPopupNum = ta.getInteger(R.styleable.PopupView_popupNum, 1);
        defaultText = ta.getTextArray(R.styleable.PopupView_defaultTxt);
        defaultTextColor = ta.getColor(R.styleable.PopupView_defaultTxtColor, 0);
        selectedTextColor = ta.getColor(R.styleable.PopupView_selectedTxtColor, 0);

        currentSelectItem = new String[mPopupNum]; // 以下拉选项菜单个数初始化currentSelectItem，存储选择的值
        ta.recycle();

        init();
        createChildLinearLayout();
        createSelectList();
        createPopupWindow();
        createPopupListView();
    }

    private void init() {
        // 设置父LinearLayout
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(context, 40));
        mParentLinearLayout = new LinearLayout(context);
        mParentLinearLayout.setOrientation(HORIZONTAL);
        mParentLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));//Color.parseColor("#ffffff")
        mParentLinearLayout.setLayoutParams(layoutParams);
        this.addView(mParentLinearLayout);
    }

    // 在初始化父LinearL后，根据设置的下拉选择列表设置的数目创建子LinearLayout(mChildLinearLayout)
    private void createChildLinearLayout() {
        LayoutParams childLayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        mChildLinearLayout = new ArrayList<LinearLayout>();
        LinearLayout childLinearLayout;
        for(int i = 0; i < mPopupNum; i ++) {
            childLinearLayout = new LinearLayout(context);
            childLinearLayout.setGravity(Gravity.CENTER);
            childLinearLayout.setOrientation(HORIZONTAL);
            childLinearLayout.setLayoutParams(childLayoutParams);

            mChildLinearLayout.add(childLinearLayout);// 将新创建的childLinearLayout保存起来
            mParentLinearLayout.addView(childLinearLayout); // 加入父布局
        }
    }

    /**
     * 每个childLinearLayout放置具体的选择视图，选择视图由TextView和ImageView组成
     */
    private void createSelectList() {
        LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams imageViewParams = new LayoutParams(dip2px(context, 25), dip2px(context, 25));

        TextView mTextView;
        ImageView mImageView;

        for(int i = 0; i < mChildLinearLayout.size(); i ++) {
            mTextView = new TextView(context);
            mTextView.setTextSize(14);
            if(i <= defaultText.length) {
                mTextView.setText(defaultText[i].toString());
            } else {
                mTextView.setText("请选择");
            }
            mTextView.setTextColor(defaultTextColor != 0 ? defaultTextColor
                    : getResources().getColor(R.color.selectMenuDefaultTxtColor));
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setLayoutParams(textViewParams);

            mImageView = new ImageView(context);
            mImageView.setImageResource(R.drawable.icon_arrow_down);
            mImageView.setLayoutParams(imageViewParams);

            mChildLinearLayout.get(i).addView(mTextView);
            mChildLinearLayout.get(i).addView(mImageView);
        }
    }

    private void createPopupWindow() {
        mPopupView = View.inflate(context, R.layout.popup_window_listview, null);
        mPopupWindow = new PopupWindow(mPopupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.popup_window_anim_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                for(int i = 0; i < mChildLinearLayout.size(); i ++) {
                    ((TextView) mChildLinearLayout.get(i)
                            .getChildAt(0))
                            .setTextColor(defaultTextColor != 0 ? defaultTextColor
                                : getResources().getColor(R.color.selectMenuDefaultTxtColor));
                }
            }
        });
    }

    /**
     * PopupWindow要放置的ListView
     */
    private void createPopupListView() {
        mPopupListView = (ListView) mPopupView.findViewById(R.id.popwin_supplier_list_lv);
        mPopupView.findViewById(R.id.popwin_supplier_list_bottom).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    public void setPopupViewAdapter(List<PopupViewAdapter> popupViewAdapters, final OnItemSelectedListener selectedListener) {
        this.mPopupViewAdapters = popupViewAdapters;

        setPopupListViewItemClick(selectedListener); // 设置ListView中Item的点击事件

        for(int i = 0; i < mChildLinearLayout.size(); i ++) {
            final int index = i;
            mChildLinearLayout.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(menuIndex != -1) {
                        mPopupWindow.dismiss();
                    }
                    ((TextView) mChildLinearLayout.get(index)
                            .getChildAt(0))
                            .setTextColor(selectedTextColor != 0 ? selectedTextColor
                                    : getResources().getColor(R.color.selectedTxtColor));
                    mPopupListView.setAdapter(mPopupViewAdapters.get(index));
                    mPopupWindow.showAsDropDown(mChildLinearLayout.get(index), 0, 2);
                    menuIndex = index; // 记录点击的哪一个下拉列表菜单
                }
            });
        }
    }

    private void setPopupListViewItemClick(final OnItemSelectedListener selectedListener) {

        mPopupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mPopupWindow.dismiss();

                currentSelectItem[menuIndex] = (String) mPopupViewAdapters.get(menuIndex).getItem(position);
                ((TextView) mChildLinearLayout.get(menuIndex).getChildAt(0)).setText(currentSelectItem[menuIndex]);

                selectedListener.onItemSelected(currentSelectItem);
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public  interface OnItemSelectedListener {
        void onItemSelected(String[] result);
    }

}
