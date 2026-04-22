package com.debuglog.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParsedLog {
    String format;
    String errorType;
    String message;
    String stackTrace;
    String errorHash;
}