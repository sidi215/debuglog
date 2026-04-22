package com.debuglog.service;

import com.debuglog.model.Resolution;
import com.debuglog.repository.ResolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KnowledgeBaseService { 

    private final ResolutionRepository repository;

    public Optional<Resolution> findByHash(String errorHash) {
        return repository.findTopByErrorHashAndConfirmedTrue(errorHash)
            .map(r -> {
                r.setUsedCount(r.getUsedCount() + 1);
                return repository.save(r);
            });
    }

    public Resolution save(String errorHash, String errorType,
                           String pattern, String solution, boolean confirmed) {
        return repository.save(Resolution.builder()
            .errorHash(errorHash)
            .errorType(errorType)
            .pattern(pattern)
            .solution(solution)
            .confirmed(confirmed)
            .usedCount(0)
            .build());
    }

    public void confirm(Integer id) {
        repository.findById(id).ifPresent(r -> {
            r.setConfirmed(true);
            repository.save(r);
        });
    }
}