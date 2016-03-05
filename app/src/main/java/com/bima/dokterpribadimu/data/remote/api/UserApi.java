package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.UserService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Token;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas on 3/5/16.
 */
public class UserApi extends BaseApi<UserService> {

    private UserService userService;

    public UserApi() {
        super();
        userService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return UserService.class;
    }

    /**
     * UserApi implementation to login
     * @return Observable<BaseResponse<Token>>
     */
    public Observable<BaseResponse<Token>> login(final String email, final String password, final String loginType) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<Token>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<Token>> subscriber) {
                try {
                    Response response = userService.login(email, password, loginType).execute();
                    if (response.isSuccess()) {
                        subscriber.onNext((BaseResponse<Token>) response.body());
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
     * UserApi implementation to register
     * @return Observable<BaseResponse<Token>>
     */
    public Observable<BaseResponse<Token>> register(final String email, final String password, final String loginType) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<Token>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<Token>> subscriber) {
                try {
                    Response response = userService.register(email, password, loginType).execute();
                    if (response.isSuccess()) {
                        subscriber.onNext((BaseResponse<Token>) response.body());
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
