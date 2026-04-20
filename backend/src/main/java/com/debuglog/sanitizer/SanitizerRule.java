package com.debuglog.sanitizer;

public interface SanitizerRule {
    String apply(String input);
}