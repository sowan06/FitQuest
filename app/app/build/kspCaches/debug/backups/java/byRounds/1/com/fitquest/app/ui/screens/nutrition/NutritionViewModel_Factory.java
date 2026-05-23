package com.fitquest.app.ui.screens.nutrition;

import com.fitquest.app.data.repository.CharacterRepository;
import com.fitquest.app.data.repository.FoodRepository;
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
public final class NutritionViewModel_Factory implements Factory<NutritionViewModel> {
  private final Provider<FoodRepository> foodRepositoryProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  public NutritionViewModel_Factory(Provider<FoodRepository> foodRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    this.foodRepositoryProvider = foodRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
  }

  @Override
  public NutritionViewModel get() {
    return newInstance(foodRepositoryProvider.get(), characterRepositoryProvider.get());
  }

  public static NutritionViewModel_Factory create(Provider<FoodRepository> foodRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    return new NutritionViewModel_Factory(foodRepositoryProvider, characterRepositoryProvider);
  }

  public static NutritionViewModel newInstance(FoodRepository foodRepository,
      CharacterRepository characterRepository) {
    return new NutritionViewModel(foodRepository, characterRepository);
  }
}
