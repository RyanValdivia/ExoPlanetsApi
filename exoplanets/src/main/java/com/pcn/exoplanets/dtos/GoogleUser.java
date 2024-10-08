package com.pcn.exoplanets.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUser {
    @JsonProperty ("sub")
    private String id;

    @JsonProperty ("email")
    private String email;

    @JsonProperty ("name")
    private String name;

    @JsonProperty ("picture")
    private String picture;
}
