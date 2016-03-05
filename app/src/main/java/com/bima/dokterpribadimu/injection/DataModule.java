package com.bima.dokterpribadimu.injection;

import com.bima.dokterpribadimu.data.remote.api.UserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apradanas on 2/3/16.
 */
@Module
public class DataModule {

    @Provides
    @Singleton
    UserApi provideUserService() {
        return new UserApi();
    }
}
