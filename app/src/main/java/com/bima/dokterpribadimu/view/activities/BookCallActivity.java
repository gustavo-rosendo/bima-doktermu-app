package com.bima.dokterpribadimu.view.activities;

import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.data.remote.api.FileUploadApi;
import com.bima.dokterpribadimu.databinding.ActivityBookCallBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.BookingUtils;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.ImagePickerUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.components.PhoneInfoModalDialog;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.ArrayList;

public class BookCallActivity extends BaseActivity {

    private static final String TAG = BookCallActivity.class.getSimpleName();

    private static final String CALL_ID = "call_id";
    private static final int PICK_IMAGE_ID_1 = 234; // the number doesn't matter
    private static final int PICK_IMAGE_ID_2 = 235; // the number doesn't matter
    private static boolean FileUploadResult = false;

    private static String topic = "";
    private static String subTopic = "";
    private static CharSequence userNotes = "";

    private static List<String> ImagesToUpload = new ArrayList<String>();

    @Inject
    BookingApi bookingApi;

    @Inject
    FileUploadApi fileUploadApi;

    private ActivityBookCallBinding binding;

//    private Tracker mTracker;

    private PhoneInfoModalDialog phoneInfoModalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_call);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_BOOK_CALL);
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.bookCallIconAddImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage(view,PICK_IMAGE_ID_1);
            }
        });
        binding.bookCallIconAddImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage(view,PICK_IMAGE_ID_2);
            }
        });
        validateBookTimeLimit(); //Gray-out the button if a call was booked in less than the minimum interval

        binding.bookCallStep1Empty.setVisibility(View.VISIBLE);
        binding.bookCallStep1Complete.setVisibility(View.GONE);
        binding.bookCallStep2Grey.setVisibility(View.VISIBLE);
        binding.bookCallStep2Empty.setVisibility(View.GONE);
        binding.bookCallStep2Complete.setVisibility(View.GONE);
        binding.bookCallStep3Grey.setVisibility(View.VISIBLE);
        binding.bookCallStep3Empty.setVisibility(View.GONE);
        binding.bookCallStep3Complete.setVisibility(View.GONE);
        binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
        binding.bookCallStep4Empty.setVisibility(View.GONE);


        binding.bookCallStep1NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateTopic() && validateSubTopic()) {
                    binding.bookCallStep1Empty.setVisibility(View.GONE);
                    binding.bookCallStep1Complete.setVisibility(View.VISIBLE);
                    binding.bookCallStep2Grey.setVisibility(View.GONE);
                    binding.bookCallStep2Empty.setVisibility(View.VISIBLE);
                    binding.bookCallStep2Complete.setVisibility(View.GONE);
                    binding.bookCallStep3Grey.setVisibility(View.VISIBLE);
                    binding.bookCallStep3Empty.setVisibility(View.GONE);
                    binding.bookCallStep3Complete.setVisibility(View.GONE);
                    binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
                    binding.bookCallStep4Empty.setVisibility(View.GONE);

                    final int topicPosition = binding.bookCallTopicSpinner.getSelectedItemPosition();
                    int subTopicPosition = -1;

                    switch (topicPosition) {
                        case 0:
                            topic = BookingUtils.TOPIC_REPRODUCTION;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = BookingUtils.SUBTOPIC_PREGNANCY;
                                    break;
                                case 1:
                                    subTopic = BookingUtils.SUBTOPIC_BABY_CHILDREN_HEALTH;
                                    break;
                                case 2:
                                    subTopic = BookingUtils.SUBTOPIC_HEALTH_SEXUAL_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 1:
                            topic = BookingUtils.TOPIC_HEALTH_ORGAN_DISEASE;

                            subTopicPosition = binding.bookCallSubtopicSpinner.getSelectedItemPosition();
                            switch (subTopicPosition) {
                                case 0:
                                    subTopic = BookingUtils.SUBTOPIC_INTERNAL_DISEASE;
                                    break;
                                case 1:
                                    subTopic = BookingUtils.SUBTOPIC_BONE_DISEASE;
                                    break;
                                case 2:
                                    subTopic = BookingUtils.SUBTOPIC_NEURAL_DISEASE;
                                    break;
                                case 3:
                                    subTopic = BookingUtils.SUBTOPIC_EYE_DISEASE;
                                    break;
                                case 4:
                                    subTopic = BookingUtils.SUBTOPIC_THT_DISEASE;
                                    break;
                                default:
                                    subTopic = "";
                                    break;
                            }

                            break;
                        case 2:
                            topic = BookingUtils.TOPIC_DIET_NUTRITION;
                            subTopic = "";
                            break;
                        case 3:
                            topic = BookingUtils.TOPIC_INJURY;
                            subTopic = "";
                            break;
                        case 4:
                            topic = BookingUtils.TOPIC_OTHERS;
                            subTopic = "";
                            break;
                        default:
                            topic = "";
                            subTopic = "";
                            break;
                    }

                    binding.bookCallTopicGrey.setText(BookingUtils.getTopicSimpleName(topic));
                    binding.bookCallSubtopicGrey.setText(BookingUtils.getSubTopicSimpleName(subTopic));
                }
            }
        });

        binding.bookCallStep2PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.bookCallStep1Empty.setVisibility(View.VISIBLE);
                binding.bookCallStep1Complete.setVisibility(View.GONE);
                binding.bookCallStep2Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep2Empty.setVisibility(View.GONE);
                binding.bookCallStep2Complete.setVisibility(View.GONE);
                binding.bookCallStep3Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep3Empty.setVisibility(View.GONE);
                binding.bookCallStep3Complete.setVisibility(View.GONE);
                binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep4Empty.setVisibility(View.GONE);

            }
        });

        binding.bookCallStep2NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.bookCallStep1Empty.setVisibility(View.GONE);
                binding.bookCallStep1Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep2Grey.setVisibility(View.GONE);
                binding.bookCallStep2Empty.setVisibility(View.GONE);
                binding.bookCallStep2Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep3Grey.setVisibility(View.GONE);
                binding.bookCallStep3Empty.setVisibility(View.VISIBLE);
                binding.bookCallStep3Complete.setVisibility(View.GONE);
                binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep4Empty.setVisibility(View.GONE);

                userNotes = binding.bookCallStep2EditText.getText();

                binding.bookCallStep2NotesGrey.setText(userNotes);

            }
        });

        binding.bookCallStep3PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.bookCallStep1Empty.setVisibility(View.GONE);
                binding.bookCallStep1Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep2Grey.setVisibility(View.GONE);
                binding.bookCallStep2Empty.setVisibility(View.VISIBLE);
                binding.bookCallStep2Complete.setVisibility(View.GONE);
                binding.bookCallStep3Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep3Empty.setVisibility(View.GONE);
                binding.bookCallStep3Complete.setVisibility(View.GONE);
                binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep4Empty.setVisibility(View.GONE);
            }
        });

        binding.bookCallStep3NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.bookCallStep1Empty.setVisibility(View.GONE);
                binding.bookCallStep1Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep2Grey.setVisibility(View.GONE);
                binding.bookCallStep2Empty.setVisibility(View.GONE);
                binding.bookCallStep2Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep3Grey.setVisibility(View.GONE);
                binding.bookCallStep3Empty.setVisibility(View.GONE);
                binding.bookCallStep3Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep4Grey.setVisibility(View.GONE);
                binding.bookCallStep4Empty.setVisibility(View.VISIBLE);

                if(!ImagesToUpload.isEmpty())
                {
                    FileUploadResult = true;
                }


            }
        });

        binding.bookCallStep4PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.bookCallStep1Empty.setVisibility(View.GONE);
                binding.bookCallStep1Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep2Grey.setVisibility(View.GONE);
                binding.bookCallStep2Empty.setVisibility(View.GONE);
                binding.bookCallStep2Complete.setVisibility(View.VISIBLE);
                binding.bookCallStep3Grey.setVisibility(View.GONE);
                binding.bookCallStep3Empty.setVisibility(View.VISIBLE);
                binding.bookCallStep3Complete.setVisibility(View.GONE);
                binding.bookCallStep4Grey.setVisibility(View.VISIBLE);
                binding.bookCallStep4Empty.setVisibility(View.GONE);

            }
        });

        binding.bookCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePhoneNumber() && validateBookTimeLimit()) {

                    AnalyticsHelper.logBookCallBtnClickEvent(
                            EventConstants.BTN_BOOK_SCREEN_BOOK_CALL,
                            topic,
                            subTopic);


                    bookCall(topic, subTopic, userNotes,
                            UserProfileUtils.getUserProfile(BookCallActivity.this).getMsisdn(),
                            UserProfileUtils.getUserProfile(BookCallActivity.this).getAccessToken());


//                    if(HasFileUploadImage1 == true) {

//                        if(uploadFile(ImagePickerUtils.getImageFileName(), UserProfileUtils.getUserProfile(BookCallActivity.this).getAccessToken()) == true) {

//                            if(FileUploadResult == true) {
//                                bookCall(topic, subTopic, userNotes,
//                                        UserProfileUtils.getUserProfile(BookCallActivity.this).getMsisdn(),
//                                        UserProfileUtils.getUserProfile(BookCallActivity.this).getAccessToken());

//                            }
//                        }
//                    }

                }
            }
        });

        final ArrayAdapter<String> spinnerSubTopicReproductionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_reproduksi_subtopics_arrays)) {
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
        spinnerSubTopicReproductionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> spinnerSubTopicHealthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_kesehatan_subtopics_arrays)) {
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
        spinnerSubTopicHealthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> spinnerTopicsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.call_topics_arrays)) {
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
        spinnerTopicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bookCallTopicSpinner.setAdapter(spinnerTopicsAdapter);
        binding.bookCallTopicSpinner.setSelection(spinnerTopicsAdapter.getCount());

        binding.bookCallTopicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        binding.bookCallSubtopicSpinner.setAdapter(spinnerSubTopicReproductionAdapter);
                        binding.bookCallSubtopicSpinner.setSelection(spinnerSubTopicReproductionAdapter.getCount());
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setEnabled(true);
                        break;
                    case 1:
                        binding.bookCallSubtopicSpinner.setAdapter(spinnerSubTopicHealthAdapter);
                        binding.bookCallSubtopicSpinner.setSelection(spinnerSubTopicHealthAdapter.getCount());
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.VISIBLE);
                        binding.bookCallSubtopicSpinner.setEnabled(true);
                        break;
                    default:
                        binding.bookCallSubtopicUnderlineView.setVisibility(View.GONE);
                        binding.bookCallSubtopicSpinner.setVisibility(View.GONE);
                        binding.bookCallSubtopicSpinner.setEnabled(false);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //In the beginning, the subtopics list is invisible
        binding.bookCallSubtopicUnderlineView.setVisibility(View.GONE);
        binding.bookCallSubtopicSpinner.setVisibility(View.GONE);
        binding.bookCallSubtopicSpinner.setEnabled(false);

        //Show the phone number where the user will be called
        binding.bookCallPhoneNumber.setText(UserProfileUtils.getUserProfile(BookCallActivity.this).getMsisdn());

        binding.bookCallPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneInfoModalDialog == null) {
                    phoneInfoModalDialog = new PhoneInfoModalDialog(BookCallActivity.this);
                }

                phoneInfoModalDialog.setListener(new PhoneInfoModalDialog.OnPhoneInfoModalDialogClickListener() {
                    @Override
                    public void onClick(PhoneInfoModalDialog dialog) {
                        dismissPhoneInfoModalDialog();
                    }
                });

                phoneInfoModalDialog.showDialog();
            }
        });




    }

    private void startMailIntent(String email) {
        Intent emailIntent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", email, null));
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    private void dismissPhoneInfoModalDialog() {
        phoneInfoModalDialog.dismiss();
        phoneInfoModalDialog.clearReference();
        phoneInfoModalDialog = null;
    }

    /**
     * Validate book a call topic
     * @return boolean true if topic are valid, boolean false if otherwise
     */
    private boolean validateTopic() {
        if (binding.bookCallTopicSpinner.getSelectedItemPosition()
                == getResources().getStringArray(R.array.call_topics_arrays).length - 1) {
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

    /**
     * Validate book a call sub-topic
     * @return boolean true if sub-topic are valid, boolean false if otherwise
     */
    private boolean validateSubTopic() {
        boolean isValid = false;

        if(binding.bookCallSubtopicSpinner.getVisibility() == View.VISIBLE) {
            if (binding.bookCallSubtopicSpinner.getSelectedItemPosition()
                    == binding.bookCallSubtopicSpinner.getCount()) {
                Toast.makeText(
                        this,
                        getString(R.string.invalid_call_subtopic_message),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                return true;
            }
        }
        else {
            isValid = true;
        }

        return isValid;
    }

    /**
     * Validate book a call phone number. A phone number is valid if it has at least 11 digits, like in: 02742112462
     * @return boolean true if phone number is valid, boolean false if otherwise
     */
    private boolean validatePhoneNumber() {
        boolean isValid = false;

        if(binding.bookCallPhoneNumber.getText().length() < Constants.PHONE_NUMBER_MINIMAL_DIGITS) {
            Toast.makeText(
                    this,
                    getString(R.string.invalid_call_phone_number_message),
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            isValid = true;
        }

        return isValid;
    }

    /**
     * Check if the minimum time between calls has already passed
     * @return boolean true if user can already book another call, boolean false if otherwise
     */
    private boolean validateBookTimeLimit() {
        double lastBookedCallTimeMillis = StorageUtils.getDouble(
                BookCallActivity.this,
                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                -TimeUtils.ONE_HOUR_MS); //for the first time this is accessed

        if(!TimeUtils.hasOneHourPassed(lastBookedCallTimeMillis)) {
            binding.bookCallButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            Toast.makeText(
                    this,
                    getString(R.string.time_limit_to_book_call),
                    Toast.LENGTH_LONG
            ).show();
        } else {
            binding.bookCallButton.getBackground().setColorFilter(null);
            return true;
        }
        return false;
    }

    /**
     * Do book a call.
     * @param callTopic user's email
     * @param accessToken user's access token
     */
    private void bookCall(final String callTopic, final String callSubTopic,
                          final CharSequence userNotes,
                          final String phoneNumber, final String accessToken) {
        bookingApi.bookCall(callTopic, callSubTopic, userNotes, phoneNumber, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        StorageUtils.putDouble(
                                BookCallActivity.this,
                                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                -TimeUtils.ONE_HOUR_MS); //ensure that the button will be active
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        dismissProgressDialog();

                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putDouble(
                                    BookCallActivity.this,
                                    Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                    TimeUtils.getElapsedTimeMillis());

                            if(response.getData() != null) {
                                String jsonResponse = response.getData().toString();
                                String callId = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonResponse);
                                    if(jsonObject.has(CALL_ID)) {
                                        callId = String.valueOf(jsonObject.optInt(CALL_ID));
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error reading call_id from jsonResponse = " + jsonResponse);
                                    Log.e(TAG, "Error message = " + e.getMessage());
                                }
                                StorageUtils.putString(
                                        BookCallActivity.this,
                                        Constants.KEY_BOOK_CALL_ID_LAST_CALL,
                                        callId);
                                Log.d(TAG, "Storing last call's id = " + callId);
                            }

                            showSuccessDialog(
                                    R.drawable.ic_thumb_up,
                                    getString(R.string.dialog_book_call_success),
                                    getString(R.string.dialog_book_call_done_message),
                                    getString(R.string.dialog_take_me_home),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            IntentUtils.startDoctorCallPendingActivityOnTop(BookCallActivity.this);
                                        }
                                    }
                            );
                            AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_BOOK_CALL_SUCCESS);
                        } else {
                            StorageUtils.putDouble(
                                    BookCallActivity.this,
                                    Constants.KEY_BOOK_CALL_TIME_MILLIS,
                                    -TimeUtils.ONE_HOUR_MS); //ensure that the button will be active
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }



    public void onPickImage(View view, int pick_image_control) {
        Intent chooseImageIntent = ImagePickerUtils.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, pick_image_control);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID_1:
                Bitmap bitmap_1 = ImagePickerUtils.getImageFromResult(this, resultCode, data);
                ImagesToUpload.add(data.toUri(0));
                binding.bookCallIconAddImage1.setImageBitmap(bitmap_1);
                // TODO use bitmap
                break;
            case PICK_IMAGE_ID_2:
                Bitmap bitmap_2 = ImagePickerUtils.getImageFromResult(this, resultCode, data);
                ImagesToUpload.add(data.toUri(0));
                binding.bookCallIconAddImage1.setImageBitmap(bitmap_2);
                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * Upload a File.
     * @param fileName File Name to Upload
     * @param accessToken user's access token
     */
    private boolean uploadFile(final String fileName,
                            final String accessToken) {

        FileUploadResult = false;


        /**
         * Progressbar to Display if you need
         */
        //File creating from selected URL
        File file = new File(fileName);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part file_data = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        MultipartBody.Part access_token = MultipartBody.Part.createFormData("access_token", accessToken);

        MultipartBody.Part call_id = MultipartBody.Part.createFormData("call_id", "24");


        fileUploadApi.uploadFile(file_data, access_token, call_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        dismissProgressDialog();

                        if (response.getStatus() == Constants.Status.SUCCESS) {

                            if(response.getMessage() != null) {
                                String ResponseString = response.getMessage().toString();
                                if(ResponseString.equals("File Uploaded.")) {
                                  Log.d(TAG, "File Sucessfully Uploaded.");
                                  FileUploadResult = true;
                                }


                            }


                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });

        return true;
    }

}
