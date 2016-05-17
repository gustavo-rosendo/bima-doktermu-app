package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bima.dokterpribadimu.model.Discount;

/**
 * Created by apradanas.
 */
public class DiscountItemViewModel extends BaseObservable {

    private Discount discount;

    public DiscountItemViewModel(Discount discount) {
        this.discount = discount;
    }

    @Bindable
    public String getItem() {
        return discount.getItem();
    }

    @Bindable
    public String getDiscount() {
        return discount.getDiscount().replace(" ", "");
    }
}
