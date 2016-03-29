package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogForgotPasswordBinding;
import com.bima.dokterpribadimu.utils.ValidationUtils;

/**
 * Created by apradanas on 3/29/16.
 */
public class ForgotPasswordDialog extends Dialog {

    private DialogForgotPasswordBinding binding;

    private OnForgotPasswordDialogClickListener listener;

    private Context context;

    public interface OnForgotPasswordDialogClickListener {
        void onClick(ForgotPasswordDialog dialog, String email);
    }

    public ForgotPasswordDialog(Context context) {
        super(context);

        this.context = context;
    }

    public ForgotPasswordDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected ForgotPasswordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    /**
     * Initialize dialog. Set NO TITLE, set dialog background transparent, and set dialog's content view.
     */
    private void init() {
        // requestFeature() must be called before adding content
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_forgot_password, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
        binding.forgotValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.forgotEmailField.getText().toString();
                if (validateEmail(email)) {
                    if (listener != null) {
                        listener.onClick(
                                ForgotPasswordDialog.this,
                                email);
                    }
                }
            }
        });

        binding.forgotCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private boolean validateEmail(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            binding.forgotEmailContainer.setError(context.getString(R.string.invalid_email_message));
        } else {
            binding.forgotEmailContainer.setError(null);
        }

        return ValidationUtils.isValidEmail(email);
    }

    public void clearReference() {
        this.listener = null;
        this.context = null;
    }

    /**
     * Show dialog
     * @return ForgotPasswordDialog object
     */
    public ForgotPasswordDialog showDialog() {
        show();
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return ForgotPasswordDialog object
     */
    public ForgotPasswordDialog setListener(OnForgotPasswordDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
