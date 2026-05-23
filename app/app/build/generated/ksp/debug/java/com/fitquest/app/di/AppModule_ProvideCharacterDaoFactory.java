package com.fitquest.app.di;

import com.fitquest.app.data.local.FitQuestDatabase;
import com.fitquest.app.data.local.dao.CharacterDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideCharacterDaoFactory implements Factory<CharacterDao> {
  private final Provider<FitQuestDatabase> dbProvider;

  public AppModule_ProvideCharacterDaoFactory(Provider<FitQuestDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CharacterDao get() {
    return provideCharacterDao(dbProvider.get());
  }

  public static AppModule_ProvideCharacterDaoFactory create(Provider<FitQuestDatabase> dbProvider) {
    return new AppModule_ProvideCharacterDaoFactory(dbProvider);
  }

  public static CharacterDao provideCharacterDao(FitQuestDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCharacterDao(db));
  }
}
