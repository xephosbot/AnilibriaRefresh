package com.xbot.fixtures.di

import com.xbot.domain.repository.AppearanceRepository
import com.xbot.domain.repository.AuthRepository
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.CollectionsRepository
import com.xbot.domain.repository.EpisodesRepository
import com.xbot.domain.repository.FavoritesRepository
import com.xbot.domain.repository.FranchisesRepository
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.OtpRepository
import com.xbot.domain.repository.ProfileRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository
import com.xbot.domain.utils.DispatcherProvider
import com.xbot.fixtures.repository.FakeAppearanceRepository
import com.xbot.fixtures.repository.FakeAuthRepository
import com.xbot.fixtures.repository.FakeCatalogRepository
import com.xbot.fixtures.repository.FakeCollectionsRepository
import com.xbot.fixtures.repository.FakeEpisodesRepository
import com.xbot.fixtures.repository.FakeFavoritesRepository
import com.xbot.fixtures.repository.FakeFranchisesRepository
import com.xbot.fixtures.repository.FakeGenresRepository
import com.xbot.fixtures.repository.FakeOtpRepository
import com.xbot.fixtures.repository.FakeProfileRepository
import com.xbot.fixtures.repository.FakeReleasesRepository
import com.xbot.fixtures.repository.FakeScheduleRepository
import com.xbot.fixtures.utils.TestDispatcherProvider
import org.koin.dsl.module

val fixturesModule = module {
    single<DispatcherProvider> { TestDispatcherProvider() }

    single<ReleasesRepository> { FakeReleasesRepository() }
    single<AuthRepository> { FakeAuthRepository() }
    single<CatalogRepository> { FakeCatalogRepository() }
    single<CollectionsRepository> { FakeCollectionsRepository() }
    single<EpisodesRepository> { FakeEpisodesRepository() }
    single<FavoritesRepository> { FakeFavoritesRepository() }
    single<FranchisesRepository> { FakeFranchisesRepository() }
    single<GenresRepository> { FakeGenresRepository() }
    single<OtpRepository> { FakeOtpRepository() }
    single<ProfileRepository> { FakeProfileRepository() }
    single<ScheduleRepository> { FakeScheduleRepository() }
    single<AppearanceRepository> { FakeAppearanceRepository() }
}
