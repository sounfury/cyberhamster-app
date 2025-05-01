package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.data.model.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.SelectCategoryViewHolder> {

    private final Context context;
    private final List<Category> categoryList;
    private final Set<Integer> selectedCategories = new HashSet<>();
    private OnSelectionChangeListener onSelectionChangeListener;

    public SelectCategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public SelectCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_category, parent, false);
        return new SelectCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectCategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        
        holder.tvCategoryName.setText(category.getName());
        holder.cbCategory.setChecked(selectedCategories.contains(category.getId()));
        
        // 设置点击事件
        View.OnClickListener clickListener = v -> {
            toggleCategorySelection(category.getId());
            notifyItemChanged(position);
        };
        
        holder.cbCategory.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);
    }
    
    public void toggleCategorySelection(int categoryId) {
        if (selectedCategories.contains(categoryId)) {
            selectedCategories.remove(categoryId);
        } else {
            selectedCategories.add(categoryId);
        }
        
        if (onSelectionChangeListener != null) {
            onSelectionChangeListener.onSelectionChanged(selectedCategories.size());
        }
    }
    
    // 获取已选择的分类ID
    public Set<Integer> getSelectedCategoryIds() {
        return new HashSet<>(selectedCategories);
    }
    
    // 清空选择
    public void clearSelection() {
        selectedCategories.clear();
        notifyDataSetChanged();
        
        if (onSelectionChangeListener != null) {
            onSelectionChangeListener.onSelectionChanged(0);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class SelectCategoryViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbCategory;
        TextView tvCategoryName;

        SelectCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCategory = itemView.findViewById(R.id.cb_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }
    }
    
    // 选择变化监听接口
    public interface OnSelectionChangeListener {
        void onSelectionChanged(int count);
    }
    
    // 设置选择变化监听器
    public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
        this.onSelectionChangeListener = listener;
    }
} 