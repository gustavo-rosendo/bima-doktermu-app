package com.bima.dokterpribadimu.injection;

import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.data.remote.api.CallHistoryApi;
import com.bima.dokterpribadimu.data.remote.api.CustomerApi;
import com.bima.dokterpribadimu.data.remote.api.DirectionsApi;
import com.bima.dokterpribadimu.data.remote.api.NewsApi;
import com.bima.dokterpribadimu.data.remote.api.PartnersApi;
import com.bima.dokterpribadimu.data.remote.api.ProfileApi;
import com.bima.dokterpribadimu.data.remote.api.SubscriptionApi;
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
    CustomerApi provideCustomerService() {
        return new CustomerApi();
    }

    @Provides
    @Singleton
    DirectionsApi provideDirectionsService() {
        return new DirectionsApi();
    }

    @Provides
    @Singleton
    SubscriptionApi provideSubscriptionService() {
        return new SubscriptionApi();
    }

    @Provides
    @Singleton
    NewsApi provideNewsService() {
        return new NewsApi();
    }

    @Provides
    @Singleton
    ProfileApi provideProfileService() {
        return new ProfileApi();
    }

    @Provides
    @Singleton
    PartnersApi providePartnersService() {
        return new PartnersApi();
    }

    @Provides
    @Singleton
    CallHistoryApi provideCallHistoryService() {
        return new CallHistoryApi();
    }

    @Provides
    @Singleton
    BillingClient provideBillingClient() {
        return new BillingClient();
    }
}
