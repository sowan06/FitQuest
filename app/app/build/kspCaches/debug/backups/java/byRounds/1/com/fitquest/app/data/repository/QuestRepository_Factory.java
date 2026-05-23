package com.fitquest.app.data.repository;

import com.fitquest.app.data.local.dao.QuestDao;
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
public final class QuestRepository_Factory implements Factory<QuestRepository> {
  private final Provider<ApiService> apiProvider;

  private final Provider<QuestDao> daoProvider;

  private final Provider<CharacterRepository> characterRepoProvider;

  public QuestRepository_Factory(Provider<ApiService> apiProvider, Provider<QuestDao> daoProvider,
      Provider<CharacterRepository> characterRepoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.characterRepoProvider = characterRepoProvider;
  }

  @Override
  public QuestRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), characterRepoProvider.get());
  }

  public static QuestRepository_Factory create(Provider<ApiService> apiProvider,
      Provider<QuestDao> daoProvider, Provider<CharacterRepository> characterRepoProvider) {
    return new QuestRepository_Factory(apiProvider, daoProvider, characterRepoProvider);
  }

  public static QuestRepository newInstance(ApiService api, QuestDao dao,
      CharacterRepository characterRepo) {
    return new QuestRepository(api, dao, characterRepo);
  }
}
