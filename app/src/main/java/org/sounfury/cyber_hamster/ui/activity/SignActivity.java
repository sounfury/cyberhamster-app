package org.sounfury.cyber_hamster.ui.activity;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseActivity;
import org.sounfury.cyber_hamster.ui.adapter.SignViewPagerAdapter;

public class SignActivity extends BaseActivity {
    
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private SignViewPagerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        
        initView();
        initData();
    }
    
    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        
        // 设置适配器
        adapter = new SignViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // 连接TabLayout和ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("登录");
            } else {
                tab.setText("注册");
            }
        }).attach();
    }
    
    @Override
    protected void initData() {
        // 数据初始化，如有需要
    }
} 