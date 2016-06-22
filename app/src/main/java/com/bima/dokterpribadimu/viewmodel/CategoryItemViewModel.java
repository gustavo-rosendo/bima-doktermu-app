package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.model.Category;

/**
 * Created by apradanas.
 */
public class CategoryItemViewModel extends BaseObservable {

    private Category category;
    private View.OnClickListener clickListener;

    public CategoryItemViewModel(Category category, View.OnClickListener clickListener) {
        this.category = category;
        this.clickListener = clickListener;
    }

    @Bindable
    public boolean isSelected() {
        return category.isSelected();
    }

    @BindingAdapter("bind:selected")
    public static void setViewSelected(View view, boolean selected) {
        view.setSelected(selected);
    }

    @Bindable
    public String getTitle() {
        return category.getCategoryName();
    }

    @Bindable
    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
