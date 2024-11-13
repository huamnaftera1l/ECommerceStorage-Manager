package com.jd.repository;

import com.jd.model.UnstockTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnstockTaskRepository extends JpaRepository<UnstockTask, Integer> {

}