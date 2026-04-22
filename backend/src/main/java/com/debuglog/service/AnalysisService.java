package com.debuglog.service;

import com.debuglog.ai.PromptBuilder;
import com.debuglog.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final SanitizationService sanitizationService;
    private final LogParserService logParserService;
    private final KnowledgeBaseService knowledgeBaseService;
    private final PromptBuilder promptBuilder;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AnalysisResponse analyze(AnalysisRequest request) {
        String sanitized = sanitizationService.sanitize(request.getLogContent());
        ParsedLog parsed = logParserService.parse(sanitized);

        Optional<Resolution> kbMatch = knowledgeBaseService.findByHash(parsed.getErrorHash());

        String prompt = promptBuilder.build(parsed, kbMatch.orElse(null));
        String raw = chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("```", "")
                .trim();

        String rootCause = parsed.getMessage();
        String solution = raw;
        String severity = detectSeverity(parsed.getErrorType());

        try {
            JsonNode node = objectMapper.readTree(raw);
            if (node.has("rootCause")) rootCause = node.get("rootCause").asText();
            if (node.has("solution"))  solution  = node.get("solution").asText();
            if (node.has("severity"))  severity  = node.get("severity").asText();
        } catch (Exception ignored) {}

        Resolution saved = knowledgeBaseService.save(
            parsed.getErrorHash(),
            parsed.getErrorType(),
            parsed.getStackTrace(),
            solution,
            false
        );

        return AnalysisResponse.builder()
            .format(parsed.getFormat())
            .errorType(parsed.getErrorType())
            .rootCause(rootCause)
            .solution(solution)
            .severity(severity)
            .fromKnowledgeBase(kbMatch.isPresent())
            .sessionId(saved.getId() + "-" + UUID.randomUUID().toString().substring(0, 8))
            .build();
    }

    private String detectSeverity(String errorType) {
        if (errorType.contains("OutOfMemory") || errorType.contains("StackOverflow")) return "CRITICAL";
        if (errorType.contains("NullPointer") || errorType.contains("IllegalState")) return "HIGH";
        return "MEDIUM";
    }
}