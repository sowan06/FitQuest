package com.fitquest.app.ui.screens.workout;

import com.fitquest.app.data.repository.CharacterRepository;
import com.fitquest.app.data.repository.WorkoutRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class WorkoutViewModel_Factory implements Factory<WorkoutViewModel> {
  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  public WorkoutViewModel_Factory(Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    this.workoutRepositoryProvider = workoutRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
  }

  @Override
  public WorkoutViewModel get() {
    return newInstance(workoutRepositoryProvider.get(), characterRepositoryProvider.get());
  }

  public static WorkoutViewModel_Factory create(
      Provider<WorkoutRepository> workoutRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    return new WorkoutViewModel_Factory(workoutRepositoryProvider, characterRepositoryProvider);
  }

  public static WorkoutViewModel newInstance(WorkoutRepository workoutRepository,
      CharacterRepository characterRepository) {
    return new WorkoutViewModel(workoutRepository, characterRepository);
  }
}
