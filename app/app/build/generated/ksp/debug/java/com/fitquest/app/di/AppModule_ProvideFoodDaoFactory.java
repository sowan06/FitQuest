package com.fitquest.app.di;

import com.fitquest.app.data.local.FitQuestDatabase;
import com.fitquest.app.data.local.dao.FoodDao;
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
public final class AppModule_ProvideFoodDaoFactory implements Factory<FoodDao> {
  private final Provider<FitQuestDatabase> dbProvider;

  public AppModule_ProvideFoodDaoFactory(Provider<FitQuestDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FoodDao get() {
    return provideFoodDao(dbProvider.get());
  }

  public static AppModule_ProvideFoodDaoFactory create(Provider<FitQuestDatabase> dbProvider) {
    return new AppModule_ProvideFoodDaoFactory(dbProvider);
  }

  public static FoodDao provideFoodDao(FitQuestDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFoodDao(db));
  }
}
