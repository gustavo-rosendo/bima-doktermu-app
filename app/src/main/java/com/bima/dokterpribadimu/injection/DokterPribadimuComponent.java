package com.bima.dokterpribadimu.injection;

import android.app.Application;

import com.bima.dokterpribadimu.view.activities.RegistrationSuccessActivity;
import com.bima.dokterpribadimu.view.activities.HomeActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.HomeFragment;
import com.bima.dokterpribadimu.view.fragments.RegisterFragment;
import com.bima.dokterpribadimu.view.fragments.RegistrationSuccessFragment;
import com.bima.dokterpribadimu.view.fragments.SignInFragment;

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

    void inject(LandingActivity landingActivity);
    void inject(SignInActivity signInActivity);
    void inject(RegistrationSuccessActivity registrationSuccessActivity);
    void inject(HomeActivity homeActivity);
    void inject(DrawerFragment drawerFragment);
    void inject(RegistrationSuccessFragment registrationSuccessFragment);
    void inject(HomeFragment homeFragment);
    void inject(SignInFragment signInFragment);
    void inject(RegisterFragment registerFragment);

    final class Initializer {
        public static DokterPribadimuComponent init(Application application) {
            return DaggerDokterPribadimuComponent.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
