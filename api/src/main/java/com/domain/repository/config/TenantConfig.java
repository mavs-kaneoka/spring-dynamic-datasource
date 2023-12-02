package com.domain.repository.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.domain.datasource.DynamicRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;

/**
 * テナントデータベース(tenant)用の設定クラス
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.domain.repository.tenant"},
        entityManagerFactoryRef = "tenantEntityManager",
        transactionManagerRef = "tenantTransactionManager")
public class TenantConfig {
    /** JDBCドライバーURL */
    @Value("${spring.datasource.tenant.url}")
    private String url;

    /** ユーザー名 */
    @Value("${spring.datasource.tenant.username}")
    private String username;

    /** パスワード */
    @Value("${spring.datasource.tenant.password}")
    private String password;

    /** デフォルトのデータソース */
    private HikariDataSource defaultTargetDataSource;

    /** データソース */
    private DynamicRoutingDataSource dataSource;

    /**
     * データソースを生成
     * 
     * @return HikariDataSource
     */
    @Bean(name = "tenant")
    public DynamicRoutingDataSource createDataSource() {
        defaultTargetDataSource = new HikariDataSource();
        defaultTargetDataSource.setJdbcUrl(url);
        defaultTargetDataSource.setUsername(username);
        defaultTargetDataSource.setPassword(password);

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("tenant", defaultTargetDataSource);
        dataSource = new DynamicRoutingDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    /**
     * エンティティマネージャーファクトリを生成
     * 
     * @param dataSource データソース
     * @return EntityManagerFactory
     */
    @Bean(name = "tenantEntityManager")
    public EntityManagerFactory mySqlEntityManagerFactory(
            @Qualifier("tenant") DynamicRoutingDataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.domain.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * トランザクションマネージャーを生成
     * 
     * @param entityManagerFactory エンティティマネージャーファクトリ
     * @return JpaTransactionManager
     */
    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("tenantEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * DIコンテナが破棄される前にデータソースをクローズ
     */
    @PreDestroy
    public void closeDataSource() {
        if (defaultTargetDataSource != null && !defaultTargetDataSource.isClosed()) {
            defaultTargetDataSource.close();
            dataSource.clearDataSources();
        }
    }
}
