package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.ProfileService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.ProfileResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gustavo.santos on 5/16/2016.
 */
public class ProfileApi extends BaseApi<ProfileService> {

    private ProfileService profileService;

    public ProfileApi() {
        super();
        this.profileService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return ProfileService.class;
    }

    /**
     * ProfileApi implementation to get user's profile info
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<ProfileResponse>> getProfileInfo(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<ProfileResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<ProfileResponse>> subscriber) {
                try {
                    Response response = profileService.getProfileInfo(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<ProfileResponse>) response.body());
                        subscriber.onCompleted();
                    } else {
                        BaseResponse error = GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
