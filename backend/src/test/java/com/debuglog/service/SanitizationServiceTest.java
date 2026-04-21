package com.debuglog.service;

import com.debuglog.sanitizer.PiiSanitizerChain;
import com.debuglog.sanitizer.rules.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class SanitizationServiceTest {

    private SanitizationService service;

    @BeforeEach
    void setUp() {
        PiiSanitizerChain chain = new PiiSanitizerChain(List.of(
                new IpAddressRule(), new EmailRule(),
                new ApiKeyRule(), new PasswordRule()
        ));
        service = new SanitizationService(chain);
    }

    @Test
    void sanitizesIpAddress() {
        assertThat(service.sanitize("Connection from 192.168.1.1 failed"))
                .contains("[REDACTED-IP]").doesNotContain("192.168.1.1");
    }

    @Test
    void sanitizesEmail() {
        assertThat(service.sanitize("User admin@company.com not found"))
                .contains("[REDACTED-EMAIL]").doesNotContain("admin@company.com");
    }

    @Test
    void sanitizesApiKey() {
        assertThat(service.sanitize("api_key=sk-abcdef1234567890"))
                .contains("[REDACTED-KEY]");
    }

    @Test
    void sanitizesPassword() {
        assertThat(service.sanitize("password=mySecret123"))
                .contains("[REDACTED-PASSWORD]");
    }
}