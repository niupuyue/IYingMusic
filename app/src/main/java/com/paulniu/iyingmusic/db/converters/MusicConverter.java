package com.paulniu.iyingmusic.db.converters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * Coder: niupuyue (牛谱乐)
 * Date: 2020-01-11
 * Time: 23:32
 * Desc: 数据库类型转换
 * Version:
 */
public class MusicConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
