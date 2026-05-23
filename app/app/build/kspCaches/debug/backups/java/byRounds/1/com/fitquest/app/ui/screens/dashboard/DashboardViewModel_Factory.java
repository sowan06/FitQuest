package com.fitquest.app.ui.screens.dashboard;

import com.fitquest.app.data.repository.CharacterRepository;
import com.fitquest.app.data.repository.FoodRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<FoodRepository> foodRepositoryProvider;

  private final Provider<WorkoutRepository> workoutRepositoryProvider;

  public DashboardViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<FoodRepository> foodRepositoryProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.foodRepositoryProvider = foodRepositoryProvider;
    this.workoutRepositoryProvider = workoutRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(characterRepositoryProvider.get(), foodRepositoryProvider.get(), workoutRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<FoodRepository> foodRepositoryProvider,
      Provider<WorkoutRepository> workoutRepositoryProvider) {
    return new DashboardViewModel_Factory(characterRepositoryProvider, foodRepositoryProvider, workoutRepositoryProvider);
  }

  public static DashboardViewModel newInstance(CharacterRepository characterRepository,
      FoodRepository foodRepository, WorkoutRepository workoutRepository) {
    return new DashboardViewModel(characterRepository, foodRepository, workoutRepository);
  }
}
