package com.domain.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domain.entity.User;

/**
 * ユーザー情報リポジトリ
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
