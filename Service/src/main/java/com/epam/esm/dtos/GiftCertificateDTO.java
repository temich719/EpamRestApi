package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {

    long id;

    @NotEmpty(message = "{com.epam.esm.constraint.name.message}")
    @NotNull(message = "{com.epam.esm.constraint.name.message}")
    @Size(min = 2, max = 50, message = "{com.epam.esm.constraint.size}")
    String name;

    @NotEmpty(message = "{com.epam.esm.constraint.descriptionIsEmpty}")
    @NotNull(message = "{com.epam.esm.constraint.descriptionIsNull}")
    @Size(min = 1, max = 100, message = "{com.epam.esm.constraint.descriptionSize}")
    String description;

    @NotEmpty(message = "{com.epam.esm.constraint.priceIsEmpty}")
    @NotNull(message = "{com.epam.esm.constraint.priceIsNull}")
    @Size(min = 1, max = 20, message = "{com.epam.esm.constraint.priceSize}")
    String price;

    @NotEmpty(message = "{com.epam.esm.constraint.durationIsEmpty}")
    @NotNull(message = "{com.epam.esm.constraint.durationIsNull}")
    @Size(min = 1, max = 30, message = "{com.epam.esm.constraint.durationSize}")
    @Pattern(regexp = "\\d+ days", message = "{com.epam.esm.constraint.durationPattern}")
    String duration;

    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)", message = "{com.epam.esm.constraint.datePattern}")
    String createDate;

    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)", message = "{com.epam.esm.constraint.datePattern}")
    String lastUpdateDate;

    @NotEmpty(message = "{com.epam.esm.constraint.tags}")
    @NotNull(message = "{com.epam.esm.constraint.tagsIsNull}")
    TagDTO[] tags;

}
