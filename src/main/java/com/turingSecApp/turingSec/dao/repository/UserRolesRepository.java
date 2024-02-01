package com.turingSecApp.turingSec.dao.repository;


import com.turingSecApp.turingSec.dao.entities.role.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

}
