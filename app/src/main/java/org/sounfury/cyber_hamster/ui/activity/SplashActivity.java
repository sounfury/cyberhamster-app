package org.sounfury.cyber_hamster.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.sounfury.cyber_hamster.R;
import org.sounfury.cyber_hamster.base.BaseActivity;
import org.sounfury.cyber_hamster.data.repository.UserRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 启动页Activity
 * 显示Logo和加载动画，检查登录状态并跳转到相应页面
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private static final long MIN_DISPLAY_TIME = 1500; // 最小显示时间，单位毫秒
    
    private ImageView ivLogo;
    private TextView tvAppName;
    private ProgressBar progressBar;
    private TextView tvLoading;
    
    private UserRepository userRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        initView();
        initData();
        startAnimations();
        checkLoginStatus();
    }
    
    @Override
    protected void initView() {
        ivLogo = findViewById(R.id.iv_logo);
        tvAppName = findViewById(R.id.tv_app_name);
        progressBar = findViewById(R.id.progress_bar);
        tvLoading = findViewById(R.id.tv_loading);
    }
    
    @Override
    protected void initData() {
        userRepository = UserRepository.getInstance(this);
    }
    
    /**
     * 启动Logo和应用名称的动画效果
     */
    private void startAnimations() {
        // Logo缩放动画
        ivLogo.setScaleX(0.0f);
        ivLogo.setScaleY(0.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.0f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.0f, 1.0f);
        scaleXAnimator.setDuration(800);
        scaleYAnimator.setDuration(800);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimator.start();
        scaleYAnimator.start();
        
        // 应用名称渐入动画
        tvAppName.setAlpha(0.0f);
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(tvAppName, "alpha", 0.0f, 1.0f);
        fadeInAnimator.setDuration(800);
        fadeInAnimator.setStartDelay(400);
        fadeInAnimator.start();
        
        // 加载进度条和文字渐入动画
        progressBar.setAlpha(0.0f);
        tvLoading.setAlpha(0.0f);
        ObjectAnimator progressFadeIn = ObjectAnimator.ofFloat(progressBar, "alpha", 0.0f, 1.0f);
        ObjectAnimator textFadeIn = ObjectAnimator.ofFloat(tvLoading, "alpha", 0.0f, 1.0f);
        progressFadeIn.setDuration(500);
        textFadeIn.setDuration(500);
        progressFadeIn.setStartDelay(800);
        textFadeIn.setStartDelay(800);
        progressFadeIn.start();
        textFadeIn.start();
    }
    
    /**
     * 检查登录状态并决定跳转到哪个页面
     */
    private void checkLoginStatus() {
        // 用来确保至少显示MIN_DISPLAY_TIME的时间
        Observable<Long> timerObservable = Observable.timer(MIN_DISPLAY_TIME, TimeUnit.MILLISECONDS);
        
        // 检查登录状态的Observable
        Observable<Boolean> loginObservable = userRepository.checkTokenAndAutoLogin();
        
        // 合并这两个Observable，只有当两者都完成时才进行页面跳转
        Disposable disposable = Observable.zip(timerObservable, loginObservable, (timer, isLoggedIn) -> isLoggedIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isLoggedIn -> {
                            // 淡出动画
                            startFadeOutAnimation(isLoggedIn);
                        },
                        throwable -> {
                            Log.e(TAG, "检查登录状态出错", throwable);
                            // 出错时默认跳转到登录页
                            startFadeOutAnimation(false);
                        }
                );
        
        compositeDisposable.add(disposable);
    }
    
    /**
     * 开始淡出动画，然后跳转到相应页面
     * @param isLoggedIn 是否已登录
     */
    private void startFadeOutAnimation(boolean isLoggedIn) {
        // 整体淡出动画
        View rootView = findViewById(android.R.id.content);
        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(rootView, "alpha", 1.0f, 0.0f);
        fadeOutAnimator.setDuration(500);
        fadeOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                navigateToNextScreen(isLoggedIn);
            }
        });
        fadeOutAnimator.start();
    }
    
    /**
     * 根据登录状态跳转到相应的页面
     * @param isLoggedIn 是否已登录
     */
    private void navigateToNextScreen(boolean isLoggedIn) {
        Intent intent;
        if (isLoggedIn) {
            // 已登录，跳转到主页
            intent = new Intent(this, MainActivity.class);
        } else {
            // 未登录，跳转到登录/注册页
            intent = new Intent(this, SignActivity.class);
        }
        startActivity(intent);
        
        // 使用淡入淡出效果的转场动画
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        
        // 结束当前Activity
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
} 