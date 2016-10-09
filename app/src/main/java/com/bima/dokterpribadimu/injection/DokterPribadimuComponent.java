package com.bima.dokterpribadimu.injection;

import android.app.Application;

import com.bima.dokterpribadimu.view.activities.AboutActivity;
import com.bima.dokterpribadimu.view.activities.BookCallActivity;
import com.bima.dokterpribadimu.view.activities.CallDetailsActivity;
import com.bima.dokterpribadimu.view.activities.CurrentHealthActivity;
import com.bima.dokterpribadimu.view.activities.DoctorCallActivity;
import com.bima.dokterpribadimu.view.activities.DoctorProfileActivity;
import com.bima.dokterpribadimu.view.activities.GeneralInformationActivity;
import com.bima.dokterpribadimu.view.activities.HomeActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.MedicineInformationActivity;
import com.bima.dokterpribadimu.view.activities.NewsActivity;
import com.bima.dokterpribadimu.view.activities.NewsDetailActivity;
import com.bima.dokterpribadimu.view.activities.OnboardingActivity;
import com.bima.dokterpribadimu.view.activities.PartnersActivity;
import com.bima.dokterpribadimu.view.activities.PartnersDetailActivity;
import com.bima.dokterpribadimu.view.activities.PartnersLandingActivity;
import com.bima.dokterpribadimu.view.activities.PartnersSearchActivity;
import com.bima.dokterpribadimu.view.activities.ProfileActivity;
import com.bima.dokterpribadimu.view.activities.RegisterNameActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.activities.SubscriptionActivity;
import com.bima.dokterpribadimu.view.activities.SubscriptionPlansActivity;
import com.bima.dokterpribadimu.view.fragments.AboutFragment;
import com.bima.dokterpribadimu.view.fragments.DoctorCallFragment;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.HomeFragment;
import com.bima.dokterpribadimu.view.fragments.MedicineInformationFragment;
import com.bima.dokterpribadimu.view.fragments.NewsFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingListCorporateFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingListTestimonialFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingOpeningFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingPhotoFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingSocialFragment;
import com.bima.dokterpribadimu.view.fragments.PartnersFragment;
import com.bima.dokterpribadimu.view.fragments.ProfileFragment;
import com.bima.dokterpribadimu.view.fragments.RegisterFragment;
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

    void inject(OnboardingActivity landingActivity);
    void inject(LandingActivity landingActivity);
    void inject(SignInActivity signInActivity);
    void inject(RegisterNameActivity registerNameActivity);
    void inject(HomeActivity homeActivity);
    void inject(DoctorCallActivity doctorCallActivity);
    void inject(SubscriptionActivity subscriptionActivity);
    void inject(SubscriptionPlansActivity subscriptionPlansActivity);
    void inject(BookCallActivity bookCallActivity);
    void inject(PartnersActivity partnersActivity);
    void inject(PartnersLandingActivity partnersLandingActivity);
    void inject(PartnersSearchActivity partnersSearchActivity);
    void inject(PartnersDetailActivity partnersDetailActivity);
    void inject(NewsActivity newsActivity);
    void inject(NewsDetailActivity newsDetailActivity);
    void inject(ProfileActivity profileActivity);
    void inject(GeneralInformationActivity generalInformationActivity);
    void inject(CurrentHealthActivity currentHealthActivity);
    void inject(MedicineInformationActivity medicineInformationActivity);
    void inject(DoctorProfileActivity doctorProfileActivity);
    void inject(CallDetailsActivity callDetailsActivity);
    void inject(AboutActivity aboutActivity);
    void inject(OnboardingOpeningFragment onboardingOpeningFragment);
    void inject(OnboardingPhotoFragment onboardingPhotoFragment);
    void inject(OnboardingListTestimonialFragment onboardingListTestimonialFragment);
    void inject(OnboardingListCorporateFragment onboardingListCorporateFragment);
    void inject(OnboardingSocialFragment onboardingSocialFragment);
    void inject(DrawerFragment drawerFragment);
    void inject(HomeFragment homeFragment);
    void inject(SignInFragment signInFragment);
    void inject(RegisterFragment registerFragment);
    void inject(DoctorCallFragment doctorCallFragment);
    void inject(PartnersFragment partnersFragment);
    void inject(NewsFragment newsFragment);
    void inject(ProfileFragment profileFragment);
    void inject(MedicineInformationFragment medicineInformationFragment);
    void inject(AboutFragment profileFragment);

    final class Initializer {
        public static DokterPribadimuComponent init(Application application) {
            return DaggerDokterPribadimuComponent.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }
    }
}
