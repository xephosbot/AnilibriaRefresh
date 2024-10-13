package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.Anime
import com.xbot.api.models.AnimeCatalogResponse
import javax.inject.Inject

class AnilibriaClient @Inject constructor(
    private val service: AnilibriaService
) {
    /**
     * Получение списка релизов аниме с применением различных фильтров.
     *
     * @param page Номер страницы в выдаче.
     * @param limit Количество релизов на странице.
     * @param genres Список [Int] идентификаторов жанров для фильтрации.
     * @param types Список типов релизов [Type] (например, TV, OVA) для фильтрации.
     * @param seasons Список сезонов релизов [Season] (например, winter, spring, summer, autumn) для фильтрации.
     * @param fromYear Минимальный год выхода релиза для фильтрации.
     * @param toYear Максимальный год выхода релиза для фильтрации.
     * @param search Строка поиска для фильтрации релизов по названию.
     * @param sorting Тип сортировки [Sorting] результатов (например, по дате или популярности).
     * @param ageRatings Список возрастных рейтингов [AgeRating] (например, R16_PLUS, R18_PLUS) для фильтрации.
     * @param publishStatuses Список статусов публикации релизов [PublishStatus] (например, ongoing, completed) для фильтрации.
     * @param productionStatuses Список статусов производства релизов [ProductionStatus] (например, inProduction, finished) для фильтрации.
     * @return [ApiResponse] содержащий список релизов аниме [Anime] и метаданные.
     */
    suspend fun getReleases(
        page: Int,
        limit: Int,
        genres: List<Int>? = null,
        types: List<Type>? = null,
        seasons: List<Season>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: Sorting? = null,
        ageRatings: List<AgeRating>? = null,
        publishStatuses: List<PublishStatus>? = null,
        productionStatuses: List<ProductionStatus>? = null
    ): ApiResponse<AnimeCatalogResponse> {
        return service.getReleases(
            page = page,
            limit = limit,
            genres = genres?.joinToString(","),
            types = types?.joinToString(","),
            seasons = seasons?.joinToString(","),
            fromYear = fromYear,
            toYear = toYear,
            search = search,
            sorting = sorting?.toString(),
            ageRatings = ageRatings?.joinToString(","),
            publishStatuses = publishStatuses?.joinToString(","),
            productionStatuses = productionStatuses?.joinToString(",")
        )
    }

    /**
     * Получение информации о релизе аниме по его идентификатору.
     *
     * @param id [Int] Идентификатор релиза аниме для получения информации.
     * @return [ApiResponse] содержащий информацию о релизе аниме [Anime].
     */
    suspend fun getRelease(id: Int): ApiResponse<Anime> {
        return service.getRelease(id)
    }



    enum class Type(private val type: String) {
        TV("TV"),
        ONA("ONA"),
        WEB("WEB"),
        OVA("OVA"),
        OAD("OAD"),
        MOVIE("MOVIE"),
        DORAMA("DORAMA"),
        SPECIAL("SPECIAL");

        override fun toString(): String {
            return type
        }
    }

    enum class Season(private val season: String) {
        WINTER("winter"),
        SPRING("spring"),
        SUMMER("summer"),
        AUTUMN("autumn");

        override fun toString(): String {
            return season
        }
    }

    enum class Sorting(private val value: String) {
        FRESH_AT_DESC("FRESH_AT_DESC"),
        FRESH_AT_ASC("FRESH_AT_ASC"),
        RATING_DESC("RATING_DESC"),
        RATING_ASC("RATING_ASC"),
        YEAR_DESC("YEAR_DESC"),
        YEAR_ASC("YEAR_ASC");

        override fun toString(): String {
            return value
        }
    }

    enum class AgeRating(private val value: String) {
        R0_PLUS("R0_PLUS"),
        R6_PLUS("R6_PLUS"),
        R12_PLUS("R12_PLUS"),
        R16_PLUS("R16_PLUS"),
        R18_PLUS("R18_PLUS");

        override fun toString(): String {
            return value
        }
    }

    enum class PublishStatus(private val value: String) {
        IS_ONGOING("IS_ONGOING"),
        IS_NOT_ONGOING("IS_NOT_ONGOING");

        override fun toString(): String {
            return value
        }
    }

    enum class ProductionStatus(private val value: String) {
        IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
        IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

        override fun toString(): String {
            return value
        }
    }
}