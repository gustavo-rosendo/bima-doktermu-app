package com.bima.dokterpribadimu.injection;

import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.data.remote.api.UserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apradanas on 2/3/16.
 */
@Module
public final class DataModule {

    @Provides
    @Singleton
    UserApi provideUserService() {
        return new UserApi();
    }

    @Provides
    @Singleton
    BookingApi provideBookingService() {
        return new BookingApi();
    }

    @Provides
    @Singleton
    BillingClient provideBillingClient() {
        return new BillingClient();
    }
}
