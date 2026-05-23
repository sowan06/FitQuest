package com.fitquest.app.notifications;

import com.fitquest.app.data.auth.TokenStore;
import com.fitquest.app.data.repository.AuthRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class FcmService_MembersInjector implements MembersInjector<FcmService> {
  private final Provider<TokenStore> tokenStoreProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public FcmService_MembersInjector(Provider<TokenStore> tokenStoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.tokenStoreProvider = tokenStoreProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  public static MembersInjector<FcmService> create(Provider<TokenStore> tokenStoreProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new FcmService_MembersInjector(tokenStoreProvider, authRepositoryProvider);
  }

  @Override
  public void injectMembers(FcmService instance) {
    injectTokenStore(instance, tokenStoreProvider.get());
    injectAuthRepository(instance, authRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.fitquest.app.notifications.FcmService.tokenStore")
  public static void injectTokenStore(FcmService instance, TokenStore tokenStore) {
    instance.tokenStore = tokenStore;
  }

  @InjectedFieldSignature("com.fitquest.app.notifications.FcmService.authRepository")
  public static void injectAuthRepository(FcmService instance, AuthRepository authRepository) {
    instance.authRepository = authRepository;
  }
}
