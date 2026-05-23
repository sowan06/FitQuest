package com.fitquest.app.ui.navigation;

import com.fitquest.app.data.repository.AuthRepository;
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
public final class AuthGate_Factory implements Factory<AuthGate> {
  private final Provider<AuthRepository> repoProvider;

  public AuthGate_Factory(Provider<AuthRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public AuthGate get() {
    return newInstance(repoProvider.get());
  }

  public static AuthGate_Factory create(Provider<AuthRepository> repoProvider) {
    return new AuthGate_Factory(repoProvider);
  }

  public static AuthGate newInstance(AuthRepository repo) {
    return new AuthGate(repo);
  }
}
