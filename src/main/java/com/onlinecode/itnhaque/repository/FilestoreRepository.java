package com.onlinecode.itnhaque.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.onlinecode.itnhaque.domain.Filestore;

@Repository
public interface FilestoreRepository extends JpaRepository<Filestore, Integer>,
        JpaSpecificationExecutor<Filestore> {
    Optional<Filestore> findByName(String name);
}
