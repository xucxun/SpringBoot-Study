package com.example.repository;

import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户信息接口，通过用户名查询用户信息
 * @author: xcx
 * @since: 2021/6/16
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
