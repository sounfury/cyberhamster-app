package org.sounfury.cyber_hamster.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseActivity;

public class LoginActivity extends BaseActivity {
    
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        
        initView();
        initData();
    }
    
    @Override
    protected void initView() {
        // 初始化视图
    }
    
    @Override
    protected void initData() {
        // 初始化数据
    }
} 