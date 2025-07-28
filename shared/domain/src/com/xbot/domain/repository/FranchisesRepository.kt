package com.xbot.domain.repository

import androidx.paging.PagingSource
import com.xbot.domain.models.Franchise

interface FranchisesRepository {
    suspend fun getFranchises(): PagingSource<Int, Franchise>
    suspend fun getFranchise(franchiseId: String): Result<Franchise>
    suspend fun getRandomFranchises(limit: Int): Result<List<Franchise>>
    suspend fun getReleaseFranchises(releaseId: String): Result<List<Franchise>>
}