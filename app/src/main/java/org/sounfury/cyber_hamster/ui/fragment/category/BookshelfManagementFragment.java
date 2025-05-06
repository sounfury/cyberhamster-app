package org.sounfury.cyber_hamster.ui.fragment.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Category;
import org.sounfury.cyber_hamster.data.network.response.GroupedBooks;
import org.sounfury.cyber_hamster.ui.adapter.GroupedBookAdapter;
import org.sounfury.cyber_hamster.ui.adapter.SelectCategoryAdapter;
import org.sounfury.cyber_hamster.ui.viewmodel.BookshelfViewModel;
import org.sounfury.cyber_hamster.ui.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookshelfManagementFragment extends BaseFragment {
    
    private RecyclerView rvBooks;
    private TextView tvTitle;
    private TextView tvSelectedCount;
    private GroupedBookAdapter adapter;
    private List<GroupedBooks> bookList = new ArrayList<>();
    private BookshelfViewModel viewModel;
    private CategoryViewModel categoryViewModel;
    
    // 底部操作栏
    private LinearLayout bottomActionBar;
    private CheckBox cbSelectAll;
    private Button btnInvertSelection;
    private Button btnBottomGroup;
    private Button btnBottomDelete;
    
    private MenuItem menuItemSelect;
    private MenuItem menuItemSelectAll;
    private MenuItem menuItemDeselectAll;
    private MenuItem menuItemGroup;
    private MenuItem menuItemDelete;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 允许创建菜单
        setHasOptionsMenu(true);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(BookshelfViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        
        return inflater.inflate(R.layout.fragment_bookshelf_management, container, false);
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookshelf_management, menu);
        
        menuItemSelect = menu.findItem(R.id.action_select);
        menuItemSelectAll = menu.findItem(R.id.action_select_all);
        menuItemDeselectAll = menu.findItem(R.id.action_deselect_all);
        menuItemGroup = menu.findItem(R.id.action_group);
        menuItemDelete = menu.findItem(R.id.action_delete);
        
        // 默认处于选择模式
        updateMenuVisibility(true);
        
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_select) {
            toggleSelectMode();
            return true;
        } else if (itemId == R.id.action_select_all) {
            adapter.selectAll();
            return true;
        } else if (itemId == R.id.action_deselect_all) {
            adapter.deselectAll();
            return true;
        } else if (itemId == R.id.action_group) {
            showGroupDialog();
            return true;
        } else if (itemId == R.id.action_delete) {
            showDeleteConfirmDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 观察数据变化
        viewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookList.clear();
                bookList.addAll(books);
                adapter.notifyDataSetChanged();
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
        });
        
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });
        
        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
                viewModel.loadBooksToGrouped(); // 刷新列表
            }
        });
    }
    
    @Override
    protected void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.bookshelf_management);
        
        tvSelectedCount = view.findViewById(R.id.tv_selected_count);
        
        // 初始化RecyclerView
        rvBooks = view.findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 初始化底部操作栏
        bottomActionBar = view.findViewById(R.id.bottom_action_bar);
        cbSelectAll = view.findViewById(R.id.cb_select_all);
        btnInvertSelection = view.findViewById(R.id.btn_invert_selection);
        btnBottomGroup = view.findViewById(R.id.btn_bottom_group);
        btnBottomDelete = view.findViewById(R.id.btn_bottom_delete);
        
        // 默认显示底部操作栏
        bottomActionBar.setVisibility(View.VISIBLE);
        
        // 设置底部栏事件
        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.selectAll();
            } else {
                adapter.deselectAll();
            }
        });
        
        // 设置反选按钮
        btnInvertSelection.setOnClickListener(v -> {
            invertSelection();
        });
        
        btnBottomGroup.setOnClickListener(v -> showGroupDialog());
        btnBottomDelete.setOnClickListener(v -> showDeleteConfirmDialog());
        
        // 初始化适配器
        adapter = new GroupedBookAdapter(getContext(), bookList);
        rvBooks.setAdapter(adapter);
        
        // 设置Item点击事件
        adapter.setOnItemClickListener(book -> {
            // 单击图书项目的处理
            Toast.makeText(getContext(), book.getBookName(), Toast.LENGTH_SHORT).show();
        });
        
        // 设置分组按钮点击事件
        adapter.setOnGroupClickListener(book -> {
            List<Integer> bookIds = new ArrayList<>();
            bookIds.add(book.getId());
            showSelectCategoryDialogForSingleBook(book);
        });
        
        // 设置删除按钮点击事件
        adapter.setOnDeleteClickListener(book -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.delete)
                    .setMessage(R.string.delete_books_confirm)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        List<Long> bookIds = new ArrayList<>();
                        bookIds.add((long) book.getId());
                        viewModel.deleteBooks(bookIds);
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        
        // 设置选择变化监听器
        adapter.setOnSelectChangeListener(count -> {
            updateSelectState(count);
        });
    }
    
    @Override
    protected void initData() {
        // 加载数据
        viewModel.loadBooksToGrouped();
        categoryViewModel.loadCategories();
    }
    
    // 反选操作
    private void invertSelection() {
        Set<Integer> currentSelected = adapter.getSelectedBookIds();
        Set<Integer> allIds = new HashSet<>();
        
        // 获取所有图书ID
        for (GroupedBooks book : bookList) {
            allIds.add(book.getId());
        }
        
        // 清除当前所有选择
        adapter.deselectAll();
        
        // 循环选中之前未选中的项
        for (Integer id : allIds) {
            if (!currentSelected.contains(id)) {
                // 反选 - 选中之前未选中的
                adapter.toggleBookSelection(id);
            }
        }
        
        adapter.notifyDataSetChanged();
    }
    
    // 切换选择模式
    private void toggleSelectMode() {
        adapter.toggleSelectMode();
        boolean isSelectMode = adapter.isSelectMode();
        updateMenuVisibility(isSelectMode);
        
        // 更新底部操作栏可见性
        bottomActionBar.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        
        // 重置选择计数器
        if (isSelectMode) {
            updateSelectState(0);
        } else {
            tvSelectedCount.setVisibility(View.GONE);
        }
    }
    
    // 更新选择状态
    private void updateSelectState(int count) {
        // 更新选择计数
        if (count > 0) {
            tvSelectedCount.setVisibility(View.VISIBLE);
            tvSelectedCount.setText(String.format("已选择%d项", count));
        } else {
            tvSelectedCount.setVisibility(View.GONE);
        }
        
        // 更新菜单项状态
        updateMenuVisibility(adapter.isSelectMode());
        updateGroupAndDeleteButton(count > 0);
        
        // 更新底部按钮状态
        btnBottomGroup.setEnabled(count > 0);
        btnBottomDelete.setEnabled(count > 0);
        
        // 更新全选状态 (避免递归调用)
        if (count == bookList.size() && count > 0) {
            cbSelectAll.setChecked(true);
        } else if (count == 0) {
            cbSelectAll.setChecked(false);
        }
    }
    
    // 更新菜单项的可见性
    private void updateMenuVisibility(boolean isSelectMode) {
        if (menuItemSelect != null) {
            menuItemSelect.setTitle(isSelectMode ? R.string.cancel_selection : R.string.select_books);
            menuItemSelectAll.setVisible(isSelectMode);
            menuItemDeselectAll.setVisible(isSelectMode);
            menuItemGroup.setVisible(isSelectMode);
            menuItemDelete.setVisible(isSelectMode);
            
            if (isSelectMode) {
                updateGroupAndDeleteButton(false);
            }
        }
    }
    
    // 更新分组和删除按钮的可用性
    private void updateGroupAndDeleteButton(boolean enabled) {
        if (menuItemGroup != null && menuItemDelete != null) {
            menuItemGroup.setEnabled(enabled);
            menuItemDelete.setEnabled(enabled);
        }
    }
    
    // 显示分组确认对话框
    private void showGroupDialog() {
        Set<Integer> selectedBookIds = adapter.getSelectedBookIds();
        if (selectedBookIds.isEmpty()) {
            Toast.makeText(getContext(), R.string.no_book_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        
        List<Integer> bookIdList = new ArrayList<>(selectedBookIds);
        showSelectCategoryDialog(bookIdList);
    }
    
    // 显示选择分类对话框
    private void showSelectCategoryDialog(List<Integer> bookIds) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_category, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(dialogView).create();
        
        RecyclerView rvCategories = dialogView.findViewById(R.id.rv_categories);
        Button btnAddCategory = dialogView.findViewById(R.id.btn_add_category);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnClearAll = dialogView.findViewById(R.id.btn_clear_all);
        
        // 设置RecyclerView
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 获取分类列表
        List<Category> categories = categoryViewModel.getCategoryList().getValue();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        
        // 设置适配器
        SelectCategoryAdapter categoryAdapter = new SelectCategoryAdapter(getContext(), categories);
        rvCategories.setAdapter(categoryAdapter);
        
        // 如果只有一本书，预选择当前书籍所在的分类
        if (bookIds.size() == 1) {
            int bookId = bookIds.get(0);
            // 在bookList中找到对应的书籍
            for (GroupedBooks book : bookList) {
                if (book.getId() == bookId) {
                    // 找到书籍的当前分类，并在适配器中预选
                    List<Category> bookCategories = book.getUserCategories();
                    if (bookCategories != null && !bookCategories.isEmpty()) {
                        for (Category category : bookCategories) {
                            categoryAdapter.toggleCategorySelection(category.getId());
                        }
                    }
                    break;
                }
            }
        }
        
        // 设置添加分类按钮点击事件
        btnAddCategory.setOnClickListener(v -> {
            showAddCategoryDialog(dialog, categoryAdapter);
        });
        
        // 设置取消按钮点击事件
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        // 设置清空分类按钮点击事件
        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.clear_categories)
                    .setMessage(R.string.clear_categories_confirm)
                    .setPositiveButton(R.string.confirm, (dialogInterface, which) -> {
                        // 将书籍从所有分类中移除 (传递空的分类ID列表)
                        List<Long> bookIdsLong = bookIds.stream()
                                .map(Integer::longValue)
                                .collect(Collectors.toList());
                        
                        viewModel.addBooksToCategories(bookIdsLong, new ArrayList<>());
                        dialog.dismiss();
                        
                        // 不退出选择模式，只清除选择
                        adapter.deselectAll();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        
        // 设置确认按钮点击事件
        btnConfirm.setOnClickListener(v -> {
            Set<Integer> selectedCategoryIds = categoryAdapter.getSelectedCategoryIds();
            
            // 将书籍添加到选中的分类
            List<Long> categoryIds = selectedCategoryIds.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
                    
            List<Long> bookIdsLong = bookIds.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
                    
            viewModel.addBooksToCategories(bookIdsLong, categoryIds);
            dialog.dismiss();
            
            // 不退出选择模式，只清除选择
            adapter.deselectAll();
        });
        
        dialog.show();
    }
    
    // 显示添加分类对话框
    private void showAddCategoryDialog(AlertDialog parentDialog, SelectCategoryAdapter categoryAdapter) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_bookshelf, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(dialogView).create();
        
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        EditText etBookshelfName = dialogView.findViewById(R.id.et_bookshelf_name);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        tvTitle.setText(R.string.add_bookshelf);
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            String name = etBookshelfName.getText().toString().trim();
            if (name.isEmpty()) {
                etBookshelfName.setError("书架名称不能为空");
                return;
            }
            
            categoryViewModel.createCategory(name);
            dialog.dismiss();
            
            // 关闭并重新打开父对话框以刷新分类列表
            parentDialog.dismiss();
            categoryViewModel.loadCategories();
            
            // 延迟一点再重新打开选择对话框，确保分类已经加载
            requireView().postDelayed(() -> {
                if (isAdded()) { // 确保Fragment仍然附加到Activity
                    List<Integer> selectedBookIds = new ArrayList<>(adapter.getSelectedBookIds());
                    showSelectCategoryDialog(selectedBookIds);
                }
            }, 500);
        });
        
        dialog.show();
    }
    
    // 显示删除确认对话框
    private void showDeleteConfirmDialog() {
        Set<Integer> selectedBookIds = adapter.getSelectedBookIds();
        if (selectedBookIds.isEmpty()) {
            Toast.makeText(getContext(), R.string.no_book_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_books_confirm)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    List<Long> bookIds = selectedBookIds.stream()
                            .map(Integer::longValue)
                            .collect(Collectors.toList());
                    viewModel.deleteBooks(bookIds);
                    
                    // 退出选择模式
                    toggleSelectMode();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    // 显示错误消息
    private void showError(String errorMessage) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show();
    }
    
    // 显示单本图书的选择分类对话框
    private void showSelectCategoryDialogForSingleBook(GroupedBooks book) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_category, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(dialogView).create();
        
        RecyclerView rvCategories = dialogView.findViewById(R.id.rv_categories);
        Button btnAddCategory = dialogView.findViewById(R.id.btn_add_category);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnClearAll = dialogView.findViewById(R.id.btn_clear_all);
        
        // 设置RecyclerView
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 获取分类列表
        List<Category> categories = categoryViewModel.getCategoryList().getValue();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        
        // 设置适配器
        SelectCategoryAdapter categoryAdapter = new SelectCategoryAdapter(getContext(), categories);
        rvCategories.setAdapter(categoryAdapter);
        
        // 预选择当前书籍所在的分类
        List<Category> bookCategories = book.getUserCategories();
        if (bookCategories != null && !bookCategories.isEmpty()) {
            for (Category category : bookCategories) {
                categoryAdapter.toggleCategorySelection(category.getId());
            }
        }
        
        // 设置添加分类按钮点击事件
        btnAddCategory.setOnClickListener(v -> {
            showAddCategoryDialog(dialog, categoryAdapter);
        });
        
        // 设置取消按钮点击事件
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        // 设置清空分类按钮点击事件
        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.clear_categories)
                    .setMessage(R.string.clear_categories_confirm)
                    .setPositiveButton(R.string.confirm, (dialogInterface, which) -> {
                        // 将书籍从所有分类中移除 (传递空的分类ID列表)
                        viewModel.addSingleBookToCategories(book.getId(), new ArrayList<>());
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        
        // 设置确认按钮点击事件
        btnConfirm.setOnClickListener(v -> {
            Set<Integer> selectedCategoryIds = categoryAdapter.getSelectedCategoryIds();
            
            // 将书籍添加到选中的分类
            List<Long> categoryIds = selectedCategoryIds.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
                    
            viewModel.addSingleBookToCategories(book.getId(), categoryIds);
            dialog.dismiss();
        });
        
        dialog.show();
    }
} 