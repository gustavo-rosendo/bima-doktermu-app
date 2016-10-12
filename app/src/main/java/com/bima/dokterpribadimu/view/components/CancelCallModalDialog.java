package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogCancelCallModalBinding;

/**
 * Created by gusta_000 on 12/10/2016.
 */

public class CancelCallModalDialog extends Dialog {

    private DialogCancelCallModalBinding binding;

    private CancelCallModalDialog.OnCancelCallModalDialogClickListener listener;

    private Context context;

    public interface OnCancelCallModalDialogClickListener {
        void onClick(CancelCallModalDialog dialog);
    }

    public CancelCallModalDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CancelCallModalDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected CancelCallModalDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_cancel_call_modal, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {

        binding.cancelCallModalNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.cancelCallModalConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(
                            CancelCallModalDialog.this);
                }
            }
        });

    }

    public void clearReference() {
        this.listener = null;
        this.context = null;
    }

    /**
     * Show dialog
     * @return CancelCallModalDialog object
     */
    public CancelCallModalDialog showDialog() {
        show();
        //AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_BOOK_CALL_PHONE_NUMBER);
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return CancelCallModalDialog object
     */
    public CancelCallModalDialog setListener(CancelCallModalDialog.OnCancelCallModalDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
