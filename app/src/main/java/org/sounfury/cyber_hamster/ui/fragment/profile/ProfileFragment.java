package org.sounfury.cyber_hamster.ui.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;

public class ProfileFragment extends BaseFragment {
    
    private ImageView ivAvatar;
    private TextView tvUsername;
    private TextView tvBookCount;
    private TextView tvNoteCount;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        // 加载个人资料数据
        tvUsername.setText("测试用户");
        tvBookCount.setText("书籍: 12");
        tvNoteCount.setText("笔记: 5");
    }
} 