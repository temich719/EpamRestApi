package com.epam.esm.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private UserNameDTO userNameDTO;
    private Long[] giftCertificateIds;

}
