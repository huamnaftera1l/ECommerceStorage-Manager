package com.jd.repository;


import com.jd.model.Product;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 根据名称查找产品
    Optional<Product> findByName(String name);

    // 查找特定货架上的所有产品
    List<Product> findByShelfId(Integer shelfId);

    // 查找库存低于指定数量的产品
    List<Product> findByQuantityLessThan(Integer quantity);

    // 根据价格范围查找产品
    List<Product> findByPriceBetween(int minPrice, int maxPrice);

    @EntityGraph(attributePaths = {"shelf"})
    Optional<Product> findById(Integer id);

    // 检查产品名称是否已存在
    boolean existsByName(String name);

    // 自定义查询：查找最近添加的 n 个产品
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(SpringDataWebProperties.Pageable pageable);

    // 自定义更新查询：更新产品库存
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity + :amount WHERE p.id = :id")
    int updateProductStock(@Param("id") Integer id, @Param("amount") int amount);
}
