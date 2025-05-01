package org.sounfury.cyber_hamster.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseActivity;
import org.sounfury.cyber_hamster.ui.fragment.category.BookshelfManagementFragment;
import org.sounfury.cyber_hamster.ui.fragment.home.HomeFragment;
import org.sounfury.cyber_hamster.ui.fragment.note.NoteFragment;
import org.sounfury.cyber_hamster.ui.fragment.profile.ProfileFragment;
import org.sounfury.cyber_hamster.ui.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {
    
    private ViewPager2 viewPager;
    private View fragmentContainer;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAdd;
    private MainViewModel viewModel;
    
    private final int TOTAL_PAGES = 4;
    private final int HOME_PAGE = 0;
    private final int CATEGORY_PAGE = 1;
    private final int NOTE_PAGE = 2;
    private final int PROFILE_PAGE = 3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        initView();
        initData();
        
        // 观察当前页面变化
        viewModel.getCurrentPage().observe(this, page -> {
            if (page != null) {
                viewPager.setCurrentItem(page);
                updateBottomNavigation(page);
            }
        });
        
        // 添加返回键监听
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            updateContainerVisibility();
        });
    }
    
    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        fragmentContainer = findViewById(R.id.fragment_container);
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
                        return new BookshelfManagementFragment();
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
        
        // 切换页面
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewModel.setCurrentPage(HOME_PAGE);
                return true;
            } else if (itemId == R.id.nav_category) {
                viewModel.setCurrentPage(CATEGORY_PAGE);
                return true;
            } else if (itemId == R.id.nav_add) {
                // 不做任何切换，只触发FAB点击
                fabAdd.performClick();
                return false;
            } else if (itemId == R.id.nav_note) {
                viewModel.setCurrentPage(NOTE_PAGE);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewModel.setCurrentPage(PROFILE_PAGE);
                return true;
            }
            return false;
        });
        
        // 添加按钮点击事件
        fabAdd.setOnClickListener(view -> {
            // 打开添加书籍界面
            showDetailFragment(
                org.sounfury.cyber_hamster.ui.fragment.book.AddBookFragment.newInstance()
            );
        });
    }
    
    @Override
    protected void initData() {
        // 初始化数据
    }
    
    // 更新底部导航栏选中状态
    private void updateBottomNavigation(int position) {
        int id;
        switch (position) {
            case HOME_PAGE:
                id = R.id.nav_home;
                break;
            case CATEGORY_PAGE:
                id = R.id.nav_category;
                break;
            case NOTE_PAGE:
                id = R.id.nav_note;
                break;
            case PROFILE_PAGE:
                id = R.id.nav_profile;
                break;
            default:
                id = R.id.nav_home;
        }
        if (bottomNav.getSelectedItemId() != id) {
            bottomNav.setSelectedItemId(id);
        }
    }
    
    /**
     * 显示详情Fragment
     */
    public void showDetailFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        
        updateContainerVisibility();
    }
    
    /**
     * 更新容器可见性
     */
    private void updateContainerVisibility() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // 有详情页面，显示详情容器
            viewPager.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
            // 底部导航栏由Fragment自己处理可见性
        } else {
            // 没有详情页面，显示主页面
            viewPager.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
            // 恢复底部导航栏
            bottomNav.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    
    /**
     * 刷新首页数据
     * 当添加了新书后需要调用此方法刷新首页数据
     */
    public void refreshHomeData() {
        // 如果当前在首页，通知首页Fragment刷新数据
        if (viewPager.getCurrentItem() == HOME_PAGE && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentByTag("f" + HOME_PAGE);
            if (homeFragment != null) {
                homeFragment.refreshData();
            }
        }
    }
} 