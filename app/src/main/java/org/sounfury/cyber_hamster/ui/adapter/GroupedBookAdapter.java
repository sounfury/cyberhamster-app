package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.network.response.GroupedBooks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupedBookAdapter extends RecyclerView.Adapter<GroupedBookAdapter.GroupedBookViewHolder> {

    private final Context context;
    private final List<GroupedBooks> bookList;
    private final Set<Integer> selectedBooks = new HashSet<>();
    private boolean selectMode = true; // 默认处于选择模式
    
    private OnItemClickListener onItemClickListener;
    private OnGroupClickListener onGroupClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private OnSelectChangeListener onSelectChangeListener;

    public GroupedBookAdapter(Context context, List<GroupedBooks> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public GroupedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grouped_book, parent, false);
        return new GroupedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupedBookViewHolder holder, int position) {
        GroupedBooks book = bookList.get(position);
        
        // 设置书籍基本信息
        holder.tvBookTitle.setText(book.getBookName());
        holder.tvBookAuthor.setText(book.getAuthor());
        
        if (!TextUtils.isEmpty(book.getPress())) {
            holder.tvBookPress.setVisibility(View.VISIBLE);
            holder.tvBookPress.setText(context.getString(R.string.press_info, book.getPress()));
        } else {
            holder.tvBookPress.setVisibility(View.GONE);
        }
        
        // 设置所属分类
        if (book.getUserCategories() != null && !book.getUserCategories().isEmpty()) {
            String categories = TextUtils.join(", ", book.getUserCategories().stream()
                                                    .map(Category::getName)
                                                    .toArray(String[]::new));
            holder.tvBookCategories.setVisibility(View.VISIBLE);
            holder.tvBookCategories.setText(context.getString(R.string.book_categories, categories));
        } else {
            holder.tvBookCategories.setVisibility(View.GONE);
        }
        
        // 设置选择框 - 总是显示
        holder.cbSelect.setVisibility(View.VISIBLE);
        holder.cbSelect.setChecked(selectedBooks.contains(book.getId()));
        
        holder.cbSelect.setOnClickListener(v -> {
            toggleBookSelection(book.getId());
            notifyItemChanged(position);
        });
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (selectMode) {
                toggleBookSelection(book.getId());
                notifyItemChanged(position);
            } else if (onItemClickListener != null) {
                onItemClickListener.onItemClick(book);
            }
        });
        
        // 设置分组按钮点击事件
        holder.btnGroup.setOnClickListener(v -> {
            if (onGroupClickListener != null) {
                onGroupClickListener.onGroupClick(book);
            }
        });
        
        // 设置删除按钮点击事件
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(book);
            }
        });
    }
    
    // 切换书籍选中状态 - 改为公共方法
    public void toggleBookSelection(int bookId) {
        if (selectedBooks.contains(bookId)) {
            selectedBooks.remove(bookId);
        } else {
            selectedBooks.add(bookId);
        }
        
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelectChanged(selectedBooks.size());
        }
    }
    
    // 切换选择模式
    public void toggleSelectMode() {
        selectMode = !selectMode;
        if (!selectMode) {
            selectedBooks.clear();
        }
        notifyDataSetChanged();
        
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelectChanged(selectedBooks.size());
        }
    }
    
    // 获取选择模式状态
    public boolean isSelectMode() {
        return selectMode;
    }
    
    // 全选
    public void selectAll() {
        if (!selectMode) return;
        
        selectedBooks.clear();
        for (GroupedBooks book : bookList) {
            selectedBooks.add(book.getId());
        }
        notifyDataSetChanged();
        
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelectChanged(selectedBooks.size());
        }
    }
    
    // 取消全选
    public void deselectAll() {
        if (!selectMode) return;
        
        selectedBooks.clear();
        notifyDataSetChanged();
        
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelectChanged(0);
        }
    }
    
    // 获取已选择的图书ID
    public Set<Integer> getSelectedBookIds() {
        return new HashSet<>(selectedBooks);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class GroupedBookViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        TextView tvBookTitle;
        TextView tvBookAuthor;
        TextView tvBookPress;
        TextView tvBookCategories;
        Button btnGroup;
        Button btnDelete;

        GroupedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelect = itemView.findViewById(R.id.cb_select);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvBookAuthor = itemView.findViewById(R.id.tv_book_author);
            tvBookPress = itemView.findViewById(R.id.tv_book_press);
            tvBookCategories = itemView.findViewById(R.id.tv_book_categories);
            btnGroup = itemView.findViewById(R.id.btn_group);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(GroupedBooks book);
    }

    public interface OnGroupClickListener {
        void onGroupClick(GroupedBooks book);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(GroupedBooks book);
    }
    
    public interface OnSelectChangeListener {
        void onSelectChanged(int count);
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        this.onGroupClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    
    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        this.onSelectChangeListener = listener;
    }
} 