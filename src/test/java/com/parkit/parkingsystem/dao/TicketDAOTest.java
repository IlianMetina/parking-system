package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @InjectMocks
    private TicketDAO ticketDAO;

    private Ticket ticket;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement ps;

    @BeforeEach
    void cleanUp(){
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
    }

    @Test
    public void saveSuccessfullyTicketTest() throws Exception{

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.execute()).thenReturn(true);

        boolean result = ticketDAO.saveTicket(ticket);

        assertTrue(result);
        verify(ps).setInt(1, ticket.getParkingSpot().getId());
        verify(ps).setString(2, "ABC123");
        verify(ps).execute();
        verify(dataBaseConfig).closeConnection(connection);
    }

    @Test
    public void getTicketTest() throws Exception{

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.execute()).thenReturn(true);



    }

    @Test
    public void updateTicketSuccessfullyTest() throws Exception{

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);

        boolean result = ticketDAO.updateTicket(ticket);

        assertTrue(result);

        verify(ps).setDouble(1, ticket.getPrice());
        verify(ps).setTimestamp(eq(2), any(Timestamp.class));
        verify(ps).setInt(3, ticket.getId());
        verify(ps).execute();
        verify(dataBaseConfig).closeConnection(connection);
    }
}
