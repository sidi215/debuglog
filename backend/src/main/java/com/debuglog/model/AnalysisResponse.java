package com.debuglog.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalysisResponse {
    String format;
    String errorType;
    String rootCause;
    String solution;
    String severity;
    boolean fromKnowledgeBase;
    String sessionId;
}