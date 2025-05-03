package org.sounfury.cyber_hamster.ui.fragment.book;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.data.model.UserBook;
import org.sounfury.cyber_hamster.ui.fragment.note.NoteEditorFragment;
import org.sounfury.cyber_hamster.ui.viewmodel.BookDetailViewModel;
import org.sounfury.cyber_hamster.utils.ImageUtils;

import java.util.List;

public class BookDetailFragment extends BaseFragment {

    private static final String ARG_BOOK_ID = "book_id";

    private BookDetailViewModel viewModel;
    private long bookId;

    // UI组件
    private Toolbar toolbar;
    private ImageView ivBookCover;
    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private TextView tvCategory;
    private TextView tvPages;
    private TextView tvLanguage;
    private TextView tvBookIntro;
    private Button btnTakeNotes;
    private Button btnContinueReading;
    
    // UserBook信息相关UI组件
    private TextView tvStorageLocation;
    private TextView tvEntryTime;
    private TextView tvRemark;
    
    // 最近笔记相关UI组件
    private TextView tvNoNotes;
    private TextView tvRecentNotesHint;
    private LinearLayout llNoteItem1;
    private LinearLayout llNoteItem2;
    private LinearLayout llNoteItem3;
    private TextView tvNoteTitle1;
    private TextView tvNoteTitle2;
    private TextView tvNoteTitle3;
    private TextView tvNoteSummary1;
    private TextView tvNoteSummary2;
    private TextView tvNoteSummary3;
    private TextView tvNoteTime1;
    private TextView tvNoteTime2;
    private TextView tvNoteTime3;
    
    // 底部导航栏和悬浮按钮
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAdd;

    public static BookDetailFragment newInstance(long bookId) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getLong(ARG_BOOK_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);

        // 观察图书数据
        viewModel.getBook().observe(getViewLifecycleOwner(), this::updateBookUI);
        
        // 观察UserBook数据（可以获取额外的信息，如借阅状态等）
        viewModel.getUserBook().observe(getViewLifecycleOwner(), this::updateUserBookInfo);
        
        // 观察最近笔记数据
        viewModel.getRecentNotes().observe(getViewLifecycleOwner(), this::updateRecentNotes);

