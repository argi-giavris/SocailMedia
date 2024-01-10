package org.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig {
    private String secretKey;
    private int expiresInMinutes;

    // Standard getters and setters
}


