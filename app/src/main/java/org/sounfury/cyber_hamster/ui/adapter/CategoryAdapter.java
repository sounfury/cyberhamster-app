package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.data.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    
    private Context context;
    private List<Category> categoryList;
    private OnItemClickListener listener;
    
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
    
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        
        // 设置分类信息
        holder.tvName.setText(category.getName());
        
        // 设置点击事件
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }
    
    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    // 点击监听接口
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
    
    // ViewHolder类
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName;
        
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_category);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }
    }
} 