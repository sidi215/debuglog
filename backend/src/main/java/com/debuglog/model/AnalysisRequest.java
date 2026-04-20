package com.debuglog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AnalysisRequest {

    @NotBlank(message = "Log content must not be blank")
    @Size(max = 50_000, message = "Log content exceeds maximum allowed size")
    String logContent;
}