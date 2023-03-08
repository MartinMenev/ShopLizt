package com.example.myshoppingapp.repository;

import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.roles.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findRoleEntityByRole(UserRole role);
}