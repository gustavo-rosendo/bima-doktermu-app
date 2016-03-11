package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogDokterPribadimuBinding;

/**
 * Created by apradanas on 2/20/16.
 */
public class DokterPribadimuDialog extends Dialog {

    // dialog type
    public static final int DIALOG_TYPE_SUCCESS = 0;
    public static final int DIALOG_TYPE_ERROR = 1;
    public static final int DIALOG_TYPE_LATE = 2;

    private DialogDokterPribadimuBinding binding;

    private OnDokterPribadimuDialogClickListener dialogClickListener;

    private int dialogType = DIALOG_TYPE_SUCCESS;
    private int dialogImageResource;
    private String dialogTitle;
    private String dialogMessage;
    private String dialogButtonText;

    private Context context;

    public interface OnDokterPribadimuDialogClickListener {
        void onClick(DokterPribadimuDialog dialog);
    }

    public DokterPribadimuDialog(Context context) {
        super(context);

        this.context = context;
    }

    public DokterPribadimuDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected DokterPribadimuDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_dokter_pribadimu, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
        binding.dialogButton.setOnClickListener(clickListener);
        binding.dialogImage.setImageResource(dialogImageResource != 0 ? dialogImageResource : getDefaultDialogImageResource());
        binding.dialogTitle.setText(dialogTitle != null ? dialogTitle : getDefaultDialogTitle());
        binding.dialogMessage.setText(dialogMessage != null ? dialogMessage : getDefaultDialogMessage());
        binding.dialogButton.setText(dialogButtonText != null ? dialogButtonText : context.getString(R.string.ok));
    }

    /**
     * Get default image resouce of the dialog
     * @return dialog image resource id
     */
    private @DrawableRes int getDefaultDialogImageResource() {
        int resId = R.drawable.ic_dialog_success;
        switch (dialogType) {
            case DIALOG_TYPE_ERROR:
                resId = R.drawable.ic_bug;
                break;
            case DIALOG_TYPE_LATE:
                resId = R.drawable.ic_night;
                break;
            case DIALOG_TYPE_SUCCESS:
                resId = R.drawable.ic_dialog_success;
                break;
        }
        return resId;
    }

    /**
     * Get default dialog title based on dialog type.
     * @return String default dialog title
     */
    private String getDefaultDialogTitle() {
        int stringId = R.string.dialog_success;
        switch (dialogType) {
            case DIALOG_TYPE_ERROR:
                stringId = R.string.dialog_failed;
                break;
            case DIALOG_TYPE_LATE:
                stringId = R.string.dialog_late;
                break;
            case DIALOG_TYPE_SUCCESS:
                stringId = R.string.dialog_success;
                break;
        }
        return context.getString(stringId);
    }

    /**
     * Get default dialog message based on dialog type.
     * @return String default dialog message
     */
    private String getDefaultDialogMessage() {
        int stringId = R.string.dialog_sign_in_success_message;
        switch (dialogType) {
            case DIALOG_TYPE_ERROR:
                stringId = R.string.dialog_sign_in_failed_message;
                break;
            case DIALOG_TYPE_LATE:
                stringId = R.string.dialog_late_message;
                break;
            case DIALOG_TYPE_SUCCESS:
                stringId = R.string.dialog_sign_in_success_message;
                break;
        }
        return context.getString(stringId);
    }

    /**
     * Show dialog. Reset button state and re-initialize dialog's view if binding is not null so
     * the dialog instance can be reusable for another dialog type.
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog showDialog() {
        if (binding != null) {
            initDialogViews();
        }
        show();
        return this;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (dialogClickListener != null) {
                dialogClickListener.onClick(DokterPribadimuDialog.this);
            }

            dismiss();
        }
    };

    /**
     * Set image resource id of the dialog
     * @param dialogImageResource type of dialog
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogImageResource(int dialogImageResource) {
        this.dialogImageResource = dialogImageResource;
        return this;
    }

    /**
     * Get the image resource id of the dialog
     * @return int dialog image resource id
     */
    public int getDialogImageResource() {
        return dialogImageResource;
    }

    /**
     * Set the type of the dialog
     * @param dialogType type of dialog
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogType(int dialogType) {
        this.dialogType = dialogType;
        return this;
    }

    /**
     * Get the type of the dialog
     * @return int dialog type
     */
    public int getDialogType() {
        return dialogType;
    }

    /**
     * Set the title of the dialog
     * @param dialogTitle String dialog title, or null if using the default one
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    /**
     * Get the title of the dialog
     * @return String dialog title
     */
    public String getDialogTitle() {
        return dialogTitle;
    }

    /**
     * Set the message of the dialog
     * @param dialogMessage String dialog message, or null if using the default one
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
        return this;
    }

    /**
     * Get the message of the dialog
     * @return String dialog message
     */
    public String getDialogMessage() {
        return dialogMessage;
    }

    /**
     * Set the text of the dialog button
     * @param dialogButtonText String dialog button text
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogButtonText(String dialogButtonText) {
        this.dialogButtonText = dialogButtonText;
        return this;
    }

    /**
     * Get the text of the dialog button
     * @return String dialog button text
     */
    public String getDialogButtonText() {
        return dialogButtonText;
    }

    /**
     * Set whether dialog is cancelable or not
     * @param cancelable true if dialog is cancelable, false if otherwise
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setDialogCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param clickListener object for button callback
     * @return DokterPribadimuDialog object
     */
    public DokterPribadimuDialog setClickListener(OnDokterPribadimuDialogClickListener clickListener) {
        dialogClickListener = clickListener;
        return this;
    }
}
