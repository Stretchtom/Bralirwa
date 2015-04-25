package com.example.wendy_guo.j4sp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class UploadRecord implements Parcelable {
    private String mName;
    private String mDate;
    private String mTotal;
    private String mComments;
    private long rID;
    private String mPath;


    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }



    public String getComments() {
        return mComments;
    }

    public void setComments(String mComments) {
        this.mComments = mComments;
    }

    public long getrID() {
        return rID;
    }

    public void setrID(long ID) {
        this.rID = ID;
    }

    public UploadRecord(String name, String date, String total, String comments,String path,long rid){
        mName = name;
        mDate = date;
        mTotal = total;
        mComments = comments;
        mPath = path;
        rID = rid;

    }
    public UploadRecord(String name, String date, String total, String comments){
        mName = name;
        mDate = date;
        mTotal = total;
        mComments = comments;

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
    public UploadRecord(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);

        this.mName = data[0];
        this.mDate = data[1];
        this.mTotal = data[2];
        this.mComments = data[3];
        this.mPath = data[4];
        this.rID = Long.parseLong(data[5]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {mName,mDate,mTotal,
                mComments,mPath,rID+""});

    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UploadRecord createFromParcel(Parcel in) {
            return new UploadRecord(in);
        }

        public UploadRecord[] newArray(int size) {
            return new UploadRecord[size];
        }
    };
}
