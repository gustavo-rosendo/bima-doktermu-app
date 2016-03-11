package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.BookingService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas on 3/11/16.
 */
public class BookingApi extends BaseApi<BookingService> {

    private BookingService bookingService;

    public BookingApi() {
        super();
        bookingService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return BookingService.class;
    }

    /**
     * BookingApi implementation to book a call
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> bookCall(
            final String callTopic, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = bookingService.bookCall(callTopic, accessToken).execute();
                    if (response.isSuccess()) {
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
