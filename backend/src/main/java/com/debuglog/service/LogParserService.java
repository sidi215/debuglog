package com.debuglog.service;

import com.debuglog.exception.LogParsingException;
import com.debuglog.model.ParsedLog;
import com.debuglog.parser.LogParserStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogParserService {

    private final List<LogParserStrategy> parsers;

    public ParsedLog parse(String logContent) {
        return parsers.stream()
                .filter(p -> p.supports(logContent))
                .findFirst()
                .map(p -> p.parse(logContent))
                .orElseThrow(() -> new LogParsingException("No parser found for log content"));
    }
}