package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.SubscriptionService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Subscription;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas on 3/12/16.
 */
public class SubscriptionApi extends BaseApi<SubscriptionService> {

    private SubscriptionService subscriptionService;

    public SubscriptionApi() {
        super();
        subscriptionService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return SubscriptionService.class;
    }

    /**
     * SubscriptionApi implementation to register subscription
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> registerSubscription(final Subscription subscription) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = subscriptionService.registerSubscription(
                            subscription.getSubscriptionType(),
                            subscription.getSubscriptionLat(),
                            subscription.getSubscriptionLong(),
                            subscription.getSubscriptionToken(),
                            subscription.getSubscriptionStart(),
                            subscription.getSubscriptionEnd(),
                            subscription.getName(),
                            subscription.getOrderDate(),
                            subscription.getOrderId(),
                            subscription.getPaymentMethod(),
                            subscription.getPhoneNumber(),
                            subscription.getProductName(),
                            subscription.getPrice(),
                            subscription.getDateOfBirth(),
                            subscription.getGender(),
                            subscription.getDateOfPurchase(),
                            subscription.getPolicyActiveDate(),
                            subscription.getPolicyExpiryDate(),
                            subscription.getAccessToken()
                    ).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse) response.body());
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
     * SubscriptionApi implementation to set subscription as expired
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> setSubscriptionExpired(final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = subscriptionService.setSubscriptionExpired(accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse) response.body());
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
     * SubscriptionApi implementation to set subscription as stopped
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> setSubscriptionStopped(final String subscriptionEnd, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = subscriptionService.setSubscriptionStopped(subscriptionEnd, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse) response.body());
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
