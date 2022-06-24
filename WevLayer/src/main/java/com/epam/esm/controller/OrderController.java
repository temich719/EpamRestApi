package com.epam.esm.controller;

import com.epam.esm.dto.DateAndTimeOrderDTO;
import com.epam.esm.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final static Logger LOGGER = Logger.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<DateAndTimeOrderDTO> getOrderDateAndTimeOfPurchase(@PathVariable long orderId) {
        LOGGER.info("Getting order's date and time of its purchase");
        Link selfLink = linkTo(OrderController.class).withSelfRel();
        Link link = linkTo(methodOn(OrderController.class).getOrderDateAndTimeOfPurchase(orderId)).withSelfRel();
        List<DateAndTimeOrderDTO> dateAndTimeOrderDTOs = new ArrayList<>();
        dateAndTimeOrderDTOs.add(orderService.getOrderDateAndTimeOfPurchase(orderId));
        return CollectionModel.of(dateAndTimeOrderDTOs, selfLink, link);
    }

}
