package com.epam.esm.hateoas.newImpl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDTO;
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
public class TagHateoasImpl implements Hateoas<TagDTO> {

    @Override
    public void buildDefaultHateoas(RepresentationModel<?> model) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        model.add(linkTo(methodOn(TagController.class).getTagList(1, 10)).withSelfRel().withType("GET"));
        model.add(linkTo(methodOn(TagController.class).createTag(new TagDTO(), null, Locale.getDefault())).withSelfRel().withType("POST"));
    }

    @Override
    public void buildFullHateoas(TagDTO tagDTO) throws InvalidInputDataException, NoSuchIdException, ServiceException, NotInsertedException {
        buildDefaultHateoas(tagDTO);
        tagDTO.add(linkTo(methodOn(TagController.class).getTagById(tagDTO.getId(), Locale.getDefault())).withSelfRel().withType("GET"));
        tagDTO.add(linkTo(methodOn(TagController.class).deleteTag(tagDTO.getId(), Locale.getDefault())).withSelfRel().withType("DELETE"));
    }
}
