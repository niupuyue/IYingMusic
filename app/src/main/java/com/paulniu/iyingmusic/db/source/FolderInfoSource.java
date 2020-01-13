package com.paulniu.iyingmusic.db.source;

import com.paulniu.iyingmusic.App;
import com.paulniu.iyingmusic.db.AppDataBase;
import com.paulniu.iyingmusic.db.entity.FolderInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-11
 * Time: 23:16
 * Desc: 文件夹操作对外接口类
 * Version:
 */
public class FolderInfoSource {

    /**
     * 获取默认文件夹(我的最爱)
     */
    public static FolderInfo getDefaultFolder() {
        return AppDataBase.getInstance(App.getContext()).getFolderInfoDao().findFolderById(1);
    }

    /**
     * 根据id获取文件夹
     */
    public static FolderInfo getFolderInfoById(int folderId) {
        if (folderId <= 0) {
            return null;
        }
        return AppDataBase.getInstance(App.getContext()).getFolderInfoDao().findFolderById(folderId);
    }

    /**
     * 新建文件夹/更新文件夹
     */
    public static long updateOrInsertFolder(FolderInfo folderInfo) {
        if (null == folderInfo) {
            return -1;
        }
        if (folderInfo.folderId <= 0) {
           return AppDataBase.getInstance(App.getContext()).getFolderInfoDao().insert(folderInfo)[0];
        } else {
            FolderInfo oldFolderInfo = getFolderInfoById(folderInfo.folderId);
            if (null != oldFolderInfo) {
                folderInfo.folderId = oldFolderInfo.folderId;
                 AppDataBase.getInstance(App.getContext()).getFolderInfoDao().update(folderInfo);
                 // 如果返回0，表示更新成功 TODO
                 return 0;
            }
        }
        return -1;
    }

    /**
     * 删除文件夹
     */
    public static void deleteFolderById(int folderId) {
        if (folderId <= 0) {
            return;
        }
        FolderInfo folderInfo = getFolderInfoById(folderId);
        if (null != folderInfo) {
            AppDataBase.getInstance(App.getContext()).getFolderInfoDao().delete(folderInfo);
        }
    }

}
