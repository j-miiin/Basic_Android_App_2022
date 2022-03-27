package com.example.calculator_part2_chapter04.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calculator_part2_chapter04.model.History

@Dao
interface HistoryDao {

    // 모든 history 조회
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

//    // 하나만 삭제
//    @Delete
//    fun delete(history: History)
//
//    // LIMIT 1 : 1개만 반환
//    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
//    fun findByResult(result: String)
}