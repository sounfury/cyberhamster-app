package org.sounfury.cyber_hamster.ui.fragment.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.ui.activity.SignActivity;
import org.sounfury.cyber_hamster.ui.viewmodel.ProfileViewModel;
import org.sounfury.cyber_hamster.ui.viewmodel.UserViewModel;
import org.sounfury.cyber_hamster.utils.ImageUtils;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends BaseFragment {
    
    private ImageView ivAvatar;
    private TextView tvUsername;
    private TextView tvBookCount;
    private TextView tvNoteCount;
    private Button btnChangePassword;
    private Button btnLogout;
    private UserViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        
        // 观察数据变化
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.getNickname());
                // 如果有头像，可以加载头像
                if (user.getAvatar() == null) {
                    ImageView ivAvatar = view.findViewById(R.id.iv_avatar);
                    ivAvatar.setImageResource(R.drawable.ic_profile);
                } else {
                    // 转化为Uri
                    Log.d("ProfileFragment", "onViewCreated: " + user.getAvatar());
                    Uri AvatarUri = Uri.parse(user.getAvatar());
                    ImageUtils.loadImage(getContext(), user.getAvatar(), ivAvatar);
                }
            }
        });
        
        // 设置按钮点击事件
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        btnLogout.setOnClickListener(v -> logout());
        
        // 观察错误信息
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        
        // 观察书籍和笔记数量
        viewModel.getBookCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvBookCount.setText("书籍: " + count);
            }
        });

        viewModel.getNoteCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvNoteCount.setText("笔记: " + count);
            }
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
            // 例如: progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // 每次回到页面时重新加载统计数据
        if (viewModel != null && viewModel.isLoggedIn()) {
            viewModel.loadProfileStats();
        }
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvUsername = view.findViewById(R.id.tv_username);
        tvBookCount = view.findViewById(R.id.tv_book_count);
        tvNoteCount = view.findViewById(R.id.tv_note_count);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnLogout = view.findViewById(R.id.btn_logout);
    }
    
    @Override
    protected void initData() {
        // 加载用户统计数据
        if (viewModel != null && viewModel.isLoggedIn()) {
            viewModel.loadProfileStats();
        }
    }
    
    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);
        
        TextInputEditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        TextInputEditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        TextInputEditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        
        AlertDialog dialog = builder.create();
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            
            if (TextUtils.isEmpty(oldPassword)) {
                Toast.makeText(getContext(), "请输入当前密码", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (TextUtils.isEmpty(newPassword)) {
                Toast.makeText(getContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 调用ViewModel中的修改密码方法
            viewModel.changePassword(oldPassword, newPassword);
            dialog.dismiss();
        });
        
        dialog.show();
    }
    
    /**
     * 退出登录
     */
    private void logout() {
        viewModel.logout();
        
        // 跳转到登录界面
        Intent intent = new Intent(getActivity(), SignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        // 确保结束当前Activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
} 