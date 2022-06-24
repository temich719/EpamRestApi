package com.epam.esm.dto;

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

    private long id;
    private String date;
    private String cost;
    private List<GiftCertificateDTO> giftCertificateDTOs;

}
