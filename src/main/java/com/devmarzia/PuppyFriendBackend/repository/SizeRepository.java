package com.devmarzia.PuppyFriendBackend.repository;

import com.devmarzia.PuppyFriendBackend.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findByLabel(String label);
}
