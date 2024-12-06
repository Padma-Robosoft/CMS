package com.example.demo.util;

import com.example.demo.dto.APIResponseDTO;

public class APIResponseUtil {
    public static <T> APIResponseDTO<T> createSuccessResponse(T data, String message) {
        return new APIResponseDTO<>(200, 0, message, data);
    }

    public static <T> APIResponseDTO<T> createFailureResponse(String message) {
        return new APIResponseDTO<>(500, 1000, message, null);
    }
}
