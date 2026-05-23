package com.fitquest.app.ui.screens.quests;

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
public final class QuestsViewModel_Factory implements Factory<QuestsViewModel> {
  private final Provider<QuestRepository> questRepositoryProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  public QuestsViewModel_Factory(Provider<QuestRepository> questRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    this.questRepositoryProvider = questRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
  }

  @Override
  public QuestsViewModel get() {
    return newInstance(questRepositoryProvider.get(), characterRepositoryProvider.get());
  }

  public static QuestsViewModel_Factory create(Provider<QuestRepository> questRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider) {
    return new QuestsViewModel_Factory(questRepositoryProvider, characterRepositoryProvider);
  }

  public static QuestsViewModel newInstance(QuestRepository questRepository,
      CharacterRepository characterRepository) {
    return new QuestsViewModel(questRepository, characterRepository);
  }
}
