package org.sounfury.cyber_hamster.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.ui.viewmodel.UserViewModel;

public class RegisterFragment extends BaseFragment {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etNickname;
    private Button btnRegister;
    private ProgressBar progressBar;
    
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    protected void initView(View view) {
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etEmail = view.findViewById(R.id.et_email);
        etNickname = view.findViewById(R.id.et_nickname);
        btnRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progress_bar);
        
        btnRegister.setOnClickListener(v -> register());
    }

    @Override
    protected void initData() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        
        // 观察注册状态
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // 注册成功并自动登录，跳转到主页
                Toast.makeText(requireContext(), "注册并登录成功", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        
        // 观察错误信息
        userViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        
        // 观察加载状态
        userViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!isLoading);
        });
    }
    
    private void register() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        
        // 验证输入
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("请输入用户名");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("请输入密码");
            return;
        }
        
        if (password.length() < 6) {
            etPassword.setError("密码长度不能少于6位");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("两次输入的密码不一致");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("请输入邮箱");
            return;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("请输入有效的邮箱地址");
            return;
        }
        
        if (TextUtils.isEmpty(nickname)) {
            etNickname.setError("请输入昵称");
            return;
        }
        
        // 执行注册
        userViewModel.register(username, password, email, nickname);
    }
} 