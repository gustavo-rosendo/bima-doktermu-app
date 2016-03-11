package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogChangePasswordBinding;
import com.bima.dokterpribadimu.utils.ValidationUtils;

/**
 * Created by apradanas on 3/11/16.
 */
public class ChangePasswordDialog extends Dialog {

    private DialogChangePasswordBinding binding;

    private OnChangePasswordDialogClickListener listener;

    private Context context;

    public interface OnChangePasswordDialogClickListener {
        void onClick(ChangePasswordDialog dialog, String oldPassword, String newPassword);
    }

    public ChangePasswordDialog(Context context) {
        super(context);

        this.context = context;
    }

    public ChangePasswordDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected ChangePasswordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
        //requestFeature() must be called before adding content
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_change_password, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
        binding.editValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = binding.editOldPasswordField.getText().toString();
                String newPassword = binding.editNewPasswordField.getText().toString();
                if (validatePassword(oldPassword, newPassword)) {
                    if (listener != null) {
                        listener.onClick(
                                ChangePasswordDialog.this,
                                oldPassword,
                                newPassword);
                    }
                }
            }
        });
    }

    /**
     * Validate new and old password.
     * @param oldPassword user's old password
     * @param newPassword user's new password
     * @return boolean true if new and old password are valid, boolean false if otherwise
     */
    private boolean validatePassword(String oldPassword, String newPassword) {
        if (!ValidationUtils.isValidPassword(oldPassword)
                || !ValidationUtils.isValidPassword(newPassword)) {
            Toast.makeText(
                    context,
                    String.format(
                            context.getString(R.string.invalid_password_message),
                            ValidationUtils.MINIMUM_PASSWORD_LENGTH),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }

    /**
     * Show dialog
     * @return ChangePasswordDialog object
     */
    public ChangePasswordDialog showDialog() {
        show();
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return ChangePasswordDialog object
     */
    public ChangePasswordDialog setListener(OnChangePasswordDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
