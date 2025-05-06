package org.sounfury.cyber_hamster.base;

import android.app.Application;

import com.jeremyliao.liveeventbus.LiveEventBus;

public class CyberHamsterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化LiveEventBus
        LiveEventBus.config()
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false);
    }
} 