package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentProfileBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String EMAIL_FILTER_REGEX = "(?<=.{2}).(?=[^@]*?.@)";

    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        // set default profile picture
        Picasso.with(getActivity())
                .load(R.drawable.ic_profile_picture)
                .into(binding.profilePictureImage);

        UserProfile userProfile = UserProfileUtils.getUserProfile(getActivity());

        try {
            Picasso.with(getActivity())
                    .load(userProfile.getProfilePicture())
                    .fit()
                    .placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture)
                    .into(binding.profilePictureImage);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }

        binding.profileNameText.setText(userProfile.getName());

        binding.profileAppInfo.setText(
                String.format(getString(R.string.profile_app_info), BuildConfig.VERSION_NAME));

        if (userProfile.getLoginType().equals(Constants.LOGIN_TYPE_EMAIL)) {
            binding.profileEmailAddressText.setText(
                    userProfile.getEmail().replaceAll(EMAIL_FILTER_REGEX, "*"));
        } else {
            binding.profilePersonalInfoLayout.setVisibility(View.GONE);
        }
    }

}
