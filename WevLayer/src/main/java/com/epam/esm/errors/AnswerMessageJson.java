package com.epam.esm.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Component
public class AnswerMessageJson extends RepresentationModel<AnswerMessageJson> {
    String status;
    String message;
    String code;
}
