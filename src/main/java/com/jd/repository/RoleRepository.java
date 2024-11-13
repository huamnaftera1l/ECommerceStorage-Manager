package com.jd.repository;

import com.jd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据角色名称查找角色
     * @param name 角色名称
     * @return 包含角色的 Optional 对象，如果未找到则为空
     */
    Optional<Role> findByName(String name);

    /**
     * 检查指定名称的角色是否存在
     * @param name 角色名称
     * @return 如果角色存在则返回 true，否则返回 false
     */
    boolean existsByName(String name);
}