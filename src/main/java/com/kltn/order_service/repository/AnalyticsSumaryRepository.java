package com.kltn.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kltn.order_service.model.AnalyticsSumary;

@Repository
public interface AnalyticsSumaryRepository extends JpaRepository<AnalyticsSumary, Long> {
}
