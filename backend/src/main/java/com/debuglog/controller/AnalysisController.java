package com.debuglog.controller;

import com.debuglog.model.AnalysisRequest;
import com.debuglog.model.AnalysisResponse;
import com.debuglog.service.AnalysisService;
import com.debuglog.service.KnowledgeBaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;
    private final KnowledgeBaseService knowledgeBaseService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@Valid @RequestBody AnalysisRequest request) {
        return ResponseEntity.ok(analysisService.analyze(request));
    }

    @PostMapping("/confirm/{sessionId}")
    public ResponseEntity<Map<String, String>> confirm(@PathVariable String sessionId) {
        Long id = Long.parseLong(sessionId.split("-")[0]);
        knowledgeBaseService.confirm(id);
        return ResponseEntity.ok(Map.of("status", "confirmed"));
    }
}