package com.fitquest.app.data.repository;

import com.fitquest.app.data.local.dao.FoodDao;
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
public final class FoodRepository_Factory implements Factory<FoodRepository> {
  private final Provider<ApiService> apiProvider;

  private final Provider<FoodDao> daoProvider;

  private final Provider<CharacterRepository> characterRepoProvider;

  public FoodRepository_Factory(Provider<ApiService> apiProvider, Provider<FoodDao> daoProvider,
      Provider<CharacterRepository> characterRepoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.characterRepoProvider = characterRepoProvider;
  }

  @Override
  public FoodRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), characterRepoProvider.get());
  }

  public static FoodRepository_Factory create(Provider<ApiService> apiProvider,
      Provider<FoodDao> daoProvider, Provider<CharacterRepository> characterRepoProvider) {
    return new FoodRepository_Factory(apiProvider, daoProvider, characterRepoProvider);
  }

  public static FoodRepository newInstance(ApiService api, FoodDao dao,
      CharacterRepository characterRepo) {
    return new FoodRepository(api, dao, characterRepo);
  }
}
