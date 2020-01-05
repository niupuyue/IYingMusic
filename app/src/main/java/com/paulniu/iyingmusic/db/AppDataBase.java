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
import com.paulniu.iyingmusic.db.dao.MusicInfoDao;
import com.paulniu.iyingmusic.model.MusicInfo;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-05
 * Time: 21:53
 * Desc: 数据库操作
 * Version:
 */
@Database(entities = {MusicInfo.class}, version = 1)
@TypeConverters({})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE = null;

    public abstract MusicInfoDao getMusicInfoDao();

    public static final Migration mirgration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 删除本地歌曲表
            database.execSQL("DROP TABLE IF EXISTS `MusicInfo`");
            // 创建本地歌曲表
            database.execSQL("CREATE TABLE IF NOT EXISTS `MusicInfo` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `songId` INTEGER NOT NULL, `albumId` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `musicName` TEXT, `artist` TEXT, `data` TEXT, `folder` TEXT, `musicNameKey` TEXT, `artistKey` TEXT, `favorite` INTEGER NOT NULL)");
            // 创建本地歌曲索引
            database.execSQL("CREATE  INDEX `index_MusicInfo__id_songId_albumId` ON `${TABLE_NAME}` (`_id`, `songId`, `albumId`)");
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
