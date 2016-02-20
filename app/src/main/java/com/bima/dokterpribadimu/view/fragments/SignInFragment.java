package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentSignInBinding;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class SignInFragment extends BaseFragment {

    private FragmentSignInBinding binding;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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
        binding = FragmentSignInBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signInEmailField.getText().toString();
                String password = binding.signInPasswordField.getText().toString();
                if (validateSignIn(email, password)) {
                    // TODO: request sign-in
                    showSuccessDialog(
                            getString(R.string.dialog_success),
                            getString(R.string.dialog_sign_in_success_message),
                            getString(R.string.dialog_get_started),
                            new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                @Override
                                public void onClick(DokterPribadimuDialog dialog) {
                                    startHomeActivityOnTop();
                                }
                            });
                }
            }
        });
    }

    /**
     * Validate login email and password.
     * @param email user's email
     * @param password user's password
     * @return boolean true if email and password are valid, boolean false if otherwise
     */
    private boolean validateSignIn(String email, String password) {
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
