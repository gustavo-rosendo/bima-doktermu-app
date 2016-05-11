package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.NewsService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.model.NewsResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas on 5/11/16.
 */
public class NewsApi extends BaseApi<NewsService> {

    private NewsService newsService;

    public NewsApi() {
        super();
        this.newsService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return NewsService.class;
    }

    /**
     * NewsApi implementation to get news
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<NewsResponse>> getNews(final String newsCategory, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<NewsResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<NewsResponse>> subscriber) {
                try {
                    Response response = newsService.getNews(newsCategory, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<NewsResponse>) response.body());
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
