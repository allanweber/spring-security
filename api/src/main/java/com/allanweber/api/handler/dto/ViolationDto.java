package com.allanweber.api.handler.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViolationDto {

    private final String fieldName;

    private final String message;
}
