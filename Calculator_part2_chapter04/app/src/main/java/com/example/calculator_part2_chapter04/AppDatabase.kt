package com.example.calculator_part2_chapter04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator_part2_chapter04.dao.HistoryDao
import com.example.calculator_part2_chapter04.model.History

// version은 앱 업데이트 시 데이터베이스 마이그레이션을 위해 작성
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao   // AppDatabase를 사용할 때 HistoryDao를 가져와서 사용
}