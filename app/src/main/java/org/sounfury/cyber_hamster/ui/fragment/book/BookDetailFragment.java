package org.sounfury.cyber_hamster.ui.fragment.book;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import org.sounfury.cyber_hamster.data.model.UserBook;
import org.sounfury.cyber_hamster.ui.viewmodel.BookDetailViewModel;
import org.sounfury.cyber_hamster.utils.ImageUtils;

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

        // 设置按钮点击事件 - 目前只是显示提示信息
        btnTakeNotes.setOnClickListener(v -> 
            Toast.makeText(requireContext(), "做笔记功能尚未实现", Toast.LENGTH_SHORT).show());
        
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

    private void showError(String errorMessage) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show();
    }
} 