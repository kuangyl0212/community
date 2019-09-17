package com.jnucc.community.mapper;

import com.jnucc.community.entity.Ticket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper {

   int insertTicket(Ticket ticket);

   Ticket findTicket(String ticket);

   int updateStatus(String ticket, int status);

}
