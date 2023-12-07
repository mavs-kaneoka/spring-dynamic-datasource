package com.mavs.common.interceptor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.domain.datasource.DynamicRoutingDataSource;
import com.domain.entity.Connection;
import com.domain.service.ConnectionService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * データソース用の共通処理クラス
 */
@Component
public class DataSourceInterceptor implements HandlerInterceptor {
    /** テナントデータベース用の動的データソース */
    @Autowired
    @Qualifier("tenant")
    DynamicRoutingDataSource dynamicRoutingDataSource;

    /** データベース接続情報サービス */
    @Autowired
    private ConnectionService connectionService;

    /** データソース */
    private HikariDataSource dataSource = null;

    /**
     * コントローラー前処理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        // HTTPヘッダーからテナントIDを取得
        String tenantId = request.getHeader("X-Mavs-TenantId");

        // テナントIDをキーにデータベース接続情報を取得
        Connection connection = connectionService.findByTenantId(tenantId);
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 該当のデータソースを設定
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl(connection.getDatabase());
        this.dataSource.setUsername(connection.getUsername());
        this.dataSource.setPassword(connection.getPassword());

        targetDataSources.put("tenant", this.dataSource);
        dynamicRoutingDataSource.setTargetDataSources(targetDataSources);
        dynamicRoutingDataSource.setDefaultTargetDataSource(this.dataSource);
        dynamicRoutingDataSource.afterPropertiesSet();

        DynamicRoutingDataSource.setCurrentLookupKey(tenantId);
        return true;
    }

    /**
     * コントローラー後処理
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, @Nullable Exception ex) throws Exception {
        // 該当のデータソースをクローズ
        this.dataSource.close();
        dynamicRoutingDataSource.clearDataSources();
    }
}
