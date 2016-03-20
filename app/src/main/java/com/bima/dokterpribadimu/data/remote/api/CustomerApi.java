package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.CustomerService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Customer;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas on 3/12/16.
 */
public class CustomerApi extends BaseApi<CustomerService> {

    private CustomerService customerService;

    public CustomerApi() {
        super();
        this.customerService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return CustomerService.class;
    }

    /**
     * CustomerApi implementation to register customer
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> registerCustomer(final Customer customer) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = customerService.registerCustomer(
                                                customer.getName(),
                                                customer.getDob(),
                                                customer.getMsisdn(),
                                                customer.getProduct(),
                                                customer.getSubscriptionType(),
                                                customer.getDateOfPurchase(),
                                                customer.getPolicyActiveDate(),
                                                customer.getPolicyExpiryDate(),
                                                customer.getEmail(),
                                                customer.getGender(),
                                                customer.getSubscriptionLat(),
                                                customer.getSubscriptionLong(),
                                                customer.getSubscriptionToken(),
                                                customer.getDeviceType(),
                                                customer.getDeviceImei(),
                                                customer.getDeviceSoftware(),
                                                customer.getDeviceOperator(),
                                                customer.getAccessToken()
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
}
