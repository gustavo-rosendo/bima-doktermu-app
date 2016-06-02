package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.model.UserProfile;

/**
 * Created by apradanas on 3/6/16.
 */
public class DeviceInfoUtils {

    public static final String NETWORK_OPERATOR_TELKOMSEL = "TELKOMSEL";

    private static TelephonyManager telephonyManager;
    private static DeviceInfoUtils instance = null;

    private String deviceId;
    private String board;
    private String brand;
    private String device;
    private String buildId;
    private String hardware;
    private String manufacturer;
    private String model;
    private String product;
    private String release;
    private String releaseType;
    private Integer sdk;
    private Integer simMnc;
    private Integer simMcc;
    private String simOperatorName;
    private String msisdnPhoneNumber;
    private String networkOperatorName;

    protected DeviceInfoUtils(Context context) {
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        deviceId = telephonyManager.getDeviceId();

        UserProfile userProfile = UserProfileUtils.getUserProfile(
                DokterPribadimuApplication.getInstance().getApplicationContext());
        if(userProfile != null && userProfile.getMsisdn() != null && userProfile.getMsisdn() != "") {
            msisdnPhoneNumber = userProfile.getMsisdn();
        }
        else {
            if(telephonyManager.getLine1Number() != null) {
                msisdnPhoneNumber = telephonyManager.getLine1Number();
            }
            else if(telephonyManager.getVoiceMailNumber() != null) {
                msisdnPhoneNumber = telephonyManager.getVoiceMailNumber();
            }
            else {
                msisdnPhoneNumber = "000000000000";
            }
        }

        board = Build.BOARD;
        brand = Build.BRAND;
        device = Build.DEVICE;
        buildId = Build.DISPLAY;
        hardware = Build.HARDWARE;
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        product = Build.PRODUCT;
        release = Build.VERSION.RELEASE;
        releaseType = Build.TYPE;
        sdk = Build.VERSION.SDK_INT;

        try {
            String operator = telephonyManager.getSimOperator();
            simMcc = Integer.valueOf(operator.substring(0,3));
            simMnc = Integer.valueOf(operator.substring(3));
            simOperatorName = telephonyManager.getSimOperatorName();
            networkOperatorName = telephonyManager.getNetworkOperatorName();
        }
        catch (IndexOutOfBoundsException e) {}

    }

    /**
     *
     * @param context of the application
     * @return an instance of this device info
     */
    public static DeviceInfoUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceInfoUtils(context);
        }

        return instance;
    }

    /**
     * Returns an unique device id (IMEI for GSM, ESN for CDMA)
     * @return unique device id
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Returns the name of the operator being used (TELKOMSEL, AXIS, etc.)
     * @return name of the operator
     */
    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    /**
     * @return Name of the underlying board
     */
    public String getBoard() {
        return board;
    }

    /**
     * @return carrier that the device is customized to
     */
    public String getBrand() {
        return brand;
    }

    /**
     *
     * @return name of the industrial design
     */
    public String getDevice() {
        return device;
    }

    /**
     *
     * @return build id
     */
    public String getBuildId() {
        return buildId;
    }

    /**
     *
     * @return name of the hardware
     */
    public String getHardware() {
        return hardware;
    }

    /**
     *
     * @return manufacturer of the device
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     *
     * @return model of the device
     */
    public String getModel() {
        return model;
    }

    /**
     *
     * @return name of the product
     */
    public String getProduct() {
        return product;
    }

    /**
     *
     * @return version release string
     */
    public String getRelease() {
        return release;
    }

    /**
     *
     * @return release type
     */
    public String getReleaseType() {
        return releaseType;
    }

    /**
     *
     * @return SDK number
     */
    public Integer getSdk() {
        return sdk;
    }

    /**
     * Get the SIM card mobile network code (null if unavailable)
     * @return
     */
    public Integer getSimMnc() {
        return simMnc;
    }

    /**
     * Get the SIM card mobile country code (null if unavailable)
     * @return
     */
    public Integer getSimMcc() {
        return simMcc;
    }

    /**
     * Get the SIM card mobile operator name (null if unavailable)
     * @return
     */
    public String getSimOperatorName() {
        return simOperatorName;
    }


    /**
     * Returns the phone number string for line 1, for example, the MSISDN
     * for a GSM phone. Return string 000000000000 if it is unavailable.
     * @return phone number string
     */
    public String getMsisdnPhoneNumber() {
        return msisdnPhoneNumber;
    }


}
