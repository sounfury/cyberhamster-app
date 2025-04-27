package org.sounfury.cyber_hamster.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseActivity;
import org.sounfury.cyber_hamster.ui.fragment.category.CategoryFragment;
import org.sounfury.cyber_hamster.ui.fragment.home.HomeFragment;
import org.sounfury.cyber_hamster.ui.fragment.note.NoteFragment;
import org.sounfury.cyber_hamster.ui.fragment.profile.ProfileFragment;

public class MainActivity extends BaseActivity {
    
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAdd;
    
    private final int TOTAL_PAGES = 4;
    private final int HOME_PAGE = 0;
    private final int CATEGORY_PAGE = 1;
    private final int NOTE_PAGE = 2;
    private final int PROFILE_PAGE = 3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        initData();
    }
    
    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_navigation);
        fabAdd = findViewById(R.id.fab_add);
        
        // 设置ViewPager适配器
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case HOME_PAGE:
                        return new HomeFragment();
                    case CATEGORY_PAGE:
                        return new CategoryFragment();
                    case NOTE_PAGE:
                        return new NoteFragment();
                    case PROFILE_PAGE:
                        return new ProfileFragment();
                    default:
                        return new HomeFragment();
                }
            }
            
            @Override
            public int getItemCount() {
                return TOTAL_PAGES;
            }
        });
        
        // 禁用ViewPager滑动
        viewPager.setUserInputEnabled(false);
        
        // 设置底部导航点击事件
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(HOME_PAGE);
                return true;
            } else if (itemId == R.id.nav_category) {
                viewPager.setCurrentItem(CATEGORY_PAGE);
                return true;
            } else if (itemId == R.id.nav_add) {
                // 不做任何切换，只触发FAB点击
                fabAdd.performClick();
                return false;
            } else if (itemId == R.id.nav_note) {
                viewPager.setCurrentItem(NOTE_PAGE);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(PROFILE_PAGE);
                return true;
            }
            return false;
        });
        
        // 添加按钮点击事件
        fabAdd.setOnClickListener(view -> {
            // 打开添加书籍界面
            // TODO: 实现添加书籍功能
        });
    }
    
    @Override
    protected void initData() {
        // 初始化数据
    }
} 