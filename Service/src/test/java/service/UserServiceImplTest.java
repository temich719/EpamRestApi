package service;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dtos.*;
import com.epam.esm.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;
    @Mock
    private Mapper mapper;

    private OrderDTO orderDTO;
    private Order order;
    private User user;
    private UserDTO userDTO;
    private Long[] ids;
    private AnswerOrderDTO answerOrderDTO;
    private UserHighestOrdersCostDTO userHighestOrdersCostDTO;

    @BeforeEach
    public void initUseCase() {
        UserNameDTO userNameDTO = new UserNameDTO();
        userNameDTO.setUserName("Artem");
        orderDTO = new OrderDTO();
        orderDTO.setUserNameDTO(userNameDTO);
        ids = new Long[]{1L, 2L};
        orderDTO.setGiftCertificateIds(ids);
        order = new Order();
        user = new User();
        order.setUser(user);
        order.setDate("22.06.2021");
        order.setCost("1000$");
        userDTO = new UserDTO();
        userDTO.setUserName("Sergio");
        answerOrderDTO = new AnswerOrderDTO();
        userHighestOrdersCostDTO = new UserHighestOrdersCostDTO();
    }

    @Test
    public void testMakeOrder() {
        when(mapper.mapToOrder(orderDTO)).thenReturn(order);
        doNothing().when(userDAO).makeOrder(order, ids);
        userDAO.makeOrder(mapper.mapToOrder(orderDTO), orderDTO.getGiftCertificateIds());
        verify(mapper, times(1)).mapToOrder(orderDTO);
        verify(userDAO, times(1)).makeOrder(mapper.mapToOrder(orderDTO), orderDTO.getGiftCertificateIds());
    }

    @Test
    public void testGetUserById() {
        when(mapper.mapToUserDTO(user)).thenReturn(userDTO);
        long id = 1;
        Optional<User> optionalUser = Optional.of(user);
        when(userDAO.getUserById(id)).thenReturn(optionalUser);

        User newUser = userDAO.getUserById(id).orElse(null);
        assert newUser != null;
        UserDTO newUserDTO = mapper.mapToUserDTO(newUser);

        verify(mapper, times(1)).mapToUserDTO(newUser);
        verify(userDAO, times(1)).getUserById(id);

        assertThat(newUser).isEqualTo(user);
        assertThat(newUserDTO).isEqualTo(userDTO);
    }

    @Test
    public void testGetUsersOrders() {
        String name = "Sasha";
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        List<AnswerOrderDTO> answerOrderDTOs = new ArrayList<>();
        answerOrderDTOs.add(answerOrderDTO);
        when(userDAO.getUsersOrders(name)).thenReturn(orders);
        when(mapper.mapToAnswerOrderDTO(order)).thenReturn(answerOrderDTO);

        List<Order> newOrders = userDAO.getUsersOrders(name);
        List<AnswerOrderDTO> newAnswerOrderDTOs = new ArrayList<>();
        AnswerOrderDTO newAnswerOrderDTO = mapper.mapToAnswerOrderDTO(newOrders.get(0));
        newAnswerOrderDTOs.add(newAnswerOrderDTO);

        verify(mapper, times(1)).mapToAnswerOrderDTO(newOrders.get(0));
        verify(userDAO, times(1)).getUsersOrders(name);

        assertThat(newOrders).isEqualTo(orders);
        assertThat(newAnswerOrderDTOs).isEqualTo(answerOrderDTOs);
    }

    @Test
    public void testGetUserWithHighestOrdersCostWithMostWidelyUsedTag() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userDAO.getUsersWithHighestOrdersCostWithMostWidelyUsedTags(1, 10)).thenReturn(users);
        when(mapper.mapToUserHighestOrdersCostDTOFromUser(user)).thenReturn(userHighestOrdersCostDTO);

        List<User> newUsers = userDAO.getUsersWithHighestOrdersCostWithMostWidelyUsedTags(1, 10);
        UserHighestOrdersCostDTO newUserHighestCostDTO = mapper.mapToUserHighestOrdersCostDTOFromUser(newUsers.get(0));

        verify(mapper, times(1)).mapToUserHighestOrdersCostDTOFromUser(newUsers.get(0));
        verify(userDAO, times(1)).getUsersWithHighestOrdersCostWithMostWidelyUsedTags(1, 10);

        assertThat(newUsers.get(0)).isEqualTo(user);
        assertThat(newUserHighestCostDTO).isEqualTo(userHighestOrdersCostDTO);
    }

    @Test
    public void testGetUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        List<UserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(userDTO);
        when(mapper.mapToUserDTOList(users)).thenReturn(userDTOs);
        when(userDAO.getUsers(1, 10)).thenReturn(users);

        List<User> newUsers = userDAO.getUsers(1, 10);
        List<UserDTO> newUserDTOs = mapper.mapToUserDTOList(newUsers);

        verify(mapper, times(1)).mapToUserDTOList(newUsers);
        verify(userDAO, times(1)).getUsers(1, 10);

        assertThat(newUsers.get(0).getUserName()).isEqualTo(users.get(0).getUserName());
        assertThat(newUserDTOs.get(0)).isEqualTo(userDTOs.get(0));

    }

}
