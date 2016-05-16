package com.bima.dokterpribadimu.view.components;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.PopupMenuCategoryBinding;
import com.bima.dokterpribadimu.model.Category;
import com.bima.dokterpribadimu.viewmodel.CategoryItemViewModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by apradanas.
 */
public class CategoriesPopupWindow extends PopupWindow {

    private Context context;
    private List<Category> categories = new ArrayList<>();

    private PopupMenuCategoryBinding binding;

    private CategoryListViewModel categoryListViewModel = new CategoryListViewModel();

    private CategoryClickListener clickListener;

    public CategoriesPopupWindow(Context context) {
        super(context);
        this.context = context;

        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                R.layout.popup_menu_category,
                null,
                false);

        binding.setViewModel(categoryListViewModel);

        setContentView(binding.getRoot());
        setBackgroundDrawable(new BitmapDrawable());

        setOutsideTouchable(true);
        setFocusable(true);

        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void release() {
        for (CategoryItemViewModel itemViewModel : categoryListViewModel.items) {
            itemViewModel.setClickListener(null);
        }
        categoryListViewModel.items.clear();

        clickListener = null;
        context = null;
    }

    public void setClickListener(CategoryClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);

        notifyDataSetChanges();
    }

    private void notifyDataSetChanges() {
        categoryListViewModel.items.clear();

        for (final Category category : categories) {
            categoryListViewModel.items.add(
                    new CategoryItemViewModel(
                            category,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (clickListener != null) {
                                        clickListener.onClick(category);
                                    }
                                }
                            }
                    ));
        }
    }

    public static class CategoryListViewModel {
        public final ObservableList<CategoryItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.category_item_viewmodel, R.layout.item_category);
    }

    public interface CategoryClickListener {
        void onClick(Category category);
    }
}
