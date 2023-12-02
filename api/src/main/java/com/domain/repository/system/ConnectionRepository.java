package com.domain.repository.system;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.domain.entity.Connection;

/**
 * データベース接続情報リポジトリ
 */
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    List<Connection> findByTenantId(String tenantId);
}
