package org.onyx.moneymoney.database


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.onyx.moneymoney.App
import org.onyx.moneymoney.database.coverters.Coverters
import org.onyx.moneymoney.database.dao.RecordDao
import org.onyx.moneymoney.database.dao.RecordTypeDao
import org.onyx.moneymoney.database.entity.Record
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.datasource.RecordTypeDataInit

/**
 * App database
 * 数据库操作的入口，单例模式
 *
 * @constructor Create empty App database
 */
@Database(entities = [Record::class, RecordType::class], version = 1, exportSchema = false)
@TypeConverters(Coverters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME: String = "MoneyMoney.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    App.getInstance(),
                    AppDatabase::class.java,
                    DB_NAME
                ).build().also { INSTANCE = it }
            }
    }

    // RecordDao由Room自动生成实现
    abstract fun getRecordDao(): RecordDao

    // RecordTypeDao由Room自动生成实现
    abstract fun getRecordTypeDao(): RecordTypeDao
}