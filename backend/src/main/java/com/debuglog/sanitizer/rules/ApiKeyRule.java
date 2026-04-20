package com.debuglog.sanitizer.rules;

import com.debuglog.sanitizer.SanitizerRule;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class ApiKeyRule implements SanitizerRule {
    private static final Pattern API_KEY = Pattern.compile(
            "(?i)(api[_-]?key|token|secret|bearer)\\s*[=:]\\s*[\\w\\-\\.]{8,}"
    );

    @Override
    public String apply(String input) {
        return API_KEY.matcher(input).replaceAll("$1=[REDACTED-KEY]");
    }
}