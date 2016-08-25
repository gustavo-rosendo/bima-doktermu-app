package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.ProfileService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.HealthConditionResponse;
import com.bima.dokterpribadimu.model.InformationResponse;
import com.bima.dokterpribadimu.model.MedicineInfoResponse;
import com.bima.dokterpribadimu.model.ProfileResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.http.Field;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gustavo.santos on 5/16/2016.
 */
public class ProfileApi extends BaseApi<ProfileService> {

    private ProfileService profileService;

    public ProfileApi() {
        super(BaseApi.API_TYPE_SERVER);
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

    /**
     * ProfileApi implementation to change phone number
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> changePhoneNumber(
            final String newPhoneNumber, final String accessToken){
        return Observable.create(new Observable.OnSubscribe<BaseResponse>(){
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber){
                try{
                    Response response = profileService.changePhoneNumber(newPhoneNumber, accessToken).execute();
                    if(response.isSuccessful()){
                        subscriber.onNext((BaseResponse)response.body());
                        subscriber.onCompleted();
                    }else{
                        BaseResponse error= GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * ProfileApi implementation to get user's health info
     * @return Observable<BaseResponse<InformationResponse>>
     */
    public Observable<BaseResponse<InformationResponse>> getHealthInformation(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<InformationResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<InformationResponse>> subscriber) {
                try {
                    Response response = profileService.getHealthInformation(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<InformationResponse>) response.body());
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

    /**
     * ProfileApi implementation to update user's health information
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> updateHealthInformation(final String height, final String weight, final String religion,
                                                            final String bloodType, final String smoker, final String physicalExercise,
                                                            final String healthInsurance, final String accessToken){
        return Observable.create(new Observable.OnSubscribe<BaseResponse>(){
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber){
                try{
                    Response response = profileService.updateHealthInformation(
                            height, weight, religion, bloodType, smoker, physicalExercise, healthInsurance, accessToken).execute();
                    if(response.isSuccessful()){
                        subscriber.onNext((BaseResponse)response.body());
                        subscriber.onCompleted();
                    }else{
                        BaseResponse error= GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * ProfileApi implementation to get user's health condition
     * @return Observable<BaseResponse<HealthConditionResponse>>
     */
    public Observable<BaseResponse<HealthConditionResponse>> getHealthCondition(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<HealthConditionResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<HealthConditionResponse>> subscriber) {
                try {
                    Response response = profileService.getHealthCondition(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<HealthConditionResponse>) response.body());
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

    /**
     * ProfileApi implementation to update user's health condition
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> updateHealthCondition(final String diabetes, final List<String> cancer,
                                                          final String bloodPressure, final List<String> allergies,
                                                          final String asthma, final String pregnant, final String accessToken){
        return Observable.create(new Observable.OnSubscribe<BaseResponse>(){
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber){
                try{
                    Response response = profileService.updateHealthCondition(
                            diabetes, cancer, bloodPressure, allergies, asthma, pregnant, accessToken).execute();
                    if(response.isSuccessful()){
                        subscriber.onNext((BaseResponse)response.body());
                        subscriber.onCompleted();
                    }else{
                        BaseResponse error= GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                }catch(IOException e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * ProfileApi implementation to get user's medicine information
     * @return Observable<BaseResponse<MedicineInfoResponse>>
     */
    public Observable<BaseResponse<MedicineInfoResponse>> getMedicineInformation(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<MedicineInfoResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<MedicineInfoResponse>> subscriber) {
                try {
                    Response response = profileService.getMedicineInformation(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<MedicineInfoResponse>) response.body());
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
