package org.sounfury.cyber_hamster.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.data.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    
    private Context context;
    private List<Note> noteList;
    private OnItemClickListener listener;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat thisYearFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
    private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());

    {
        // 统一设置为北京时间,为了调试（Asia/Shanghai）
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        inputFormat.setTimeZone(chinaTimeZone);
        todayFormat.setTimeZone(chinaTimeZone);
        thisYearFormat.setTimeZone(chinaTimeZone);
        fullDateFormat.setTimeZone(chinaTimeZone);
    }



    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_card, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        // 设置笔记信息
        holder.tvTitle.setText(note.getTitle());

        // 处理摘要，为实现瀑布流效果，控制摘要显示长度
        String summary = note.getSummary();
        if (summary != null && !summary.isEmpty()) {
            // 根据内容长度动态设置显示行数
            int maxLines = getRandomMaxLines(summary);
            holder.tvSummary.setMaxLines(maxLines);
            holder.tvSummary.setText(summary);
        } else {
            holder.tvSummary.setText("");
            holder.tvSummary.setMaxLines(2);
        }

        // 处理日期格式
        String formattedDate = "";
        try {
            if (note.getUpdateTime() != null) {
                Date date = inputFormat.parse(note.getUpdateTime());
                if (date != null) {
                    Log.d("NoteAdapter", "Parsed date: " + date);
                    formattedDate = formatDateBasedOnAge(date);
                }
            }
        } catch (ParseException e) {
            formattedDate = note.getUpdateTime();
        }

        // 设置日期文本
        holder.tvDate.setText(formattedDate);

        // 设置书籍名称（如果有）
        if (note.getBook() != null && note.getBook().getBookName() != null) {
            holder.tvBookName.setText("《" + note.getBook().getBookName() + "》");
            holder.tvBookName.setVisibility(View.VISIBLE);
        } else {
            holder.tvBookName.setVisibility(View.GONE);
        }
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(note);
            }
        });
    }
    
    /**
     * 根据日期的年龄格式化日期
     * 今天：显示时分秒
     * 今年：显示月日
     * 更早：显示年月日
     */
    private String formatDateBasedOnAge(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        Calendar today = Calendar.getInstance();
        
        boolean isToday = (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) &&
                (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR));

        //打印
        Log.d("NoteAdapter", "isToday: " + isToday);
        Log.d("NoteAdapter", "isThisYear: " + (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)));
        Log.d("NoteAdapter", "isToday: " + (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)));
        boolean isThisYear = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR);
        
        if (isToday) {
            Log.d("NoteAdapter", "Date is today: " + date);
            return todayFormat.format(date);
        } else if (isThisYear) {
            Log.d("NoteAdapter", "Date is this year: " + date);
            return thisYearFormat.format(date);
        } else {
            Log.d("NoteAdapter", "Date is older: " + date);
            return fullDateFormat.format(date);
        }
    }
    
    /**
     * 根据内容长度和随机因素确定最大显示行数
     * @param summary 笔记摘要内容
     * @return 显示行数
     */
    private int getRandomMaxLines(String summary) {
        // 根据内容长度和随机因素确定显示行数
        //>10 2行
        // 10-20 3行
        // 20-30 4行，如此类推
        int length = summary.length();
        return 2 + (length / 10) + 1;
    }
    
    @Override
    public int getItemCount() {
        return noteList == null ? 0 : noteList.size();
    }
    
    // 添加新数据（用于分页加载）
    public void addData(List<Note> newData) {
        if (newData != null && !newData.isEmpty()) {
            int startPos = noteList.size();
            noteList.addAll(newData);
            notifyItemRangeInserted(startPos, newData.size());
        }
    }
    
    // 清空并重置数据
    public void setData(List<Note> data) {
        noteList.clear();
        if (data != null) {
            noteList.addAll(data);
        }
        notifyDataSetChanged();
    }
    
    // 获取最后一个笔记的ID（用于分页）
    public Long getLastItemId() {
        if (noteList != null && !noteList.isEmpty()) {
            return noteList.get(noteList.size() - 1).getId();
        }
        return null;
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
        TextView tvTitle;
        TextView tvSummary;
        TextView tvDate;
        TextView tvBookName;
        
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvSummary = itemView.findViewById(R.id.tv_note_summary);
            tvDate = itemView.findViewById(R.id.tv_note_date);
            tvBookName = itemView.findViewById(R.id.tv_book_name);
        }
    }
} 