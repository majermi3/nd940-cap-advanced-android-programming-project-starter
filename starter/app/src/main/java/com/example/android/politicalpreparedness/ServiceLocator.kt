package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionsRepository

object ServiceLocator {
    private var database: ElectionDatabase? = null

    @Volatile
    var electionsRepository: ElectionsRepository? = null
        @VisibleForTesting set

    fun provideElectionsRepository(context: Context): ElectionsRepository {
        synchronized(this) {
            return electionsRepository ?: createElectionsRepository(context)
        }
    }

    private fun createElectionsRepository(context: Context): ElectionsRepository {
        val database = database ?: createDataBase(context)
        val newRepo = ElectionsRepository(database.electionDao)
        electionsRepository = newRepo
        return newRepo
    }

    private fun createDataBase(context: Context): ElectionDatabase {
        val result = Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java, "election_database"
        ).build()
        database = result
        return result
    }
}