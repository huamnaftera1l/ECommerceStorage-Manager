package com.jd.repository;

import com.jd.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限名称查找权限
     * @param name 权限名称
     * @return 包含权限的 Optional 对象，如果未找到则为空
     */
    Optional<Permission> findByName(String name);

    /**
     * 检查指定名称的权限是否存在
     * @param name 权限名称
     * @return 如果权限存在则返回 true，否则返回 false
     */
    boolean existsByName(String name);
}
