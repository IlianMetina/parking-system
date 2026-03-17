package com.parkit.parkingsystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    @Test
    public void getIdTest(){

        Ticket ticket = new Ticket();
        ticket.setId(5);
        assertEquals(5, ticket.getId());

    }

    @Test
    public void setIdTest(){

        Ticket ticket = new Ticket();
        ticket.setId(5);
        assertEquals(5, ticket.getId());
    }
}
