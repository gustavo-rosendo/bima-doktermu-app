package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.BookingService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallAssignedResponse;
import com.bima.dokterpribadimu.model.CallHistoryResponse;
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
        super(BaseApi.API_TYPE_SERVER);
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
            final String callTopic, final String callSubTopic,
            final CharSequence callUserNotes,
            final String phoneNumber, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = bookingService.bookCall(callTopic,
                                                                callSubTopic,
                                                                callUserNotes,
                                                                phoneNumber,
                                                                accessToken).execute();
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
     * BookingApi implementation to cancel a call
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> cancelCall(
            final String callId, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(final Subscriber<? super BaseResponse> subscriber) {
                try {
                    Response response = bookingService.cancelCall(callId,
                            accessToken).execute();
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
     * BookingApi implementation to get the details of the assigned call
     * @return Observable<BaseResponse<CallAssignedResponse>
     */
    public Observable<BaseResponse<CallAssignedResponse>> getCallAssignment(final String callId, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<CallAssignedResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<CallAssignedResponse>> subscriber) {
                try {
                    Response response = bookingService.getCallAssignment(callId, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<CallAssignedResponse>) response.body());
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
