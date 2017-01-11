package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.databinding.DialogGpsDisabledBinding;

/**
 * Created by gustavo.santos on 1/11/2017.
 */

public class GPSDisabledDialog extends Dialog {

    private DialogGpsDisabledBinding binding;

    private GPSDisabledDialog.OnGPSDisabledDialogClickListener listener;

    private Context context;

    public interface OnGPSDisabledDialogClickListener {
        void onClick(GPSDisabledDialog dialog);
    }

    public GPSDisabledDialog(Context context) {
        super(context);

        this.context = context;
    }

    public GPSDisabledDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected GPSDisabledDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_gps_disabled, null, false);
        setContentView(binding.getRoot());

        initDialogViews();
    }

    /**
     * Initialize dialog views. Do all view related here.
     */
    private void initDialogViews() {
        binding.gpsActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(
                            GPSDisabledDialog.this);
                }
            }
        });

        binding.gpsCancelButton.setOnClickListener(new View.OnClickListener() {
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
     * @return GPSDisabledDialog object
     */
    public GPSDisabledDialog showDialog() {
        show();
        AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_PARTNERS_GPS_DISABLED);
        return this;
    }

    /**
     * Set dialog action button click listener
     * @param listener object for button callback
     * @return GPSDisabledDialog object
     */
    public GPSDisabledDialog setListener(GPSDisabledDialog.OnGPSDisabledDialogClickListener listener) {
        this.listener = listener;
        return this;
    }
}
