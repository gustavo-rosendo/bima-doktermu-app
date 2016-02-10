package com.bima.dokterpribadimu.injection;

import android.app.Application;

import com.bima.dokterpribadimu.view.activities.HomeActivity;
import com.bima.dokterpribadimu.view.activities.LoginActivity;
import com.bima.dokterpribadimu.view.activities.RegisterActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.HomeFragment;

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
    void inject(RegisterActivity registerActivity);
    void inject(HomeActivity homeActivity);
    void inject(DrawerFragment drawerFragment);
    void inject(HomeFragment homeFragment);

    final class Initializer {
        public static DokterPribadimuComponent init(Application application) {
            return DaggerDokterPribadimuComponent.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
