package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election

class ElectionsRepository(private val electionsDao: ElectionDao) {
    suspend fun getAll(): List<Election> {
        return electionsDao.getAll()
    }

    suspend fun get(electionId: Int): Election? {
        return electionsDao.get(electionId)
    }

    suspend fun delete(electionId: Int) {
        electionsDao.delete(electionId)
    }

    suspend fun insert(election: Election) {
        electionsDao.insert(election)
    }
}