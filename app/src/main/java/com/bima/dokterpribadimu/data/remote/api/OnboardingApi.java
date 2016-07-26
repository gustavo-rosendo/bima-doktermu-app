package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.OnboardingService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.OnboardingResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas.
 */
public class OnboardingApi extends BaseApi<OnboardingService> {

    private OnboardingService onboardingService;

    public OnboardingApi() {
        super(BaseApi.API_TYPE_SERVER);
        this.onboardingService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return OnboardingService.class;
    }

    /**
     * OnboardingApi implementation to get onboarding list
     * @return Observable<BaseResponse<OnboardingResponse>>
     */
    public Observable<BaseResponse<OnboardingResponse>> getOnboarding() {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<OnboardingResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<OnboardingResponse>> subscriber) {
                try {
                    Response response = onboardingService.getOnboarding().execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<OnboardingResponse>) response.body());
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
