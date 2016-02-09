package com.bima.dokterpribadimu.injection;

import android.app.Application;

import com.bima.dokterpribadimu.view.activities.LoginActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by apradanas on 2/3/16.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class
})

public interface DokterPribadimuComponent {

    void inject(LoginActivity loginActivity);
    void inject(SignInActivity signInActivity);

    final class Initializer {
        public static DokterPribadimuComponent init(Application application) {
            return DaggerDokterPribadimuComponent.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
