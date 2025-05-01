package org.sounfury.cyber_hamster.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.ui.activity.MainActivity;
import org.sounfury.cyber_hamster.ui.adapter.BookAdapter;
import org.sounfury.cyber_hamster.ui.fragment.book.BookDetailFragment;
import org.sounfury.cyber_hamster.ui.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    
    private RecyclerView rvBooks;
    private EditText etSearch;
    private ChipGroup chipGroupCategories;
    private Chip chipAll;
    
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    private HomeViewModel viewModel;
    private boolean isLoading = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // 观察数据变化
        viewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookList.clear();
                bookList.addAll(books);
                bookAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            this.isLoading = isLoading;
        });
        
        viewModel.getSelectedCategory().observe(getViewLifecycleOwner(), categoryId -> {
            updateSelectedCategoryChip(categoryId);
        });
        
        // 观察分类数据
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                setupCategoryChips(categories);
            }
        });
        
        // 观察错误信息
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });
    }
    
    private void showError(String errorMessage) {
        if (getView() != null) {
            Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        etSearch = view.findViewById(R.id.et_search);
        
        // 获取ChipGroup
        chipGroupCategories = view.findViewById(R.id.chip_group_categories);
        
        // 创建"全部"分类Chip
        chipAll = view.findViewById(R.id.chip_all);
        chipAll.setOnClickListener(this);
        
        // 设置RecyclerView
        rvBooks = view.findViewById(R.id.rv_books);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rvBooks.setLayoutManager(layoutManager);
        
        // 初始化适配器
        bookAdapter = new BookAdapter(getContext(), bookList);
        rvBooks.setAdapter(bookAdapter);
        
        // 设置点击监听器
        bookAdapter.setOnItemClickListener(book -> {
            // 跳转到图书详情页面
            navigateToBookDetail(book.getId());
        });
        
        // 添加滚动监听器实现分页加载
        rvBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                // 向下滚动且未处于加载状态
                if (dy > 0 && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    // 当可见的最后一项接近总数时，加载更多数据
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3
                            && firstVisibleItemPosition >= 0) {
                        loadMoreBooks();
                    }
                }
            }
        });
    }
    
    private void setupCategoryChips(List<Category> categories) {
        // 清除除了"全部"分类以外的所有Chip
        for (int i = 0; i < chipGroupCategories.getChildCount(); i++) {
            View child = chipGroupCategories.getChildAt(i);
            if (child instanceof Chip && child.getId() != R.id.chip_all) {
                chipGroupCategories.removeView(child);
                i--; // 由于删除了一项，需要减少索引
            }
        }
        
        // 为每个分类创建一个Chip
        for (Category category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category.getName());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            
            // 设置tag存储分类ID
            chip.setTag(category.getId());
            
            // 设置点击监听器
            chip.setOnClickListener(v -> {
                int categoryId = (int) v.getTag();
                viewModel.setSelectedCategory(categoryId);
            });
            
            // 添加到ChipGroup
            chipGroupCategories.addView(chip);
        }
    }
    
    private void loadMoreBooks() {
        if (!isLoading) {
            isLoading = true;
            viewModel.loadNextPage();
        }
    }
    
    @Override
    protected void initData() {
        // 加载数据由ViewModel负责
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chip_all) {
            viewModel.setSelectedCategory(0);
        }
    }
    
    // 更新分类标签选中状态
    private void updateSelectedCategoryChip(int categoryId) {
        // 设置"全部"分类的选中状态
        chipAll.setChecked(categoryId == 0);
        
        // 设置其他分类的选中状态
        for (int i = 0; i < chipGroupCategories.getChildCount(); i++) {
            View child = chipGroupCategories.getChildAt(i);
            if (child instanceof Chip && child.getId() != R.id.chip_all) {
                Chip chip = (Chip) child;
                int chipCategoryId = (int) chip.getTag();
                chip.setChecked(chipCategoryId == categoryId);
            }
        }
    }
    
    /**
     * 跳转到图书详情页面
     * 
     * @param bookId 图书ID
     */
    private void navigateToBookDetail(long bookId) {
        if (getActivity() instanceof MainActivity) {
           MainActivity activity = (MainActivity) getActivity();
           activity.showDetailFragment(BookDetailFragment.newInstance(bookId));
        }
    }
    
    /**
     * 刷新数据
     * 当添加新书后，可以调用此方法刷新书籍列表
     */
    public void refreshData() {
        // 重置加载状态和页码
        isLoading = false;
        
        // 调用ViewModel刷新数据
        viewModel.refreshData();
    }
} 