package com.paulniu.iyingmusic.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.paulniu.iyingmusic.Constant;
import com.paulniu.iyingmusic.db.converters.MusicConverter;
import com.paulniu.iyingmusic.db.dao.FolderInfoDao;
import com.paulniu.iyingmusic.db.dao.SongInfoDao;
import com.paulniu.iyingmusic.db.entity.FolderInfo;
import com.paulniu.iyingmusic.db.entity.SongInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 21:53
 * Desc: 数据库操作
 * Version:
 */
@Database(entities = {FolderInfo.class, SongInfo.class}, version = 1)
@TypeConverters({MusicConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE = null;

    public abstract SongInfoDao getSongInfoDao();

    public abstract FolderInfoDao getFolderInfoDao();

    public static final Migration mirgration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 删除本地歌曲表
            database.execSQL("DROP TABLE IF EXISTS `SongInfo`");
            // 创建本地歌曲表
            database.execSQL("CREATE TABLE IF NOT EXISTS `SongInfo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `songName` TEXT,`folderId` INTEGER NOT NULL,`favorite` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `artist` TEXT, `album` TEXT, `album_id` TEXT, `album_path` TEXT, `year` INTEGER NOT NULL, `data` TEXT, `size` INTEGER NOT NULL, `mime_type` TEXT, `date_modified` INTEGER NOT NULL, `display_name` TEXT, `date_added` INTEGER NOT NULL)");
            // 创建本地歌曲索引
            database.execSQL("CREATE  INDEX `index_SongInfo_id` ON `SongInfo` (`id`)");

            // 删除本地歌曲文件夹表
            database.execSQL("DROP TABLE IF EXISTS `FolderInfo`");
            // 创建本地歌曲文件夹表
            database.execSQL("CREATE TABLE IF NOT EXISTS `FolderInfo` (`folderId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,`folderName` TEXT NOT NULL,`folderPath` TEXT NOT NULL)");
            // 创建本地歌曲文件夹索引
            database.execSQL("CREATE INDEX `index_FolderInfo_folderId` ON `FolderInfo` (`folderId`)");
        }
    };

    public static AppDataBase getInstance(Context context) {
        synchronized (AppDataBase.class) {
            if (null == INSTANCE) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, Constant.DATABASE_NAME)
                        .setJournalMode(JournalMode.TRUNCATE)
                        .allowMainThreadQueries()
                        .addMigrations(mirgration_1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

}
