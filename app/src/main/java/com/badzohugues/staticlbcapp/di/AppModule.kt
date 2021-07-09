package com.badzohugues.staticlbcapp.di

import android.content.Context
import androidx.room.Room
import com.badzohugues.staticlbcapp.StaticLBCApplication
import com.badzohugues.staticlbcapp.data.api.datasource.ApiDatasource
import com.badzohugues.staticlbcapp.data.api.service.AlbumItemApiService
import com.badzohugues.staticlbcapp.data.db.dao.AlbumItemDao
import com.badzohugues.staticlbcapp.data.db.datasource.DbDatasource
import com.badzohugues.staticlbcapp.data.db.room.StaticLBCDatabase
import com.badzohugues.staticlbcapp.data.repository.AlbumItemRepository
import com.badzohugues.staticlbcapp.data.repository.Repository
import com.badzohugues.staticlbcapp.misc.Constants.BASE_URL
import com.badzohugues.staticlbcapp.misc.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext application: Context): StaticLBCApplication =
        application as StaticLBCApplication

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        StaticLBCDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRepositoryDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAlbumItemDao(database: StaticLBCDatabase) = database.albumItemDao()

    @Singleton
    @Provides
    fun provideAlbumItemApiService(): AlbumItemApiService {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AlbumItemApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(
        dbDatasource: DbDatasource,
        apiDatasource: ApiDatasource,
        dispatcher: CoroutineDispatcher
    ) = AlbumItemRepository(dbDatasource, apiDatasource, dispatcher) as Repository

    @Singleton
    @Provides
    fun provideApiDatasource(apiService: AlbumItemApiService) = ApiDatasource(apiService)

    @Singleton
    @Provides
    fun provideDbDatasource(albumItemDao: AlbumItemDao) = DbDatasource(albumItemDao)
}
