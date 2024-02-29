package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramsRepository extends JpaRepository<BugBountyProgramEntity,Long> {

}
