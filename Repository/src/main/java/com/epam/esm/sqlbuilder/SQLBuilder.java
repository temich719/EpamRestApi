package com.epam.esm.sqlbuilder;

import com.epam.esm.domain.Tag;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SQLBuilder {

    private static final String SELECT_FROM_GIFT_CERTIFICATES_WHERE = "SELECT * FROM gift_certificate WHERE ";
    private static final String ID_ASSIGN = "id = ";
    private static final String LEFT_BRACKET = "(";
    private static final String OR = " || ";
    private static final String RIGHT_BRACKET = ")";
    private static final String NAME_PART = "namePart";
    private static final String NAME_PART_LIKE_EXPRESSION_BEGINNING = " && name LIKE '%";
    private static final String DESCRIPTION_PART_LIKE_EXPRESSION_BEGINNING = " && description LIKE '%";
    private static final String DESCRIPTION_PART = "descriptionPart";
    private static final String LIKE_EXPRESSION_ENDING = "%'";
    private static final String SORT_BY_NAME = "sortByName";
    private static final String SORT_BY_DATE = "sortByDate";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String ORDER_BY_NAME = " order by name ";
    private static final String ORDER_BY_CREATE_DATE = " order by create_date ";
    private static final String PLUS_CREATE_DATE = ", create_date ";
    private static final String SEMICOLON = ";";

    private static final String SELECT_CERTIFICATE_BY_TAGS = "select gift_certificate.id, " +
            "gift_certificate.name, description, price, duration, create_date, last_update_date, " +
            "gift_certificate.creationEntityDate, gift_certificate.modificationEntityDate, gift_certificate.createdEntityBy, \n" +
            "gift_certificate.modifiedEntityBy \n" +
            "from gift_certificate join certificates_and_tags cat on gift_certificate.id = cat.gift_certificate_id\n" +
            "join tag t on cat.tag_id = t.id\n" +
            "where ";
    private static final String TAG_NAME = "t.name = ";
    private static final String OR_TAG = " or ";
    private static final String GROUP_BY = " group by gift_certificate.id having count(t.name) = ";
    private static final String LIMIT = " limit ";
    private static final String OFFSET = " offset ";
    private static final String QUOTE = "'";

    public String createGetCertificatesByTagsSQL(List<Tag> tags, int page, int size) {
        StringBuilder sql = new StringBuilder(SELECT_CERTIFICATE_BY_TAGS);

        for (int i = 0; i < tags.size() - 1; i++) {
            sql.append(TAG_NAME).append(QUOTE).append(tags.get(i).getName()).append(QUOTE).append(OR_TAG);
        }
        sql.append(TAG_NAME).append(QUOTE).append(tags.get(tags.size() - 1).getName()).append(QUOTE);
        sql.append(GROUP_BY).append(tags.size()).append(LIMIT).append(size)
                .append(OFFSET).append((page - 1) * size).append(SEMICOLON);
        return sql.toString();
    }


    public String createSearchSQL(Map<String, String> mapOfSearchParams, List<Long> giftCertificateIds, int page, int size) {
        StringBuilder sql = new StringBuilder(SELECT_FROM_GIFT_CERTIFICATES_WHERE);

        if (giftCertificateIds.size() == 1) {
            sql.append(ID_ASSIGN).append(giftCertificateIds.get(0));
        } else {
            sql.append(LEFT_BRACKET);
            for (int i = 0; i < giftCertificateIds.size() - 1; i++) {
                sql.append(ID_ASSIGN).append(giftCertificateIds.get(i)).append(OR);
            }
            sql.append(ID_ASSIGN).append(giftCertificateIds.get(giftCertificateIds.size() - 1)).append(RIGHT_BRACKET);
        }

        String namePart = mapOfSearchParams.get(NAME_PART);
        if (Objects.nonNull(namePart)) {
            sql.append(NAME_PART_LIKE_EXPRESSION_BEGINNING).append(namePart).append(LIKE_EXPRESSION_ENDING);
        }

        String descriptionPart = mapOfSearchParams.get(DESCRIPTION_PART);
        if (Objects.nonNull(descriptionPart)) {
            sql.append(DESCRIPTION_PART_LIKE_EXPRESSION_BEGINNING).append(descriptionPart).append(LIKE_EXPRESSION_ENDING);
        }

        String sortByName = mapOfSearchParams.get(SORT_BY_NAME);
        if (Objects.nonNull(sortByName)) {
            if (sortByName.equalsIgnoreCase(ASC) || sortByName.equalsIgnoreCase(DESC)) {
                sql.append(ORDER_BY_NAME).append(sortByName);
            }
        }

        String sortByDate = mapOfSearchParams.get(SORT_BY_DATE);
        if (Objects.isNull(sortByName)) {
            if (Objects.nonNull(sortByDate)) {
                if (sortByDate.equalsIgnoreCase(ASC) || sortByDate.equalsIgnoreCase(DESC)) {
                    sql.append(ORDER_BY_CREATE_DATE).append(sortByDate);
                }
            }
        } else if (Objects.nonNull(sortByDate)) {
            if (sortByDate.equalsIgnoreCase(ASC) || sortByDate.equalsIgnoreCase(DESC)) {
                sql.append(PLUS_CREATE_DATE).append(sortByDate);
            }
        }

        sql.append(LIMIT).append(size).append(OFFSET).append((page - 1) * size).append(SEMICOLON);

        return sql.toString();
    }

}
