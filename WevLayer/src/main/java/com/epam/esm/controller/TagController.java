package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.exception.*;
import com.epam.esm.hateoas.Hateoas;
import com.epam.esm.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/tags")
public class TagController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(TagController.class);

    private static final String TAG_HAS_BEEN_CREATED = "New tag has been created!";
    private static final String DELETED = "Deleted successfully";
    private static final String DEFAULT_CREATE_TAG_CODE = "11";
    private static final String DEFAULT_DELETE_TAG_CODE = "12";

    private final TagService tagService;
    private final Hateoas<TagDTO> tagDTOHateoas;

    @Autowired
    public TagController(TagService tagService, AnswerMessageJson answerMessageJson, Hateoas<TagDTO> tagDTOHateoas) {
        super(answerMessageJson);
        this.tagService = tagService;
        this.tagDTOHateoas = tagDTOHateoas;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerMessageJson createTag(@Valid @RequestBody TagDTO tagDTO, BindingResult bindingResult, Locale locale) throws ServiceException, InvalidInputDataException, NoSuchIdException, NotInsertedException {
        LOGGER.info("Start tag creation");
        bindingResultCheck(bindingResult);
        tagService.createTag(tagDTO, locale);
        answerMessageJson.setMessage(TAG_HAS_BEEN_CREATED);
        HttpStatus httpStatus = HttpStatus.CREATED;
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setCode(httpStatus.value() + DEFAULT_CREATE_TAG_CODE);
        LOGGER.info(TAG_HAS_BEEN_CREATED);
        tagDTOHateoas.buildDefaultHateoas(answerMessageJson);
        return answerMessageJson;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TagDTO getTagById(@PathVariable long id, Locale locale) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Getting tag by id");
        TagDTO tagDTO = tagService.getTagById(id, locale);
        tagDTOHateoas.buildFullHateoas(tagDTO);
        return tagDTO;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> getTagList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Getting tag list");
        List<TagDTO> tagDTOs = tagService.getTagList(page, size);
        for (TagDTO tagDTO : tagDTOs) {
            tagDTOHateoas.buildFullHateoas(tagDTO);
        }
        return tagDTOs;
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson deleteTag(@PathVariable long id, Locale locale) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Tag deletion by id");
        tagService.deleteTag(id, locale);
        HttpStatus httpStatus = HttpStatus.OK;
        answerMessageJson.setMessage(DELETED);
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setCode(httpStatus.value() + DEFAULT_DELETE_TAG_CODE);
        tagDTOHateoas.buildDefaultHateoas(answerMessageJson);
        return answerMessageJson;
    }
}
