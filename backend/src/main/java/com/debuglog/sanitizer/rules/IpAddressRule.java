package com.debuglog.sanitizer.rules;

import com.debuglog.sanitizer.SanitizerRule;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class IpAddressRule implements SanitizerRule {
    private static final Pattern IP = Pattern.compile(
            "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b"
    );

    @Override
    public String apply(String input) {
        return IP.matcher(input).replaceAll("[REDACTED-IP]");
    }
}