package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReportsRepository extends JpaRepository<ReportsEntity, Long> {

}
