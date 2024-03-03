package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findByEmail(String email);


}
