package com.jnucc.community.controller.interceptor;

import com.jnucc.community.entity.Ticket;
import com.jnucc.community.entity.User;
import com.jnucc.community.service.UserService;
import com.jnucc.community.util.CommunityUtil;
import com.jnucc.community.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CommunityUtil.getCookieValueFrom(request, "ticket");
        if (ticket != null) {
            Ticket loginTicket = userService.findTicket(ticket);
            if (isTicketValid(loginTicket)) {
                User user = userService.findByUserId(loginTicket.getUserId());
                userHolder.setUser(user);
            }
            else userHolder.clear();
        }
        else userHolder.clear();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHolder.getUser();
        if (user != null) modelAndView.addObject("loginUser", user);
    }

    private boolean isTicketValid(Ticket ticket) {
        return ticket != null && ticket.getStatus() == 0 && ticket.getExpired().after(new Date());
    }
}
