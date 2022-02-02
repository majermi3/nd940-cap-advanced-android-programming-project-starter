package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    @Query("SELECT * FROM ELECTION_TABLE WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): Election?

    @Query("SELECT * FROM ELECTION_TABLE")
    suspend fun getAll(): List<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    @Query("DELETE FROM ELECTION_TABLE WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("DELETE FROM ELECTION_TABLE")
    suspend fun deleteAll(): Int
}