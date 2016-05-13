package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogPhoneInfoModalBinding;

/**
 * Created by gustavo.santos on 5/13/2016.
 */
public class PhoneInfoModalDialog extends Dialog {

    private DialogPhoneInfoModalBinding binding;

    private OnPhoneInfoModalDialogClickListener listener;

    private Context context;

    public interface OnPhoneInfoModalDialogClickListener {
        void onClick(PhoneInfoModalDialog dialog);
    }

    public PhoneInfoModalDialog(Context context) {
        super(context);
        this.context = context;
    }

    public PhoneInfoModalDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected PhoneInfoModalDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_phone_info_modal, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {

        binding.phoneInfoModalOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void clearReference() {
        this.listener = null;
        this.context = null;
    }

    /**
     * Show dialog
     * @return PhoneInfoModalDialog object
     */
    public PhoneInfoModalDialog showDialog() {
        show();
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return PhoneInfoModalDialog object
     */
    public PhoneInfoModalDialog setListener(OnPhoneInfoModalDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
