package com.fitquest.app.data.repository;

import com.fitquest.app.data.local.dao.WorkoutDao;
import com.fitquest.app.data.remote.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class WorkoutRepository_Factory implements Factory<WorkoutRepository> {
  private final Provider<ApiService> apiProvider;

  private final Provider<WorkoutDao> daoProvider;

  private final Provider<CharacterRepository> characterRepoProvider;

  public WorkoutRepository_Factory(Provider<ApiService> apiProvider,
      Provider<WorkoutDao> daoProvider, Provider<CharacterRepository> characterRepoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.characterRepoProvider = characterRepoProvider;
  }

  @Override
  public WorkoutRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), characterRepoProvider.get());
  }

  public static WorkoutRepository_Factory create(Provider<ApiService> apiProvider,
      Provider<WorkoutDao> daoProvider, Provider<CharacterRepository> characterRepoProvider) {
    return new WorkoutRepository_Factory(apiProvider, daoProvider, characterRepoProvider);
  }

  public static WorkoutRepository newInstance(ApiService api, WorkoutDao dao,
      CharacterRepository characterRepo) {
    return new WorkoutRepository(api, dao, characterRepo);
  }
}
