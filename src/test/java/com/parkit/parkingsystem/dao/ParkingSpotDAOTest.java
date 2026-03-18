package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    @InjectMocks
    private ParkingSpotDAO parkingSpotDAO;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    @Test
    public void updateParkingSuccessTest() throws Exception{

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        assertTrue(result);

        verify(ps).setBoolean(1, true);
        verify(ps).setInt(2, 1);
        verify(ps).executeUpdate();
        verify(dataBaseConfig).closePreparedStatement(ps);
        verify(dataBaseConfig).closeConnection(connection);

    }

    @Test
    public void getNextAvailableSlotSuccessTest() throws Exception{

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(1, result);

        verify(ps).setString(1, "CAR");
        verify(dataBaseConfig).closeResultSet(rs);
        verify(dataBaseConfig).closePreparedStatement(ps);
        verify(dataBaseConfig).closeConnection(connection);
    }

}
