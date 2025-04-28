package org.sounfury.cyber_hamster.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.sounfury.cyber_hamster.ui.fragment.LoginFragment;
import org.sounfury.cyber_hamster.ui.fragment.RegisterFragment;

public class SignViewPagerAdapter extends FragmentStateAdapter {
    
    private static final int NUM_PAGES = 2;
    private static final int LOGIN_PAGE = 0;
    private static final int REGISTER_PAGE = 1;
    
    public SignViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case LOGIN_PAGE:
                return new LoginFragment();
            case REGISTER_PAGE:
                return new RegisterFragment();
            default:
                return new LoginFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
} 