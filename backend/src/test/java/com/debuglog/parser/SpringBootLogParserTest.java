package com.debuglog.parser;

import com.debuglog.model.ParsedLog;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SpringBootLogParserTest {

    private final SpringBootLogParser parser = new SpringBootLogParser();

    private static final String SAMPLE = """
        2024-01-15 ERROR c.a.UserService - Failed to process
        java.lang.NullPointerException: Cannot invoke method getUser()
            at com.aegislog.service.UserService.getUser(UserService.java:42)
            at com.aegislog.controller.UserController.get(UserController.java:21)
        """;

    @Test
    void supportsSpringBootLog() {
        assertThat(parser.supports(SAMPLE)).isTrue();
    }

    @Test
    void parsesErrorType() {
        ParsedLog result = parser.parse(SAMPLE);
        assertThat(result.getErrorType()).isEqualTo("java.lang.NullPointerException");
    }

    @Test
    void setsFormatCorrectly() {
        assertThat(parser.parse(SAMPLE).getFormat()).isEqualTo("spring-boot");
    }

    @Test
    void generatesConsistentHash() {
        assertThat(parser.parse(SAMPLE).getErrorHash())
                .isEqualTo(parser.parse(SAMPLE).getErrorHash());
    }
}