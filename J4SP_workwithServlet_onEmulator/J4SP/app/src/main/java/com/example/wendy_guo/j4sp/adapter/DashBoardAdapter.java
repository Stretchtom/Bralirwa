package com.example.wendy_guo.j4sp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.wendy_guo.j4sp.R;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class DashBoardAdapter extends BaseAdapter {
    private Context mContext;

    public DashBoardAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(450, 450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


    private Integer[] mThumbIds = {
            R.drawable.ic_dark_upload, R.drawable.ic_dark_history,
            R.drawable.ic_dark_photo, R.drawable.ic_dark_camera,
            R.drawable.ic_dark_search, R.drawable.ic_dark_share,
            R.drawable.ic_dark_web,R.drawable.ic_dark_setting

    };
}