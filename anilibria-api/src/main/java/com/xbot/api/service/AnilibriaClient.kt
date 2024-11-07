package com.xbot.api.service

import com.skydoves.sandwich.ApiResponse
import com.xbot.api.models.Franchise
import com.xbot.api.models.Genre
import com.xbot.api.models.Release
import com.xbot.api.models.ReleaseCatalogResponse
import com.xbot.api.models.Schedule
import com.xbot.api.models.ValDesc
import com.xbot.api.models.enums.AgeRatingEnum
import com.xbot.api.models.enums.ProductionStatusEnum
import com.xbot.api.models.enums.PublishStatusEnum
import com.xbot.api.models.enums.ReleaseTypeEnum
import com.xbot.api.models.enums.SeasonEnum
import com.xbot.api.models.enums.SocialNetworkEnum
import com.xbot.api.models.enums.SortingTypeEnum
import com.xbot.api.models.login.LoginRequest
import com.xbot.api.models.login.LoginResponse
import com.xbot.api.models.login.LoginSocialNetwork
import javax.inject.Inject

class AnilibriaClient @Inject constructor(
    private val service: AnilibriaService
) {
    /**
     * Авторизация пользователя по логину и паролю. Создание сессии пользователя, выдача токена авторизации для использования в cookies или в Bearer Token.
     *
     * @param loginRequest [LoginRequest] Объект, содержащий логин и пароль.
     * @return [ApiResponse] содержащий информацию об авторизации [LoginResponse].
     */
    suspend fun loginUser(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return service.loginUser(loginRequest = loginRequest)
    }

    /**
     * Деавторизация пользователя.
     *
     * @return [ApiResponse] содержащий информацию о деавторизации [LoginResponse].
     */
    suspend fun logoutUser(): ApiResponse<LoginResponse> {
        return service.logoutUser()
    }

    /**
     * Авторизация пользователя через некоторые социальные сети: VK, Google, Discord, Patreon.
     *
     * @param provider [String] Провайдер социальной сети.
     * @return [ApiResponse] содержащий информацию о ключе, используемом для авторизации [LoginSocialNetwork].
     */
    suspend fun loginWithSocialNetwork(
        provider: SocialNetworkEnum
    ): ApiResponse<LoginSocialNetwork> {
        return service.loginWithSocialNetwork(provider = provider)
    }

    /**
     * Позволяет аутентифицировать авторизованного через социальную сеть пользователя.
     *
     * @param state [String] Ключ аутентификации.
     * @return [ApiResponse] содержащий информацию об авторизации [LoginResponse].
     */
    suspend fun authenticateWithSocialNetwork(
        state: String
    ): ApiResponse<LoginResponse> {
        return service.authenticateWithSocialNetwork(state = state)
    }

    /**
     * Получение списка релизов аниме с применением различных фильтров.
     *
     * @param page Номер страницы в выдаче.
     * @param limit Количество релизов на странице.
     * @param genres Список [Int] идентификаторов жанров для фильтрации.
     * @param type Список типов релизов [ReleaseTypeEnum] (например, TV, OVA) для фильтрации.
     * @param seasons Список сезонов релизов [SeasonEnum] (например, winter, spring, summer, autumn) для фильтрации.
     * @param fromYear Минимальный год выхода релиза для фильтрации.
     * @param toYear Максимальный год выхода релиза для фильтрации.
     * @param search Строка поиска для фильтрации релизов по названию.
     * @param sorting Тип сортировки [SortingTypeEnum] результатов (например, по дате или популярности).
     * @param ageRatings Список возрастных рейтингов [AgeRatingEnum] (например, R16_PLUS, R18_PLUS) для фильтрации.
     * @param publishStatuses Список статусов публикации релизов [PublishStatusEnum] (например, ongoing, completed) для фильтрации.
     * @param productionStatuses Список статусов производства релизов [ProductionStatusEnum] (например, inProduction, finished) для фильтрации.
     * @return [ApiResponse] содержащий список релизов аниме [Release] и метаданные.
     */
    suspend fun getReleases(
        page: Int,
        limit: Int,
        genres: List<Int>? = null,
        type: List<ReleaseTypeEnum>? = null,
        seasons: List<SeasonEnum>? = null,
        fromYear: Int? = null,
        toYear: Int? = null,
        search: String? = null,
        sorting: SortingTypeEnum? = null,
        ageRatings: List<AgeRatingEnum>? = null,
        publishStatuses: List<PublishStatusEnum>? = null,
        productionStatuses: List<ProductionStatusEnum>? = null
    ): ApiResponse<ReleaseCatalogResponse> {
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
     * @return [ApiResponse] содержащий информацию о релизе аниме [Release].
     */
    suspend fun getRelease(id: Int): ApiResponse<Release> {
        return service.getRelease(id)
    }

    /**
     * Возвращает список возможных возрастных рейтингов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о возрастных рейтингах [AgeRatingEnum]
     */
    suspend fun getAgeRatings(): ApiResponse<List<ValDesc<AgeRatingEnum>>> {
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
    suspend fun getProductionStatuses(): ApiResponse<List<ValDesc<ProductionStatusEnum>>> {
        return service.getProductionStatuses()
    }

    /**
     * Возвращает список возможных статусов выхода релиза в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о статусах выхода релиза [PublishStatusEnum]
     */
    suspend fun getPublishStatuses(): ApiResponse<List<ValDesc<PublishStatusEnum>>> {
        return service.getPublishStatuses()
    }

    /**
     * Возвращает список возможных сезонов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о сезоне [SeasonEnum]
     */
    suspend fun getSeasons(): ApiResponse<List<ValDesc<SeasonEnum>>> {
        return service.getSeasons()
    }

    /**
     * Возвращает список возможных типов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о типах сортировки [SortingTypeEnum]
     */
    suspend fun getSortingTypes(): ApiResponse<List<ValDesc<SortingTypeEnum>>> {
        return service.getSortingTypes()
    }

    /**
     * Возвращает список возможных типов релизов в каталоге.
     *
     * @return [ApiResponse] содержащий информацию о типах релизов [ReleaseTypeEnum]
     */
    suspend fun getTypeReleases(): ApiResponse<List<ValDesc<ReleaseTypeEnum>>> {
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
     * @return [ApiResponse] содержащий информацию о релизе [ReleaseCatalogResponse].
     */
    suspend fun getAllReleasesByGenre(
        id: Int,
        page: Int,
        limit: Int
    ): ApiResponse<ReleaseCatalogResponse> {
        return service.getAllReleasesByGenre(
            id = id,
            page = page,
            limit = limit
        )
    }

    /**Возвращает данные по последним релизам.
     *
     * @param limit Количество релизов в выдаче.
     * @return [ApiResponse] содержащий информацию о последних релизах [Release].
     */
    suspend fun getLatestReleases(
        limit: Int
    ): ApiResponse<List<Release>> {
        return service.getLatestReleases(limit = limit)
    }

    /**Возвращает данные по случайным релизам.
     *
     * @param limit Количество релизов в выдаче.
     * @return [ApiResponse] содержащий информацию о случайных релизах [Release].
     */
    suspend fun getRandomReleases(
        limit: Int
    ): ApiResponse<List<Release>> {
        return service.getRandomReleases(limit = limit)
    }

    /**Возвращает данные по расписанию релизав на текущую неделю.
     *
     * @return [ApiResponse] содержащий информацию о расписании [Schedule] релизов на текущую неделю.
     */
    suspend fun getScheduleWeek(): ApiResponse<List<Schedule>> =
        service.getScheduleWeek()
}