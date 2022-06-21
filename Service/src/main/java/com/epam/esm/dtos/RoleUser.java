package com.epam.esm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUser {

    String userName;
    List<AnswerOrderDTO> orderDTOs;
    String password;
    String role;

}
