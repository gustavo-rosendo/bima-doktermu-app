package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogRateYourCallBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;

/**
 * Created by gusta_000 on 13/5/2016.
 */
public class RateYourCallDialog extends Dialog {

    private DialogRateYourCallBinding binding;

    private OnRateYourCallDialogClickListener listener;

    private Context context;

    public interface OnRateYourCallDialogClickListener {
        void onClick(RateYourCallDialog dialog, float rating);
    }

    public RateYourCallDialog(Context context) {
        super(context);

        this.context = context;
    }

    public RateYourCallDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected RateYourCallDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_rate_your_call, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
//        Drawable progress = binding.ratingBar.getProgressDrawable();
//        DrawableCompat.setTint(progress, Color.GRAY);
        binding.ratingBar.setProgress(0);

        binding.rateSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = binding.ratingBar.getRating();
                if (validateRating(rating)) {
                    if (listener != null) {
                        listener.onClick(
                                RateYourCallDialog.this,
                                rating);
                        Log.d("RateYourCallDialog", "Rating given: " + rating);
                    }
                }
                else {
                    Toast.makeText(
                            DokterPribadimuApplication.getInstance().getApplicationContext(),
                            DokterPribadimuApplication.getInstance().getString(R.string.rate_your_call_invalid),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        binding.rateCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        // Reset last call id
        resetLastCallId();
        super.dismiss();
    }

    private boolean validateRating(float rating) {
        boolean isRatingValid = false;
        if (rating > 0.0) {
            isRatingValid = true;
        }

        return isRatingValid;
    }

    public void clearReference() {
        this.listener = null;
        this.context = null;
    }

    /**
     * Show dialog
     * @return RateYourCallDialog object
     */
    public RateYourCallDialog showDialog() {
        show();
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return RateYourCallDialog object
     */
    public RateYourCallDialog setListener(OnRateYourCallDialogClickListener listener) {
        this.listener = listener;
        return this;
    }

    private void resetLastCallId() {
        // Reset last call id so that the Rating dialog will not show again
        StorageUtils.putString(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Constants.KEY_BOOK_CALL_ID_LAST_CALL,
                "0");
        // Reset book call time
        StorageUtils.putDouble(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                -1);
    }
}
