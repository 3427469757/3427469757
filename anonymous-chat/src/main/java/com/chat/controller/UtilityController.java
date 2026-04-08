package com.chat.controller;

import com.chat.dto.ApiResponse;
import com.chat.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for utility endpoints
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UtilityController {

    /**
     * Generate a new unique user ID for anonymous users
     * This endpoint should be called when a user first visits the site
     * @return A unique user identifier
     */
    @GetMapping("/generate-user-id")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateUserId() {
        String userId = IdGenerator.generateUserId();
        
        Map<String, String> result = new HashMap<>();
        result.put("userId", userId);
        result.put("message", "Save this ID to maintain your anonymous identity");

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Service is running"));
    }
}
