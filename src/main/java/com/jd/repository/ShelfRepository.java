package com.jd.repository;

import com.jd.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Integer> {

    // 根据名称查找货架
    Optional<Shelf> findByName(String name);

    // 查找容量大于指定值的货架
    List<Shelf> findByCapacityGreaterThan(Integer capacity);

    // 查找空货架（没有产品的货架）
    List<Shelf> findByProductsIsEmpty();

    // 检查货架名称是否已存在
    boolean existsByName(String name);

    // 自定义查询：查找包含特定产品的货架
    @Query("SELECT s FROM Shelf s JOIN s.products p WHERE p.name = :productName")
    List<Shelf> findShelvesContainingProduct(@Param("productName") String productName);

    // 自定义查询：查找剩余容量大于指定值的货架
    @Query("SELECT s FROM Shelf s WHERE s.capacity - SIZE(s.products) > :remainingCapacity")
    List<Shelf> findShelvesWithRemainingCapacity(@Param("remainingCapacity") Integer remainingCapacity);
}
