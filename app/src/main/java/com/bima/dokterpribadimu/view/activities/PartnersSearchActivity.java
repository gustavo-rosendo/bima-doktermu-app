package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.PartnersApi;
import com.bima.dokterpribadimu.databinding.ActivityPartnersSearchBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Partner;
import com.bima.dokterpribadimu.model.PartnerResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.viewmodel.SearchItemViewModel;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PartnersSearchActivity extends BaseActivity {

    private static final String TAG = PartnersSearchActivity.class.getSimpleName();

    private static final int MINIMUM_SEARCH_LENGTH = 4;

    @Inject
    PartnersApi partnersApi;

    private ActivityPartnersSearchBinding binding;

    private SearchListViewModel searchListViewModel = new SearchListViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_partners_search);

        DokterPribadimuApplication.getComponent().inject(this);

        initViews();
    }

    private void initViews() {
        binding.setViewModel(searchListViewModel);

        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.toolbarSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= MINIMUM_SEARCH_LENGTH) {
                    searchPartners(
                            editable.toString(),
                            UserProfileUtils.getUserProfile(PartnersSearchActivity.this).getAccessToken()
                    );
                }
            }
        });

        binding.toolbarSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPartners(
                            binding.toolbarSearch.getText().toString(),
                            UserProfileUtils.getUserProfile(PartnersSearchActivity.this).getAccessToken()
                    );
                }
                return false;
            }
        });
    }

    /**
     * Get partners by input query
     * @param accessToken user's access token
     */
    private void searchPartners(final String query, final String accessToken) {
        partnersApi.getPartners("", query, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<PartnerResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<PartnerResponse>>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        searchListViewModel.items.clear();

                        Toast.makeText(
                                PartnersSearchActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onNext(BaseResponse<PartnerResponse> partnerResponse) {
                        if (partnerResponse.getStatus() == Constants.Status.SUCCESS) {
                            searchListViewModel.items.clear();

                            for (Partner partner : partnerResponse.getData().getPartner()) {
                                searchListViewModel.items.add(
                                        new SearchItemViewModel(
                                                partner,
                                                R.drawable.ic_place,
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        // TODO
                                                    }
                                                })
                                );
                            }
                        } else {
                            searchListViewModel.items.clear();

                            Toast.makeText(
                                    PartnersSearchActivity.this,
                                    partnerResponse.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    public static class SearchListViewModel {
        public final ObservableList<SearchItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.search_item_viewmodel, R.layout.item_search);
    }
}
