package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthUser {

    private BigInteger id;
    private String name;
    private String email;
    private String gender;
    private String locale;
    private LocalDateTime lastVisit;

}
