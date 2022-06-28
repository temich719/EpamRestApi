package com.epam.esm.controller;

import com.epam.esm.dto.SecurityUserDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.exception.InvalidPasswordException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.security.jwtProvider.JwtProvider;
import com.epam.esm.security.model.AuthenticationRequest;
import com.epam.esm.security.model.AuthenticationResponse;
import com.epam.esm.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController extends AbstractController{

    private static final String NEW_USER_CODE = "91";
    private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationController(AnswerMessageJson answerMessageJson, UserService userService, JwtProvider jwtProvider) {
        super(answerMessageJson);
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AnswerMessageJson registerNewUser(@RequestBody SecurityUserDTO securityUserDTO) throws ServiceException {
        LOGGER.info("Registration...");
        String password = securityUserDTO.getPassword();
        securityUserDTO.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userService.saveUser(securityUserDTO);
        answerMessageJson.setMessage("New user has been created!");
        answerMessageJson.setStatus(HttpStatus.ACCEPTED.toString());
        answerMessageJson.setCode(HttpStatus.ACCEPTED.value() + NEW_USER_CODE);
        return answerMessageJson;
    }

    @PostMapping(value = "/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthenticationResponse sighIn(@RequestBody AuthenticationRequest authenticationRequest) throws ServiceException, InvalidPasswordException {
        LOGGER.info("Login...");
        UserDTO userDTO = userService.getUserByName(authenticationRequest.getLogin());
        String token;
        if (BCrypt.checkpw(authenticationRequest.getPassword(), userDTO.getPassword())){
            token = jwtProvider.generateToken(authenticationRequest.getLogin());
        } else {
            throw new InvalidPasswordException("Wrong password");
        }
        return new AuthenticationResponse(token);
    }

}
