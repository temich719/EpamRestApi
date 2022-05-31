package com.epam.esm.hateoas;

import com.epam.esm.exception.InvalidInputDataException;
import com.epam.esm.exception.NoSuchIdException;
import com.epam.esm.exception.NotInsertedException;
import com.epam.esm.exception.ServiceException;
import org.springframework.hateoas.RepresentationModel;

public interface Hateoas<T extends RepresentationModel<? extends T>> {

    void buildDefaultHateoas(RepresentationModel<?> model) throws NoSuchIdException, InvalidInputDataException, NotInsertedException, ServiceException;

    void buildFullHateoas(T t) throws InvalidInputDataException, NotInsertedException, NoSuchIdException, ServiceException;

}
