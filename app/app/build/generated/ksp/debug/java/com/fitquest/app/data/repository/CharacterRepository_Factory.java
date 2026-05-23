package com.fitquest.app.data.repository;

import com.fitquest.app.data.local.dao.CharacterDao;
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
public final class CharacterRepository_Factory implements Factory<CharacterRepository> {
  private final Provider<ApiService> apiProvider;

  private final Provider<CharacterDao> daoProvider;

  public CharacterRepository_Factory(Provider<ApiService> apiProvider,
      Provider<CharacterDao> daoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public CharacterRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get());
  }

  public static CharacterRepository_Factory create(Provider<ApiService> apiProvider,
      Provider<CharacterDao> daoProvider) {
    return new CharacterRepository_Factory(apiProvider, daoProvider);
  }

  public static CharacterRepository newInstance(ApiService api, CharacterDao dao) {
    return new CharacterRepository(api, dao);
  }
}
