package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogChangePhoneNumberBinding;
import com.bima.dokterpribadimu.utils.UserProfileUtils;

/**
 * Created by gustavo.santos on 5/19/2016.
 */
public class ChangePhoneNumberDialog extends Dialog {

    private DialogChangePhoneNumberBinding binding;

    private OnChangePhoneNumberDialogClickListener listener;

    private Context context;

    public interface OnChangePhoneNumberDialogClickListener {
        void onClick(ChangePhoneNumberDialog dialog, String newPhoneNumber);
    }

    public ChangePhoneNumberDialog(Context context) {
        super(context);

        this.context = context;
    }

    public ChangePhoneNumberDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected ChangePhoneNumberDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_change_phone_number, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
        binding.changePhoneNumberValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPhoneNumber = binding.changePhoneNumberDialogField.getText().toString();
                if (UserProfileUtils.validatePhoneNumber(newPhoneNumber)) {
                    if (listener != null) {
                        listener.onClick(
                                ChangePhoneNumberDialog.this,
                                newPhoneNumber);
                    }
                }
                else {
                    Toast.makeText(
                            DokterPribadimuApplication.getInstance().getApplicationContext(),
                            DokterPribadimuApplication.getInstance().getResources().getString(R.string.invalid_phone_number_message),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    /**
     * Show dialog
     * @return ChangePhoneNumberDialog object
     */
    public ChangePhoneNumberDialog showDialog() {
        show();
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return ChangePhoneNumberDialog object
     */
    public ChangePhoneNumberDialog setListener(ChangePhoneNumberDialog.OnChangePhoneNumberDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
