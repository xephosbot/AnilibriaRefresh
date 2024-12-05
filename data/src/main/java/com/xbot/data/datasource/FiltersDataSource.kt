package com.xbot.data.datasource

import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.AnilibriaClient
import com.xbot.data.mapper.SuccessAgeRatingsMapper
import com.xbot.data.mapper.SuccessGenresMapper
import com.xbot.data.mapper.SuccessProductionStatusesMapper
import com.xbot.data.mapper.SuccessPublishStatusesMapper
import com.xbot.data.mapper.SuccessReleaseTypesMapper
import com.xbot.data.mapper.SuccessSeasonsMapper
import com.xbot.data.mapper.SuccessSortingTypesMapper
import com.xbot.data.utils.handleErrors
import com.xbot.domain.models.GenreModel
import com.xbot.domain.models.enums.AgeRating
import com.xbot.domain.models.enums.ProductionStatus
import com.xbot.domain.models.enums.PublishStatus
import com.xbot.domain.models.enums.ReleaseType
import com.xbot.domain.models.enums.Season
import com.xbot.domain.models.enums.SortingTypes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FiltersDataSource(
    private val client: AnilibriaClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun getAgeRatings(): Flow<List<AgeRating>> = flow {
        val response = client.getAgeRatings()
        response.suspendOnSuccess(SuccessAgeRatingsMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getGenres(): Flow<List<GenreModel>> = flow {
        val response = client.getGenres()
        response.suspendOnSuccess(SuccessGenresMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getProductionStatuses(): Flow<List<ProductionStatus>> = flow {
        val response = client.getProductionStatuses()
        response.suspendOnSuccess(SuccessProductionStatusesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getPublishStatuses(): Flow<List<PublishStatus>> = flow {
        val response = client.getPublishStatuses()
        response.suspendOnSuccess(SuccessPublishStatusesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getSeasons(): Flow<List<Season>> = flow {
        val response = client.getSeasons()
        response.suspendOnSuccess(SuccessSeasonsMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getSortingTypes(): Flow<List<SortingTypes>> = flow {
        val response = client.getSortingTypes()
        response.suspendOnSuccess(SuccessSortingTypesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getReleaseType(): Flow<List<ReleaseType>> = flow {
        val response = client.getTypeReleases()
        response.suspendOnSuccess(SuccessReleaseTypesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getYears(): Flow<List<Int>> = flow {
        val response = client.getYears()
        response.suspendOnSuccess {
            emit(data)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    companion object {
        private const val TAG = "FiltersDataSource"
    }
}
