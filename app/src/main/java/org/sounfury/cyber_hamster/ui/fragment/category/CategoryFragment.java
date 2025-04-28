package org.sounfury.cyber_hamster.ui.fragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.ui.adapter.CategoryAdapter;
import org.sounfury.cyber_hamster.ui.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment {
    
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        
        // 观察数据变化
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryList.clear();
                categoryList.addAll(categories);
                categoryAdapter.notifyDataSetChanged();
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
        });
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        rvCategories = view.findViewById(R.id.rv_categories);
        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        // 初始化适配器
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        rvCategories.setAdapter(categoryAdapter);
        
        // 设置点击监听器
        categoryAdapter.setOnItemClickListener(category -> {
            // 处理分类点击事件
            Toast.makeText(getContext(), "点击了: " + category.getName(), Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    protected void initData() {
        // 加载数据由ViewModel负责
    }
} 