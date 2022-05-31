package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerOrderDTO extends RepresentationModel<AnswerOrderDTO> {

    long id;
    String date;
    String cost;
    List<GiftCertificateDTO> giftCertificateDTOs;

}
