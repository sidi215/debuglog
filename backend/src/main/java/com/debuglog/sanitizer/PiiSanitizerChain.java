package com.debuglog.sanitizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PiiSanitizerChain {

    private final List<SanitizerRule> rules;

    public String sanitize(String input) {
        String result = input;
        for (SanitizerRule rule : rules) {
            result = rule.apply(result);
        }
        return result;
    }
}