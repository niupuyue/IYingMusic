package com.paulniu.iyingmusic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.paulniu.iyingmusic.db.entity.FolderInfo;

import java.util.List;

/**
 * Coder: niupuyue
 * Date: 2020/1/9
 * Time: 10:14
 * Desc: 文件夹操作类
 * Version:
 */
@Dao
public abstract class FolderInfoDao {

    @Update
    public abstract void update(FolderInfo... folderInfos);

    @Insert
    public abstract void insert(FolderInfo... folderInfos);

    @Delete
    public abstract void delete(FolderInfo... folderInfos);

    /**
     * 根据id查找文件夹
     */
    @Query("select * from FolderInfo where folderId = :folderId")
    public abstract FolderInfo findFolderById(int folderId);

    /**
     * 查找本地所有存储的文件夹
     */
    @Query("select * from FolderInfo")
    public abstract List<FolderInfo> findAllFolder();

}
