package com.bima.dokterpribadimu.view.fragments;


import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentRegisterBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.DeviceInfoUtils;
import com.bima.dokterpribadimu.utils.TokenUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.activities.RegisterNameActivity;
import com.bima.dokterpribadimu.view.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    private static final int RC_PHONE_STATE_PERMISSION = 0;

    private static final String TAG = RegisterFragment.class.getSimpleName();

    @Inject
    UserApi userApi;

    private FragmentRegisterBinding binding;

    private DeviceInfoUtils deviceInfoUtils;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        initDeviceInfo();
        initViews();

        return binding.getRoot();
    }

    @AfterPermissionGranted(RC_PHONE_STATE_PERMISSION)
    public void initDeviceInfo() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            deviceInfoUtils = DeviceInfoUtils.getInstance(getActivity());
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_PHONE_STATE_PERMISSION, Manifest.permission.READ_PHONE_STATE);
        }
    }

    private void initViews() {
        binding.registerPasswordHint.setText(
                String.format(
                        getString(R.string.invalid_password_message),
                        ValidationUtils.MINIMUM_PASSWORD_LENGTH));

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.registerEmailField.getText().toString();
                String password = binding.registerPasswordField.getText().toString();
                if (validateRegistration(email, password)) {
                    UserProfile userProfile = new UserProfile(
                            "",
                            "",
                            "",
                            "",
                            email,
                            "",
                            "",
                            "",
                            Constants.LOGIN_TYPE_EMAIL,
                            0.0,
                            0.0,
                            TokenUtils.generateToken(email + System.currentTimeMillis())
                    );

                    if (deviceInfoUtils != null) {
                        userProfile.setDeviceType(
                                String.format(
                                        Constants.DEVICE_TYPE_FORMAT,
                                        deviceInfoUtils.getBrand(),
                                        deviceInfoUtils.getProduct()));
                        userProfile.setDeviceImei(deviceInfoUtils.getDeviceId());
                        userProfile.setDeviceOperator(deviceInfoUtils.getSimOperatorName());
                        userProfile.setDeviceSoftware(String.format(Constants.DEVICE_SOFTWARE_FORMAT, deviceInfoUtils.getRelease()));
                    }

                    startActivity(RegisterNameActivity.create(getActivity(), userProfile, password));
                }
            }
        });
    }

    /**
     * Validate registration email and password.
     * @param email user's email
     * @param password user's password
     * @return boolean true if email and password are valid, boolean false if otherwise
     */
    private boolean validateRegistration(String email, String password) {
        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.invalid_email_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (!ValidationUtils.isValidPassword(password)) {
            Toast.makeText(
                    getActivity(),
                    String.format(getString(R.string.invalid_password_message), ValidationUtils.MINIMUM_PASSWORD_LENGTH),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
