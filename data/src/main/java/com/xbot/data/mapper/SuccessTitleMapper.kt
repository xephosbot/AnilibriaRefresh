import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Release
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.EpisodeModel
import com.xbot.domain.model.MemberModel
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleDetailModel

object SuccessTitleMapper : ApiSuccessModelMapper<Release, TitleDetailModel> {
    override fun map(apiSuccessResponse: ApiResponse.Success<Release>): TitleDetailModel {
        val title = apiSuccessResponse.data
        return TitleDetailModel(
            id = title.id,
            type = title.type.description ?: "",
            year = title.year,
            name = title.name.main,
            season = title.description ?: "",
            description = title.description ?: "",
            poster = PosterModel(
                src = title.poster.optimized.src,
                thumbnail = title.poster.optimized.thumbnail
            ),
            freshAt = title.freshAt ?: "",
            createdAt = title.createdAt ?: "",
            updatedAt = title.updatedAt ?: "",
            isOngoing = title.isOngoing,
            ageRating = title.ageRating.label,
            publishDay = DayOfWeek.fromInt(title.publishDay.value),
            notification = title.notification ?: "",
            episodesTotal = title.episodesTotal,
            isInProduction = title.isInProduction,
            addedInUsersFavorites = title.addedInUsersFavorites,
            averageDurationOfEpisode = title.averageDurationOfEpisode,
            genres = title.genres?.map { it.name } ?: listOf(),
            members = title.members?.map {
                MemberModel(
                    id = it.id,
                    name = it.nickname,
                    role = it.role.description
                )
            } ?: listOf(),
            episodes = title.episodes?.map {
                EpisodeModel(
                    id = it.id,
                    name = it.name,
                    duration = it.duration,
                    preview = PosterModel(
                        src = it.preview.optimized.src,
                        thumbnail = it.preview.optimized.thumbnail
                    ),
                    ordinal = it.ordinal
                )
            } ?: listOf()
        )
    }
}