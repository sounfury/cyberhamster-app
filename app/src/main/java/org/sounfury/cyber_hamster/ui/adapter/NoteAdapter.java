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
import org.sounfury.cyber_hamster.data.model.Note;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    
    private Context context;
    private List<Note> noteList;
    private OnItemClickListener listener;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    
    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }
    
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        
        // 设置笔记信息
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        
        if (note.getUpdateTime() != null) {
            holder.tvTime.setText(sdf.format(note.getUpdateTime()));
        }
        
        // 设置点击事件
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(note);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return noteList == null ? 0 : noteList.size();
    }
    
    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    // 点击监听接口
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
    
    // ViewHolder类
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_note);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvContent = itemView.findViewById(R.id.tv_note_content);
            tvTime = itemView.findViewById(R.id.tv_note_time);
        }
    }
} 