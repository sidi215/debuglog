package com.debuglog.service;

import com.debuglog.ai.PromptBuilder;
import com.debuglog.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.prompt.PromptTemplate;
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
    private final OllamaChatClient ollamaChatClient;

    public AnalysisResponse analyze(AnalysisRequest request) {
        String sanitized = sanitizationService.sanitize(request.getLogContent());
        ParsedLog parsed = logParserService.parse(sanitized);

        Optional<Resolution> kbMatch = knowledgeBaseService.findByHash(parsed.getErrorHash());

        String prompt = promptBuilder.build(parsed, kbMatch.orElse(null));
        String aiResponse = ollamaChatClient.call(prompt);

        Resolution saved = knowledgeBaseService.save(
                parsed.getErrorHash(),
                parsed.getErrorType(),
                parsed.getStackTrace(),
                aiResponse,
                false
        );

        return AnalysisResponse.builder()
                .format(parsed.getFormat())
                .errorType(parsed.getErrorType())
                .rootCause(parsed.getMessage())
                .solution(aiResponse)
                .severity(detectSeverity(parsed.getErrorType()))
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