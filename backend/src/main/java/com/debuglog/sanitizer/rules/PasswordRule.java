package com.debuglog.sanitizer.rules;

import com.debuglog.sanitizer.SanitizerRule;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PasswordRule implements SanitizerRule {
    private static final Pattern PASSWORD = Pattern.compile(
            "(?i)(password|passwd|pwd)\\s*[=:]\\s*\\S+"
    );

    @Override
    public String apply(String input) {
        return PASSWORD.matcher(input).replaceAll("$1=[REDACTED-PASSWORD]");
    }
}