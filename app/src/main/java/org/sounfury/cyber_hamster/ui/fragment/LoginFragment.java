package org.sounfury.cyber_hamster.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.repository.UserRepository;
import org.sounfury.cyber_hamster.ui.activity.MainActivity;
import org.sounfury.cyber_hamster.ui.viewmodel.UserViewModel;

public class LoginFragment extends BaseFragment {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private ProgressBar progressBar;
    
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void initView(View view) {
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        cbRemember = view.findViewById(R.id.cb_remember);
        btnLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progress_bar);
        
        btnLogin.setOnClickListener(v -> login());
    }

    @Override
    protected void initData() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        
        // 观察登录状态
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // 弹出登录成功提示
                Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);

                // 再关闭当前 Activity
                requireActivity().finish();
            }
        });


        userViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
        });
    }
    
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean rememberMe = cbRemember.isChecked();
        
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("请输入用户名");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("请输入密码");
            return;
        }
        
        userViewModel.login(username, password, rememberMe);
    }
} 