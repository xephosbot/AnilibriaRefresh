package com.xbot.data.mapper

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Genre
import com.xbot.domain.models.GenreModel

object SuccessGenresMapper: ApiSuccessModelMapper<List<Genre>, List<GenreModel>> {
    override fun map(apiSuccessResponse: ApiResponse.Success<List<Genre>>): List<GenreModel> {
        val genres = apiSuccessResponse.data
        return genres.map { genre ->
            GenreModel(
                id = genre.id,
                name = genre.name
            )
        }
    }
}