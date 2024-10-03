package com.pcn.exoplanets.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String name;
    private String email;
    private String imageUrl;

    private String provider;
    private String providerId;
}
