package org.sounfury.cyber_hamster.ui.fragment.book;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.sounfury.cyber_hamster.ui.viewmodel.AddBookViewModel;
import org.sounfury.cyber_hamster.ui.activity.MainActivity;

public class AddBookFragment extends BaseFragment {

    private AddBookViewModel viewModel;
    
    // UI组件
    private EditText etIsbn;
    private EditText etStorageLocation;
    private EditText etRemark;
    private Button btnSave;
    private View llScanBarcode;
    private View llTakeCoverPhoto;
    private View llIsbnSearch;
    
    // 底部导航栏和悬浮按钮
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAdd;
    
    // 加载对话框
    private ProgressDialog progressDialog;

    public static AddBookFragment newInstance() {
        return new AddBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(AddBookViewModel.class);
        
        // 观察数据变化
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::updateLoadingState);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showError);
        viewModel.getAddSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                showAddSuccess();
            }
        });
        
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
        // 初始化UI组件
        etIsbn = view.findViewById(R.id.et_isbn);
        etStorageLocation = view.findViewById(R.id.et_storage_location);
        etRemark = view.findViewById(R.id.et_remark);
        btnSave = view.findViewById(R.id.btn_save);
        llScanBarcode = view.findViewById(R.id.ll_scan_barcode);
        llTakeCoverPhoto = view.findViewById(R.id.ll_take_cover_photo);
        llIsbnSearch = view.findViewById(R.id.ll_isbn_search);
        
        // 设置按钮点击事件
        btnSave.setOnClickListener(v -> saveBook());
        
        // 设置扫描条形码点击事件
        llScanBarcode.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "扫描条形码功能尚未实现", Toast.LENGTH_SHORT).show();
        });
        
        // 设置拍摄封面点击事件
        llTakeCoverPhoto.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "拍摄封面功能尚未实现", Toast.LENGTH_SHORT).show();
        });
        
        // 设置ISBN搜索点击事件
        llIsbnSearch.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "ISBN搜索功能尚未实现", Toast.LENGTH_SHORT).show();
        });
        
        // 初始化加载对话框
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(getString(R.string.adding_book));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void initData() {
        // 不需要初始化数据
    }
    
    /**
     * 保存图书
     */
    private void saveBook() {
        String isbn = etIsbn.getText().toString().trim();
        String storageLocation = etStorageLocation.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();
        
        // 校验输入
        if (isbn.isEmpty()) {
            showError(getString(R.string.isbn_required));
            return;
        }
        
        // 调用ViewModel添加图书
        viewModel.addBook(isbn, storageLocation, remark);
    }
    
    /**
     * 更新加载状态
     */
    private void updateLoadingState(Boolean isLoading) {
        if (isLoading != null && isLoading) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }
    
    /**
     * 显示添加成功
     */
    private void showAddSuccess() {
        Toast.makeText(requireContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
        
        // 通知MainActivity刷新首页数据
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshHomeData();
        }
        
        // 返回上一页
        requireActivity().onBackPressed();
    }
    
    /**
     * 显示错误信息
     */
    private void showError(String errorMessage) {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show();
        }
    }
} 