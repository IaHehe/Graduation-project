package com.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.R;
import com.library.widget.util.FastDoubleClickUtils;

/**
 * @author Dongjun Zou
 * @Description 自定义导航栏（自定义布局文件）
 * @email 984147586@qq.com
 * @date 2017/4/30 22:43
 */

public class NavigationBar extends LinearLayout {

    private OnLeftButtonClickListener mLeftButtonClickListener;
    private OnRightButtonClickListener mRightButtonClickListener;
    private NavigationBarViewHolder mViewHolder;
    private View viewAppTitle;

    public NavigationBar(Context context) {
        super(context);
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    /**
     * 使用该注解(@TargetApi(Build.VERSION_CODES.HONEYCOMB))的方法适用于
     * 系统版本  为3.0及以上系统的手机
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public NavigationBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init();
    }

    private void init()
    {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewAppTitle = inflater.inflate(R.layout.navigationbar, null);
        this.addView(viewAppTitle, layoutParams);

        mViewHolder = new NavigationBarViewHolder(this);
        mViewHolder.llLeftGoBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (FastDoubleClickUtils.isFastDoubleClick())
                {
                    return;
                }

                if (mLeftButtonClickListener != null)
                {
                    mLeftButtonClickListener.onLeftButtonClick(v);
                }
            }
        });
        mViewHolder.llRight.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (FastDoubleClickUtils.isFastDoubleClick())
                {
                    return;
                }

                if (mRightButtonClickListener != null)
                {
                    mRightButtonClickListener.OnRightButtonClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftButtonVisile, boolean isCenterTitleVisile, boolean isRightIconVisile, boolean isRightTitleVisile)
    {
        // 左侧返回
        mViewHolder.llLeftGoBack.setVisibility(isLeftButtonVisile ? View.VISIBLE : View.INVISIBLE);

        // 中间标题
        mViewHolder.tvCenterTitle.setVisibility(isCenterTitleVisile ? View.VISIBLE : View.INVISIBLE);

        // 右侧返回图标,文字
        if (!isRightIconVisile && !isRightTitleVisile)
        {
            mViewHolder.llRight.setVisibility(View.INVISIBLE);
        }
        else
        {
            mViewHolder.llRight.setVisibility(View.VISIBLE);
        }
        mViewHolder.ivRightComplete.setVisibility(isRightIconVisile ? View.VISIBLE : View.GONE);
        mViewHolder.tvRightComplete.setVisibility(isRightTitleVisile ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setLeftTitle(String title) {
        if(!TextUtils.isEmpty(title)) {
            mViewHolder.tvLeftComplete.setText(title);
        }
    }

    // 为导航栏左边按钮设置图标
    public void setLeftDrawableLeft(int sourceId) {
        Drawable drawable = getResources().getDrawable(sourceId, null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mViewHolder.tvLeftComplete.setText("");
        mViewHolder.tvLeftComplete.setCompoundDrawables(drawable, null, null, null);
    }

    public void setAppTitle(String title)
    {
        if (!TextUtils.isEmpty(title))
        {
            mViewHolder.tvCenterTitle.setText(title);
        }
    }

    public void setRightTitle(String text)
    {
        if (!TextUtils.isEmpty(text))
        {
            mViewHolder.tvRightComplete.setText(text);
        }
    }

    public void setRightIcon(int sourceID)
    {
        mViewHolder.ivRightComplete.setImageResource(sourceID);
    }

    public void setLeftOnclick(OnLeftButtonClickListener mOnLeftButtonClickListener)
    {
        if (mOnLeftButtonClickListener != null)
        {
        }
    }

    public void setAppBackground(int color)
    {
        viewAppTitle.setBackgroundColor(color);
    }

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listen)
    {
        mLeftButtonClickListener = listen;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listen)
    {
        mRightButtonClickListener = listen;
    }

    public static abstract interface OnLeftButtonClickListener
    {
        public abstract void onLeftButtonClick(View v);
    }

    public static abstract interface OnRightButtonClickListener
    {
        public abstract void OnRightButtonClick(View v);
    }

    private static class NavigationBarViewHolder {
        LinearLayout llLeftGoBack;
        TextView tvLeftComplete;
        TextView tvCenterTitle;
        LinearLayout llRight;
        ImageView ivRightComplete;
        TextView tvRightComplete;

        public NavigationBarViewHolder(View v)
        {
            llLeftGoBack = (LinearLayout) v.findViewById(R.id.llLeftGoBack);
            tvLeftComplete = (TextView) v.findViewById(R.id.tvLeftComplete);
            tvCenterTitle = (TextView) v.findViewById(R.id.tvCenterTitle);
            llRight = (LinearLayout) v.findViewById(R.id.llRight);
            ivRightComplete = (ImageView) v.findViewById(R.id.ivRightComplete);
            tvRightComplete = (TextView) v.findViewById(R.id.tvRightComplete);
        }
    }
}
