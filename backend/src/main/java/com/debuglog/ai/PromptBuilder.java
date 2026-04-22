package com.debuglog.ai;

import com.debuglog.model.ParsedLog;
import com.debuglog.model.Resolution;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String build(ParsedLog log, Resolution kbMatch) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an expert DevSecOps engineer.\n");
        prompt.append("Respond ONLY with raw JSON. No markdown, no backticks, no explanation.\n");
        prompt.append("Exact format: {\"rootCause\": \"...\", \"solution\": \"...\", \"severity\": \"LOW|MEDIUM|HIGH|CRITICAL\"}\n\n");
        prompt.append("Format: ").append(log.getFormat()).append("\n");
        prompt.append("Error type: ").append(log.getErrorType()).append("\n");
        prompt.append("Stack trace:\n").append(log.getStackTrace()).append("\n");

        if (kbMatch != null) {
            prompt.append("\nPreviously confirmed fix for similar error:\n");
            prompt.append(kbMatch.getSolution()).append("\n");
            prompt.append("Validate or improve this solution.\n");
        }

        return prompt.toString();
    }
}