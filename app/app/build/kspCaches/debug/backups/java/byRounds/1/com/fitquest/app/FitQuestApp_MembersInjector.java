package com.fitquest.app;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class FitQuestApp_MembersInjector implements MembersInjector<FitQuestApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public FitQuestApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<FitQuestApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new FitQuestApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(FitQuestApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.fitquest.app.FitQuestApp.workerFactory")
  public static void injectWorkerFactory(FitQuestApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
