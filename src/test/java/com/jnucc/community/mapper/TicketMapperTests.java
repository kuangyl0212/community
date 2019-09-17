package com.jnucc.community.mapper;


import com.jnucc.community.CommunityApplication;
import com.jnucc.community.entity.Ticket;
import com.jnucc.community.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TicketMapperTests {

    @Autowired
    private TicketMapper mapper;

    private Ticket ticket;
    private String ticketText;

    private void initTicket() {
        ticket = new Ticket();
        ticketText = CommunityUtil.generateUUID();
        ticket.setTicket(ticketText);
        ticket.setUserId(163);
        ticket.setStatus(0);
        ticket.setExpired(new Date());
    }

    @Test
    public void testInsertTicket() {
        initTicket();
        int rows = mapper.insertTicket(ticket);
        assertEquals(1, rows);
    }

    @Test
    public void testFindTicket() {
        initTicket();
        mapper.insertTicket(ticket);
        Ticket found = mapper.findTicket(ticketText);
        assertEquals(ticketText, found.getTicket());
    }

    @Test
    public void testUpdateStatus() {
        initTicket();
        mapper.insertTicket(ticket);
        int rows= mapper.updateStatus(ticket.getTicket(), 1);
        assertEquals(1, rows);
        Ticket found = mapper.findTicket(ticketText);
        assertEquals(1, found.getStatus());
    }
}
