package com.debuglog.parser;

import com.debuglog.model.ParsedLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonLogParser implements LogParserStrategy {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String logContent) {
        String trimmed = logContent.trim();
        return trimmed.startsWith("{") || trimmed.startsWith("[");
    }

    @Override
    public ParsedLog parse(String logContent) {
        try {
            JsonNode root = objectMapper.readTree(logContent);
            String errorType = root.path("exception").asText("UnknownException");
            String message = root.path("message").asText("");
            return ParsedLog.builder()
                    .format("json")
                    .errorType(errorType)
                    .message(message)
                    .stackTrace(logContent)
                    .errorHash(String.valueOf((errorType + message).hashCode()))
                    .build();
        } catch (Exception e) {
            return GenericStackTraceParser.buildGeneric(logContent);
        }
    }
}