package com.debuglog.model;

import lombok.Builder;
import lombok.Data;

@Data
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