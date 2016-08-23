package com.bima.dokterpribadimu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.DoctorProfileApi;
import com.bima.dokterpribadimu.databinding.ActivityDoctorProfileBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.DoctorProfile;
import com.bima.dokterpribadimu.model.DoctorProfileResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gustavo.santos on 8/23/2016.
 */
public class DoctorProfileActivity extends BaseActivity {

    private static final String TAG = DoctorProfileActivity.class.getSimpleName();

    private static final String DOCTOR_ID = "doctor_id";

    private String doctorId = "";

    private DoctorProfile doctorProfile;

    @Inject
    DoctorProfileApi doctorProfileApi;

    private ActivityDoctorProfileBinding binding;

    public static Intent create(Context context, String doctorId) {
        Intent intent = new Intent(context, DoctorProfileActivity.class);
        intent.putExtra(DOCTOR_ID, doctorId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_profile);
        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        // set default profile picture
        Picasso.with(this)
                .load(R.drawable.ic_profile_picture)
                .into(binding.doctorPictureImage);

        doctorId = getIntent().getExtras().getString(DOCTOR_ID);

        if(doctorId != "") {
            getDoctorProfile(doctorId, UserProfileUtils.getUserProfile(this).getAccessToken());
        }


    }

    /**
     * Get doctor's profile info
     * @param doctorId doctor's id in the database
     * @param accessToken user's access token
     */
    private void getDoctorProfile(final String doctorId, final String accessToken) {
        doctorProfileApi.getDoctorProfile(doctorId, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<DoctorProfileResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<DoctorProfileResponse>>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<DoctorProfileResponse> doctorProfileResponse) {
                        dismissProgressDialog();

                        if (doctorProfileResponse.getStatus() == Constants.Status.SUCCESS) {
                            doctorProfile = doctorProfileResponse.getData().getDoctorProfile();

                            if(doctorProfile != null) {
                                if(doctorProfile.getDoctorName() != null) {
                                    binding.doctorNameText.setText(doctorProfile.getDoctorName());
                                }

                                if(doctorProfile.getCertificationCode() != null) {
                                    binding.doctorCertificationCode.setText(doctorProfile.getCertificationCode());
                                }

                                if(doctorProfile.getSpeciality() != null) {
                                    binding.doctorSpeciality.setText(doctorProfile.getSpeciality());
                                }

                                if(doctorProfile.getUniversity() != null) {
                                    binding.doctorUniversity.setText(doctorProfile.getUniversity());
                                }

                                if(doctorProfile.getDoctorBio() != null) {
                                    binding.doctorBio.setText(doctorProfile.getDoctorBio());
                                }

                                try {
                                    Picasso.with(DoctorProfileActivity.this)
                                            .load(doctorProfile.getDoctorPicture())
                                            .centerCrop()
                                            .fit()
                                            .placeholder(R.drawable.ic_profile_picture)
                                            .error(R.drawable.ic_profile_picture)
                                            .into(binding.doctorPictureImage);
                                } catch (RuntimeException re) {
                                    re.printStackTrace();
                                }
                            }
                            else {
                                // Show default error message
                                handleError(TAG, null);
                            }
                        } else {
                            handleError(TAG, doctorProfileResponse.getMessage());
                        }
                    }
                });
    }

}
