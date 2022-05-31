package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateAndTimeOrderDTO extends RepresentationModel<DateAndTimeOrderDTO> {
    String date;
    String cost;
}
