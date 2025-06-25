package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;

@Repository
@EnableJpaRepositories
public interface RoleRepo extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
