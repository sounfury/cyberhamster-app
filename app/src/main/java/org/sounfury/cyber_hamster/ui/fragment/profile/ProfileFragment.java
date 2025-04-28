package org.sounfury.cyber_hamster.ui.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.ui.viewmodel.ProfileViewModel;

public class ProfileFragment extends BaseFragment {
    
    private ImageView ivAvatar;
    private TextView tvUsername;
    private TextView tvBookCount;
    private TextView tvNoteCount;
    private ProfileViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        
        // 观察数据变化
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.getNickname());
                // 如果有头像，可以加载头像
            }
        });
        
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
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
        });
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvUsername = view.findViewById(R.id.tv_username);
        tvBookCount = view.findViewById(R.id.tv_book_count);
        tvNoteCount = view.findViewById(R.id.tv_note_count);
    }
    
    @Override
    protected void initData() {
        // 加载数据由ViewModel负责
    }
} 