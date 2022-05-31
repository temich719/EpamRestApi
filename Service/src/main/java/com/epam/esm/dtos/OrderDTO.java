package com.epam.esm.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    UserNameDTO userNameDTO;
    Long[] giftCertificateIds;

}
