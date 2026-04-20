package com.debuglog.parser;

import com.debuglog.model.ParsedLog;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NginxLogParser implements LogParserStrategy {

    private static final Pattern NGINX_ERROR = Pattern.compile(
            "\\[error\\].*?:(.*?)(?=,|$)"
    );

    @Override
    public boolean supports(String logContent) {
        return logContent.contains("[error]") && logContent.contains("nginx");
    }

    @Override
    public ParsedLog parse(String logContent) {
        Matcher m = NGINX_ERROR.matcher(logContent);
        String message = m.find() ? m.group(1).trim() : logContent;
        return ParsedLog.builder()
                .format("nginx")
                .errorType("NginxError")
                .message(message)
                .stackTrace(logContent)
                .errorHash(String.valueOf(message.hashCode()))
                .build();
    }
}