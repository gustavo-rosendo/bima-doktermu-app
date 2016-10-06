package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.CallHistoryService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallHistoryDetailsResponse;
import com.bima.dokterpribadimu.model.CallHistoryResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public class CallHistoryApi extends BaseApi<CallHistoryService> {

    private CallHistoryService callHistoryService;

    public CallHistoryApi() {
        super(BaseApi.API_TYPE_SERVER);
        this.callHistoryService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return CallHistoryService.class;
    }


    /**
     * CallHistoryApi implementation to get call history
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<CallHistoryResponse>> getCallHistory(final int limit, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<CallHistoryResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<CallHistoryResponse>> subscriber) {
                try {
                    Response response = callHistoryService.getCallHistory(limit, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<CallHistoryResponse>) response.body());
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
     * CallHistoryApi implementation to get call history details
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<CallHistoryDetailsResponse>> getCallHistoryDetails(final String callId, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<CallHistoryDetailsResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<CallHistoryDetailsResponse>> subscriber) {
                try {
                    Response response = callHistoryService.getCallHistoryDetails(callId, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<CallHistoryDetailsResponse>) response.body());
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
