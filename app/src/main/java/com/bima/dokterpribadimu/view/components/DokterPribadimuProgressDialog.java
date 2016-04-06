package com.bima.dokterpribadimu.view.components;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.DialogDokterPribadimuProgressBinding;

/**
 * Created by apradanas on 3/19/16.
 */
public class DokterPribadimuProgressDialog extends Dialog {

    private Context context;

    private DialogDokterPribadimuProgressBinding binding;

    public DokterPribadimuProgressDialog(Context context) {
        super(context);

        this.context = context;
    }

    public DokterPribadimuProgressDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
    }

    protected DokterPribadimuProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_dokter_pribadimu_progress, null, false);
        setContentView(binding.getRoot());

        setCancelable(false);

        initViews();
    }

    private void initViews() {
        binding.dialogProgressBar.getIndeterminateDrawable().setColorFilter(
                context.getResources().getColor(R.color.bima_blue),
                android.graphics.PorterDuff.Mode.SRC_IN);
    }
}
