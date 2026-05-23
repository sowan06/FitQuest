package com.fitquest.app.di;

import com.fitquest.app.data.local.FitQuestDatabase;
import com.fitquest.app.data.local.dao.WorkoutDao;
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
public final class AppModule_ProvideWorkoutDaoFactory implements Factory<WorkoutDao> {
  private final Provider<FitQuestDatabase> dbProvider;

  public AppModule_ProvideWorkoutDaoFactory(Provider<FitQuestDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public WorkoutDao get() {
    return provideWorkoutDao(dbProvider.get());
  }

  public static AppModule_ProvideWorkoutDaoFactory create(Provider<FitQuestDatabase> dbProvider) {
    return new AppModule_ProvideWorkoutDaoFactory(dbProvider);
  }

  public static WorkoutDao provideWorkoutDao(FitQuestDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWorkoutDao(db));
  }
}
