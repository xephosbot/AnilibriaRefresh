import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiSuccessModelMapper
import com.xbot.api.models.Anime
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleModel

object SuccessTitleMapper : ApiSuccessModelMapper<Anime, TitleModel> {
    override fun map(apiSuccessResponse: ApiResponse.Success<Anime>): TitleModel {
        val title = apiSuccessResponse.data
        return TitleModel(
            id = title.id,
            name = title.name.main,
            description = title.description ?: "",
            poster = PosterModel(
                src = title.poster.optimized.src,
                thumbnail = title.poster.optimized.thumbnail
            ),
            uploadedTime = title.updatedAt
        )
    }
}