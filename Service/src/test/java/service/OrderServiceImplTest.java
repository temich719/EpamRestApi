package service;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.DateAndTimeOrderDTO;
import com.epam.esm.util.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDAO orderDAO;
    @Mock
    private Mapper mapper;

    @Test
    public void testGetOrderDateAndTimeOfPurchase() {
        DateAndTimeOrderDTO dateAndTimeOrderDTO = new DateAndTimeOrderDTO();
        Order order = new Order();
        long id = 1;
        when(mapper.mapToDateTimeOrderDTO(order)).thenReturn(dateAndTimeOrderDTO);
        when(orderDAO.getOrderDateAndTimeOfPurchase(id)).thenReturn(order);

        Order newOrder = orderDAO.getOrderDateAndTimeOfPurchase(id);
        DateAndTimeOrderDTO newDateAndTimeOrderDTO = mapper.mapToDateTimeOrderDTO(newOrder);

        verify(mapper, times(1)).mapToDateTimeOrderDTO(newOrder);
        verify(orderDAO, times(1)).getOrderDateAndTimeOfPurchase(id);

        assertThat(newOrder).isEqualTo(order);
        assertThat(newDateAndTimeOrderDTO).isEqualTo(dateAndTimeOrderDTO);
    }

}
