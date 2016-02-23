package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentRegisterBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class RegisterFragment extends BaseFragment {

    private FragmentRegisterBinding binding;

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

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.registerEmailField.getText().toString();
                String password = binding.registerPasswordField.getText().toString();
                if (validateRegistration(email, password)) {
                    // TODO: request sign-in
                    showSuccessDialog(
                            R.drawable.ic_dialog_success,
                            getString(R.string.dialog_success),
                            getString(R.string.dialog_sign_in_success_message),
                            getString(R.string.dialog_get_started),
                            new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                @Override
                                public void onClick(DokterPribadimuDialog dialog) {
                                    UserProfile userProfile = new UserProfile(
                                                                        "",
                                                                        email,
                                                                        "",
                                                                        "",
                                                                        email,
                                                                        "",
                                                                        Constants.LOGIN_TYPE_EMAIL
                                                                );
                                    StorageUtils.putString(
                                            getActivity(),
                                            Constants.KEY_USER_PROFILE,
                                            GsonUtils.toJson(userProfile)
                                    );

                                    startDoctorCallActivityOnTop();
                                }
                            });
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

}
