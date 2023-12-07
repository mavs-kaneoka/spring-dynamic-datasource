package com.domain.datasource;

import java.util.HashMap;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * データソースを動的に切り替えるためのクラス
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    /** データソースのキー用のThreadLocal */
    private static final ThreadLocal<Object> currentLookupKey = new ThreadLocal<>();

    /**
     * データソース動的切り替えメソッド
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // ThreadLocalから現在のキーを取得
        return currentLookupKey.get();
    }

    /**
     * 現在のスレッドに関連付けられたデータソースのキーを設定
     */
    public static void setCurrentLookupKey(Object lookupKey) {
        currentLookupKey.set(lookupKey);
    }

    /**
     * 現在のスレッドに関連付けられたデータソースのキーをクリア
     */
    public static void clearCurrentLookupKey() {
        currentLookupKey.remove();
        currentLookupKey.set(null);
    }

    /**
     * データソースをクリア
     */
    public void clearDataSources() {
        synchronized (this) {
            setTargetDataSources(new HashMap<>());
            super.afterPropertiesSet();
        }
    }
}
