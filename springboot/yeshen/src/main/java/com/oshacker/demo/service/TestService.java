package com.oshacker.demo.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public String getMessage(int userId) {
        return "hello Message"+String.valueOf(userId);
    }
}
