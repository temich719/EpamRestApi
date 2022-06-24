package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    private static final String PURCHASED_CODE = "31";

    private final static Logger LOGGER = Logger.getLogger(UserController.class);
    private static final Link selfLink = linkTo(UserController.class).withSelfRel();

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, AnswerMessageJson answerMessageJson) {
        super(answerMessageJson);
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerMessageJson makeOrder(@RequestBody SecurityOrderDTO securityOrderDTO) {
        LOGGER.info("Making order");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setGiftCertificateIds(securityOrderDTO.getGiftCertificateIds());
        orderDTO.setUserNameDTO(new UserNameDTO(userName));
        userService.makeOrder(orderDTO);
        HttpStatus httpStatus = HttpStatus.CREATED;
        answerMessageJson.setMessage("Order has been purchased!");
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setCode(httpStatus.value() + PURCHASED_CODE);
        LOGGER.info("Order has been purchased");
        answerMessageJson.add(selfLink);
        return answerMessageJson;
    }

    @GetMapping(value = "/orders/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<AnswerOrderDTO> getUsersOrders(@PathVariable String userName) throws ServiceException {
        LOGGER.info("Get user's orders");
        return CollectionModel.of(userService.getUsersOrders(userName), selfLink);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDTO> getUserById(@PathVariable long id, Locale locale) throws NoSuchIdException {
        LOGGER.info("Getting user by id");
        List<UserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(userService.getUserById(id, locale));
        return CollectionModel.of(userDTOs, selfLink);
    }

    @GetMapping(value = "/maxCostWithMostWidelyTags", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserHighestOrdersCostDTO> getHighestOrdersCostUsersWithMostWidelyUsedTags(@RequestParam(defaultValue = "1") int page,
                                                                                                     @RequestParam(defaultValue = "10") int size, Locale locale) {
        LOGGER.info("Getting the highest orders cost of user with the most widely used tags");
        Link link = linkTo(methodOn(UserController.class).getHighestOrdersCostUsersWithMostWidelyUsedTags(page, size, locale)).withRel("getHighestOrdersCostUsersWithMostWidelyUsedTags");
        return CollectionModel.of(userService.getUserWithHighestOrdersCostWithMostWidelyUsedTag(page, size, locale), selfLink, link);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDTO> getUsers(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Getting users");
        Link link = linkTo(methodOn(UserController.class).getUsers(page, size)).withRel("users");
        return CollectionModel.of(userService.getUsers(page, size), selfLink, link);
    }

}
