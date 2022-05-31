package com.epam.esm.searchparamcontainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SearchParamContainer {

    private static final String TAG_NAME = "tagName";
    private static final String NAME_PART = "namePart";
    private static final String DESCRIPTION_PART = "descriptionPart";
    private static final String SORT_BY_NAME = "sortByName";
    private static final String SORT_BY_DATE = "sortByDate";

    @NotNull(message = "{com.epam.esm.constraint.name.message}")
    @NotEmpty(message = "{com.epam.esm.constraint.nameIsEmpty}")
    String tagName;

    String namePart;
    String descriptionPart;

    @Pattern(regexp = "asc|desc", message = "{com.epam.esm.constraint.sort}")
    String sortByName;

    @Pattern(regexp = "asc|desc", message = "{com.epam.esm.constraint.sort}")
    String sortByDate;

    public Map<String, String> getMapOfSearchParams() {
        Map<String, String> mapOfSearchParams = new HashMap<>();
        mapOfSearchParams.put(TAG_NAME, tagName);
        mapOfSearchParams.put(NAME_PART, namePart);
        mapOfSearchParams.put(DESCRIPTION_PART, descriptionPart);
        mapOfSearchParams.put(SORT_BY_NAME, sortByName);
        mapOfSearchParams.put(SORT_BY_DATE, sortByDate);
        return mapOfSearchParams;
    }
}
