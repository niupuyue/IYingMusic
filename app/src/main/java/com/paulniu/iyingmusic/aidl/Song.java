package com.paulniu.iyingmusic.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Coder: niupuyue
 * Date: 2020/1/17
 * Time: 10:08
 * Desc: aidl中使用的SongInfo对象，为了更好的传递数据，只需要传递data数据
 * Version:
 */
public class Song implements Parcelable {

    //与客户端 SongInfo 中的 data 域对应，对于同一首歌曲（文件路径相同），两者应该相同
    public String path;

    public Song(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return path.equals(song.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
    }

    protected Song(Parcel in) {
        this.path = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
