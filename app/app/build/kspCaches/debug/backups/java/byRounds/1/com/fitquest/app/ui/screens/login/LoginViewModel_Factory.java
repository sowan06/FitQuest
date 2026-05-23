package com.fitquest.app.ui.screens.login;

import com.fitquest.app.data.repository.AuthRepository;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<CharacterRepository> characterRepositoryProvider;

  private final Provider<QuestRepository> questRepositoryProvider;

  public LoginViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<QuestRepository> questRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.characterRepositoryProvider = characterRepositoryProvider;
    this.questRepositoryProvider = questRepositoryProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(authRepositoryProvider.get(), characterRepositoryProvider.get(), questRepositoryProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<CharacterRepository> characterRepositoryProvider,
      Provider<QuestRepository> questRepositoryProvider) {
    return new LoginViewModel_Factory(authRepositoryProvider, characterRepositoryProvider, questRepositoryProvider);
  }

  public static LoginViewModel newInstance(AuthRepository authRepository,
      CharacterRepository characterRepository, QuestRepository questRepository) {
    return new LoginViewModel(authRepository, characterRepository, questRepository);
  }
}
