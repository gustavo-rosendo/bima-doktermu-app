package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bima.dokterpribadimu.model.Partner;

/**
 * Created by apradanas.
 */
public class SearchItemViewModel extends BaseObservable {

    private static final String DISCOUNT_FORMAT = "-%s";

    private Partner partner;
    private int icon;
    private View.OnClickListener clickListener;

    public SearchItemViewModel(Partner partner, int icon, View.OnClickListener clickListener) {
        this.partner = partner;
        this.icon = icon;
        this.clickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Bindable
    public String getName() {
        return partner.getPartnerName();
    }

    @Bindable
    public String getAddress() {
        return partner.getPartnerAddress();
    }

    @Bindable
    public boolean getHasDiscount() {
        return partner.getDiscount().size() > 0;
    }

    @Bindable
    public String getDiscount() {
        String discount = getHasDiscount() ?
                String.format(
                    DISCOUNT_FORMAT,
                    partner.getDiscount().get(0).getDiscount().replace(" ", ""))
                : "";
        return discount;
    }

    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @Bindable
    public int getIcon() {
        return icon;
    }

    @Bindable
    public View.OnClickListener getClickListener() {
        return clickListener;
    }
}
