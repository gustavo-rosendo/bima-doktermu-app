package com.bima.dokterpribadimu.view.fragments;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.NewsApi;
import com.bima.dokterpribadimu.databinding.FragmentNewsBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.model.NewsResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.viewmodel.HomeItemViewModel;
import com.bima.dokterpribadimu.viewmodel.NewsItemViewModel;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class NewsFragment extends BaseFragment {

    private static final String TAG = NewsFragment.class.getSimpleName();

    private static final String NEWS_CATEGORY = "news_category";

    @Inject
    NewsApi newsApi;

    private FragmentNewsBinding binding;
    private String newsCategory;

    private NewsListViewModel newsListViewModel = new NewsListViewModel();

    public static NewsFragment newInstance(String newsCategory) {
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_CATEGORY, newsCategory);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        newsCategory = getArguments().getString(NEWS_CATEGORY);

        binding.setViewModel(newsListViewModel);

        getNews(newsCategory, UserProfileUtils.getUserProfile(getActivity()).getAccessToken());
    }

    @Override
    public void onDestroy() {
        for (NewsItemViewModel itemViewModel : newsListViewModel.items) {
            itemViewModel.setClickListener(null);
        }
        newsListViewModel.items.clear();

        super.onDestroy();
    }

    /**
     * Get news list
     * @param newsCategory news category
     * @param accessToken user's access token
     */
    private void getNews(final String newsCategory, final String accessToken) {
        newsApi.getNews(newsCategory, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<NewsResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<NewsResponse>>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                        binding.dialogProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.dialogProgressBar.setVisibility(View.GONE);

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<NewsResponse> newsResponse) {
                        if (newsResponse.getStatus() == Constants.Status.SUCCESS) {
                            newsListViewModel.items.clear();

                            for (final News news : newsResponse.getData().getNews()) {
                                newsListViewModel.items.add(
                                    new NewsItemViewModel(
                                            news,
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    IntentUtils.startNewsDetailActivity(getActivity(), news);
                                                }
                                            })
                                );
                            }
                        } else {
                            handleError(TAG, newsResponse.getMessage());
                        }
                    }
                });
    }

    public static class NewsListViewModel {
        public final ObservableList<NewsItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.news_item_viewmodel, R.layout.item_news);
    }
}
