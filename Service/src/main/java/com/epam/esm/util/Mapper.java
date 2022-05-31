package com.epam.esm.util;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.dtos.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public List<UserDTO> mapToUserDTOList(List<User> users) {
        return users.stream().map(this::mapToUserDTO).collect(Collectors.toList());
    }

    public List<Tag> mapToTagList(List<TagDTO> tagDTOs) {
        return tagDTOs.stream().map(this::mapToTag).collect(Collectors.toList());
    }

    public UserHighestOrdersCostDTO mapToUserHighestOrdersCostDTOFromUser(User user) {
        UserHighestOrdersCostDTO userHighestOrdersCostDTO = new UserHighestOrdersCostDTO();
        userHighestOrdersCostDTO.setId(user.getId());
        userHighestOrdersCostDTO.setUserName(user.getUserName());
        userHighestOrdersCostDTO.setSum(user.getSum());
        userHighestOrdersCostDTO.setTagDTOs(mapToTagDTOList(user.getTags()));
        return userHighestOrdersCostDTO;
    }

    public DateAndTimeOrderDTO mapToDateTimeOrderDTO(Order order) {
        DateAndTimeOrderDTO dateAndTimeOrderDTO = new DateAndTimeOrderDTO();
        dateAndTimeOrderDTO.setDate(order.getDate());
        dateAndTimeOrderDTO.setCost(order.getCost());
        return dateAndTimeOrderDTO;
    }

    public Order mapToOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUser(userNameDTOMapToUser(orderDTO.getUserNameDTO()));
        return order;
    }

    public User userNameDTOMapToUser(UserNameDTO userNameDTO) {
        return new User(userNameDTO.getUserName());
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        List<AnswerOrderDTO> answerOrderDTOs = new ArrayList<>();
        for (Order order : user.getOrders()) {
            answerOrderDTOs.add(mapToAnswerOrderDTO(order));
        }
        userDTO.setOrderDTOs(answerOrderDTOs);
        return userDTO;
    }

    public AnswerOrderDTO mapToAnswerOrderDTO(Order order) {
        AnswerOrderDTO answerOrderDTO = new AnswerOrderDTO();
        answerOrderDTO.setId(order.getId());
        answerOrderDTO.setCost(order.getCost());
        answerOrderDTO.setDate(order.getDate());
        answerOrderDTO.setGiftCertificateDTOs(mapToGiftCertificateDTOList(order.getGiftCertificates()));
        return answerOrderDTO;
    }

    public TagDTO mapToTagDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    public Tag mapToTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }

    public GiftCertificateDTO mapToGiftCertificateDTO(GiftCertificate giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(giftCertificate.getId());
        giftCertificateDTO.setName(giftCertificate.getName());
        giftCertificateDTO.setDescription(giftCertificate.getDescription());
        giftCertificateDTO.setPrice(giftCertificate.getPrice());
        giftCertificateDTO.setDuration(giftCertificate.getDuration());
        giftCertificateDTO.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateDTO.setLastUpdateDate(giftCertificate.getLastUpdateDate());

        TagDTO[] tagDTOs = new TagDTO[giftCertificate.getTags().size()];

        int i = 0;
        for (Tag tag : giftCertificate.getTags()) {
            tagDTOs[i] = mapToTagDTO(tag);
            i++;
        }

        giftCertificateDTO.setTags(tagDTOs);

        return giftCertificateDTO;
    }

    public GiftCertificate mapToGiftCertificate(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(giftCertificateDTO.getId());
        giftCertificate.setName(giftCertificateDTO.getName());
        giftCertificate.setDescription(giftCertificateDTO.getDescription());
        giftCertificate.setPrice(giftCertificateDTO.getPrice());
        giftCertificate.setDuration(giftCertificateDTO.getDuration());
        giftCertificate.setCreateDate(giftCertificateDTO.getCreateDate());
        giftCertificate.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());

        Set<Tag> tags = new HashSet<>();

        TagDTO[] tagDTOs = giftCertificateDTO.getTags();
        if (Objects.nonNull(tagDTOs)) {
            for (TagDTO tagDTO : giftCertificateDTO.getTags()) {
                tags.add(mapToTag(tagDTO));
            }
        }

        giftCertificate.setTags(tags);

        return giftCertificate;
    }

    public List<GiftCertificateDTO> mapToGiftCertificateDTOList(List<GiftCertificate> giftCertificates) {
        return Objects.isNull(giftCertificates) ? null : giftCertificates.stream().map(this::mapToGiftCertificateDTO).collect(Collectors.toList());
    }

    public List<TagDTO> mapToTagDTOList(List<Tag> tags) {
        return tags.stream().map(this::mapToTagDTO).collect(Collectors.toList());
    }
}
