package com.fitquest.app.di

import android.content.Context
import androidx.room.Room
import com.fitquest.app.BuildConfig
import com.fitquest.app.data.local.FitQuestDatabase
import com.fitquest.app.data.local.dao.CharacterDao
import com.fitquest.app.data.local.dao.FoodDao
import com.fitquest.app.data.local.dao.QuestDao
import com.fitquest.app.data.local.dao.WorkoutDao
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): FitQuestDatabase =
        Room.databaseBuilder(ctx, FitQuestDatabase::class.java, "fitquest.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideCharacterDao(db: FitQuestDatabase): CharacterDao = db.characterDao()
    @Provides fun provideWorkoutDao(db: FitQuestDatabase): WorkoutDao = db.workoutDao()
    @Provides fun provideFoodDao(db: FitQuestDatabase): FoodDao = db.foodDao()
    @Provides fun provideQuestDao(db: FitQuestDatabase): QuestDao = db.questDao()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(log)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
