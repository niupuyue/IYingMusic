package com.paulniu.iyingmusic.db.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Coder: niupuyue
 * Date: 2020/1/9
 * Time: 9:56
 * Desc: 本地歌曲文件夹
 * Version:
 */
@Entity(indices = {@Index(value = {"folderId"})})
public class FolderInfo implements Parcelable {

    public static String KEY_FOLDER_ID = "folderId";
    public static String KEY_FOLDER_NAME = "folderName";
    public static String KEY_FOLDER_PATH = "folderPath";

    @PrimaryKey(autoGenerate = true)
    public int folderId;

    @ColumnInfo(name = "folderName")
    public String folderName;

    @ColumnInfo(name = "folderPath")
    public String folderPath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FOLDER_ID, folderId);
        bundle.putString(KEY_FOLDER_NAME, folderName);
        bundle.putString(KEY_FOLDER_PATH, folderPath);
        dest.writeBundle(bundle);
    }

    // 用来创建自定义的Parcelable的对象
    public static Parcelable.Creator<FolderInfo> CREATOR = new Parcelable.Creator<FolderInfo>() {

        @Override
        public FolderInfo createFromParcel(Parcel source) {
            FolderInfo info = new FolderInfo();
            Bundle bundle = source.readBundle();
            info.folderId = bundle.getInt(KEY_FOLDER_ID);
            info.folderName = bundle.getString(KEY_FOLDER_NAME);
            info.folderPath = bundle.getString(KEY_FOLDER_PATH);
            return info;
        }

        @Override
        public FolderInfo[] newArray(int size) {
            return new FolderInfo[size];
        }
    };
}
