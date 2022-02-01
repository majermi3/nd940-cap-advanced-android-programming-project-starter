package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election

class ElectionsRepository(private val electionsDao: ElectionDao): DataSource {
    suspend fun getAll(): List<Election> {
        return electionsDao.getAll()
    }
}