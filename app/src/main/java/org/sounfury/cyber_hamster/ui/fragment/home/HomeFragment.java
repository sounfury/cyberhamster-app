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

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.ui.adapter.BookAdapter;
import org.sounfury.cyber_hamster.ui.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    
    private RecyclerView rvBooks;
    private EditText etSearch;
    private Chip chipAll, chipNovel, chipTech, chipSocial, chipEducation, chipEconomics;
    
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    private HomeViewModel viewModel;
    
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
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
        });
        
        viewModel.getSelectedCategory().observe(getViewLifecycleOwner(), categoryId -> {
            updateSelectedCategoryChip(categoryId);
        });
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        etSearch = view.findViewById(R.id.et_search);
        
        chipAll = view.findViewById(R.id.chip_all);
        chipNovel = view.findViewById(R.id.chip_novel);
        chipTech = view.findViewById(R.id.chip_tech);
        chipSocial = view.findViewById(R.id.chip_social);
        chipEducation = view.findViewById(R.id.chip_education);
        chipEconomics = view.findViewById(R.id.chip_economics);
        
        // 设置点击事件
        chipAll.setOnClickListener(this);
        chipNovel.setOnClickListener(this);
        chipTech.setOnClickListener(this);
        chipSocial.setOnClickListener(this);
        chipEducation.setOnClickListener(this);
        chipEconomics.setOnClickListener(this);
        
        // 设置RecyclerView
        rvBooks = view.findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new GridLayoutManager(getContext(), 1));
        
        // 初始化适配器
        bookAdapter = new BookAdapter(getContext(), bookList);
        rvBooks.setAdapter(bookAdapter);
        
        // 设置点击监听器
        bookAdapter.setOnItemClickListener(book -> {
            // 处理图书点击事件
            Toast.makeText(getContext(), "点击了: " + book.getBookName(), Toast.LENGTH_SHORT).show();
        });
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
        } else if (id == R.id.chip_novel) {
            viewModel.setSelectedCategory(1);
        } else if (id == R.id.chip_tech) {
            viewModel.setSelectedCategory(2);
        } else if (id == R.id.chip_social) {
            viewModel.setSelectedCategory(3);
        } else if (id == R.id.chip_education) {
            viewModel.setSelectedCategory(4);
        } else if (id == R.id.chip_economics) {
            viewModel.setSelectedCategory(5);
        }
    }
    
    // 更新分类标签选中状态
    private void updateSelectedCategoryChip(int categoryId) {
        chipAll.setChecked(categoryId == 0);
        chipNovel.setChecked(categoryId == 1);
        chipTech.setChecked(categoryId == 2);
        chipSocial.setChecked(categoryId == 3);
        chipEducation.setChecked(categoryId == 4);
        chipEconomics.setChecked(categoryId == 5);
    }
} 