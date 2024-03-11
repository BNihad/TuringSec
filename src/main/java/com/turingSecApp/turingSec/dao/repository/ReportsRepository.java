package com.turingSecApp.turingSec.dao.repository;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.dao.entities.ReportsEntity;
import com.turingSecApp.turingSec.dao.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository

public interface ReportsRepository extends JpaRepository<ReportsEntity, Long> {
    List<ReportsEntity> findByUser(UserEntity user);
    List<ReportsEntity> findByBugBountyProgram(BugBountyProgramEntity program);
    @Query("SELECT r FROM ReportsEntity r WHERE r.bugBountyProgram IN :programs")
    List<ReportsEntity> findByBugBountyProgramIn(Collection<BugBountyProgramEntity> programs);
}
