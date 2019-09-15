package com.jnucc.community.dao;

import com.jnucc.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {

   int insertTicket(LoginTicket ticket);

   LoginTicket findTicket(String ticket);

   int updateStatus(String ticket, int status);

}
