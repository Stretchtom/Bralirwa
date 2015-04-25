package com.example.wendy_guo.j4sp.pojo;

import android.media.Image;

/**
 * Created by Wendy_Guo on 3/17/15.
 */
public class DownloadRecord {
    private Image mImage;
    private String mName;
    private String mDate;
    private String mTotal;
    private String mComments;

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    private String mType;
    private long _ID;



    public DownloadRecord(String name, String date, String total, String comments, long _id, String type,Image i ){
        mName = name;
        mDate = date;
        mTotal = total;
        mComments = comments;
        _ID = _id;
        mImage = i;
        mType = type;
    }
    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String mTotal) {
        this.mTotal = mTotal;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String mComments) {
        this.mComments = mComments;
    }

    public long getID() {
        return _ID;
    }

    public void setID(long _ID) {
        this._ID = _ID;
    }

    public Image getmImage() {
        return mImage;
    }

    public void setmImage(Image mImage) {
        this.mImage = mImage;
    }

}
