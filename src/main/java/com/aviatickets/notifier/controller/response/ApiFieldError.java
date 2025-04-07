package com.aviatickets.notifier.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiFieldError {
    private String objectName;
    private String field;
    private String code;
    private Object rejectedValue;
    private String message;
    private String error;
}