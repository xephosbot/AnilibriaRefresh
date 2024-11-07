package com.xbot.data.datasource

import com.skydoves.sandwich.suspendOnSuccess
import com.xbot.api.service.AnilibriaClient
import com.xbot.data.mapper.SuccessAgeRatingsMapper
import com.xbot.data.mapper.SuccessGenresMapper
import com.xbot.data.mapper.SuccessProductionStatusesMapper
import com.xbot.data.mapper.SuccessPublishStatusMapper
import com.xbot.data.mapper.SuccessReleaseTypesMapper
import com.xbot.data.mapper.SuccessSeasonMapper
import com.xbot.data.mapper.SuccessSortingTypesMapper
import com.xbot.data.utils.handleErrors
import com.xbot.domain.model.AgeRatingEnumModel
import com.xbot.domain.model.GenreModel
import com.xbot.domain.model.ProductionStatusesEnumModel
import com.xbot.domain.model.PublishStatusEnumModel
import com.xbot.domain.model.ReleaseTypeEnumModel
import com.xbot.domain.model.SeasonEnumModel
import com.xbot.domain.model.SortingTypesEnumModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilterDataSource @Inject constructor(
    private val client: AnilibriaClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getAgeRatings(): Flow<List<AgeRatingEnumModel>> = flow {
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

    fun getProductionStatuses(): Flow<List<ProductionStatusesEnumModel>> = flow {
        val response = client.getProductionStatuses()
        response.suspendOnSuccess(SuccessProductionStatusesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getPublishStatuses(): Flow<List<PublishStatusEnumModel>> = flow {
        val response = client.getPublishStatuses()
        response.suspendOnSuccess(SuccessPublishStatusMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getSeasons(): Flow<List<SeasonEnumModel>> = flow {
        val response = client.getSeasons()
        response.suspendOnSuccess(SuccessSeasonMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getSortingTypes(): Flow<List<SortingTypesEnumModel>> = flow {
        val response = client.getSortingTypes()
        response.suspendOnSuccess(SuccessSortingTypesMapper) {
            emit(this)
        }.handleErrors(TAG)
    }.flowOn(dispatcher)

    fun getReleaseType(): Flow<List<ReleaseTypeEnumModel>> = flow {
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