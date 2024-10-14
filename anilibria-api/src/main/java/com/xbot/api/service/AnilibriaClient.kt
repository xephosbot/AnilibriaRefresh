package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.AgeRating
import com.xbot.api.models.Anime
import com.xbot.api.models.AnimeCatalogResponse
import com.xbot.api.models.Franchise
import com.xbot.api.models.Genre
import com.xbot.api.models.ProductionStatus
import com.xbot.api.models.PublishStatus
import com.xbot.api.models.Season
import com.xbot.api.models.SortingType
import com.xbot.api.models.Type
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
     * @param type Список типов релизов [TypeEnum] (например, TV, OVA) для фильтрации.
     * @param seasons Список сезонов релизов [SeasonEnum] (например, winter, spring, summer, autumn) для фильтрации.
     * @param fromYear Минимальный год выхода релиза для фильтрации.
     * @param toYear Максимальный год выхода релиза для фильтрации.
     * @param search Строка поиска для фильтрации релизов по названию.
     * @param sorting Тип сортировки [SortingEnum] результатов (например, по дате или популярности).
     * @param ageRatings Список возрастных рейтингов [AgeRatingEnum] (например, R16_PLUS, R18_PLUS) для фильтрации.
     * @param publishStatuses Список статусов публикации релизов [PublishStatusEnum] (например, ongoing, completed) для фильтрации.
     * @param productionStatuses Список статусов производства релизов [ProductionStatusEnum] (например, inProduction, finished) для фильтрации.
     * @return [ApiResponse] содержащий список релизов аниме [Anime] и метаданные.
     */
    suspend fun getReleases(
        page: Int,
        limit: Int,
        genres: List<Int>? = null,
        type: List<TypeEnum>? = null,
        seasons: List<SeasonEnum>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: SortingEnum? = null,
        ageRatings: List<AgeRatingEnum>? = null,
        publishStatuses: List<PublishStatusEnum>? = null,
        productionStatuses: List<ProductionStatusEnum>? = null
    ): ApiResponse<AnimeCatalogResponse> {
        return service.getReleases(
            page = page,
            limit = limit,
            genres = genres?.joinToString(","),
            types = type?.joinToString(","),
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

    /**
     * Возвращает список возможных возрастных рейтингов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о возрастных рейтингах [AgeRatingEnum]
     */
    suspend fun getAgeRatings(): ApiResponse<List<AgeRating>> {
        return service.getAgeRatings()
    }

    /**
     * Возвращает список всех жанров в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о всех жанрах [Genre]
     */
    suspend fun getGenres(): ApiResponse<List<Genre>> {
        return service.getGenres()
    }

    /**
     * Возвращает список возможных статусов озвучки релиза в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о статусах озвучки релиза [ProductionStatusEnum]
     */
    suspend fun getProductionStatuses(): ApiResponse<List<ProductionStatus>> {
        return service.getProductionStatuses()
    }

    /**
     * Возвращает список возможных статусов выхода релиза в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о статусах выхода релиза [PublishStatusEnum]
     */
    suspend fun getPublishStatuses(): ApiResponse<List<PublishStatus>> {
        return service.getPublishStatuses()
    }

    /**
     * Возвращает список возможных сезонов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о сезоне [SeasonEnum]
     */
    suspend fun getSeasons(): ApiResponse<List<Season>> {
        return service.getSeasons()
    }

    /**
     * Возвращает список возможных типов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о типах сортировки [SortingType]
     */
    suspend fun getSortingTypes(): ApiResponse<List<SortingType>> {
        return service.getSortingTypes()
    }

    /**
     * Возвращает список возможных типов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о типах релизов [Type]
     */
    suspend fun getTypeReleases(): ApiResponse<List<Type>> {
        return service.getTypeReleases()
    }

    /**
     * Возвращает список годов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о годах релизов.
     */
    suspend fun getYears(): ApiResponse<List<Int>> {
        return service.getYears()
    }

    /**
     * Возвращает список франшиз.
     *
     * @return [ApiResponse] содержащий информацию о франшизе [Franchise].
     */
    suspend fun getFranchises(): ApiResponse<List<Franchise>> {
        return service.getFranchises()
    }

    /**
     * Возвращает информацию о франшизе по ее идентификатору.
     *
     * @param id [Int] Идентификатор франшизы для получения информации.
     * @return [ApiResponse] содержащий информацию о франшизе [Franchise].
     */
    suspend fun getFranchiseById(id: Int): ApiResponse<Franchise> {
        return service.getFranchiseById(id)
    }

    /**Получение списка случайных франшиз с применением различных фильтров.
    *
    * @param limit Количество случайных франшиз в выдаче.
     * @return [ApiResponse] содержащий информацию о франшизе [Franchise].
    */
    suspend fun getFranchisesRandom(limit: Int): ApiResponse<List<Franchise>> {
        return service.getFranchisesRandom(limit = limit)
    }

    /**Возвращает список франшиз, в которых участвует релиз.
     *
     * @param id [Int] Идентификатор релиза для получения информации.
     * @return [ApiResponse] содержащий информацию о франшизе [Franchise].
     */
    suspend fun getFranchisesByRelease(id: Int): ApiResponse<List<Franchise>> {
        return service.getFranchisesByRelease(id)
    }

    /**Возвращает список всех жанров.
     *
     * @return [ApiResponse] содержащий информацию о жанре [Genre].
     */
    suspend fun getAllGenres(): ApiResponse<List<Genre>> {
        return service.getAllGenres()
    }

    /**Возвращает данные по определенному жанру.
     *
     * @param id [Int] Идентификатор жанра для получения информации.
     * @return [ApiResponse] содержащий информацию о жанре [Genre].
     */
    suspend fun getGenreById(id: Int): ApiResponse<Genre> {
        return service.getGenreById(id)
    }

    /**Возвращает список случайных жанров.
     *
     * @param limit Количество жанров в выдаче.
     * @return [ApiResponse] содержащий информацию о жанре [Genre].
     */
    suspend fun getRandomGenres(limit: Int): ApiResponse<List<Genre>> {
        return service.getRandomGenres(limit = limit)
    }

    /**Получение списка всех релизов жанра с применением различных фильтров.
     *
     * @param id [Int] Идентификатор жанра для получения информации.
     * @param page Страница в выдаче.
     * @param limit Количество релизов в выдаче.
     * @return [ApiResponse] содержащий информацию о релизе [AnimeCatalogResponse].
     */
    suspend fun getAllReleasesByGenre(
        id: Int,
        page: Int,
        limit: Int
    ): ApiResponse<AnimeCatalogResponse> {
        return service.getAllReleasesByGenre(
            id = id,
            page = page,
            limit = limit
        )
    }

    enum class TypeEnum(private val type: String) {
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

    enum class SeasonEnum(private val season: String) {
        WINTER("winter"),
        SPRING("spring"),
        SUMMER("summer"),
        AUTUMN("autumn");

        override fun toString(): String {
            return season
        }
    }

    enum class SortingEnum(private val value: String) {
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

    enum class AgeRatingEnum(private val value: String) {
        R0_PLUS("R0_PLUS"),
        R6_PLUS("R6_PLUS"),
        R12_PLUS("R12_PLUS"),
        R16_PLUS("R16_PLUS"),
        R18_PLUS("R18_PLUS");

        override fun toString(): String {
            return value
        }
    }

    enum class PublishStatusEnum(private val value: String) {
        IS_ONGOING("IS_ONGOING"),
        IS_NOT_ONGOING("IS_NOT_ONGOING");

        override fun toString(): String {
            return value
        }
    }

    enum class ProductionStatusEnum(private val value: String) {
        IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
        IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION");

        override fun toString(): String {
            return value
        }
    }
}