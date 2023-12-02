package com.domain.repository.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;

/**
 * システム共通データベース(system)用の設定クラス
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.domain.repository.system"},
        entityManagerFactoryRef = "systemEntityManager",
        transactionManagerRef = "systemTransactionManager")
public class SystemConfig {
    /** JDBCドライバーURL */
    @Value("${spring.datasource.system.url}")
    private String url;

    /** ユーザー名 */
    @Value("${spring.datasource.system.username}")
    private String username;

    /** パスワード */
    @Value("${spring.datasource.system.password}")
    private String password;

    /** データソース */
    private HikariDataSource dataSource;

    /**
     * データソースを生成
     * 
     * @return HikariDataSource
     */
    @Primary
    @Bean(name = "system")
    public HikariDataSource createDataSource() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * エンティティマネージャーファクトリを生成
     * 
     * @param dataSource データソース
     * @return EntityManagerFactory
     */
    @Primary
    @Bean(name = "systemEntityManager")
    public EntityManagerFactory mySqlEntityManagerFactory(
            @Qualifier("system") HikariDataSource dataSource) {

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
    @Primary
    @Bean(name = "systemTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("systemEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * DIコンテナが破棄される前にデータソースをクローズ
     */
    @PreDestroy
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
