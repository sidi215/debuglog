package com.debuglog.parser;

import com.debuglog.model.ParsedLog;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpringBootLogParser implements LogParserStrategy {

    private static final Pattern EXCEPTION = Pattern.compile(
            "([\\w\\.]+Exception)[:\\s]+(.*?)(?=\\n\\s+at |$)", Pattern.DOTALL
    );

    @Override
    public boolean supports(String logContent) {
        return logContent.contains("ERROR") &&
                (logContent.contains("at com.") || logContent.contains("at org.springframework"));
    }

    @Override
    public ParsedLog parse(String logContent) {
        Matcher m = EXCEPTION.matcher(logContent);
        String errorType = "UnknownException";
        String message = "";
        if (m.find()) {
            errorType = m.group(1);
            message = m.group(2).trim();
        }
        return ParsedLog.builder()
                .format("spring-boot")
                .errorType(errorType)
                .message(message)
                .stackTrace(logContent)
                .errorHash(computeHash(errorType, logContent))
                .build();
    }

    private String computeHash(String errorType, String logContent) {
        String[] lines = logContent.split("\n");
        StringBuilder key = new StringBuilder(errorType);
        int count = 0;
        for (String line : lines) {
            if (line.trim().startsWith("at ") && count < 3) {
                key.append(line.trim());
                count++;
            }
        }
        return String.valueOf(key.toString().hashCode());
    }
}