        // 观察错误信息
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });

        // 加载图书数据
        viewModel.loadBook(bookId);
        
        // 隐藏底部导航栏
        hideBottomNav();
    }
    
    private void hideBottomNav() {
        // 获取Activity中的底部导航栏和浮动按钮
        bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        fabAdd = requireActivity().findViewById(R.id.fab_add);
        
        // 隐藏底部导航栏和浮动按钮
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
        
        if (fabAdd != null) {
            fabAdd.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 恢复底部导航栏和浮动按钮
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
        if (fabAdd != null) {
            fabAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView(View view) {
        // 初始化工具栏
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            // 处理返回按钮点击
            requireActivity().onBackPressed();
        });
        
        // 初始化UI组件
        ivBookCover = view.findViewById(R.id.iv_book_cover);
        tvBookTitle = view.findViewById(R.id.tv_book_title);
        tvBookAuthor = view.findViewById(R.id.tv_book_author);
        tvCategory = view.findViewById(R.id.tv_category);
        tvPages = view.findViewById(R.id.tv_pages);
        tvLanguage = view.findViewById(R.id.tv_language);
        tvBookIntro = view.findViewById(R.id.tv_book_intro);
        btnTakeNotes = view.findViewById(R.id.btn_take_notes);
        btnContinueReading = view.findViewById(R.id.btn_continue_reading);
        
        // 初始化UserBook相关UI组件
        tvStorageLocation = view.findViewById(R.id.tv_storage_location);
        tvEntryTime = view.findViewById(R.id.tv_entry_time);
        tvRemark = view.findViewById(R.id.tv_remark);
        
        // 初始化最近笔记相关UI组件
        tvNoNotes = view.findViewById(R.id.tv_no_notes);
        tvRecentNotesHint = view.findViewById(R.id.tv_recent_notes_hint);
        llNoteItem1 = view.findViewById(R.id.ll_note_item_1);
        llNoteItem2 = view.findViewById(R.id.ll_note_item_2);
        llNoteItem3 = view.findViewById(R.id.ll_note_item_3);
        tvNoteTitle1 = view.findViewById(R.id.tv_note_title_1);
        tvNoteTitle2 = view.findViewById(R.id.tv_note_title_2);
        tvNoteTitle3 = view.findViewById(R.id.tv_note_title_3);
        tvNoteSummary1 = view.findViewById(R.id.tv_note_summary_1);
        tvNoteSummary2 = view.findViewById(R.id.tv_note_summary_2);
        tvNoteSummary3 = view.findViewById(R.id.tv_note_summary_3);
        tvNoteTime1 = view.findViewById(R.id.tv_note_time_1);
        tvNoteTime2 = view.findViewById(R.id.tv_note_time_2);
        tvNoteTime3 = view.findViewById(R.id.tv_note_time_3);

        // 设置按钮点击事件 - 目前只是显示提示信息
        btnTakeNotes.setOnClickListener(v -> {
            // 跳转到添加笔记界面
            if (getActivity() != null) {
                NoteEditorFragment noteEditorFragment = NoteEditorFragment.newInstance(bookId, tvBookTitle.getText().toString());
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, noteEditorFragment)
                    .addToBackStack(null)
                    .commit();
            }
        });
        
        btnContinueReading.setOnClickListener(v -> 
            Toast.makeText(requireContext(), "继续阅读功能尚未实现", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void initData() {
        // 不需要初始化数据，已在onViewCreated中加载
    }

    /**
     * 更新UI显示图书信息
     */
    private void updateBookUI(Book book) {
        if (book == null) return;

        // 设置标题和作者
        tvBookTitle.setText(book.getBookName());
        tvBookAuthor.setText(book.getAuthor());
        
        // 更新工具栏标题
        if (!TextUtils.isEmpty(book.getBookName())) {
            toolbar.setTitle(book.getBookName());
        }

        // 设置分类
        if (!TextUtils.isEmpty(book.getClcName())) {
            tvCategory.setText(book.getClcName());
        }

        // 设置页数
        if (book.getPages() != null) {
            tvPages.setText(String.format("%d页", book.getPages()));
        } else {
            tvPages.setText("未知");
        }

        // 设置语言
        if (!TextUtils.isEmpty(book.getLanguage())) {
            tvLanguage.setText(book.getLanguage());
        } else {
            tvLanguage.setText("未知");
        }

        // 设置简介
        if (!TextUtils.isEmpty(book.getBookDesc())) {
            tvBookIntro.setText(book.getBookDesc());
        } else {
            tvBookIntro.setText("暂无简介");
        }

        // 设置封面图片
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            ImageUtils.loadImage(requireContext(), book.getCoverUrlArray()[0], ivBookCover);
        } else {
            ivBookCover.setImageResource(R.drawable.ic_book_placeholder);
        }
    }
    
    /**
     * 更新UserBook相关信息（例如存放位置、入库时间等）
     */
    private void updateUserBookInfo(UserBook userBook) {
        if (userBook == null) {
            setDefaultUserBookInfo();
            return;
        }
        
        // 设置物理位置
        if (!TextUtils.isEmpty(userBook.getStorageLocation())) {
            tvStorageLocation.setText(userBook.getStorageLocation());
        } else {
            tvStorageLocation.setText(R.string.not_specified);
        }
        
        // 设置入库时间
        if (!TextUtils.isEmpty(userBook.getEntryTime())) {
            tvEntryTime.setText(userBook.getEntryTime());
        } else {
            tvEntryTime.setText(R.string.not_specified);
        }
        
        // 设置备注
        if (!TextUtils.isEmpty(userBook.getRemark())) {
            tvRemark.setText(userBook.getRemark());
        } else {
            tvRemark.setText(R.string.not_specified);
        }
    }
    
    /**
     * 设置默认的UserBook信息
     */
    private void setDefaultUserBookInfo() {
        tvStorageLocation.setText(R.string.not_specified);
        tvEntryTime.setText(R.string.not_specified);
        tvRemark.setText(R.string.not_specified);
    }

    /**
     * 更新最近笔记UI
     */
    private void updateRecentNotes(List<Note> notes) {
        if (notes == null || notes.isEmpty()) {
            // 没有笔记，显示暂无笔记提示
            tvNoNotes.setVisibility(View.VISIBLE);
            tvRecentNotesHint.setVisibility(View.VISIBLE);
            llNoteItem1.setVisibility(View.GONE);
            llNoteItem2.setVisibility(View.GONE);
            llNoteItem3.setVisibility(View.GONE);
            return;
        }
        
        // 有笔记，隐藏暂无笔记提示，显示提示文本
        tvNoNotes.setVisibility(View.GONE);
        tvRecentNotesHint.setVisibility(View.VISIBLE);
        
        // 最多展示3条笔记
        int size = Math.min(notes.size(), 3);
        
        // 设置笔记项
        for (int i = 0; i < size; i++) {
            Note note = notes.get(i);
            switch (i) {
                case 0:
                    setNoteItem(llNoteItem1, tvNoteTitle1, tvNoteSummary1, tvNoteTime1, note);
                    break;
                case 1:
                    setNoteItem(llNoteItem2, tvNoteTitle2, tvNoteSummary2, tvNoteTime2, note);
                    break;
                case 2:
                    setNoteItem(llNoteItem3, tvNoteTitle3, tvNoteSummary3, tvNoteTime3, note);
                    break;
            }
        }
        
        // 隐藏多余的笔记项
        if (size < 1) llNoteItem1.setVisibility(View.GONE);
        if (size < 2) llNoteItem2.setVisibility(View.GONE);
        if (size < 3) llNoteItem3.setVisibility(View.GONE);
    }
    
    /**
     * 设置笔记项内容
     */
    private void setNoteItem(LinearLayout layout, TextView titleView, TextView summaryView, TextView timeView, Note note) {
        // 设置可见性
        layout.setVisibility(View.VISIBLE);
        
        // 设置标题
        titleView.setText(note.getTitle());
        
        // 设置摘要
        if (!TextUtils.isEmpty(note.getSummary())) {
            summaryView.setText(note.getSummary());
            summaryView.setVisibility(View.VISIBLE);
        } else {

            String content = note.getContent();
            if (!TextUtils.isEmpty(content)) {
                String summary = "暂无摘要";
                summaryView.setText(summary);
                summaryView.setVisibility(View.VISIBLE);
            } else {
                summaryView.setVisibility(View.GONE);
            }
        }
        
        // 设置时间
        if (!TextUtils.isEmpty(note.getUpdateTime())) {
            String formattedDate = note.getUpdateTime().replace('T', ' ').substring(0, 16);
            String timePrefix = "记录于%s";
            timeView.setText(String.format(timePrefix, formattedDate));
        } else {
            timeView.setVisibility(View.GONE);
        }
        
        // 设置点击事件
        layout.setOnClickListener(v -> navigateToEditNote(note));
    }
    
    /**
     * 跳转到编辑笔记界面
     */
    private void navigateToEditNote(Note note) {
        if (getActivity() != null && note != null && note.getId() != null) {
            NoteEditorFragment noteEditorFragment = NoteEditorFragment.newInstance(note.getId(), bookId, tvBookTitle.getText().toString());
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, noteEditorFragment)
                .addToBackStack(null)
                .commit();
        }
    }

    private void showError(String errorMessage) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show();
    }
} 