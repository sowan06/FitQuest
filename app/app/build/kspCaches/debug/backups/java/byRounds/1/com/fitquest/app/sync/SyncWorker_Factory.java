package com.fitquest.app.sync;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.fitquest.app.data.local.dao.FoodDao;
import com.fitquest.app.data.local.dao.WorkoutDao;
import com.fitquest.app.data.remote.ApiService;
import com.fitquest.app.data.repository.CharacterRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SyncWorker_Factory {
  private final Provider<WorkoutDao> workoutDaoProvider;

  private final Provider<FoodDao> foodDaoProvider;

  private final Provider<ApiService> apiProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  public SyncWorker_Factory(Provider<WorkoutDao> workoutDaoProvider,
      Provider<FoodDao> foodDaoProvider, Provider<ApiService> apiProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    this.workoutDaoProvider = workoutDaoProvider;
    this.foodDaoProvider = foodDaoProvider;
    this.apiProvider = apiProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
  }

  public SyncWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, workoutDaoProvider.get(), foodDaoProvider.get(), apiProvider.get(), characterRepositoryProvider.get());
  }

  public static SyncWorker_Factory create(Provider<WorkoutDao> workoutDaoProvider,
      Provider<FoodDao> foodDaoProvider, Provider<ApiService> apiProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    return new SyncWorker_Factory(workoutDaoProvider, foodDaoProvider, apiProvider, characterRepositoryProvider);
  }

  public static SyncWorker newInstance(Context appContext, WorkerParameters params,
      WorkoutDao workoutDao, FoodDao foodDao, ApiService api,
      CharacterRepository characterRepository) {
    return new SyncWorker(appContext, params, workoutDao, foodDao, api, characterRepository);
  }
}
