package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add select single election query
    @Query("SELECT * FROM ELECTION_TABLE WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): Election

    //TODO: Add select all election query
    @Query("SELECT * FROM ELECTION_TABLE")
    suspend fun getAll(): List<Election>

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    //TODO: Add delete query
    @Query("DELETE FROM ELECTION_TABLE WHERE id = :id")
    suspend fun delete(id: Int): Int

    //TODO: Add clear query
    @Query("DELETE FROM ELECTION_TABLE")
    suspend fun deleteAll(): Int

}