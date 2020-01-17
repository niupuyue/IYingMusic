package com.paulniu.iyingmusic.interfaces;

import android.content.ComponentName;
import android.os.IBinder;

public interface OnServiceConnect {

    void onConnected(ComponentName name, IBinder service);

    void disConnected(ComponentName name);
}
