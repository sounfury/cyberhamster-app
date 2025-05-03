package org.sounfury.cyber_hamster.ui.fragment.note;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.ui.viewmodel.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 笔记编辑器Fragment，同时支持添加和编辑功能
 */
public class NoteEditorFragment extends BaseFragment {

    private static final String ARG_NOTE_ID = "note_id";
    private static final String ARG_BOOK_ID = "book_id";
    private static final String ARG_BOOK_NAME = "book_name";

    private NoteViewModel viewModel;
    private long noteId;
    private long bookId;
    private String bookName;
    private boolean isEditMode = false; // 是否为编辑模式

    // UI组件
    private ImageButton btnBack;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDate;
    private TextView tvWordCount;
    private TextView tvBookInfo;
    private ImageButton btnSave;
    private ImageButton btnUndo;
    private ImageButton btnRedo;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("M月d日 HH:mm", Locale.getDefault());
    private Note currentNote;
    private Date currentDate;

    /**
     * 创建新实例用于新建笔记
     * @param bookId 书籍ID
     * @param bookName 书籍名称
     * @return NoteEditorFragment实例
     */
    public static NoteEditorFragment newInstance(long bookId, String bookName) {
        NoteEditorFragment fragment = new NoteEditorFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BOOK_ID, bookId);
        args.putString(ARG_BOOK_NAME, bookName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 创建新实例用于编辑笔记
     * @param noteId 笔记ID
     * @param bookId 书籍ID
     * @param bookName 书籍名称
     * @return NoteEditorFragment实例
     */
    public static NoteEditorFragment newInstance(long noteId, long bookId, String bookName) {
        NoteEditorFragment fragment = new NoteEditorFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_NOTE_ID, noteId);
        args.putLong(ARG_BOOK_ID, bookId);
        args.putString(ARG_BOOK_NAME, bookName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = new Date(); // 获取当前时间
        
        if (getArguments() != null) {
            noteId = getArguments().getLong(ARG_NOTE_ID, 0);
            bookId = getArguments().getLong(ARG_BOOK_ID, 0);
            bookName = getArguments().getString(ARG_BOOK_NAME);
            
            // 如果noteId > 0，说明是编辑模式
            isEditMode = noteId > 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // 观察笔记数据
        viewModel.getNote().observe(getViewLifecycleOwner(), note -> {
            if (note != null) {
                currentNote = note;
                updateNoteUI(note);
            }
        });

        // 观察保存结果
        viewModel.getSaveNoteResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "笔记保存成功", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed(); // 返回上一页
            }
        });

        // 观察错误信息
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });

        // 如果是编辑模式，加载笔记数据
        if (isEditMode) {
            viewModel.loadNoteById(noteId);
        }
    }

    @Override
    protected void initView(View view) {
        // 初始化返回按钮
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // 初始化UI组件
        etTitle = view.findViewById(R.id.et_title);
        etContent = view.findViewById(R.id.et_content);
        tvDate = view.findViewById(R.id.tv_date);
        tvWordCount = view.findViewById(R.id.tv_word_count);
        tvBookInfo = view.findViewById(R.id.tv_book_info);
        btnSave = view.findViewById(R.id.btn_save);
        btnUndo = view.findViewById(R.id.btn_undo);
        btnRedo = view.findViewById(R.id.btn_redo);

        // 如果不是编辑模式，设置当前日期
        if (!isEditMode) {
            String timePrefix = getTimePrefix(currentDate.getHours());
            tvDate.setText(String.format("%s %s", dateFormat.format(currentDate), timePrefix));
        }

        // 设置关联图书信息
        if (!TextUtils.isEmpty(bookName)) {
            tvBookInfo.setText(String.format("《%s》", bookName));
            tvBookInfo.setVisibility(View.VISIBLE);
        } else {
            tvBookInfo.setVisibility(View.GONE);
        }

        // 设置初始字数
        updateWordCount(0);

        // 添加内容文本变化监听
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要实现
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 不需要实现
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 更新字数统计
                updateWordCount(s.length());
            }
        });

        // 设置保存按钮点击事件
        btnSave.setOnClickListener(v -> saveNote());
        
        // 设置撤销重做按钮点击事件（这里只是显示提示，实际功能需要另外实现）
        btnUndo.setOnClickListener(v -> Toast.makeText(requireContext(), "撤销功能尚未实现", Toast.LENGTH_SHORT).show());
        btnRedo.setOnClickListener(v -> Toast.makeText(requireContext(), "重做功能尚未实现", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void initData() {
        // 数据在onViewCreated中初始化
    }
    
    /**
     * 根据小时数获取时间前缀（早上、下午、晚上、半夜）
     */
    private String getTimePrefix(int hour) {
        if (hour >= 5 && hour < 12) {
            return "早上";
        } else if (hour >= 12 && hour < 18) {
            return "下午";
        } else if (hour >= 18 && hour < 23) {
            return "晚上";
        } else {
            return "半夜";
        }
    }
    
    /**
     * 更新笔记UI
     */
    private void updateNoteUI(Note note) {
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
        
        // 更新时间显示
        if (note.getUpdateTime() != null && !note.getUpdateTime().isEmpty()) {
            String formattedDate = note.getUpdateTime().replace('T', ' ').substring(0, 16);
            tvDate.setText(formattedDate);
        } else {
            tvDate.setVisibility(View.GONE);
        }
        
        // 如果笔记有关联的书籍，则显示书籍信息
        if (note.getBook() != null && note.getBook().getBookName() != null) {
            bookName = note.getBook().getBookName();
            bookId = note.getBookId();
            tvBookInfo.setText(String.format("《%s》", bookName));
            tvBookInfo.setVisibility(View.VISIBLE);
        } else if (bookName != null && !bookName.isEmpty()) {
            tvBookInfo.setText(String.format("《%s》", bookName));
            tvBookInfo.setVisibility(View.VISIBLE);
        } else {
            tvBookInfo.setVisibility(View.GONE);
        }
        
        // 更新字数统计
        updateWordCount(note.getContent() != null ? note.getContent().length() : 0);
    }

    /**
     * 更新字数统计
     */
    private void updateWordCount(int count) {
        tvWordCount.setText(String.format("%d字", count));
    }

    /**
     * 保存笔记
     */
    private void saveNote() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";

        // 验证输入
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "请输入笔记内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建或更新Note对象
        if (currentNote == null) {
            currentNote = new Note();
            if (isEditMode) {
                currentNote.setId(noteId);
            }
            currentNote.setBookId(bookId);
            
            // 如果是新建笔记，设置createTime
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String currentIsoTime = isoFormat.format(new Date());
            currentNote.setCreateTime(currentIsoTime);
        }
        
        currentNote.setTitle(title);
        currentNote.setContent(content);
        
        // 更新updateTime
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String currentIsoTime = isoFormat.format(new Date());
        currentNote.setUpdateTime(currentIsoTime);
        
        // 生成摘要
        if (content.length() > 50) {
            currentNote.setSummary(content.substring(0, 50) + "...");
        } else {
            currentNote.setSummary(content);
        }

        // 保存笔记
        viewModel.saveNote(currentNote);
    }

    private void showError(String errorMessage) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show();
    }
} 