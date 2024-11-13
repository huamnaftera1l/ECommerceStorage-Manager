package com.jd.repository;

import com.jd.model.StockTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTaskRepository extends JpaRepository<StockTask, Integer> {

}