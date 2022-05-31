package com.epam.esm.controller;

import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.exception.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(ExceptionController.class);

    private static final String SOMETHING_WENT_WRONG = "Something went wrong!";
    private static final String SOMETHING_WENT_WRONG_CODE = "21";
    private static final String NO_SUCH_ID_CODE = "22";
    private static final String NOT_INSERTED_CODE = "23";
    private static final String INVALID_INPUT_DATE_CODE = "25";
    private static final String SERVICE_EXCEPTION_CODE = "26";

    private static final HttpStatus httpStatusNotFound = HttpStatus.NOT_FOUND;
    private static final HttpStatus httpStatusInternalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    @Autowired
    public ExceptionController(AnswerMessageJson answerMessageJson) {
        super(answerMessageJson);
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleServiceException(ServiceException e) {
        LOGGER.error("Handle ServiceException");
        answerMessageJson.setMessage(e.getMessage());
        answerMessageJson.setStatus(httpStatusInternalServerError.toString());
        answerMessageJson.setCode(httpStatusInternalServerError.value() + SERVICE_EXCEPTION_CODE);
        return answerMessageJson;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleNotFoundStatusError() {
        LOGGER.error("Handle Throwable");
        answerMessageJson.setMessage(SOMETHING_WENT_WRONG);
        answerMessageJson.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        answerMessageJson.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + SOMETHING_WENT_WRONG_CODE);
        return answerMessageJson;
    }

    @ExceptionHandler(NoSuchIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AnswerMessageJson handleNoSuchIdException(NoSuchIdException e) {
        LOGGER.error("Handle NoSuchIdException");
        answerMessageJson.setMessage(e.getLocalizedMessage());
        answerMessageJson.setStatus(httpStatusNotFound.toString());
        answerMessageJson.setCode(httpStatusNotFound.value() + NO_SUCH_ID_CODE);
        return answerMessageJson;
    }

    @ExceptionHandler(NotInsertedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleNotInsertedException(NotInsertedException e) {
        LOGGER.error("Handle NotInsertedException");
        answerMessageJson.setMessage(e.getMessage());
        answerMessageJson.setStatus(httpStatusInternalServerError.toString());
        answerMessageJson.setCode(httpStatusInternalServerError.value() + NOT_INSERTED_CODE);
        return answerMessageJson;
    }

    @ExceptionHandler(InvalidInputDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleInvalidInputDataException(InvalidInputDataException e) {
        LOGGER.error("Handle InvalidInputDataException");
        answerMessageJson.setMessage(e.getMessage());
        answerMessageJson.setStatus(httpStatusInternalServerError.toString());
        answerMessageJson.setCode(httpStatusInternalServerError.value() + INVALID_INPUT_DATE_CODE);
        return answerMessageJson;
    }
}
