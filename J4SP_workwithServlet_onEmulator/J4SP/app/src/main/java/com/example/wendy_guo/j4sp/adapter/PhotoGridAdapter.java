package com.example.wendy_guo.j4sp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.wendy_guo.j4sp.R;

/**
 * Created by Wendy_Guo on 4/4/15.
 */
public class PhotoGridAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor cursor ;
    private static Uri[] mUrls = null;
    private static String[] strUrls = null;

    public  PhotoGridAdapter(Context c) {
        mContext = c;
        cursor= mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (cursor != null){
            try {
                cursor.moveToFirst();
                mUrls = new Uri[cursor.getCount()];
                strUrls = new String[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    mUrls[i] = Uri.parse(cursor.getString(1));
                    strUrls[i] = cursor.getString(1);
                }

            } catch (Exception e) {
            }

        }
    }

    public int getCount() {
        return cursor.getCount();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Bitmap bmp = decodeURI(mUrls[position].getPath());

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(450,450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bmp);
        return imageView;
    }

    public Bitmap decodeURI(String filePath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }
}

