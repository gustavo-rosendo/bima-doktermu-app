package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityBookCallBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

public class BookCallActivity extends BaseActivity {

    private ActivityBookCallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_call);

        DokterPribadimuApplication.getComponent().inject(this);

        initViews();
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.bookCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateTopic()) {
                    showSuccessDialog(
                            R.drawable.ic_thumb_up,
                            getString(R.string.dialog_success),
                            getString(R.string.dialog_book_call_done_message),
                            getString(R.string.dialog_waiting),
                            new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                @Override
                                public void onClick(DokterPribadimuDialog dialog) {
                                    startDoctorCallActivityOnTop();
                                }
                            }
                    );
                }
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_about_arrays)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // Hint to be displayed
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.subscriptionTopicSpinner.setAdapter(spinnerAdapter);
        binding.subscriptionTopicSpinner.setSelection(spinnerAdapter.getCount());
    }


    /**
     * Validate book a call topic
     * @return boolean true if topic are valid, boolean false if otherwise
     */
    private boolean validateTopic() {
        if (binding.subscriptionTopicSpinner.getSelectedItemPosition()
                == getResources().getStringArray(R.array.call_about_arrays).length - 1) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_call_topic_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }
}
