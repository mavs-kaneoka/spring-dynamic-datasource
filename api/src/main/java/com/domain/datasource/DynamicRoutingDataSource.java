package com.domain.datasource;

import java.util.HashMap;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * データソースを動的に切り替えるためのクラス
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    /**
     * データソースをクリア
     */
    public void clearDataSources() {
        synchronized (this) {
            setTargetDataSources(new HashMap<>());
            super.afterPropertiesSet();
        }
    }

    /**
     * データソース動的切り替えメソッド
     * 
     * 今回は使用していないが、オーバーライドしないとエラーになるため、空Objectを返却している
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return new Object();
    }
}
