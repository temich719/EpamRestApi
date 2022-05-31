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
public class UserHighestOrdersCostDTO extends RepresentationModel<UserHighestOrdersCostDTO> {

    long id;
    String userName;
    List<TagDTO> tagDTOs;
    int sum;

}
