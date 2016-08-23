package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.DoctorProfileService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.DoctorProfileResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gustavo.santos on 8/23/2016.
 */
public class DoctorProfileApi extends BaseApi<DoctorProfileService> {

    private DoctorProfileService doctorProfileService;

    public DoctorProfileApi() {
        super(BaseApi.API_TYPE_SERVER);
        this.doctorProfileService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return DoctorProfileService.class;
    }

    /**
     * DoctorProfileApi implementation to get doctor's profile
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<DoctorProfileResponse>> getDoctorProfile(final String doctorId,
                                                                            final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<DoctorProfileResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<DoctorProfileResponse>> subscriber) {
                try {
                    Response response = doctorProfileService.getDoctorProfile(doctorId, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<DoctorProfileResponse>) response.body());
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
