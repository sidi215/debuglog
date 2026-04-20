package com.debuglog.parser;

import com.debuglog.model.ParsedLog;

public interface LogParserStrategy {
    boolean supports(String logContent);
    ParsedLog parse(String logContent);
}