package com.jnucc.community.service;

import com.jnucc.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaService {

    private final AlphaDao alphaDao;


    @Autowired
    public AlphaService(AlphaDao alphaDao) {
        this.alphaDao = alphaDao;
    }

    public String find() {
        return alphaDao.select();
    }
}
