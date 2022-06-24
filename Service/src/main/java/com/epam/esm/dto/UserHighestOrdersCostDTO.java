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
public class UserHighestOrdersCostDTO extends RepresentationModel<UserHighestOrdersCostDTO> {

    private long id;
    private String userName;
    private List<TagDTO> tagDTOs;
    private int sum;

}
