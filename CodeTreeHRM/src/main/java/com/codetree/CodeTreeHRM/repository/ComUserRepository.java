package com.codetree.CodeTreeHRM.repository;

import com.codetree.CodeTreeHRM.entity.ComUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComUserRepository extends JpaRepository<ComUser, Long> {
    Optional<ComUser> findByUserId(String userId);
}