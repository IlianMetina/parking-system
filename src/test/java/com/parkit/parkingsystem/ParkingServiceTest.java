package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    public void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void getNextParkingNumberIfAvailableTest(){
        parkingService.getNextParkingNumberIfAvailable();
    }

    @Test
    public void processExitingVehicleTest(){
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void getVehicleTypeCarTest() throws Exception{

        InputReaderUtil inputReaderUtil = mock(InputReaderUtil.class);
        ParkingSpotDAO parkingSpotDAO = mock(ParkingSpotDAO.class);
        TicketDAO ticketDAO = mock(TicketDAO.class);

        when(inputReaderUtil.readSelection()).thenReturn(1);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        Method getVehicleType = ParkingService.class.getDeclaredMethod("getVehicleType");
        getVehicleType.setAccessible(true);
        Object result = getVehicleType.invoke(parkingService);

        assertEquals(ParkingType.CAR, result);
    }

    @Test
    public void getVehicleTypeBikeTest() throws Exception{

        InputReaderUtil inputReaderUtil = mock(InputReaderUtil.class);
        ParkingSpotDAO parkingSpotDAO = mock(ParkingSpotDAO.class);
        TicketDAO ticketDAO = mock(TicketDAO.class);

        when(inputReaderUtil.readSelection()).thenReturn(2);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        Method getVehicleType = ParkingService.class.getDeclaredMethod("getVehicleType");
        getVehicleType.setAccessible(true);
        Object result = getVehicleType.invoke(parkingService);

        assertEquals(ParkingType.BIKE, result);
    }

    @Test
    public void processIncomingVehicleCarTest() throws Exception {

        InputReaderUtil inputReaderUtil = mock(InputReaderUtil.class);
        ParkingSpotDAO parkingSpotDAO = mock(ParkingSpotDAO.class);
        TicketDAO ticketDAO = mock(TicketDAO.class);

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");

        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingVehicleBikeTest() throws Exception{

        InputReaderUtil inputReaderUtil = mock(InputReaderUtil.class);
        ParkingSpotDAO parkingSpotDAO = mock(ParkingSpotDAO.class);
        TicketDAO ticketDAO = mock(TicketDAO.class);

        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("123ABC");

        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(1);

        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

}
