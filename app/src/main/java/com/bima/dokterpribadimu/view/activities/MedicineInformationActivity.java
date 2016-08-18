package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.ProfileApi;
import com.bima.dokterpribadimu.databinding.ActivityMedicineInformationBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.MedicineInfo;
import com.bima.dokterpribadimu.model.MedicineInfoResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.fragments.MedicineInformationFragment;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MedicineInformationActivity extends BaseActivity {

    private static final String TAG = MedicineInformationActivity.class.getSimpleName();

    private static final int PRESCRIPTION_POSITION = 0;
    private static final int IMMUNISATION_POSITION = 1;

    private ActivityMedicineInformationBinding binding;

    @Inject
    ProfileApi profileApi;

    private static final int[] TITLE_STRING_IDS = {
            R.string.medicine_prescription,
            R.string.medicine_immunisation
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_information);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.medicineViewpager.setVisibility(View.GONE);
        binding.medicineViewpagerTab.setVisibility(View.GONE);

        getMedicineInformation(UserProfileUtils.getUserProfile(this).getAccessToken());
    }

    /**
     * Get medicine information list
     * @param accessToken user's access token
     */
    private void getMedicineInformation(final String accessToken) {
        profileApi.getMedicineInformation(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<MedicineInfoResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<MedicineInfoResponse>>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG, e.getMessage(), new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                            @Override
                            public void onClick(DokterPribadimuDialog dialog) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onNext(BaseResponse<MedicineInfoResponse> medicineInfoResponse) {
                        if (medicineInfoResponse.getStatus() == Constants.Status.SUCCESS) {
                            MedicineInfo medicineInfo = medicineInfoResponse.getData().getMedicineInfo();
                            updateViews(medicineInfo);
                        } else {
                            handleError(TAG, medicineInfoResponse.getMessage(), new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                @Override
                                public void onClick(DokterPribadimuDialog dialog) {
                                    finish();
                                }
                            });
                        }
                    }
                });
    }

    private void updateViews(final MedicineInfo medicineInfo) {
        binding.medicineViewpager.setVisibility(View.VISIBLE);
        binding.medicineViewpagerTab.setVisibility(View.VISIBLE);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case PRESCRIPTION_POSITION:
                        return MedicineInformationFragment.newInstance(medicineInfo.getPrescription(), null, false);
                    case IMMUNISATION_POSITION:
                        return MedicineInformationFragment.newInstance(null, medicineInfo.getImmunisation(), true);
                    default:
                        return MedicineInformationFragment.newInstance(medicineInfo.getPrescription(), null, false);
                }
            }

            @Override
            public int getCount() {
                return TITLE_STRING_IDS.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(TITLE_STRING_IDS[position]);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        binding.medicineViewpager.setAdapter(adapter);
        binding.medicineViewpagerTab.setupWithViewPager(binding.medicineViewpager);
    }
}
