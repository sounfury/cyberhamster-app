package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.data.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    
    private final Context context;
    private final List<Category> categories;
    private OnItemClickListener onItemClickListener;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }
    
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        
        // 设置分类名称
        holder.tvCategoryName.setText(category.getName());
        
        // 设置图书数量
        if (category.getBookIds() != null) {
            int bookCount = category.getBookIds().size();
            holder.tvBookCount.setText(context.getString(R.string.book_count, bookCount));
        } else {
            holder.tvBookCount.setText(context.getString(R.string.book_count, 0));
        }
        
        // 设置点击事件
        holder.cardCategory.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(category);
            }
        });
        
        // 设置编辑按钮点击事件
        holder.btnEdit.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(category);
            }
        });
        
        // 设置删除按钮点击事件
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(category);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return categories.size();
    }
    
    // ViewHolder类
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardCategory;
        TextView tvCategoryName;
        TextView tvBookCount;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCategory = itemView.findViewById(R.id.card_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvBookCount = itemView.findViewById(R.id.tv_book_count);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
    
    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
    
    public interface OnEditClickListener {
        void onEditClick(Category category);
    }
    
    public interface OnDeleteClickListener {
        void onDeleteClick(Category category);
    }
    
    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }
    
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
} 