package org.sounfury.cyber_hamster.ui.fragment.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseFragment;
import org.sounfury.cyber_hamster.data.model.Note;
import org.sounfury.cyber_hamster.ui.adapter.NoteAdapter;
import org.sounfury.cyber_hamster.ui.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends BaseFragment {
    
    private RecyclerView rvNotes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList = new ArrayList<>();
    private NoteViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        
        // 观察数据变化
        viewModel.getNoteList().observe(getViewLifecycleOwner(), notes -> {
            if (notes != null) {
                noteList.clear();
                noteList.addAll(notes);
                noteAdapter.notifyDataSetChanged();
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // 可以在这里显示或隐藏加载指示器
        });
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        rvNotes = view.findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 初始化适配器
        noteAdapter = new NoteAdapter(getContext(), noteList);
        rvNotes.setAdapter(noteAdapter);
        
        // 设置点击监听器
        noteAdapter.setOnItemClickListener(note -> {
            // 处理笔记点击事件
            Toast.makeText(getContext(), "点击了: " + note.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    protected void initData() {
        // 加载数据由ViewModel负责
    }
} 