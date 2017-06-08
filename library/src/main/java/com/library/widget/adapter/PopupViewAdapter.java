package com.library.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.library.R;


/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/8 02:26
 */

public class PopupViewAdapter extends BaseAdapter {
    private String[] names;
    private Context context;

    public PopupViewAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public int getCount() {
        return this.names.length;
    }

    @Override
    public Object getItem(int position) {
        return this.names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if(convertView == null) {
            view = View.inflate(context, R.layout.item_listview_popup_window, null);
        } else {
            view = convertView;
        }

        TextView mTextView = (TextView) view.findViewById(R.id.listview_popwind_tv);
        mTextView.setText(this.names[position]);

        return view;
    }
}
