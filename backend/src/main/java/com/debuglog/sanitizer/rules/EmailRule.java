package com.debuglog.sanitizer.rules;

import com.debuglog.sanitizer.SanitizerRule;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class EmailRule implements SanitizerRule {
    private static final Pattern EMAIL = Pattern.compile(
            "[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}"
    );

    @Override
    public String apply(String input) {
        return EMAIL.matcher(input).replaceAll("[REDACTED-EMAIL]");
    }
}