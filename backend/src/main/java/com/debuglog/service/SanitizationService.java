package com.debuglog.service;

import com.debuglog.sanitizer.PiiSanitizerChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SanitizationService {

    private final PiiSanitizerChain chain;

    public String sanitize(String input) {
        return chain.sanitize(input);
    }
}