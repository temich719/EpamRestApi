package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthUser {

    BigInteger id;
    String name;
    String email;
    String gender;
    String locale;
    LocalDateTime lastVisit;

}
