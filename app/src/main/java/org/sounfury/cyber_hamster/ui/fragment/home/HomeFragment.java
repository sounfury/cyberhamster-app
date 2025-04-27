package org.sounfury.cyber_hamster.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.ui.adapter.BookAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    
    private RecyclerView rvBooks;
    private EditText etSearch;
    private Chip chipAll, chipNovel, chipTech, chipSocial, chipEducation, chipEconomics;
    
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
    }
    
    @Override
    protected void initData() {
        // 加载图书数据
        loadMockData();
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chip_all) {
            filterBooks(0);
        } else if (id == R.id.chip_novel) {
            filterBooks(1);
        } else if (id == R.id.chip_tech) {
            filterBooks(2);
        } else if (id == R.id.chip_social) {
            filterBooks(3);
        } else if (id == R.id.chip_education) {
            filterBooks(4);
        } else if (id == R.id.chip_economics) {
            filterBooks(5);
        }
    }
    
    // 根据类别过滤图书
    private void filterBooks(int categoryId) {
        // TODO: 实际应用中根据类别过滤图书
        // 此处为示例代码
    }
    
    // 加载模拟数据
    private void loadMockData() {
        bookList.clear();
        
        // 添加一些模拟数据
        bookList.add(new Book(1, "Cracking the Coding Interview", "刘慧欣", "Publisher 1", "123456789", 
                null, "Description 1", 2, new Date(), 0));
        
        bookList.add(new Book(2, "图灵的秘密", "安德鲁 霍金斯", "Publisher 2", "234567890", 
                null, "Description 2", 2, new Date(), 1));
        
        bookList.add(new Book(3, "活着", "余华", "Publisher 3", "345678901", 
                null, "Description 3", 1, new Date(), 2));
                
        bookList.add(new Book(4, "解忧杂货店", "东野圭吾", "Publisher 4", "456789012", 
                null, "Description 4", 1, new Date(), 1));
        
        bookAdapter.notifyDataSetChanged();
    }
} 