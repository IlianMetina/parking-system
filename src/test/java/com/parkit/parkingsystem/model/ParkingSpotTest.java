package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotTest {

    @Test
    public void testSetId(){
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpot.setId(5);
        assertEquals(5, parkingSpot.getId());
    }

    @Test
    public void isAvailableTestFalse(){

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        assertTrue(parkingSpot.isAvailable());
    }

    @Test
    public void isAvailableTestTrue(){

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertFalse(parkingSpot.isAvailable());

    }

    @Test
    public void setParkingTypeTest(){

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpot.setParkingType(ParkingType.BIKE);
        assertEquals(ParkingType.BIKE ,parkingSpot.getParkingType());
    }

    @Test
    public void testEqualsSameNumber(){

        ParkingSpot firstSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot secondSpot = new ParkingSpot(1, ParkingType.BIKE, true);

        assertEquals(firstSpot, secondSpot);
    }

    @Test
    public void testEqualsDifferentNumber(){

        ParkingSpot firstSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot secondSpot = new ParkingSpot(2, ParkingType.BIKE, true);

        assertNotEquals(firstSpot, secondSpot);
    }

    @Test
    public void testEqualsSame(){
        ParkingSpot spot = new ParkingSpot(1, ParkingType.CAR, true);

        assertEquals(spot, spot);
    }



    @Test
    public void hashCodeTest(){

        ParkingSpot firstSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot secondSpot = new ParkingSpot(1, ParkingType.BIKE, true);

        assertEquals(firstSpot.hashCode(), secondSpot.hashCode());
    }
}
