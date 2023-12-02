package com.domain.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.domain.entity.User;
import com.domain.repository.tenant.UserRepository;

/**
 * ユーザー情報サービス
 */
@Service
public class UserService {
    /** ユーザー情報リポジトリ */
    @Autowired
    UserRepository userRepository;

    @Transactional(transactionManager = "tenantTransactionManager")
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
