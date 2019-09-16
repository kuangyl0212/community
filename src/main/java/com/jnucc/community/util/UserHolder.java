package com.jnucc.community.util;

import com.jnucc.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {
    private ThreadLocal<User> holder = new ThreadLocal<>();

    public void setUser(User user) {
        holder.set(user);
    }

    public User getUser() {
        return holder.get();
    }

    public void clear() {
        holder.remove();
    }
}
