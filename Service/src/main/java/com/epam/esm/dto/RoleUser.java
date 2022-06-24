package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUser {

    private String userName;
    private List<AnswerOrderDTO> orderDTOs;
    private String password;
    private String role;

}
