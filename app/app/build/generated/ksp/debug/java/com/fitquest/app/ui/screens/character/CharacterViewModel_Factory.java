package com.fitquest.app.ui.screens.character;

import com.fitquest.app.data.repository.CharacterRepository;
import com.fitquest.app.data.repository.QuestRepository;
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
public final class CharacterViewModel_Factory implements Factory<CharacterViewModel> {
  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<QuestRepository> questRepositoryProvider;

  public CharacterViewModel_Factory(Provider<CharacterRepository> characterRepositoryProvider,
      Provider<QuestRepository> questRepositoryProvider) {
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.questRepositoryProvider = questRepositoryProvider;
  }

  @Override
  public CharacterViewModel get() {
    return newInstance(characterRepositoryProvider.get(), questRepositoryProvider.get());
  }

  public static CharacterViewModel_Factory create(
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<QuestRepository> questRepositoryProvider) {
    return new CharacterViewModel_Factory(characterRepositoryProvider, questRepositoryProvider);
  }

  public static CharacterViewModel newInstance(CharacterRepository characterRepository,
      QuestRepository questRepository) {
    return new CharacterViewModel(characterRepository, questRepository);
  }
}
