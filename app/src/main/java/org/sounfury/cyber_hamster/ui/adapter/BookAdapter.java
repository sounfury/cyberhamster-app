package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
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
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        
        // 设置封面图片
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            ImageUtils.loadImage(context, book.getCoverUrl(), holder.ivCover);
        } else {
            // 设置默认封面
            holder.ivCover.setImageResource(R.drawable.ic_book_placeholder);
        }
        
        // 设置阅读状态
        switch (book.getReadStatus()) {
            case Constants.READ_STATUS_UNREAD:
                holder.tvStatus.setText("未读");
                break;
            case Constants.READ_STATUS_READING:
                holder.tvStatus.setText("在读");
                break;
            case Constants.READ_STATUS_FINISHED:
                holder.tvStatus.setText("已读");
                break;
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
        TextView tvStatus;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvStatus = itemView.findViewById(R.id.tv_reading_status);
        }
    }
} 