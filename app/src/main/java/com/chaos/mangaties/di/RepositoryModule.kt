package com.chaos.mangaties.di

import com.chaos.mangaties.data.remote.repository.*
import com.chaos.mangaties.data.remote.repository.auth.forgetpassword.ForgetPasswordImpl
import com.chaos.mangaties.data.remote.repository.auth.login.LoginRepositoryImpl
import com.chaos.mangaties.data.remote.repository.auth.signup.SignUpRepositoryImpl
import com.chaos.mangaties.data.remote.repository.auth.statemanger.AuthStateManagerImpl
import com.chaos.mangaties.domain.repository.auth.forgetpassword.ForgetPasswordRepository
import com.chaos.mangaties.domain.repository.auth.login.LoginRepository
import com.chaos.mangaties.domain.repository.auth.signup.SignUpRepository
import com.chaos.mangaties.domain.repository.manga.*
import com.chaos.mangaties.domain.repository.statemanager.AuthStateManager
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSignUpRepository(
        signUpRepositoryImpl: SignUpRepositoryImpl
    ): SignUpRepository

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ) : LoginRepository

    @Binds
    @Singleton
    abstract fun bindForgetPasswordRepository(
        forgetPasswordImpl: ForgetPasswordImpl
    ) : ForgetPasswordRepository

    @Binds
    @Singleton
    abstract fun bindAuthStateMangerRepository(
        authStateManagerImpl: AuthStateManagerImpl
    ): AuthStateManager


    @Binds
    @Singleton
    abstract fun bindDownloadRepository(
        downloadRepositoryImpl: DownloadRepositoryImpl
    ): DownloadRepository

    @Binds
    @Singleton
    abstract fun bindFavouriteRepository(
        favouriteRepositoryImpl: FavouriteRepositoryImpl
    ): FavouriteRepository

    @Binds
    @Singleton
    abstract fun bindDiscoverRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}
