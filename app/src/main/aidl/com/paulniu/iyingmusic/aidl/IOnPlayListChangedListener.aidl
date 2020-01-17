// IOnPlayListChangedListener.aidl
package com.paulniu.iyingmusic.aidl;
import com.paulniu.iyingmusic.aidl.Song;

// Declare any non-default types here with import statements

interface IOnPlayListChangedListener {

    void onPlayListChange(in Song current,int index,int id);

}
