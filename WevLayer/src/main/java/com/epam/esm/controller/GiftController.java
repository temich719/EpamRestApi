package com.epam.esm.controller;

import com.epam.esm.dtos.GiftCertificateDTO;
import com.epam.esm.dtos.TagDTO;
import com.epam.esm.errors.AnswerMessageJson;
import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.NotInsertedException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.Hateoas;
import com.epam.esm.searchparamcontainer.SearchParamContainer;
import com.epam.esm.service.GiftCertificateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/certificates")
public class GiftController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(GiftController.class);

    private static final String GIFT_CERTIFICATE_HAS_BEEN_CREATED = "New gift certificate has been created!";
    private static final String CERTIFICATE_WITH_ID = "Certificate with id: ";
    private static final String WAS_DELETED = " was deleted";
    private static final String UPDATED = "Updated";
    private static final String DEFAULT_CREATE_GIFT_CERTIFICATE_CODE = "00";

    private final GiftCertificateService giftCertificateService;
    private final Hateoas<GiftCertificateDTO> giftCertificateDTOHateoas;
    private final Hateoas<TagDTO> tagDTOHateoas;

    @Autowired
    public GiftController(GiftCertificateService giftCertificateService, AnswerMessageJson answerMessageJson, Hateoas<GiftCertificateDTO> giftCertificateDTOHateoas, Hateoas<TagDTO> tagDTOHateoas) {
        super(answerMessageJson);
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateDTOHateoas = giftCertificateDTOHateoas;
        this.tagDTOHateoas = tagDTOHateoas;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerMessageJson insertCertificate(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO,
                                               BindingResult bindingResult) throws NotInsertedException, InvalidInputDataException, NoSuchIdException, ServiceException {
        LOGGER.info("Start gift certificate creation");
        bindingResultCheck(bindingResult);
        giftCertificateService.insertGiftCertificate(giftCertificateDTO);
        HttpStatus httpStatus = HttpStatus.CREATED;
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setMessage(GIFT_CERTIFICATE_HAS_BEEN_CREATED);
        answerMessageJson.setCode(httpStatus.value() + DEFAULT_CREATE_GIFT_CERTIFICATE_CODE);
        LOGGER.info(GIFT_CERTIFICATE_HAS_BEEN_CREATED);
        giftCertificateDTOHateoas.buildDefaultHateoas(answerMessageJson);
        return answerMessageJson;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDTO getCertificateByID(@PathVariable long id, Locale locale) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Getting certificate by id");
        GiftCertificateDTO giftCertificateDTO = giftCertificateService.getGiftCertificateByID(id, locale);
        giftCertificateDTOHateoas.buildFullHateoas(giftCertificateDTO);
        return giftCertificateDTO;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDTO> getCertificatesList(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Getting certificates list");
        List<GiftCertificateDTO> giftCertificateDTOs = giftCertificateService.getGiftCertificatesDTOList(page, size);
        return buildHateoasForList(giftCertificateDTOs);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AnswerMessageJson deleteGiftCertificate(@PathVariable long id, Locale locale) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Certificate deletion by id");
        giftCertificateService.deleteGiftCertificate(id, locale);
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        answerMessageJson.setMessage(CERTIFICATE_WITH_ID + id + WAS_DELETED);
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setCode(httpStatus.value() + String.valueOf(id));
        giftCertificateDTOHateoas.buildDefaultHateoas(answerMessageJson);
        return answerMessageJson;
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson updateGiftCertificate(@PathVariable long id, @RequestBody GiftCertificateDTO giftCertificateDTO, Locale locale) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        LOGGER.info("Updating certificate");
        giftCertificateService.updateGiftCertificate(id, giftCertificateDTO, locale);
        HttpStatus httpStatus = HttpStatus.OK;
        answerMessageJson.setStatus(httpStatus.toString());
        answerMessageJson.setMessage(UPDATED);
        answerMessageJson.setCode(httpStatus.value() + String.valueOf(id));
        giftCertificateDTOHateoas.buildDefaultHateoas(answerMessageJson);
        return answerMessageJson;
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDTO> searchGiftCertificate(@Valid @RequestBody SearchParamContainer searchParamContainer,
                                                          BindingResult bindingResult,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size) throws InvalidInputDataException, NoSuchIdException, ServiceException, NotInsertedException {
        LOGGER.info("Start search certificate by params");
        bindingResultCheck(bindingResult);
        List<GiftCertificateDTO> giftCertificateDTOs = giftCertificateService.getCertificatesDTOListAccordingToInputParams(searchParamContainer.getMapOfSearchParams(), page, size);
        LOGGER.info("Certificate has been found by params");
        return buildHateoasForList(giftCertificateDTOs);
    }

    @GetMapping(value = "/byTags", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDTO> getCertificatesBySeveralTags(@RequestBody List<TagDTO> tagDTOs,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size) throws NoSuchIdException, InvalidInputDataException, ServiceException, NotInsertedException {
        List<GiftCertificateDTO> giftCertificateDTOs = giftCertificateService.getCertificatesBySeveralTags(tagDTOs, page, size);
        return buildHateoasForList(giftCertificateDTOs);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDTO> getFilteredCertificateList(@RequestParam MultiValueMap<String, Object> params) throws InvalidInputDataException, ServiceException, NotInsertedException, NoSuchIdException {
        return buildHateoasForList(giftCertificateService.getFilteredCertificateList(params));
    }

    private List<GiftCertificateDTO> buildHateoasForList(List<GiftCertificateDTO> giftCertificateDTOs) throws InvalidInputDataException, ServiceException, NotInsertedException, NoSuchIdException {
        for (GiftCertificateDTO giftCertificateDTO : giftCertificateDTOs) {
            giftCertificateDTOHateoas.buildFullHateoas(giftCertificateDTO);
            for (TagDTO tagDTO : giftCertificateDTO.getTags()) {
                tagDTOHateoas.buildDefaultHateoas(tagDTO);
            }
        }
        return giftCertificateDTOs;
    }

}
