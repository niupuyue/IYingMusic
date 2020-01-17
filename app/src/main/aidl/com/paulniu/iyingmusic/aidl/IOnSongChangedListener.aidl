// IOnSongChangedListener.aidl
package com.paulniu.iyingmusic.aidl;
import com.paulniu.iyingmusic.aidl.Song;

// Declare any non-default types here with import statements

interface IOnSongChangedListener {

    // 在线程池中运行
    void onSongChange(in Song which,int index,boolean isNext);

}
