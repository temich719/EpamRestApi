package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO extends RepresentationModel<TagDTO> {
    long id;

    @NotNull(message = "{com.epam.esm.constraint.tagIsNull}")
    @NotEmpty(message = "{com.epam.esm.constraint.tagIsEmpty}")
    @Size(min = 2, max = 50, message = "{com.epam.esm.constraint.tagIsSize}")
    String name;
}
