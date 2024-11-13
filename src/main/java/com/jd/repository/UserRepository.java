package com.jd.repository;

import com.jd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 包含用户的 Optional 对象，如果未找到则为空
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 包含用户的 Optional 对象，如果未找到则为空
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 如果用户名已存在则返回 true，否则返回 false
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱地址
     * @return 如果邮箱已存在则返回 true，否则返回 false
     */
    boolean existsByEmail(String email);
}