package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.config.Constants;
import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.utils.ImageUtils;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    
    private Context context;
    private List<Book> bookList;
    private OnItemClickListener listener;
    
    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        
        // 设置书籍信息
        holder.tvTitle.setText(book.getBookName());
        holder.tvAuthor.setText(book.getAuthor());
        
        // 设置出版社
        if (!TextUtils.isEmpty(book.getPress())) {
            holder.tvPress.setVisibility(View.VISIBLE);
            holder.tvPress.setText(context.getString(R.string.press_info, book.getPress()));
        } else {
            holder.tvPress.setVisibility(View.GONE);
        }
        
        // 设置出版时间
        if (!TextUtils.isEmpty(book.getPressDate())) {
            holder.tvPublishDate.setVisibility(View.VISIBLE);
            holder.tvPublishDate.setText(context.getString(R.string.publish_date_info, book.getPressDate()));
        } else {
            holder.tvPublishDate.setVisibility(View.GONE);
        }
        
        // 设置中图法分类
        if (!TextUtils.isEmpty(book.getClcName())) {
            holder.tvClcName.setVisibility(View.VISIBLE);
            holder.tvClcName.setText(context.getString(R.string.clc_name_info, book.getClcName()));
        } else {
            holder.tvClcName.setVisibility(View.GONE);
        }
        
        // 设置封面图片
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Log.i("CoverUrl", book.getCoverUrlArray()[0]);
            ImageUtils.loadImage(context,  book.getCoverUrlArray()[0], holder.ivCover);
        } else {
            // 设置默认封面
            holder.ivCover.setImageResource(R.drawable.ic_book_placeholder);
        }
        

        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(book);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }
    
    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    // 点击监听接口
    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
    
    // ViewHolder类
    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvPress;
        TextView tvPublishDate;
        TextView tvClcName;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvPress = itemView.findViewById(R.id.tv_book_press);
            tvPublishDate = itemView.findViewById(R.id.tv_book_publish_date);
            tvClcName = itemView.findViewById(R.id.tv_book_clc_name);
        }
    }
} 