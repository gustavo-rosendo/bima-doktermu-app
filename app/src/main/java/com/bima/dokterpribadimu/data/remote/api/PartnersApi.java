package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.PartnersService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CategoriesResponse;
import com.bima.dokterpribadimu.model.PartnerResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas
 */
public class PartnersApi extends BaseApi<PartnersService> {

    private PartnersService partnersService;

    public PartnersApi() {
        super(BaseApi.API_TYPE_SERVER);
        this.partnersService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return PartnersService.class;
    }

    /**
     * PartnersApi implementation to get partners categories
     * @return Observable<BaseResponse<CategoriesResponse>>
     */
    public Observable<BaseResponse<CategoriesResponse>> getCategories(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<CategoriesResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<CategoriesResponse>> subscriber) {
                try {
                    Response response = partnersService.getCategories(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<CategoriesResponse>) response.body());
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
     * PartnersApi implementation to get partners
     * @return Observable<BaseResponse<PartnerResponse>>
     */
    public Observable<BaseResponse<PartnerResponse>> getPartners(
            final String partnerCategory, final String searchCategory, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<PartnerResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<PartnerResponse>> subscriber) {
                try {
                    Response response = partnersService.getPartners(partnerCategory, searchCategory, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<PartnerResponse>) response.body());
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
