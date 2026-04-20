package com.debuglog.repository;

import com.debuglog.model.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
    Optional<Resolution> findTopByErrorHashAndConfirmedTrue(String errorHash);
}