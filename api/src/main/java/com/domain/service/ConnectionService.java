package com.domain.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.domain.entity.Connection;
import com.domain.repository.system.ConnectionRepository;

/**
 * データベース接続情報サービス
 */
@Service
public class ConnectionService {
    /** データベース接続情報リポジトリ */
    @Autowired
    ConnectionRepository connectionRepository;

    @Transactional(transactionManager = "systemTransactionManager")
    public Connection findByTenantId(String tenantId) {
        List<Connection> connectionList = connectionRepository.findByTenantId(tenantId);
        return connectionList.get(0);
    }
}
