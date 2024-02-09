package com.turingSecApp.turingSec.dao.repository;


import com.turingSecApp.turingSec.dao.entities.HackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackerRepository extends JpaRepository<HackerEntity, Long> {


}
