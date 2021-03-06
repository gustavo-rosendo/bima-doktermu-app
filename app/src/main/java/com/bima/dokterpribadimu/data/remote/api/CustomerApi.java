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
        super(BaseApi.API_TYPE_SERVER);
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
    public Observable<BaseResponse> updateCustomer(final Customer customer) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = customerService.registerCustomer(
                                                customer.getName(),
                                                customer.getMsisdn(),
                                                customer.getDob(),
                                                customer.getGender(),
                                                customer.getEmail(),
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

    /**
     * CustomerApi implementation to change firebase token
     *
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> changeFirebaseToken(
            final String newFirebaseToken, final String accessToken){
        return Observable.create(new Observable.OnSubscribe<BaseResponse>(){
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber){
                try{
                    Response response = customerService.changeFirebaseToken(newFirebaseToken, accessToken).execute();
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
}
