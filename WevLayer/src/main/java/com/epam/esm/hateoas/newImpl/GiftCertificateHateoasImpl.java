package com.epam.esm.hateoas.newImpl;

import com.epam.esm.controller.GiftController;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.NotInsertedException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.Hateoas;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateHateoasImpl implements Hateoas<GiftCertificateDTO> {

    @Override
    public void buildDefaultHateoas(RepresentationModel<?> model) throws NoSuchIdException, InvalidInputDataException, NotInsertedException, ServiceException {
        model.add(linkTo(methodOn(GiftController.class).getCertificatesList(1, 10)).withSelfRel().withType("GET"));
        model.add(linkTo(methodOn(GiftController.class).insertCertificate(new GiftCertificateDTO(), null)).withSelfRel().withType("POST"));
    }

    @Override
    public void buildFullHateoas(GiftCertificateDTO giftCertificateDTO) throws InvalidInputDataException, NotInsertedException, NoSuchIdException, ServiceException {
        buildDefaultHateoas(giftCertificateDTO);
        giftCertificateDTO.add(linkTo(methodOn(GiftController.class).getCertificateByID(giftCertificateDTO.getId(), Locale.getDefault())).withSelfRel().withType("GET"));
        giftCertificateDTO.add(linkTo(methodOn(GiftController.class).deleteGiftCertificate(giftCertificateDTO.getId(), Locale.getDefault())).withSelfRel().withType("DELETE"));
        giftCertificateDTO.add(linkTo(methodOn(GiftController.class).updateGiftCertificate(giftCertificateDTO.getId(), new GiftCertificateDTO(), Locale.getDefault())).withSelfRel().withType("PUT"));
    }
}
