package com.debuglog.parser;

import com.debuglog.model.ParsedLog;
import org.springframework.stereotype.Component;

@Component
public class GenericStackTraceParser implements LogParserStrategy {

    @Override
    public boolean supports(String logContent) {
        return true;
    }

    @Override
    public ParsedLog parse(String logContent) {
        return buildGeneric(logContent);
    }

    static ParsedLog buildGeneric(String logContent) {
        return ParsedLog.builder()
                .format("generic")
                .errorType("UnknownError")
                .message(logContent.lines().findFirst().orElse(""))
                .stackTrace(logContent)
                .errorHash(String.valueOf(logContent.hashCode()))
                .build();
    }
}