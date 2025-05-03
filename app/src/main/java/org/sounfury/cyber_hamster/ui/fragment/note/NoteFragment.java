package org.sounfury.cyber_hamster.ui.fragment.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressLoadingMore;
    private TextView tvEmptyView;
    
    // 是否正在加载更多
    private boolean isLoadingMore = false;
    
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
                if (notes.isEmpty()) {
                    tvEmptyView.setVisibility(View.VISIBLE);
                    rvNotes.setVisibility(View.GONE);
                } else {
                    tvEmptyView.setVisibility(View.GONE);
                    rvNotes.setVisibility(View.VISIBLE);
                    noteAdapter.setData(notes);
                }
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            swipeRefresh.setRefreshing(isLoading);
        });
        
        viewModel.getIsLoadingMore().observe(getViewLifecycleOwner(), isLoadingMore -> {
            this.isLoadingMore = isLoadingMore;
            progressLoadingMore.setVisibility(isLoadingMore ? View.VISIBLE : View.GONE);
        });
        
        viewModel.getHasMoreData().observe(getViewLifecycleOwner(), hasMoreData -> {
            // 当没有更多数据时，可以提示用户
            if (!hasMoreData && !noteList.isEmpty()) {
                Toast.makeText(getContext(), "没有更多笔记了", Toast.LENGTH_SHORT).show();
            }
        });
        
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void initView(View view) {
        // 初始化视图
        rvNotes = view.findViewById(R.id.rv_notes);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressLoadingMore = view.findViewById(R.id.progress_loading_more);
        tvEmptyView = view.findViewById(R.id.tv_empty_view);
        
        // 设置为瀑布流布局，2列
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        
        // 设置间距
        int spacing = (int) (getResources().getDisplayMetrics().density * 5f);
        rvNotes.addItemDecoration(new SpacingItemDecoration(spacing));
        
        rvNotes.setLayoutManager(layoutManager);
        
        // 初始化适配器
        noteAdapter = new NoteAdapter(getContext(), noteList);
        rvNotes.setAdapter(noteAdapter);
        
        // 设置点击监听器
        noteAdapter.setOnItemClickListener(note -> {
            // 处理笔记点击事件，跳转到编辑页面
            if (note != null && note.getId() != null) {
                navigateToEditNote(note.getId(),note.getBook().getBookName());
            }
        });
        
        // 设置下拉刷新监听
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshNotes();
        });
        
        // 设置滚动监听，实现上拉加载更多
        rvNotes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                if (!isLoadingMore && dy > 0) {
                    // 找到最后一个可见项的位置
                    int[] lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null);
                    int lastVisibleItemPosition = Math.max(lastVisibleItemPositions[0], lastVisibleItemPositions[1]);
                    
                    // 如果最后一个可见项是倒数第三个或更靠后，就加载更多
                    if (lastVisibleItemPosition >= noteAdapter.getItemCount() - 3) {
                        loadMoreItems();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        // 数据已在ViewModel初始化时加载
    }

    private void loadMoreItems() {
        if (!isLoadingMore) {
            Long lastItemId = noteAdapter.getLastItemId();
            if (lastItemId != null) {
                viewModel.loadMoreNotes(lastItemId);
            }
        }
    }
    
    private void navigateToEditNote(long noteId,String bookName) {
        // 创建NoteEditorFragment并传递笔记ID
        NoteEditorFragment fragment = NoteEditorFragment.newInstance(noteId, 0, bookName);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * 瀑布流布局的间距装饰器
     */
    private static class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        public SpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                  @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.bottom = spacing * 2;
            
            // 如果是顶部的项，添加顶部间距
            if (parent.getChildAdapterPosition(view) < 2) {
                outRect.top = spacing * 2;
            } else {
                outRect.top = 0;
            }
        }
    }
} 